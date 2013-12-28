package pl.edu.agh.gratex.model.graph;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
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
import java.io.Serializable;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;


public class Graph implements Serializable {
    // TODO To jest statyczne, bo sie graf nie serializował (wymagał serializowalności GeneralControllera i całego wszechświata przez to)
    // TODO Jak zmienimy sposób zapisu, należy usunąć słowo static
    private static GeneralController generalController;

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

    public Vertex getVertexByLatexCode(String code){
        for (Vertex vertex : getVertices()) {
            if(vertex.getLatexCode().equals(code)) {
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

    public Edge getEdgeByLatexCode(String code){
        for (Edge edge : getEdges()) {
            if(edge.getLatexCode().equals(code)) {
                return edge;
            }
        }
        return null;
    }

    public List<LabelV> getLabelsV() {
        return (List<LabelV>) elements.get(GraphElementType.LABEL_VERTEX);
    }

    public LabelV getLabelVByLatexCode(String code){
        for (LabelV labelV : getLabelsV()) {
            if(labelV.getLatexCode().equals(code)) {
                return labelV;
            }
        }
        return null;
    }

    public List<LabelE> getLabelsE() {
        return (List<LabelE>) elements.get(GraphElementType.LABEL_EDGE);
    }

    public LabelE getLabelEByLatexCode(String code){
        for (LabelE labelE : getLabelsE()) {
            if(labelE.getLatexCode().equals(code)) {
                return labelE;
            }
        }
        return null;
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

    public GraphElement getElementByLatexCode(String code) {
        for(GraphElementType type : GraphElementType.values()) {
           for (GraphElement element : elements.get(type)){
               if (element.getLatexCode().equals(code)){
                   return element;
               }
           }
        }
        return null;
    }

    public void initDefaultModels() {
        //TODO
        setVertexDefaultModel(new VertexPropertyModel());
        getVertexDefaultModel().number = -1;
        getVertexDefaultModel().radius = 40;
        getVertexDefaultModel().type = 1;
        getVertexDefaultModel().vertexColor = new Color(new Float(1), new Float(0.5), new Float(0));
        getVertexDefaultModel().lineType = LineType.SOLID;
        getVertexDefaultModel().lineWidth = 1;
        getVertexDefaultModel().lineColor = Color.black;
        getVertexDefaultModel().fontColor = Color.black;
        getVertexDefaultModel().labelInside = 1;

        setEdgeDefaultModel(new EdgePropertyModel());
        getEdgeDefaultModel().lineType = LineType.SOLID;
        getEdgeDefaultModel().lineWidth = 1;
        getEdgeDefaultModel().directed = 0;
        getEdgeDefaultModel().lineColor = Color.black;
        getEdgeDefaultModel().relativeEdgeAngle = 0;

        setLabelVDefaultModel(new LabelVertexPropertyModel());
        getLabelVDefaultModel().text = "Label";
        getLabelVDefaultModel().fontColor = Color.black;
        getLabelVDefaultModel().position = 0;
        getLabelVDefaultModel().spacing = 5;

        setLabelEDefaultModel(new LabelEdgePropertyModel());
        getLabelEDefaultModel().text = "Label";
        getLabelEDefaultModel().fontColor = Color.black;
        getLabelEDefaultModel().position = 50;
        getLabelEDefaultModel().spacing = 5;
        getLabelEDefaultModel().topPlacement = 1;
        getLabelEDefaultModel().horizontalPlacement = 0;
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
