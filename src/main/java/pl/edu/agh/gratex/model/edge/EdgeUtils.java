package pl.edu.agh.gratex.model.edge;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.awt.geom.*;

public class EdgeUtils {
    // Calculates the angle of edge (in degrees) according to cursor location and type of edge
    public static int getEdgeAngleFromCursorLocation(Edge edge, int mouseX, int mouseY) {
        int intAngle;
        if (edge.getVertexA() != edge.getVertexB()) {
            // Edge is not a loop
            double angle;
            if (Point.distance(mouseX, mouseY, edge.getVertexA().getPosX(), edge.getVertexA().getPosY()) <
                    Point.distance(mouseX, mouseY, edge.getVertexB().getPosX(), edge.getVertexB().getPosY())) {
                // Cursor is closer to vertex A
                angle = Math.toDegrees(Math.atan2(
                        mouseX - edge.getVertexA().getPosX(),
                        mouseY - edge.getVertexA().getPosY())) + 270;
                angle = angle - Math.toDegrees(Math.atan2(
                        edge.getVertexB().getPosX() - edge.getVertexA().getPosX(),
                        edge.getVertexB().getPosY() - edge.getVertexA().getPosY())) + 90;
            } else {
                // Cursor is closer to vertex B
                angle = Math.toDegrees(Math.atan2(
                        mouseX - edge.getVertexB().getPosX(),
                        mouseY - edge.getVertexB().getPosY())) + 270;
                angle = -angle + Math.toDegrees(Math.atan2(
                        edge.getVertexA().getPosX() - edge.getVertexB().getPosX(),
                        edge.getVertexA().getPosY() - edge.getVertexB().getPosY())) + 270;
            }
            intAngle = ((((int) Math.floor(angle) / 5) * 5) + 720) % 360;
            if (intAngle > 60) {
                if (intAngle < 180) {
                    intAngle = 60;
                } else if (intAngle < 300) {
                    intAngle = 300;
                }

            }
        } else {
            // Edge is a loop
            double angle = (Math.toDegrees(Math.atan2(
                    mouseX - edge.getVertexB().getPosX(),
                    mouseY - edge.getVertexB().getPosY())) + 270) % 360;
            intAngle = ((int) Math.floor((angle + 45) / 90) % 4) * 90;
        }
        return intAngle;
    }

