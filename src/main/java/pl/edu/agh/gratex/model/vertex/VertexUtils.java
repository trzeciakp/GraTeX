package pl.edu.agh.gratex.model.vertex;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.geom.Area;

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
        return !((vertex.getPosX() - vertex.getRadius() - vertex.getLineWidth() / 2 < 0) || (vertex.getPosX() + vertex.getRadius() + vertex.getLineWidth() / 2 > Const.PAGE_WIDTH)
                || (vertex.getPosY() - vertex.getRadius() - vertex.getLineWidth() / 2 < 0) || (vertex.getPosY() + vertex.getRadius() + vertex.getLineWidth() / 2 > Const.PAGE_HEIGHT));
    }
}
