package pl.edu.agh.gratex.model.graph;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.PropertyModelFactory;
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
    private PropertyModelFactory propertyModelFactory = new PropertyModelFactory();

    private VertexPropertyModel vertexDefaultModel;
    private EdgePropertyModel edgeDefaultModel;
    private LabelVertexPropertyModel labelVDefaultModel;
    private LabelEdgePropertyModel labelEDefaultModel;

    private EnumMap<GraphElementType, List<? extends GraphElement>> elements;
    private EnumMap<GraphElementType, List<? extends PropertyModel>> defaultModels;

    public int gridResolutionX = 20;
    public int gridResolutionY = 20;
    public boolean gridOn;

    // TODO przeniesc to gdzie indziej, do jakiegos kontrolera?
    private GraphNumeration graphNumeration;

    public Graph() {
        elements = new EnumMap<>(GraphElementType.class);
        for(GraphElementType type : GraphElementType.values()) {
            elements.put(type, new LinkedList<GraphElement>());
            //defaultModels(type, )
        }
        initDefaultModels();

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

    public PropertyModelFactory getPropertyModelFactory() {
        return propertyModelFactory;
    }

    public void initDefaultModels() {
        //TODO
        setVertexDefaultModel(new VertexPropertyModel());
        getVertexDefaultModel().setNumber(-1);
        getVertexDefaultModel().setRadius(40);
        getVertexDefaultModel().setShape(1);
        getVertexDefaultModel().setVertexColor(new Color(new Float(1), new Float(0.5), new Float(0)));
        getVertexDefaultModel().setLineType(LineType.SOLID);
        getVertexDefaultModel().setLineWidth(1);
        getVertexDefaultModel().setLineColor(Color.black);
        getVertexDefaultModel().setFontColor(Color.black);
        getVertexDefaultModel().setLabelInside(1);

        setEdgeDefaultModel(new EdgePropertyModel());
        getEdgeDefaultModel().setLineType(LineType.SOLID);
        getEdgeDefaultModel().setLineWidth(1);
        getEdgeDefaultModel().setDirected(0);
        getEdgeDefaultModel().setLineColor(Color.black);
        getEdgeDefaultModel().setRelativeEdgeAngle(0);

        setLabelVDefaultModel(new LabelVertexPropertyModel());
        getLabelVDefaultModel().setText("Label");
        getLabelVDefaultModel().setFontColor(Color.black);
        getLabelVDefaultModel().setPosition(0);
        getLabelVDefaultModel().setSpacing(5);

        setLabelEDefaultModel(new LabelEdgePropertyModel());
        getLabelEDefaultModel().setText("Label");
        getLabelEDefaultModel().setFontColor(Color.black);
        getLabelEDefaultModel().setPosition(50);
        getLabelEDefaultModel().setSpacing(5);
        getLabelEDefaultModel().setTopPlacement(1);
        getLabelEDefaultModel().setHorizontalPlacement(0);
    }

    public VertexPropertyModel getVertexDefaultModel() {
        return vertexDefaultModel;
    }

    public void setVertexDefaultModel(VertexPropertyModel vertexDefaultModel) {
        this.vertexDefaultModel = vertexDefaultModel;
    }

    public EdgePropertyModel getEdgeDefaultModel() {
        return edgeDefaultModel;
    }

    public void setEdgeDefaultModel(EdgePropertyModel edgeDefaultModel) {
        this.edgeDefaultModel = edgeDefaultModel;
    }

    public LabelVertexPropertyModel getLabelVDefaultModel() {
        return labelVDefaultModel;
    }

    public void setLabelVDefaultModel(LabelVertexPropertyModel labelVDefaultModel) {
        this.labelVDefaultModel = labelVDefaultModel;
    }

    public LabelEdgePropertyModel getLabelEDefaultModel() {
        return labelEDefaultModel;
    }

    public void setLabelEDefaultModel(LabelEdgePropertyModel labelEDefaultModel) {
        this.labelEDefaultModel = labelEDefaultModel;
    }
}