    public static void updateLocation(Edge edge) {
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

            if (vertexA.getShape() == ShapeType.TRIANGLE) {
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
            } else if (vertexA.getShape() == ShapeType.SQUARE) {
                offsetRate = 0.5;
                arrowPosHorizRate = 0.72;
                arrowPosVertRate = 0.265;
            } else if (vertexA.getShape() == ShapeType.PENTAGON) {
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
            } else if (vertexA.getShape() == ShapeType.HEXAGON) {
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

    private static void calculateInOutPoints(Edge edge) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        double beta = (Math.toDegrees(Math.atan2(vertexB.getPosX() - vertexA.getPosX(), vertexB.getPosY() - vertexA.getPosY()))) + 270;
        edge.setOutPoint(calculateEdgeExitPoint(vertexA, edge.getRelativeEdgeAngle() + beta));
        edge.setInPoint(calculateEdgeExitPoint(vertexB, 180 - edge.getRelativeEdgeAngle() + beta));
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

        if (edge.getArrowType() == ArrowType.FILLED) {

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

    public static Point calculateEdgeExitPoint(Vertex vertex, double angle) {
        Point result = new Point();

        if (vertex.getShape() == ShapeType.CIRCLE) {
            result.x = vertex.getPosX() + (int) Math.round((vertex.getRadius() + vertex.getLineWidth() / 2) * Math.cos(Math.toRadians(-angle)));
            result.y = vertex.getPosY() + (int) Math.round((vertex.getRadius() + vertex.getLineWidth() / 2) * Math.sin(Math.toRadians(-angle)));
        } else {
            double radius = vertex.getRadius() + vertex.getLineWidth() / 2;
            angle = (angle + 1080) % 360;
            double a1 = Math.cos(Math.toRadians(angle));
            double b1 = -Math.sin(Math.toRadians(angle));
            double c1 = 0.0;

            double a2;
            double b2;
            double c2;

            if (vertex.getShape() == ShapeType.TRIANGLE) {
                a2 = 1;
                b2 = Math.sqrt(3);
                c2 = radius;

                if (angle >= 90 && angle < 210) {
                    b2 = -Math.sqrt(3);
                    c2 = radius;
                }
                if (angle >= 210 && angle < 330) {
                    b2 = 0.0;
                    c2 = -radius / 2;
                }
            } else if (vertex.getShape() == ShapeType.SQUARE) {
                a2 = 0.0;
                b2 = 1;
                c2 = radius / Math.sqrt(2);

                if (angle >= 45 && angle < 135) {
                    a2 = 1;
                    b2 = 0.0;
                    c2 = radius / Math.sqrt(2);
                }
                if (angle >= 135 && angle < 225) {
                    a2 = 0.0;
                    b2 = 1;
                    c2 = -radius / Math.sqrt(2);
                }
                if (angle >= 225 && angle < 315) {
                    a2 = 1;
                    b2 = 0.0;
                    c2 = -radius / Math.sqrt(2);
                }

            } else if (vertex.getShape() == ShapeType.PENTAGON) {
                a2 = 1;
                b2 = -Math.tan(Math.toRadians(36));
                c2 = radius;

                if (angle >= 162 && angle < 234) {
                    a2 = 1;
                    b2 = -Math.tan(Math.toRadians(108));
                    c2 = -radius * Math.sin(Math.toRadians(54)) + Math.tan(Math.toRadians(108)) * radius * Math.cos(Math.toRadians(54));
                }
                if (angle >= 234 && angle < 306) {
                    a2 = 1;
                    b2 = 0.0;
                    c2 = -radius * Math.sin(Math.toRadians(54));
                }
                if ((angle >= 306 && angle < 360) || (angle >= 0 && angle < 18)) {
                    a2 = 1;
                    b2 = Math.tan(Math.toRadians(108));
                    c2 = -radius * Math.sin(Math.toRadians(54)) + Math.tan(Math.toRadians(108)) * radius * Math.cos(Math.toRadians(54));
                }
                if (angle >= 18 && angle < 90) {
                    a2 = 1;
                    b2 = Math.tan(Math.toRadians(36));
                    c2 = radius;
                }
            } else { // vertex.getShape() == ShapeType.HEXAGON)
                a2 = 1;
                b2 = Math.sqrt(3);
                c2 = radius * Math.sqrt(3);

                if (angle >= 60 && angle < 120) {
                    a2 = 1;
                    b2 = 0.0;
                    c2 = radius * Math.sqrt(3) / 2;
                }
                if (angle >= 120 && angle < 180) {
                    a2 = 1;
                    b2 = -Math.sqrt(3);
                    c2 = radius * Math.sqrt(3);
                }
                if (angle >= 180 && angle < 240) {
                    a2 = 1;
                    b2 = Math.sqrt(3);
                    c2 = -radius * Math.sqrt(3);
                }
                if (angle >= 240 && angle < 300) {
                    a2 = 1;
                    b2 = 0.0;
                    c2 = -radius * Math.sqrt(3) / 2;
                }
                if (angle >= 300 && angle < 360) {
                    a2 = 1;
                    b2 = -Math.sqrt(3);
                    c2 = -radius * Math.sqrt(3);
                }
            }

            result.x = vertex.getPosX() + (int) Math.round((a1 * c2 - a2 * c1) / (a1 * b2 - a2 * b1));
            result.y = vertex.getPosY() - (int) Math.round((c1 * b2 - c2 * b1) / (a1 * b2 - a2 * b1));
        }

        return result;
    }
}
