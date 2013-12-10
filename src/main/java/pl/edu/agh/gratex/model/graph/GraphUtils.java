package pl.edu.agh.gratex.model.graph;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.utils.Geometry;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Iterator;
import java.util.LinkedList;

public class GraphUtils {
    public static LinkedList<GraphElement> getIntersectingElements(Graph graph, ModeType mode, Rectangle selectionArea) {
        LinkedList<GraphElement> result = new LinkedList<>();

        switch (mode) {
            case VERTEX:
                for (Vertex vertex : graph.getVertices()) {
                    Area outline = new Area(Geometry.getVertexShape(vertex.getShape() + 1, vertex.getRadius(), vertex.getPosX(), vertex.getPosY()));
                    outline.intersect(new Area(selectionArea));
                    if (!outline.isEmpty()) {
                        result.add(vertex);
                    }
                }
                break;

            case EDGE:
                for (Edge edge : graph.getEdges()) {
                    if (Geometry.checkArcRectangleIntersection(edge, selectionArea)) {
                        result.add(edge);
                    }
                }
                break;

            case LABEL_VERTEX:
                for (LabelV labelV : graph.getLabelsV()) {
                    if (labelV.getOutline().intersects(selectionArea)) {
                        result.add(labelV);
                    }
                }
                break;

            case LABEL_EDGE:
                for (LabelE labelE : graph.getLabelsE()) {
                    if (labelE.getOutline().intersects(selectionArea)) {
                        result.add(labelE);
                    }
                }
                break;
        }

        return result;
    }

    // TODO Mozna by te getXXXFromPosition dac do jednego, ale one beda chyba uzywane z roznych kontrolerow, wiec na razie tego nie robie
    public static Vertex getVertexFromPosition(Graph graph, int x, int y) {
        for (Vertex vertex : graph.getVertices()) {
            if (VertexUtils.intersects(vertex, x, y)) {
                return vertex;
            }
        }
        return null;
    }

    // TODO Mozna by te getXXXFromPosition dac do jednego, ale one beda chyba uzywane z roznych kontrolerow, wiec na razie tego nie robie
    public static Edge getEdgeFromPosition(Graph graph, int x, int y) {
        for (Edge edge : graph.getEdges()) {
            if (edge.intersects(x, y)) {
                return edge;
            }
        }
        return null;
    }

    // TODO Mozna by te getXXXFromPosition dac do jednego, ale one beda chyba uzywane z roznych kontrolerow, wiec na razie tego nie robie
    public static LabelV getLabelVFromPosition(Graph graph, int x, int y) {
        for (LabelV labelV : graph.getLabelsV()) {
            if (labelV.intersects(x, y)) {
                return labelV;
            }
        }
        return null;
    }

    // TODO Mozna by te getXXXFromPosition dac do jednego, ale one beda chyba uzywane z roznych kontrolerow, wiec na razie tego nie robie
    public static LabelE getLabelEFromPosition(Graph graph, int x, int y) {
        for (LabelE labelE : graph.getLabelsE()) {
            if (labelE.intersects(x, y)) {
                return labelE;
            }
        }
        return null;
    }

    public static boolean checkVertexCollision(Graph graph, Vertex vertex) {
        for (Vertex vertex2 : graph.getVertices()) {
            if (VertexUtils.collides(vertex2, vertex)) {
                return true;
            }
        }
        return false;
    }

    public static LinkedList<Edge> getAdjacentEdges(Graph graph, Vertex vertex) {
        LinkedList<Edge> result = new LinkedList<>();
        for (Edge edge : graph.getEdges()) {
            if (edge.getVertexA() == vertex || edge.getVertexB() == vertex) {
                result.add(edge);
            }
        }
        return result;
    }


    public static void adjustVerticesToGrid(Graph graph) {
        for (Vertex vertex : graph.getVertices()) {
            VertexUtils.adjustToGrid(vertex);
        }
    }

    public static void deleteUnusedLabels(Graph graph) {
        Iterator<LabelV> it = graph.getLabelsV().listIterator();
        while (it.hasNext()) {
            if (it.next().getOwner() == null) {
                it.remove();
            }
            else if (it.next().getOwner().getLabel() == null) {
                it.remove();
            }
        }

        Iterator<LabelE> it2 = graph.getLabelsE().listIterator();
        while (it2.hasNext()) {
            if (it2.next().getOwner() == null) {
                it2.remove();
            }
            else if (it2.next().getOwner().getLabel() == null) {
                it2.remove();
            }
        }
    }

}
