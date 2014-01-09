package pl.edu.agh.gratex.model.edge;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;
import java.awt.geom.Arc2D;

/**
 *
 */
public class EdgeDrawer implements Drawer {

    private SelectionController selectionController;

    public EdgeDrawer(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics g2d) {
        Edge edge = (Edge) graphElement;
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        Graphics2D g = (Graphics2D) g2d.create();

        edge.updateLocation();

        if (vertexA != vertexB) {
            if (edge.getRelativeEdgeAngle() != 0) {
                if (selectionController.selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.draw(edge.getArc());
                }

                g.setColor(DrawingTools.getDrawingColor(edge.getLineColor(), edge.isDummy()));
                g.setStroke(DrawingTools.getStroke(edge.getLineType(), edge.getLineWidth(), 0.0));
                g.draw(edge.getArc());
            } else {
                if (selectionController.selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() + 2 * Const.EDGE_SELECTION_MARGIN);
                    g.setStroke(drawingStroke);
                    g.drawLine(edge.getOutPoint().x, edge.getOutPoint().y, edge.getInPoint().x, edge.getInPoint().y);
                }

                g.setColor(DrawingTools.getDrawingColor(edge.getLineColor(), edge.isDummy()));
                g.setStroke(DrawingTools.getStroke(edge.getLineType(), edge.getLineWidth(), 0.0));
                g.drawLine(edge.getOutPoint().x, edge.getOutPoint().y, edge.getInPoint().x, edge.getInPoint().y);
            }
            if (shouldVisualizeAngle(edge)) {
                drawAngleVisualisation(edge, g);
            }
        } else {
            if (selectionController.selectionContains(edge)) {
                g.setColor(Const.SELECTION_COLOR);
                Stroke drawingStroke = new BasicStroke(edge.getLineWidth() + 2 * Const.EDGE_SELECTION_MARGIN);
                g.setStroke(drawingStroke);
                g.draw(edge.getArc());
            }

            g.setColor(DrawingTools.getDrawingColor(edge.getLineColor(), edge.isDummy()));
            g.setStroke(DrawingTools.getStroke(edge.getLineType(), edge.getLineWidth(), 0.0));
            g.draw(edge.getArc());
        }

        if (edge.isDirected()) {
            if (edge.getArrowType() == ArrowType.BASIC) {
                if (selectionController.selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.drawLine(edge.getArrowLine1()[0], edge.getArrowLine1()[1], edge.getArrowLine1()[2], edge.getArrowLine1()[3]);
                    g.drawLine(edge.getArrowLine2()[0], edge.getArrowLine2()[1], edge.getArrowLine2()[2], edge.getArrowLine2()[3]);
                }

                g.setColor(DrawingTools.getDrawingColor(edge.getLineColor(), edge.isDummy()));
                g.setStroke(new BasicStroke(edge.getLineWidth()));
                g.drawLine(edge.getArrowLine1()[0], edge.getArrowLine1()[1], edge.getArrowLine1()[2], edge.getArrowLine1()[3]);
                g.drawLine(edge.getArrowLine2()[0], edge.getArrowLine2()[1], edge.getArrowLine2()[2], edge.getArrowLine2()[3]);
            } else {
                if (selectionController.selectionContains(edge)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(edge.getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.fillPolygon(new Polygon(new int[]{edge.getArrowLine1()[0], edge.getArrowLine1()[2], edge.getArrowLine2()[0]},
                            new int[]{edge.getArrowLine1()[1], edge.getArrowLine1()[3], edge.getArrowLine2()[1]}, 3));
                }

                g.setColor(DrawingTools.getDrawingColor(edge.getLineColor(), edge.isDummy()));
                g.setStroke(new BasicStroke(edge.getLineWidth()));
                g.fillPolygon(new Polygon(new int[]{edge.getArrowLine1()[0], edge.getArrowLine1()[2], edge.getArrowLine2()[0]},
                        new int[]{edge.getArrowLine1()[1], edge.getArrowLine1()[3], edge.getArrowLine2()[1]}, 3));
            }
        }

        g.dispose();
    }

    private boolean shouldVisualizeAngle(Edge edge) {
        return selectionController.selectionContains(edge) &&
                selectionController.selectionSize() == 1 &&
                edge.getRelativeEdgeAngle() != 0 &&
                !edge.isLoop();
    }


    private void drawAngleVisualisation(Edge edge, Graphics2D g) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        g.setColor(Const.ANGLE_VISUALIZATION_COLOR);
        g.setStroke(Const.ANGLE_VISUALIZATION_STROKE);
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
        String alphaText = StringLiterals.ALPHA;

        int arcAngle = edge.getRelativeEdgeAngle();
        if (arcAngle > 180) {
            arcAngle -= 360;
            alphaText = "-" + StringLiterals.ALPHA;
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
