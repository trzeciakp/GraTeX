package pl.edu.agh.gratex.utils;

import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Geometry {

    public static Point calculateEdgeExitPoint(Vertex vertex, double angle) {
        Point result = new Point();

        if (vertex.getShape() == 1) {
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

            if (vertex.getShape() == 2) {
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
            } else if (vertex.getShape() == 3) {
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

            } else if (vertex.getShape() == 4) {
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
            } else {
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

    public static Shape getVertexShape(int nVertices, int radius, int posX, int posY) {
        Shape result = null;

        if (nVertices == 2) {
            return new Ellipse2D.Double(posX - radius, posY - radius, 2 * radius, 2 * radius);
        }

        if (nVertices == 3) {
            int a = (int) Math.round(Math.sqrt(3) / 2 * radius);
            int x[] = new int[]{posX - a, posX + a, posX};
            int y[] = new int[]{posY + radius / 2, posY + radius / 2, posY - radius};
            result = new Polygon(x, y, 3);
        } else if (nVertices == 4) {
            int a = (int) Math.round(Math.sqrt(2) * radius / 2);
            int x[] = new int[]{posX - a, posX + a, posX + a, posX - a};
            int y[] = new int[]{posY - a, posY - a, posY + a, posY + a};
            result = new Polygon(x, y, 4);
        } else if (nVertices == 5) {
            double a = 2 * radius * Math.cos(Math.toRadians(54));
            int x[] = new int[5];
            x[0] = posX;
            x[1] = posX - (int) Math.round(a * Math.sin(Math.toRadians(54)));
            x[2] = posX - (int) Math.round(a / 2);
            x[3] = posX + (int) Math.round(a / 2);
            x[4] = posX + (int) Math.round(a * Math.sin(Math.toRadians(54)));
            int y[] = new int[5];
            y[0] = posY - radius;
            y[1] = posY - radius + (int) Math.round(a * Math.cos(Math.toRadians(54)));
            y[2] = posY + (int) Math.round(radius * Math.sin(Math.toRadians(54)));
            y[3] = posY + (int) Math.round(radius * Math.sin(Math.toRadians(54)));
            y[4] = posY - radius + (int) Math.round(a * Math.cos(Math.toRadians(54)));
            result = new Polygon(x, y, 5);
        } else if (nVertices == 6) {
            int h = (int) Math.round(Math.sqrt(3) * radius / 2);
            int x[] = new int[]{posX - radius, posX - radius / 2, posX + radius / 2, posX + radius, posX + radius / 2, posX - radius / 2};
            int y[] = new int[]{posY, posY + h, posY + h, posY, posY - h, posY - h};
            result = new Polygon(x, y, 6);
        }

        return result;
    }

	/*public static double calculateDistanceFromOutline(Polygon polygon, Point point, boolean horizontal)
    {
		if (horizontal)
		{
			double result = 5000;
			for (int i = 0; i < polygon.npoints; i++)
			{
				double distance = point.distance(new Point(polygon.xpoints[i], polygon.ypoints[i]));
				if (distance < result)
				{
					result = distance;
				}
			}

			return result;
		}
		else
		{
			return Math.min(Line2D.ptLineDist((double) polygon.xpoints[0], (double) polygon.ypoints[0], (double) polygon.xpoints[1],
					(double) polygon.ypoints[1], (double) point.x, (double) point.y), Line2D.ptLineDist((double) polygon.xpoints[2],
					(double) polygon.ypoints[2], (double) polygon.xpoints[3], (double) polygon.ypoints[3], (double) point.x, (double) point.y));
		}
	}*/

    public static boolean checkArcLineIntersection(Point arcMiddle, Point p1, Point p2, Point l1, Point l2) {
        p1 = new Point(p1.x - arcMiddle.x, p1.y - arcMiddle.y);
        p2 = new Point(p2.x - arcMiddle.x, p2.y - arcMiddle.y);
        l1 = new Point(l1.x - arcMiddle.x, l1.y - arcMiddle.y);
        l2 = new Point(l2.x - arcMiddle.x, l2.y - arcMiddle.y);
        arcMiddle = new Point(0, 0);

        Point i1 = new Point();
        Point i2 = new Point();

        if (l1.x == l2.x) {
            i1.x = l1.x;
            i2.x = l1.x;
            i1.y = (int) Math.round(Math.sqrt(p1.distance(arcMiddle) * p1.distance(arcMiddle) - i1.x * i1.x));
            i2.y = -i1.y;

            if (i1.y < Math.min(l1.y, l2.y) || i1.y > Math.max(l1.y, l2.y)) {
                i1 = null;
            }
            if (i2.y < Math.min(l1.y, l2.y) || i2.y > Math.max(l1.y, l2.y)) {
                i2 = null;
            }
        } else {
            i1.y = l1.y;
            i2.y = l1.y;
            i1.x = (int) Math.round(Math.sqrt(p1.distance(arcMiddle) * p1.distance(arcMiddle) - i1.y * i1.y));
            i2.x = -i1.x;

            if (i1.x < Math.min(l1.x, l2.x) || i1.x > Math.max(l1.x, l2.x)) {
                i1 = null;
            }
            if (i2.x < Math.min(l1.x, l2.x) || i2.x > Math.max(l1.x, l2.x)) {
                i2 = null;
            }
        }

        if (i1 != null) {
            if (i1.distance(p1) < p1.distance(p2) && i1.distance(p2) < p1.distance(p2)) {
                return true;
            }
        }
        if (i2 != null) {
            if (i2.distance(p1) < p1.distance(p2) && i2.distance(p2) < p1.distance(p2)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkArcRectangleIntersection(Edge edge, Rectangle area) {
        if (area.contains(edge.getInPoint()) || area.contains(edge.getOutPoint())) {
            return true;
        }

        if (edge.getVertexA() == edge.getVertexB()) {
            int x = (int) edge.getArc().x;
            int y = (int) edge.getArc().y;
            int width = (int) edge.getArc().width;
            int height = (int) edge.getArc().height;

            if (edge.getRelativeEdgeAngle() == 0) {
                x = edge.getOutPoint().x;
                width -= (edge.getOutPoint().x - (int) edge.getArc().x);
            } else if (edge.getRelativeEdgeAngle() == 90) {
                height = edge.getOutPoint().y - y;
            } else if (edge.getRelativeEdgeAngle() == 180) {
                width = edge.getOutPoint().x - x;
            } else {
                y = edge.getOutPoint().y;
                height -= (edge.getOutPoint().y - (int) edge.getArc().y);
            }

            return area.intersects(new Rectangle(x, y, width, height));
        } else {
            Point p1 = new Point(area.x, area.y);
            Point p2 = new Point(area.x + area.width, area.y);
            Point p3 = new Point(area.x + area.width, area.y + area.height);
            Point p4 = new Point(area.x, area.y + area.height);

            if (checkArcLineIntersection(edge.getArcMiddle(), edge.getOutPoint(), edge.getInPoint(), p1, p2)) {
                return true;
            } else if (checkArcLineIntersection(edge.getArcMiddle(), edge.getOutPoint(), edge.getInPoint(), p2, p3)) {
                return true;
            } else if (checkArcLineIntersection(edge.getArcMiddle(), edge.getOutPoint(), edge.getInPoint(), p3, p4)) {
                return true;
            } else if (checkArcLineIntersection(edge.getArcMiddle(), edge.getOutPoint(), edge.getInPoint(), p4, p1)) {
                return true;
            }
        }

        return false;
    }

    public static Point rotatePoint(Point center, Point p, double angle) {
        double[] pt = {p.x, p.y};
        AffineTransform.getRotateInstance(Math.toRadians(360 - angle), center.x, center.y).transform(pt, 0, pt, 0, 1);
        return new Point((int) Math.round(pt[0]), (int) Math.round(pt[1]));
    }
}
