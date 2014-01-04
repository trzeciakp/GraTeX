package pl.edu.agh.gratex.model.vertex;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.ShapeType;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class VertexUtils {
    public static void setPartOfNumeration(Vertex vertex, boolean flag) {
        vertex.getGraph().getGraphNumeration().setUsed(vertex.getNumber(), flag);
    }

    public static void adjustToGrid(Vertex vertex) {
        Graph graph = vertex.getGraph();
        vertex.setPosX(((vertex.getPosX() + (graph.getGridResolutionX() / 2)) / graph.getGridResolutionX()) * graph.getGridResolutionX());
        vertex.setPosY(((vertex.getPosY() + (graph.getGridResolutionY() / 2)) / graph.getGridResolutionY()) * graph.getGridResolutionY());
    }

    public static boolean collides(Vertex vertex1, Vertex vertex2) {
        Area area = new Area(vertex1.getArea());
        area.intersect(vertex2.getArea());
        return !area.isEmpty();
    }


    public static boolean fitsIntoPage(Vertex vertex) {
        return !((vertex.getPosX() - vertex.getRadius() - vertex.getLineWidth() / 2 < 0) ||
                (vertex.getPosX() + vertex.getRadius() + vertex.getLineWidth() / 2 > Const.PAGE_WIDTH) ||
                (vertex.getPosY() - vertex.getRadius() - vertex.getLineWidth() / 2 < 0) ||
                (vertex.getPosY() + vertex.getRadius() + vertex.getLineWidth() / 2 > Const.PAGE_HEIGHT));
    }

    public static Shape getVertexShape(ShapeType shapeType, int radius, int posX, int posY) {
        Shape result = null;

        switch (shapeType) {
            case CIRCLE: {
                return new Ellipse2D.Double(posX - radius, posY - radius, 2 * radius, 2 * radius);
            }

            case TRIANGLE: {
                int a = (int) Math.round(Math.sqrt(3) / 2 * radius);
                int x[] = new int[]{posX - a, posX + a, posX};
                int y[] = new int[]{posY + radius / 2, posY + radius / 2, posY - radius};
                return new Polygon(x, y, 3);
            }

            case SQUARE: {
                int a = (int) Math.round(Math.sqrt(2) * radius / 2);
                int x[] = new int[]{posX - a, posX + a, posX + a, posX - a};
                int y[] = new int[]{posY - a, posY - a, posY + a, posY + a};
                return new Polygon(x, y, 4);
            }

            case PENTAGON: {
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
                return new Polygon(x, y, 5);
            }

            case HEXAGON: {
                int h = (int) Math.round(Math.sqrt(3) * radius / 2);
                int x[] = new int[]{posX - radius, posX - radius / 2, posX + radius / 2, posX + radius, posX + radius / 2, posX - radius / 2};
                int y[] = new int[]{posY, posY + h, posY + h, posY, posY - h, posY - h};
                return new Polygon(x, y, 6);
            }
        }

        return result;
    }
}
