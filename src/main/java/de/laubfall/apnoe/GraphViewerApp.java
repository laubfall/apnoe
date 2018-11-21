package de.laubfall.apnoe;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.graphicGraph.StyleGroup;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.swingViewer.LayerRenderer;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.Camera;
import org.graphstream.ui.swingViewer.util.GraphMetrics;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import de.laubfall.apnoe.hie.CallHierarchyNode;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;
import de.laubfall.apnoe.hie.IfElseNode;

/**
 * Sample app that demonstrate how the call hierarchy analyze result can be presented as a graph.
 * 
 * @author Daniel
 *
 */
public class GraphViewerApp extends AbstractApnoeApp
{

  private static final Logger LOG = LogManager.getLogger(GraphViewerApp.class);

  public static void main(String[] args)
  {
    LOG.info("Starting graph viewer app");

    initTypeSolver(args);

    final GraphViewerApp gva = new GraphViewerApp();
    final String scannerDefinition = argScanner(args, true);
    if (scannerDefinition != null) {
      LOG.info("Start scanning defined src folder for artifacts with possible entry points");
      final List<CallHierarchyNode> scanResult = gva.startScanner(argEntryPointSrc(args), scannerDefinition,
          (entryMethodName, artifacts) -> {
            final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
            return artifacts.stream().map(f -> has.analyze(f.getAbsolutePath(), entryMethodName))
                .collect(Collectors.toList());
          });

      gva.startScanRender(scanResult);
    } else {
      gva.start(argEntryPointSrc(args), argEntryPointMethodName(args));
    }

  }

  public void startScanRender(List<CallHierarchyNode> scanResult)
  {
    final Graph graph = new MultiGraph("GraphViewerApp");
    graph.addAttribute("ui.quality");
    graph.addAttribute("ui.antialias");
    graph.addAttribute("ui.title", "apnoe");
    addStyleSheet(graph);
    // thats the root node (your application)
    graph.addNode("app");

    scanResult.forEach(scanNode -> {
      ClassOrInterfaceDeclaration clNode = (ClassOrInterfaceDeclaration) scanNode.getNode();
      graph.addNode(scanNode.getUid()).addAttribute("ui.label",
          clNode.getNameAsString() + "." + scanNode.getScopeName());
      addLeafNodes(scanNode.getUid(), scanNode.getLeafs(), graph);
      graph.addEdge("app_edge_" + scanNode.getUid(), "app", scanNode.getUid());
    });

    final Viewer viewer = graph.display();
    viewer.getDefaultView().addKeyListener(new ZoomAndPanKeyAdapter(viewer));
    viewer.getDefaultView().setForeLayoutRenderer(new AdditionalGraphInfoRenderer(scanResult));
  }

  @Override
  CallHierarchyNode start(String pathToEntryPointSourceFile, String entryPointMethodName)
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    final CallHierarchyNode result = has.analyze(pathToEntryPointSourceFile, entryPointMethodName);

    final Graph graph = new SingleGraph("GraphViewerApp");
    graph.addAttribute("ui.quality");
    graph.addAttribute("ui.antialias");
    addStyleSheet(graph);
    graph.addNode(result.getUid()).addAttribute("ui.label", result.getScopeName());
    addLeafNodes(result.getUid(), result.getLeafs(), graph);

