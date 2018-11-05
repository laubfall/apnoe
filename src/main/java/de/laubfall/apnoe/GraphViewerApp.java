package de.laubfall.apnoe;

import java.awt.Graphics2D;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.swingViewer.LayerRenderer;
import org.graphstream.ui.swingViewer.Viewer;

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

  public static void main(String[] args)
  {
    initTypeSolver(args);

    final GraphViewerApp gva = new GraphViewerApp();
    gva.start(argEntryPointSrc(args), argEntryPointMethodName(args));
  }

  @Override
  void start(String pathToEntryPointSourceFile, String entryPointMethodName)
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    final CallHierarchyNode result = has.analyze(pathToEntryPointSourceFile, entryPointMethodName);

    final Graph graph = new SingleGraph("GraphViewerApp");
    graph.addAttribute("ui.quality");
    graph.addAttribute("ui.antialias");
    graph.addNode(result.getUid()).addAttribute("ui.label", result.getScopeName());
    addLeafNodes(result.getUid(), result.getLeafs(), graph);

    final Viewer viewer = graph.display();
    viewer.getDefaultView().setForeLayoutRenderer(new AdditionalGraphInfoRenderer(result));
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
    @Override
    public void render(Graphics2D graphics, GraphicGraph graph, double px2Gu, int widthPx, int heightPx, double minXGu,
        double minYGu, double maxXGu, double maxYGu)
    {
      graphics.drawString("Leafs: " + root.countLeafs(), 30, 30);
      graphics.drawString("Branches: " + root.countBranches(), 30, 50);
      graphics.drawString("Execution Paths: " + root.countExecutionPaths(true), 30, 70);
    }

  }
}
