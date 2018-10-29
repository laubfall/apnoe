package de.laubfall.apnoe;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import de.laubfall.apnoe.hie.CallHierarchyNode;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;
import de.laubfall.apnoe.hie.IfElseNode;
import de.laubfall.apnoe.hie.TypeSolverFactory;

public class GraphViewerApp
{

  public static void main(String[] args)
  {
    TypeSolverFactory.get().create();

    final GraphViewerApp gva = new GraphViewerApp();
    gva.createGraphAndShowIt();
  }

  public final void createGraphAndShowIt()
  {
    final HierarchyAnalyzerService has = new HierarchyAnalyzerService();
    final CallHierarchyNode result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/IfElseSample.java", "main");

    Graph graph = new SingleGraph("A.java");
    graph.addNode(result.getUid());
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
      graph.addEdge(parentUid + "_" + l.getUid(), parentUid, l.getUid());

      addLeafNodes(l.getUid(), l.getLeafs(), graph);
    }

    // now take care of the if-Statements. These were not handled before.
    leafs.stream().filter(chr -> chr.getScopeName().equals("if")).forEach(ifElseNode -> {
      final Node leafNode = graph.addNode(ifElseNode.getUid());
      leafNode.addAttribute("ui.label", "if-else-ding");
      graph.addEdge(parentUid + "_" + ifElseNode.getUid(), parentUid, ifElseNode.getUid());

      addLeafNodes(ifElseNode.getUid(), ifElseNode.getLeafs(), graph);

      final IfElseNode ien = (IfElseNode) ifElseNode;
      ien.getSuccessors().forEach(successors -> addLeafNodes(ifElseNode.getUid(), successors.getLeafs(), graph));
    });
  }
}
