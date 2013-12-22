package pl.edu.agh.gratex.model.edge;

import com.sun.deploy.xml.GeneralEntity;
import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class EdgeUtils {
    public static boolean intersects(Edge edge, int x, int y) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        Point2D va = new Point(vertexA.getPosX(), vertexA.getPosY());
        Point2D vb = new Point(vertexB.getPosX(), vertexB.getPosY());
        Point2D click = new Point(x, y);

        if (vertexA == vertexB) {
            int r = vertexA.getRadius() + vertexA.getLineWidth() / 2;
            double dx = edge.getArcMiddle().x - x;
            double dy = edge.getArcMiddle().y - y;
            double a = r * 0.75;
            double b = r * 0.375;
            if (edge.getRelativeEdgeAngle() == 90 || edge.getRelativeEdgeAngle() == 270) {
                a = r * 0.375;
                b = r * 0.75;
            }

            double distance = (dx * dx) / (a * a) + (dy * dy) / (b * b);
            return Math.abs(distance - 1) < 1;
        } else {
            if (edge.getRelativeEdgeAngle() != 0) {
                if (Math.abs(click.distance(edge.getArcMiddle()) - edge.getArcRadius()) < 10 + edge.getLineWidth() / 2) {
                    double clickAngle = (Math.toDegrees(Math.atan2(click.getX() - edge.getArcMiddle().x, click.getY() - edge.getArcMiddle().y)) + 270) % 360;
                    if (edge.getArc().extent < 0) {
                        double endAngle = (edge.getArc().start + edge.getArc().extent + 720) % 360;

                        return (clickAngle - endAngle + 720) % 360 < (edge.getArc().start - endAngle + 720) % 360;
                    } else {
                        return (edge.getArc().extent) % 360 > (clickAngle - edge.getArc().start + 720) % 360;
                    }
                }
            } else {
                if ((Math.min(vertexA.getPosX(), vertexB.getPosX()) <= x && Math.max(vertexA.getPosX(), vertexB.getPosX()) >= x)
                        || (Math.min(vertexA.getPosY(), vertexB.getPosY()) <= y && Math.max(vertexA.getPosY(), vertexB.getPosY()) >= y)) {
                    if (click.distance(va) > vertexA.getRadius() && click.distance(vb) > vertexB.getRadius()) {
                        double distance = Line2D.ptLineDist((double) vertexA.getPosX(), (double) vertexA.getPosY(), (double) vertexB.getPosX(),
                                (double) vertexB.getPosY(), (double) x, (double) y);
                        return distance < 12;
                    }
                }
            }
        }

        return false;
    }

    private static void calculateInOutPoints(Edge edge) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        double beta = (Math.toDegrees(Math.atan2(vertexB.getPosX() - vertexA.getPosX(), vertexB.getPosY() - vertexA.getPosY()))) + 270;
        edge.setOutPoint(Geometry.calculateEdgeExitPoint(vertexA, edge.getRelativeEdgeAngle() + beta));
        edge.setInPoint(Geometry.calculateEdgeExitPoint(vertexB, 180 - edge.getRelativeEdgeAngle() + beta));
        edge.setOutAngle((int) Math.round(Math.toDegrees(Math.atan2(edge.getOutPoint().x - vertexA.getPosX(), edge.getOutPoint().y - vertexA.getPosY())) + 270) % 360);
        edge.setInAngle((int) Math.round(Math.toDegrees(Math.atan2(edge.getInPoint().x - vertexB.getPosX(), edge.getInPoint().y - vertexB.getPosY())) + 270) % 360);
    }

    private static void calculateArcParameters(Edge edge) {
        if (edge.getRelativeEdgeAngle() != 0) {
            double mx = (edge.getOutPoint().x + edge.getInPoint().x) / 2;
            double my = (edge.getOutPoint().y + edge.getInPoint().y) / 2;
            double dx = (edge.getInPoint().x - edge.getOutPoint().x) / 2;
            double dy = (edge.getInPoint().y - edge.getOutPoint().y) / 2;
            edge.getArcMiddle().x = (int) Math.round(mx - dy * Math.cos(Math.toRadians(edge.getRelativeEdgeAngle())) / Math.sin(Math.toRadians(edge.getRelativeEdgeAngle())));
            edge.getArcMiddle().y = (int) Math.round(my + dx * Math.cos(Math.toRadians(edge.getRelativeEdgeAngle())) / Math.sin(Math.toRadians(edge.getRelativeEdgeAngle())));
            edge.setArcRadius((int) Math.round(Math.sqrt((edge.getArcMiddle().x - edge.getOutPoint().x) * (edge.getArcMiddle().x - edge.getOutPoint().x) +
                    (edge.getArcMiddle().y - edge.getOutPoint().y) * (edge.getArcMiddle().y - edge.getOutPoint().y))));
        }
    }

    private static void updateArrowPosition(Edge edge) {
        int ax;
        int ay;
        int bx = edge.getInPoint().x;
        int by = edge.getInPoint().y;

        if (edge.getVertexA() == edge.getVertexB()) {
            ax = 2 * bx - edge.getVertexA().getPosX();
            ay = 2 * by - edge.getVertexA().getPosY();
        } else if (edge.getRelativeEdgeAngle() != 0) {
            int x1 = bx - (edge.getArcMiddle().y - by);
            int y1 = by + (edge.getArcMiddle().x - bx);
            int x2 = bx + (edge.getArcMiddle().y - by);
            int y2 = by - (edge.getArcMiddle().x - bx);
            if (Point.distance(x1, y1, edge.getVertexB().getPosX(), edge.getVertexB().getPosY()) >
                    Point.distance(x2, y2, edge.getVertexB().getPosX(), edge.getVertexB().getPosY())) {
                ax = x1;
                ay = y1;
            } else {
                ax = x2;
                ay = y2;
            }
        } else {
            ax = edge.getVertexA().getPosX();
            ay = edge.getVertexA().getPosY();
        }

        int dx = bx - ax;
        int dy = by - ay;

        int[] p1 = new int[]{ax - dy, ay + dx};
        int[] p2 = new int[]{ax + dy, ay - dx};

        if (edge.getArrowTypeENUM() == ArrowType.FILLED) {

            p1 = new int[]{ax - dy / 2, ay + dx / 2};
            p2 = new int[]{ax + dy / 2, ay - dx / 2};
        }

        double part = (Const.ARROW_LENGTH_BASIC + Const.ARROW_LENGTH_FACTOR * edge.getLineWidth())
                / Math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]));

        p1[0] = bx - (int) Math.round(part * (bx - p1[0]));
        p1[1] = by - (int) Math.round(part * (by - p1[1]));
        p2[0] = bx - (int) Math.round(part * (bx - p2[0]));
        p2[1] = by - (int) Math.round(part * (by - p2[1]));

        edge.setArrowLine1(new int[]{p1[0], p1[1], bx, by});
        edge.setArrowLine2(new int[]{p2[0], p2[1], bx, by});
    }

    public static void updatePosition(Edge edge) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        if (vertexA != vertexB) {
            calculateInOutPoints(edge);
            calculateArcParameters(edge);

            if (edge.getRelativeEdgeAngle() != 0) {
                int x1 = edge.getOutPoint().x;
                int y1 = edge.getOutPoint().y;
                int x2 = edge.getInPoint().x;
                int y2 = edge.getInPoint().y;
                int x0 = edge.getArcMiddle().x;
                int y0 = edge.getArcMiddle().y;

                double x = x0 - edge.getArcRadius();
                double y = y0 - edge.getArcRadius();
                double width = 2 * edge.getArcRadius();
                double height = 2 * edge.getArcRadius();
                double startAngle = (Math.toDegrees(Math.atan2(x1 - x0, y1 - y0)) + 270) % 360;
                double endAngle = (Math.toDegrees(Math.atan2(x2 - x0, y2 - y0)) + 270) % 360;

                if (edge.getRelativeEdgeAngle() > 180) {
                    endAngle = (endAngle - startAngle + 360) % 360;
                } else {
                    endAngle = ((endAngle - startAngle + 360) % 360) - 360;
                }

                edge.setArc(new Arc2D.Double(x, y, width, height, startAngle, endAngle, Arc2D.OPEN));
            }
        } else {
            int r = vertexA.getRadius() + vertexA.getLineWidth() / 2;
            double x = 0;
            double y = 0;
            double width = r * 1.5;
            double height = r * 0.75;
            if (edge.getRelativeEdgeAngle() == 90 || edge.getRelativeEdgeAngle() == 270) {
                width = r * 0.75;
                height = r * 1.5;
            }

            double offsetRate = 0.75;
            double arrowPosHorizRate = 0.965;
            double arrowPosVertRate = 0.265;

            if (vertexA.getShape() == 2) {
                offsetRate = 0.375;
                arrowPosHorizRate = 0.84;
                arrowPosVertRate = 0.325;

                if (edge.getRelativeEdgeAngle() == 90) {
                    arrowPosHorizRate = 0.595;
                    arrowPosVertRate = 0.26;
                } else if (edge.getRelativeEdgeAngle() == 180) {
                    arrowPosHorizRate = 0.475;
                    arrowPosVertRate = 0.195;
                } else if (edge.getRelativeEdgeAngle() == 270) {
                    arrowPosHorizRate = 0.505;
                    arrowPosVertRate = 0.15;
                    width /= 2;
                    height /= 2;
                }
            } else if (vertexA.getShape() == 3) {
                offsetRate = 0.5;
                arrowPosHorizRate = 0.72;
                arrowPosVertRate = 0.265;
            } else if (vertexA.getShape() == 4) {
                offsetRate = 0.4375;
                arrowPosHorizRate = 0.755;
                arrowPosVertRate = 0.295;

                if (edge.getRelativeEdgeAngle() == 90) {
                    arrowPosHorizRate = 0.795;
                    arrowPosVertRate = 0.305;
                } else if (edge.getRelativeEdgeAngle() == 180) {
                    arrowPosHorizRate = 0.93;
                    arrowPosVertRate = 0.355;
                } else if (edge.getRelativeEdgeAngle() == 270) {
                    arrowPosHorizRate = 0.815;
                    arrowPosVertRate = 0.335;
                }
            } else if (vertexA.getShape() == 5) {
                offsetRate = 0.625;
                arrowPosHorizRate = 0.85;
                arrowPosVertRate = 0.27;
                if (edge.getRelativeEdgeAngle() == 90 || edge.getRelativeEdgeAngle() == 270) {
                    arrowPosHorizRate = 0.88;
                    arrowPosVertRate = 0.283;
                }
            }

            switch (edge.getRelativeEdgeAngle()) {
                case 0: {
                    x = vertexA.getPosX() + r * offsetRate;
                    y = vertexA.getPosY() - height / 2;
                    edge.setInPoint(new Point((int) Math.round(vertexA.getPosX() + r * arrowPosHorizRate), (int) Math.round(vertexA.getPosY() + r * arrowPosVertRate)));
                    edge.setOutPoint(new Point((int) Math.round(vertexA.getPosX() + r * arrowPosHorizRate),
                            (int) Math.round(vertexA.getPosY() - r * arrowPosVertRate)));
                    break;
                }
                case 90: {
                    x = vertexA.getPosX() - width / 2;
                    y = vertexA.getPosY() - r * (offsetRate + height / r);
                    edge.setInPoint(new Point((int) Math.round(vertexA.getPosX() + r * arrowPosVertRate), (int) Math.round(vertexA.getPosY() - r * arrowPosHorizRate)));
                    edge.setOutPoint(new Point((int) Math.round(vertexA.getPosX() - r * arrowPosVertRate),
                            (int) Math.round(vertexA.getPosY() - r * arrowPosHorizRate)));
                    break;
                }
                case 180: {
                    x = vertexA.getPosX() - r * (offsetRate + width / r);
                    y = vertexA.getPosY() - height / 2;
                    edge.setInPoint(new Point((int) Math.round(vertexA.getPosX() - r * arrowPosHorizRate), (int) Math.round(vertexA.getPosY() - r * arrowPosVertRate)));
                    edge.setOutPoint(new Point((int) Math.round(vertexA.getPosX() - r * arrowPosHorizRate),
                            (int) Math.round(vertexA.getPosY() + r * arrowPosVertRate)));
                    break;
                }
                case 270: {
                    x = vertexA.getPosX() - width / 2;
                    y = vertexA.getPosY() + r * offsetRate;
                    edge.setInPoint(new Point((int) Math.round(vertexA.getPosX() - r * arrowPosVertRate), (int) Math.round(vertexA.getPosY() + r * arrowPosHorizRate)));
                    edge.setOutPoint(new Point((int) Math.round(vertexA.getPosX() + r * arrowPosVertRate),
                            (int) Math.round(vertexA.getPosY() + r * arrowPosHorizRate)));
                    break;
                }
            }
            edge.setArcMiddle(new Point((int) Math.round(x + width / 2), (int) Math.round(y + height / 2)));
            edge.setArc(new Arc2D.Double(x, y, width, height, 0, 360, Arc2D.OPEN));
        }

        if (edge.isDirected()) {
            updateArrowPosition(edge);
        }
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


    public static void draw(Edge edge, Graphics2D g2d, boolean dummy) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        Graphics2D g = (Graphics2D) g2d.create();

        updatePosition(edge);

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
}