    final Viewer viewer = graph.display();
    viewer.getDefaultView().setForeLayoutRenderer(new AdditionalGraphInfoRenderer(result));
    return result;
  }

  private void addStyleSheet(final Graph graph)
  {
    try {
      graph.addAttribute("ui.stylesheet",
          IOUtils.toString(getClass().getClassLoader().getResourceAsStream("graphstream.css"), "UTF-8"));
    } catch (IOException e) {
      LOG.error("Failed to load stylesheet", e);
    }
  }

  private final void addLeafNodes(String parentUid, List<CallHierarchyNode> leafs, final Graph graph)
  {

    for (CallHierarchyNode l : leafs) {
      if (l instanceof IfElseNode) {
        continue;
      }

      final Node leafNode = graph.addNode(l.getUid());
      leafNode.addAttribute("ui.label", l.getScopeName());
      graph.addEdge(parentUid + "_" + l.getUid(), parentUid, l.getUid()).addAttribute("ui.label", provideEdgeLabel(l));

      addLeafNodes(l.getUid(), l.getLeafs(), graph);
    }

    // now take care of the if-Statements. These were not handled before.
    leafs.stream().filter(chr -> chr.getScopeName().equals("if")).forEach(ifElseNode -> {
      //      graph.addNode(ifElseNode.getUid());
      //      graph.addEdge(parentUid + "_" + ifElseNode.getUid(), parentUid, ifElseNode.getUid());
      //
      //      addLeafNodes(ifElseNode.getUid(), ifElseNode.getLeafs(), graph);

      final IfElseNode ien = (IfElseNode) ifElseNode;
      // TODO that does not work in case there is a else-block successor with an if-Statement inside its block
      // In this case the if-Block is rendered as a child-node of the current if-node (what is wrong). See the
      // IfElseSample.java for example
      //      ien.getSuccessors().forEach(successors -> addLeafNodes(ifElseNode.getUid(), successors.getLeafs(), graph));

      // a root node for the following if-else-statements
      String ifRootId = String.valueOf(System.nanoTime());
      graph.addNode(ifRootId);
      // attach this node to the parent node
      graph.addEdge(parentUid + "_" + ifRootId, parentUid, ifRootId);
      // now create nodes for the if-Statement and all of its successors
      //      graph.addNode(ifElseNode.getUid());
      //      graph.addEdge(ifRootId + "_" + ifElseNode.getUid(), ifRootId, ifElseNode.getUid());
      addLeafNodes(ifRootId, ifElseNode.getLeafs(), graph);
      // now the successors
      ien.getSuccessors().forEach(suc -> {
        graph.addNode(suc.getUid());
        graph.addEdge(ifRootId + "_" + suc.getUid(), ifRootId, suc.getUid());
        addLeafNodes(suc.getUid(), suc.getLeafs(), graph);
      });
    });
  }

  private String provideEdgeLabel(CallHierarchyNode node)
  {
    if (node.getParent() == null) {
      return "";
    }

    if (node.getParent() instanceof IfElseNode) {
      return ((IfElseNode) node.getParent()).getScopeName();
    }

    return "";
  }

  class AdditionalGraphInfoRenderer implements LayerRenderer
  {
    private CallHierarchyNode root;

    public AdditionalGraphInfoRenderer(CallHierarchyNode root)
    {
      super();
      this.root = root;
    }

    public AdditionalGraphInfoRenderer(List<CallHierarchyNode> scanResult)
    {
      root = new CallHierarchyNode();
      scanResult.forEach(root::addLeaf);
    }

    @Override
    public void render(Graphics2D graphics, GraphicGraph graph, double px2Gu, int widthPx, int heightPx, double minXGu,
        double minYGu, double maxXGu, double maxYGu)
    {
      graphics.setColor(Color.BLACK);
      graphics.drawString("Leafs: " + root.countLeafs(), 30, 30);
      graphics.drawString("Branches: " + root.countBranches(), 30, 50);
      graphics.drawString("Execution Paths: " + root.countExecutionPaths(true), 30, 70);
    }

  }

  class ZoomAndPanKeyAdapter extends KeyAdapter
  {
    private float currZoom = 1.0f;

    private Point3 viewCenter;

    private Camera camera;

    public ZoomAndPanKeyAdapter(Viewer viewer)
    {
      super();
      camera = viewer.getDefaultView().getCamera();
      viewCenter = camera.getViewCenter();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
      if (e.getKeyChar() == '-') {
        currZoom += 0.1;
        camera.setViewPercent(currZoom);
      }

      if (e.getKeyChar() == '+') {
        currZoom -= 0.1;
        camera.setViewPercent(currZoom);
      }

      if (e.getKeyChar() == 'w') {
        viewCenter.y += 0.1f;
        camera.setViewCenter(viewCenter.x, viewCenter.y, viewCenter.z);
      }

      if (e.getKeyChar() == 's') {
        viewCenter.y -= 0.1f;
        camera.setViewCenter(viewCenter.x, viewCenter.y, viewCenter.z);
      }

      if (e.getKeyChar() == 'a') {
        viewCenter.x -= 0.1f;
        camera.setViewCenter(viewCenter.x, viewCenter.y, viewCenter.z);
      }

      if (e.getKeyChar() == 'd') {
        viewCenter.x += 0.1f;
        camera.setViewCenter(viewCenter.x, viewCenter.y, viewCenter.z);
      }
    }
  }
}
