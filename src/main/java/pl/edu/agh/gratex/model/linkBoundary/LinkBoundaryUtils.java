package pl.edu.agh.gratex.model.linkBoundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.JointLabelPosition;
import pl.edu.agh.gratex.model.properties.LinkLabelPosition;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LinkBoundaryUtils {
    public static void updateLocation(LinkBoundary linkBoundary) {
        Point outPoint = getExitPointFromBoundary(linkBoundary.getBoundaryA(), linkBoundary.getOutAngle());
        linkBoundary.setOutPointX(outPoint.x);
        linkBoundary.setOutPointY(outPoint.y);

        if (linkBoundary.getBoundaryB() != null) {
            Point inPoint = getExitPointFromBoundary(linkBoundary.getBoundaryB(), linkBoundary.getInAngle());
            linkBoundary.setInPointX(inPoint.x);
            linkBoundary.setInPointY(inPoint.y);
        } else {
            double middleX = linkBoundary.getBoundaryA().getTopLeftX() + linkBoundary.getBoundaryA().getWidth() / 2.0;
            double middleY = linkBoundary.getBoundaryA().getTopLeftY() + linkBoundary.getBoundaryA().getHeight() / 2.0;
            double dist = Point.distance(outPoint.x, outPoint.y, middleX, middleY);
            linkBoundary.setInPointX((int) (middleX + (outPoint.x - middleX) * (dist + Const.LINK_BOUNDARY_VISUALISATION_LENGTH) / dist));
            linkBoundary.setInPointY((int) (middleY + (outPoint.y - middleY) * (dist + Const.LINK_BOUNDARY_VISUALISATION_LENGTH) / dist));
        }

        if (linkBoundary.isDirected()) {
            updateArrowPosition(linkBoundary);
        }

        if (linkBoundary.getLabelPosition() != LinkLabelPosition.HIDDEN) {
            FontMetrics fm = new Canvas().getFontMetrics(Const.DEFAULT_FONT);
            int width = fm.stringWidth(linkBoundary.getText());
            int height = fm.getAscent();
            int descent = fm.getDescent();

            int middleX = (linkBoundary.getOutPointX() + linkBoundary.getInPointX()) / 2;
            int middleY = (linkBoundary.getOutPointY() + linkBoundary.getInPointY()) / 2;

            int spacing = Const.LINK_BOUNDARY_LABEL_MARGIN + linkBoundary.getLineWidth() / 2;

            if (linkBoundary.getLabelPosition() == LinkLabelPosition.THROUGH) {
                linkBoundary.setLabelPosX(middleX);
                linkBoundary.setLabelPosY(middleY);
                linkBoundary.setLabelDrawX(middleX - width / 2);
                linkBoundary.setLabelDrawY(middleY - descent + height / 2);
            } else {
                int dx = middleX - linkBoundary.getOutPointX();
                int dy = middleY - linkBoundary.getOutPointY();
                double d = Math.sqrt(dx * dx + dy * dy);
                double k = 1000 / d;

                Line2D linkLine = new Line2D.Double(linkBoundary.getOutPointX(), linkBoundary.getOutPointY(),
                        linkBoundary.getInPointX(), linkBoundary.getInPointY());
                Point2D refPoint = new Point2D.Double(middleX - dy * k, middleY + dx * k);
                double minDist = linkLine.ptLineDist(refPoint.getX() + width / 2.0, refPoint.getY() + height / 2.0);
                double newMinDist = linkLine.ptLineDist(refPoint.getX() - width / 2.0, refPoint.getY() + height / 2.0);
                if (newMinDist < minDist) {
                    minDist = newMinDist;
                }
                newMinDist = linkLine.ptLineDist(refPoint.getX() - width / 2.0, refPoint.getY() - height / 2.0);
                if (newMinDist < minDist) {
                    minDist = newMinDist;
                }
                newMinDist = linkLine.ptLineDist(refPoint.getX() + width / 2.0, refPoint.getY() - height / 2.0);
                if (newMinDist < minDist) {
                    minDist = newMinDist;
                }

                k = (1000 - minDist + spacing) / d;

                Point pointAbove = new Point((int) (middleX - dy * k), (int) (middleY + dx * k));
                Point pointBelow = new Point((int) (middleX + dy * k), (int) (middleY - dx * k));
                if (pointAbove.y > middleY) {
                    pointAbove = new Point((int) (middleX + dy * k), (int) (middleY - dx * k));
                    pointBelow = new Point((int) (middleX - dy * k), (int) (middleY + dx * k));
                }

                if (linkBoundary.getLabelPosition() == LinkLabelPosition.ABOVE) {
                    linkBoundary.setLabelPosX(pointAbove.x);
                    linkBoundary.setLabelPosY(pointAbove.y);
                } else {
                    linkBoundary.setLabelPosX(pointBelow.x);
                    linkBoundary.setLabelPosY(pointBelow.y);
                }

                linkBoundary.setLabelDrawX(linkBoundary.getLabelPosX() - width / 2);
                linkBoundary.setLabelDrawY(linkBoundary.getLabelPosY() - descent + height / 2);
            }
        }
    }

    public static Point getExitPointFromBoundary(Boundary boundary, double angle) {
        double r = Math.sqrt(boundary.getWidth() * boundary.getWidth() + boundary.getHeight() * boundary.getHeight()) / 2.0;
        double x = Math.sin(angle) * r;
        double y = -Math.cos(angle) * r;

        double bottomRightX = (boundary.getWidth() + boundary.getLineWidth()) / 2.0;
        double bottomRightY = (boundary.getHeight() + boundary.getLineWidth()) / 2.0;
        double topLeftX = -bottomRightX;
        double topLeftY = -bottomRightY;

        if (y < topLeftY) {
            double xd = x * (y - topLeftY) / y;
            x = x - xd;
            y = topLeftY;
        } else if (y > bottomRightY) {
            double xd = x * (y - bottomRightY) / y;
            x = x - xd;
            y = bottomRightY;
        } else if (x < topLeftX) {
            double yd = y * (x - topLeftX) / x;
            y = y - yd;
            x = topLeftX;
        } else if (x > bottomRightX) {
            double yd = y * (x - bottomRightX) / x;
            y = y - yd;
            x = bottomRightX;
        }

        double middleX = boundary.getTopLeftX() + boundary.getWidth() / 2.0;
        double middleY = boundary.getTopLeftY() + boundary.getHeight() / 2.0;
        x += middleX;
        y += middleY;
        return new Point((int) x, (int) y);
    }

    private static void updateArrowPosition(LinkBoundary linkBoundary) {
        int ax = linkBoundary.getOutPointX();
        int ay = linkBoundary.getOutPointY();
        int bx = linkBoundary.getInPointX();
        int by = linkBoundary.getInPointY();

        int dx = bx - ax;
        int dy = by - ay;

        int[] p1 = new int[]{ax - dy, ay + dx};
        int[] p2 = new int[]{ax + dy, ay - dx};

        if (linkBoundary.getArrowType() == ArrowType.FILLED) {

            p1 = new int[]{ax - dy / 2, ay + dx / 2};
            p2 = new int[]{ax + dy / 2, ay - dx / 2};
        }

        double part = (Const.ARROW_LENGTH_BASIC + Const.ARROW_LENGTH_FACTOR * linkBoundary.getLineWidth())
                / Math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]));

        p1[0] = bx - (int) Math.round(part * (bx - p1[0]));
        p1[1] = by - (int) Math.round(part * (by - p1[1]));
        p2[0] = bx - (int) Math.round(part * (bx - p2[0]));
        p2[1] = by - (int) Math.round(part * (by - p2[1]));

        linkBoundary.setArrowLine1(new int[]{p1[0], p1[1], bx, by});
        linkBoundary.setArrowLine2(new int[]{p2[0], p2[1], bx, by});
    }

    public static double getAngleFromCursorLocation(Boundary owner, int mouseX, int mouseY) {
        double middleX = owner.getTopLeftX() + owner.getWidth() / 2;
        double middleY = owner.getTopLeftY() + owner.getHeight() / 2;
        if (mouseX == middleX && mouseY == middleY) {
            return 0;
        }
        double angle = Math.asin((mouseX - middleX) / Point.distance(middleX, middleY, mouseX, mouseY));
        if (mouseY < middleY) {
            if (mouseX < middleX) {
                angle += Math.PI * 2;
            }
        } else {
            angle = Math.PI - angle;
        }
        return angle;
    }

    public static double getAngleFromLineIntersection(Boundary boundary, int startX, int startY, int endX, int endY) {
        int closestX = boundary.getTopLeftX();
        int closestY = boundary.getTopLeftY();
        if (Math.abs(startX - boundary.getTopLeftX() - boundary.getWidth()) < Math.abs(startX - boundary.getTopLeftX())) {
            closestX = boundary.getTopLeftX() + boundary.getWidth();
        }
        if (Math.abs(startY - boundary.getTopLeftY() - boundary.getHeight()) < Math.abs(startY - boundary.getTopLeftY())) {
            closestY = boundary.getTopLeftY() + boundary.getHeight();
        }
        if (startX == endX) {
            return getAngleFromCursorLocation(boundary, endX, closestY);
        } else {
            return getAngleFromCursorLocation(boundary, closestX, endY);
        }
    }
}
