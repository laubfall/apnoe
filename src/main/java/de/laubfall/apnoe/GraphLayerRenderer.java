package de.laubfall.apnoe;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.graphicGraph.StyleGroup;
import org.graphstream.ui.swingViewer.LayerRenderer;
import org.graphstream.ui.swingViewer.util.Camera;
import org.graphstream.ui.swingViewer.util.GraphMetrics;

/**
 * Actually not used by application.
 * 
 * Place to make some tests with own graph overlay rendering. That means render something depending on a node or an edge.
 * 
 * @author Daniel
 *
 */
public class GraphLayerRenderer implements LayerRenderer
{
  private Camera camera;
  
  public GraphLayerRenderer(Camera camera)
  {
    super();
    this.camera = camera;
  }

  @Override
  public void render(Graphics2D graphics, GraphicGraph graph, double px2Gu, int widthPx, int heightPx, double minXGu,
      double minYGu, double maxXGu, double maxYGu)
  {
    // Testcode
    GraphicNode node = (GraphicNode) graph.getStyleGroups().getNode("app");
    StyleGroup nodeStyle = graph.getStyleGroups().getStyleFor(node);
    
    GraphMetrics metrics = camera.getMetrics();
    double nodeWidth = metrics.lengthToGu(nodeStyle.getSize(), 0);
    
    Point3 p3 = camera.transformGuToPx(node.x, node.y, node.z);
    Ellipse2D shape = new Ellipse2D.Double(p3.x, p3.y, nodeWidth + 15, nodeWidth + 15);
    graphics.setColor(Color.CYAN);
    graphics.fill(shape);
  }

}
