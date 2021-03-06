package pl.edu.agh.gratex.model.graph;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.boundary.BoundaryUtils;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
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
            if (vertex != vertex2 && VertexUtils.collides((Vertex) vertex2, vertex)) {
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

    public static LinkedList<LinkBoundary> getAdjacentLinks(Graph graph, Boundary boundary) {
        LinkedList<LinkBoundary> result = new LinkedList<>();
        for (GraphElement graphElement : graph.getElements(GraphElementType.LINK_BOUNDARY)) {
            LinkBoundary link = (LinkBoundary) graphElement;
            if (link.getBoundaryA() == boundary || link.getBoundaryB() == boundary) {
                result.add(link);
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

    public static void adjustElementsToGrid(Graph graph) {
        for (GraphElement vertex : graph.getElements(GraphElementType.VERTEX)) {
            VertexUtils.adjustToGrid((Vertex) vertex);
        }
        for (GraphElement boundary : graph.getElements(GraphElementType.BOUNDARY)) {
            BoundaryUtils.adjustToGrid((Boundary) boundary);
        }
    }

    public static List<Hyperedge> getAdjacentHyperedges(Graph graph, Vertex vertex) {
        LinkedList<Hyperedge> result = new LinkedList<>();
        for (GraphElement graphElement : graph.getElements(GraphElementType.HYPEREDGE)) {
            Hyperedge hyperedge = (Hyperedge) graphElement;
            if (hyperedge.getConnectedVertices().contains(vertex)) {
                result.add(hyperedge);
            }
        }
        return result;
    }

    public static LinkedList<Hyperedge> getCommonHyperedges(Graph graph, List<Vertex> vertices) {
        LinkedList<Hyperedge> result = new LinkedList<>();
        for (GraphElement graphElement : graph.getElements(GraphElementType.HYPEREDGE)) {
            Hyperedge hyperedge = (Hyperedge) graphElement;
            if (vertices.containsAll(hyperedge.getConnectedVertices())) {
                result.add(hyperedge);
            }
        }

        return result;
    }
}
