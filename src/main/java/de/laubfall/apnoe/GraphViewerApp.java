package de.laubfall.apnoe;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import de.laubfall.apnoe.hie.CallHierarchyNode;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;
import de.laubfall.apnoe.hie.IfElseNode;

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
    
    Graph graph = new SingleGraph("GraphViewerApp");
    graph.addNode(result.getUid()).addAttribute("ui.label", result.getScopeName());
    addLeafNodes(result.getUid(), result.getLeafs(), graph);
    
    graph.display();
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
      graph.addNode(ifElseNode.getUid());
      graph.addEdge(parentUid + "_" + ifElseNode.getUid(), parentUid, ifElseNode.getUid());

      addLeafNodes(ifElseNode.getUid(), ifElseNode.getLeafs(), graph);

      final IfElseNode ien = (IfElseNode) ifElseNode;
      ien.getSuccessors().forEach(successors -> addLeafNodes(ifElseNode.getUid(), successors.getLeafs(), graph));
    });
  }
  
  private String provideEdgeLabel(CallHierarchyNode node) {
    if(node.getParent() == null) {
      return "";
    }
    
    if(node.getParent() instanceof IfElseNode) {
      return ((IfElseNode)node.getParent()).getScopeName();
    }
    
    return "";
  }
}
