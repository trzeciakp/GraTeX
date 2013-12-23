package pl.edu.agh.gratex.model.graph;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DummySubgraph {
    private GeneralController generalController;
    private Graph graph;

    public LinkedList<Vertex> vertices = new LinkedList<>();
    public LinkedList<Edge> edges = new LinkedList<>();
    public LinkedList<LabelV> labelsV = new LinkedList<>();
    public LinkedList<LabelE> labelsE = new LinkedList<>();

    public int minX = 10000;
    public int minY = 10000;
    public int biasX = 0;
    public int biasY = 0;

    public DummySubgraph(GeneralController generalController, Graph graph, List<? extends GraphElement> subjects) {
        this.generalController = generalController;
        this.graph = graph;

        Collections.sort(subjects, new Comparator<GraphElement>() {
            @Override
            public int compare(GraphElement o1, GraphElement o2) {
                int v1Number = ((Vertex) o1).getNumber();
                int v2Number = ((Vertex) o2).getNumber();
                return Integer.compare(v1Number, v2Number);
            }
        });

        HashMap<Vertex, Vertex> originalToDuplicate = new HashMap<>();
        List<Vertex> originalVertices = new LinkedList<>();
        for (GraphElement vertex : subjects) {
            Vertex originalVertex = (Vertex) vertex;
            originalVertices.add(originalVertex);

            Vertex duplicatedVertex = (Vertex) vertex.getCopy();
            duplicatedVertex.setNumber(graph.getGraphNumeration().getNextFreeNumber());
            VertexUtils.setPartOfNumeration(duplicatedVertex, true);
            originalToDuplicate.put(originalVertex, duplicatedVertex);
            vertices.add(duplicatedVertex);

            if (originalVertex.getLabel() != null) {
                LabelV labelCopy = (LabelV) originalVertex.getLabel().getCopy();
                labelCopy.setOwner(duplicatedVertex);
                duplicatedVertex.setLabel(labelCopy);
                labelsV.add(labelCopy);
            }

            if (duplicatedVertex.getPosX() < minX) {
                minX = duplicatedVertex.getPosX();
                minY = duplicatedVertex.getPosY();
            }
        }

        for (Vertex vertex : vertices) {
            VertexUtils.setPartOfNumeration(vertex, false);
        }

        for (Edge edge : GraphUtils.getCommonEdges(graph, originalVertices)) {
            Edge edgeCopy = (Edge) edge.getCopy();
            edgeCopy.setVertexA(originalToDuplicate.get(edge.getVertexA()));
            edgeCopy.setVertexB(originalToDuplicate.get(edge.getVertexB()));
            edges.add(edgeCopy);

            if (edge.getLabel() != null) {
                LabelE labelCopy = (LabelE) edge.getLabel().getCopy();
                labelCopy.setOwner(edgeCopy);
                edgeCopy.setLabel(labelCopy);
                labelsE.add(labelCopy);
            }
        }
    }

    public List<GraphElement> getElements() {
        List<GraphElement> result = new LinkedList<>();
        result.addAll(vertices);
        result.addAll(edges);
        result.addAll(labelsV);
        result.addAll(labelsE);
        return result;
    }

    public void drawAll(Graphics2D g) {
        for (Edge edge : edges) {
            edge.draw(g, true);
        }
        for (Vertex vertex : vertices) {
            vertex.draw(g, true);
        }
        for (Edge edge : edges) {
            edge.drawLabel(g, true);
        }
        for (Vertex vertex : vertices) {
            vertex.drawLabel(g, true);
        }
    }

    public void calculatePositions(int targetX, int targetY) {
        for (Vertex vertex : vertices) {
            vertex.setPosX(vertex.getPosX() - biasX + targetX - minX);
            vertex.setPosY(vertex.getPosY() - biasY + targetY - minY);
        }
        biasX = targetX - minX;
        biasY = targetY - minY;
    }

    public boolean fitsIntoPosition() {
        for (Vertex dummyVertex : vertices) {
            if (!VertexUtils.fitsIntoPage(dummyVertex)) {
                return false;
            }
            for (Vertex originalVertex : graph.getVertices()) {
                if (VertexUtils.collides(dummyVertex, originalVertex)) {
                    return false;
                }
            }
        }
        return true;
    }
}
