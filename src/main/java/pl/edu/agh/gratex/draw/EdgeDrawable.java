package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;
import java.awt.geom.Arc2D;

/**
 *
 */
public class EdgeDrawable implements Drawable {
    @Override
    public void draw(GraphElement graphElement, Graphics g2d, boolean dummy) {
        Edge edge = (Edge) graphElement;
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        Graphics2D g = (Graphics2D) g2d.create();

        EdgeUtils.updatePosition(edge);

        if (vertexA != vertexB) {
            if (edge.getRelativeEdgeAngle() != 0) {
                if (edge.getGraph().getGeneralController().getSelectionController().selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.draw(edge.getArc());
                }

                g.setColor(edge.getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(edge.getLineColor()));
                }
                g.setStroke(DrawingTools.getStroke(edge.getLineWidth(), edge.getLineType(), 0.0));
                g.draw(edge.getArc());
            } else {
                if (edge.getGraph().getGeneralController().getSelectionController().selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.drawLine(vertexA.getPosX(), vertexA.getPosY(), vertexB.getPosX(), vertexB.getPosY());
                }

                g.setColor(edge.getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(edge.getLineColor()));
                }
                g.setStroke(DrawingTools.getStroke(edge.getLineWidth(), edge.getLineType(), 0.0));
                g.drawLine(vertexA.getPosX(), vertexA.getPosY(), vertexB.getPosX(), vertexB.getPosY());
            }
            GeneralController generalController = edge.getGraph().getGeneralController();
            if (((generalController.getSelectionController().selectionContains(edge) && generalController.getSelectionController().selectionSize() == 1) ||
                    generalController.getMouseController().isEdgeCurrentlyAdded(edge)) && edge.getRelativeEdgeAngle() != 0) {
                drawAngleVisualisation(edge, g);
            }
        } else {
            if (edge.getGraph().getGeneralController().getSelectionController().selectionContains(edge)) {
                g.setColor(Const.SELECTION_COLOR);
                Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                g.setStroke(drawingStroke);
                g.draw(edge.getArc());
            }

            g.setColor(edge.getLineColor());
            if (dummy) {
                g.setColor(DrawingTools.getDummyColor(edge.getLineColor()));
            }
            g.setStroke(DrawingTools.getStroke(edge.getLineWidth(), edge.getLineType(), 0.0));
            g.draw(edge.getArc());
        }

        if (edge.isDirected()) {
            if (edge.getArrowTypeENUM() == ArrowType.BASIC) {
                if (edge.getGraph().getGeneralController().getSelectionController().selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.drawLine(edge.getArrowLine1()[0], edge.getArrowLine1()[1], edge.getArrowLine1()[2], edge.getArrowLine1()[3]);
                    g.drawLine(edge.getArrowLine2()[0], edge.getArrowLine2()[1], edge.getArrowLine2()[2], edge.getArrowLine2()[3]);
                }

                g.setColor(edge.getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(edge.getLineColor()));
                }
                g.setStroke(new BasicStroke(edge.getLineWidth()));
                g.drawLine(edge.getArrowLine1()[0], edge.getArrowLine1()[1], edge.getArrowLine1()[2], edge.getArrowLine1()[3]);
                g.drawLine(edge.getArrowLine2()[0], edge.getArrowLine2()[1], edge.getArrowLine2()[2], edge.getArrowLine2()[3]);
            } else {
                if (edge.getGraph().getGeneralController().getSelectionController().selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.fillPolygon(new Polygon(new int[]{edge.getArrowLine1()[0], edge.getArrowLine1()[2], edge.getArrowLine2()[0]},
                            new int[]{edge.getArrowLine1()[1], edge.getArrowLine1()[3], edge.getArrowLine2()[1]}, 3));
                }

                g.setColor(edge.getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(edge.getLineColor()));
                }
                g.setStroke(new BasicStroke(edge.getLineWidth()));
                g.fillPolygon(new Polygon(new int[]{edge.getArrowLine1()[0], edge.getArrowLine1()[2], edge.getArrowLine2()[0]},
                        new int[]{edge.getArrowLine1()[1], edge.getArrowLine1()[3], edge.getArrowLine2()[1]}, 3));
            }
        }

        g.dispose();
    }


    private static void drawAngleVisualisation(Edge edge, Graphics2D g) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        g.setColor(Color.gray);
        g.setStroke(DrawingTools.getStroke(2, LineType.DASHED, 0.0));
        g.drawLine(vertexA.getPosX(), vertexA.getPosY(), vertexB.getPosX(), vertexB.getPosY());

        Point m = new Point((vertexA.getPosX() + vertexB.getPosX()) / 2, (vertexA.getPosY() + vertexB.getPosY()) / 2);
        double d = 2 * m.distance(vertexA.getPosX(), vertexB.getPosY());

        Point p1 = new Point((int) Math.round(vertexA.getPosX() + (d / (2.5 * vertexA.getRadius())) * (edge.getOutPoint().x - vertexA.getPosX())),
                (int) Math.round(vertexA.getPosY() + (d / (2.5 * vertexA.getRadius())) * (edge.getOutPoint().y - vertexA.getPosY())));
        Point p2 = new Point((int) Math.round(vertexB.getPosX() + (d / (2.5 * vertexB.getRadius())) * (edge.getInPoint().x - vertexB.getPosX())),
                (int) Math.round(vertexB.getPosY() + (d / (2.5 * vertexB.getRadius())) * (edge.getInPoint().y - vertexB.getPosY())));
        g.drawLine(vertexA.getPosX(), vertexA.getPosY(), p1.x, p1.y);
        g.drawLine(vertexB.getPosX(), vertexB.getPosY(), p2.x, p2.y);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
        FontMetrics fm = g.getFontMetrics();
        String alphaText = Character.toString('\u03B1');

        int arcAngle = edge.getRelativeEdgeAngle();
        if (arcAngle > 180) {
            arcAngle -= 360;
            alphaText = '-' + Character.toString('\u03B1');
        }
        double angle = Math.toRadians(edge.getOutAngle() - arcAngle / 2);
        int x = vertexA.getPosX() - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
        int y = vertexA.getPosY() + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
        g.drawString(alphaText, x, y);
        angle = Math.toRadians(edge.getInAngle() + arcAngle / 2);
        x = vertexB.getPosX() - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
        y = vertexB.getPosY() + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
        g.drawString(alphaText, x, y);
        g.draw(new Arc2D.Double(vertexA.getPosX() - (d / 3), vertexA.getPosY() - (d / 3), d / 1.5, d / 1.5, edge.getOutAngle(), -arcAngle, Arc2D.OPEN));
        g.draw(new Arc2D.Double(vertexB.getPosX() - (d / 3), vertexB.getPosY() - (d / 3), d / 1.5, d / 1.5, edge.getInAngle(), arcAngle, Arc2D.OPEN));
    }

}
