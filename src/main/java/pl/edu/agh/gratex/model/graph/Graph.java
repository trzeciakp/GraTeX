package pl.edu.agh.gratex.model.graph;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModelFactory;
import pl.edu.agh.gratex.model.PropertyModelFactoryImpl;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;

import java.awt.*;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;


public class Graph {
    private EnumMap<GraphElementType, List<? extends GraphElement>> elements;

    private int gridResolutionX = 20;
    private int gridResolutionY = 20;
    private boolean gridOn;

    // TODO przeniesc to gdzie indziej, do jakiegos kontrolera?
    private GraphNumeration graphNumeration;

    public Graph() {
        elements = new EnumMap<>(GraphElementType.class);
        for(GraphElementType type : GraphElementType.values()) {
            elements.put(type, new LinkedList<GraphElement>());
        }

        graphNumeration = new GraphNumeration();
    }

    public GraphNumeration getGraphNumeration() {
        return graphNumeration;
    }

    public void drawAll(Graphics2D g) {
        for (Edge edge : getEdges()) {
            edge.draw(g, false);
        }
        for (Vertex vertex : getVertices()) {
            vertex.draw(g, false);
        }
        for (Edge edge : getEdges()) {
            edge.drawLabel(g, false);
        }
        for (Vertex vertex : getVertices()) {
            vertex.drawLabel(g, false);
        }
    }

    public List<Vertex> getVertices() {
        return (List<Vertex>) elements.get(GraphElementType.VERTEX);
    }

    public Vertex getVertexById(int id) {
        for (Vertex vertex : getVertices()) {
            if(vertex.getNumber() == id) {
                return vertex;
            }
        }
        return null;
    }

    public List<Edge> getEdges() {
        return (List<Edge>) elements.get(GraphElementType.EDGE);
    }

    public Edge getEdgeById(int id) {
        for (Edge edge : getEdges()) {
            if(edge.getNumber() == id) {
                return edge;
            }
        }
        return null;
    }

    public List<LabelV> getLabelsV() {
        return (List<LabelV>) elements.get(GraphElementType.LABEL_VERTEX);
    }

    public List<LabelE> getLabelsE() {
        return (List<LabelE>) elements.get(GraphElementType.LABEL_EDGE);
    }

    public List<? extends GraphElement> getElements(GraphElementType type) {
        return elements.get(type);
    }

    public List<GraphElement> getAllElements()
    {
        List<GraphElement> result = new LinkedList<>();
        for(GraphElementType type : GraphElementType.values()) {
            result.addAll(elements.get(type));
        }
        return result;
    }

    public int getGridResolutionX() {
        return gridResolutionX;
    }

    public void setGridResolutionX(int gridResolutionX) {
        this.gridResolutionX = gridResolutionX;
    }

    public int getGridResolutionY() {
        return gridResolutionY;
    }

    public void setGridResolutionY(int gridResolutionY) {
        this.gridResolutionY = gridResolutionY;
    }

    public boolean isGridOn() {
        return gridOn;
    }

    public void setGridOn(boolean gridOn) {
        this.gridOn = gridOn;
    }
}
