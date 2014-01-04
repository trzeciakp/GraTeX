package pl.edu.agh.gratex.model.graph;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEUtils;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.utils.Geometry;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GraphUtils {
    public static LinkedList<GraphElement> getIntersectingElements(Graph graph, ModeType mode, Rectangle selectionArea) {
        LinkedList<GraphElement> result = new LinkedList<>();
        for (GraphElement element : graph.getElements(mode.getRelatedElementType())) {
            if (element.intersects(selectionArea)) {
                result.add(element);
            }
        }
        return result;
    }
    
    public static boolean checkVertexCollision(Graph graph, Vertex vertex) {
        for (GraphElement vertex2 : graph.getElements(GraphElementType.VERTEX)) {
            if (VertexUtils.collides((Vertex) vertex2, vertex)) {
                return true;
            }
        }
        return false;
    }

    public static LinkedList<Edge> getAdjacentEdges(Graph graph, Vertex vertex) {
        LinkedList<Edge> result = new LinkedList<>();
        for (GraphElement graphElement : graph.getElements(GraphElementType.EDGE)) {
            Edge edge = (Edge) graphElement;
            if (edge.getVertexA() == vertex || edge.getVertexB() == vertex) {
                result.add(edge);
            }
        }
        return result;
    }

    public static LinkedList<Edge> getCommonEdges(Graph graph, List<Vertex> vertices) {
        LinkedList<Edge> result = new LinkedList<>();
        for (GraphElement graphElement : graph.getElements(GraphElementType.EDGE)) {
            Edge edge = (Edge) graphElement;
            if (vertices.contains(edge.getVertexA()) && vertices.contains(edge.getVertexB())) {
                result.add(edge);
            }
        }

        return result;
    }

    public static void adjustVerticesToGrid(Graph graph) {
        for (GraphElement vertex : graph.getElements(GraphElementType.VERTEX)) {
            VertexUtils.adjustToGrid((Vertex) vertex);
        }
    }
}
