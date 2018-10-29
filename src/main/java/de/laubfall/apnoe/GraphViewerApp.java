package de.laubfall.apnoe;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import de.laubfall.apnoe.hie.CallHierachyResult;
import de.laubfall.apnoe.hie.HierarchyAnalyzerService;
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
    final CallHierachyResult result = has.analyze("src/test/java/de/laubfall/apnoe/dummy/A.java", "main");

    Graph graph = new SingleGraph("A.java");
    graph.addNode(result.getUid());
    addLeafNodes(result, graph);

    graph.display();
  }

  private final void addLeafNodes(CallHierachyResult parent, final Graph graph)
  {

    for (CallHierachyResult l : parent.getLeafs()) {
      final Node leafNode = graph.addNode(l.getUid());
      leafNode.addAttribute("ui.label", l.getScopeName());
      graph.addEdge(parent.getUid() + "_" + l.getUid(), parent.getUid(), l.getUid());
      
      addLeafNodes(l, graph);
    }

  }
}
