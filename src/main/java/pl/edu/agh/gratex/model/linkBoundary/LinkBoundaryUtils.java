package pl.edu.agh.gratex.model.linkBoundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.properties.ArrowType;

import java.awt.*;
import java.awt.geom.Point2D;

public class LinkBoundaryUtils {
    public static void updateLocation(LinkBoundary linkBoundary) {
        Point outPoint = getExitPointFromBoundary(linkBoundary.getBoundaryA(), linkBoundary.getOutAngle());
        Point inPoint = getExitPointFromBoundary(linkBoundary.getBoundaryB(), linkBoundary.getInAngle());
        linkBoundary.setOutPointX(outPoint.x);
        linkBoundary.setOutPointY(outPoint.y);
        linkBoundary.setInPointX(inPoint.x);
        linkBoundary.setInPointY(inPoint.y);
        if (linkBoundary.isDirected()) {
            updateArrowPosition(linkBoundary);
        }
    }

    public static Point getExitPointFromBoundary(Boundary boundary, double angle) {
        double r = Math.sqrt(boundary.getWidth() * boundary.getWidth() + boundary.getHeight() * boundary.getHeight()) / 2.0;
        double x = Math.sin(Math.toRadians(angle)) * r;
        double y = -Math.cos(Math.toRadians(angle)) * r;

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
        int middleX = owner.getTopLeftX() + owner.getWidth() / 2;
        int middleY = owner.getTopLeftY() + owner.getHeight() / 2;
        Point2D p1 = new Point(middleX, middleY);
        Point2D p2 = new Point(mouseX, mouseY);
        double angle = Math.toDegrees(Math.asin((mouseX - middleX) / p1.distance(p2)));
        if (mouseY < middleY) {
            if (mouseX < middleX) {
                angle += 360;
            }
        } else {
            angle = 180 - angle;
        }
        return angle;
    }
}
