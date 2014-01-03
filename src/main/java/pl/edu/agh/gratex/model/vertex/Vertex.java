package pl.edu.agh.gratex.model.vertex;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphNumeration;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class Vertex extends GraphElement {
    private VertexPropertyModel propertyModel = new VertexPropertyModel(this);

    private int posX;
    private int posY;
    private LabelV label = null;
    private String text;

    public Vertex(Graph graph) {
        super(graph);
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.VERTEX;
    }

    @Override
    public void addToGraph() {
        graph.getVertices().add(this);
        VertexUtils.setPartOfNumeration(this, true);
    }

    @Override
    public void removeFromGraph() {
        graph.getVertices().remove(this);
        VertexUtils.setPartOfNumeration(this, false);
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        List<GraphElement> result = new LinkedList<>();
        if (label != null) {
            result.add(label);
        }
        for (Edge edge : GraphUtils.getAdjacentEdges(graph, this)) {
            result.add(edge);
        }
        return result;
    }

    public void drawLabel(Graphics2D g, boolean dummy) {
        if (getLabel() != null) {
            getLabel().draw(g, dummy);
        }
    }

    public void setModel(PropertyModel pm) {
        propertyModel.mergeWithModel(pm);
    }

    public PropertyModel getModel() {
        VertexPropertyModel result = new VertexPropertyModel(this);

        result.number = getNumber();
        result.radius = getRadius();
        result.shape = getShape();
        result.vertexColor = new Color(getVertexColor().getRGB());
        result.lineWidth = getLineWidth();
        result.lineType = getLineType();
        result.lineColor = new Color(getLineColor().getRGB());
        result.labelInside = 0;
        if (isLabelInside()) {
            result.fontColor = new Color(getFontColor().getRGB());
            result.labelInside = 1;
        }

        return result;
    }

    // ============================================
    // Getters & setters

    public int getNumber() {
        return propertyModel.number;
    }

    public int getRadius() {
        return propertyModel.radius;
    }

    public void setRadius(int radius) {
        propertyModel.radius = radius;
    }

    public int getShape() {
        return propertyModel.shape;
    }

    public void setShape(int shape) {
        propertyModel.shape = shape;
    }

    public void setShape(ShapeType shape) {
        propertyModel.shape = shape.getValue();
    }

    public ShapeType getShapeENUM() {
        return ShapeType.values()[propertyModel.shape];
    }

    public Color getVertexColor() {
        return propertyModel.vertexColor;
    }

    public void setVertexColor(Color vertexColor) {
        propertyModel.vertexColor = vertexColor;
    }

    public int getLineWidth() {
        return propertyModel.lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        propertyModel.lineWidth = lineWidth;
    }

    public LineType getLineType() {
        return propertyModel.lineType;
    }

    public void setLineType(LineType lineType) {
        propertyModel.lineType = lineType;
    }

    public Color getLineColor() {
        return propertyModel.lineColor;
    }

    public void setLineColor(Color lineColor) {
        propertyModel.lineColor = lineColor;
    }

    public Color getFontColor() {
        return propertyModel.fontColor;
    }

    public void setFontColor(Color fontColor) {
        propertyModel.fontColor = fontColor;
    }

    public boolean isLabelInside() {
        return propertyModel.labelInside == 1;
    }

    public void setLabelInside(boolean labelInside) {
        propertyModel.labelInside = labelInside ? 1 : 0;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public LabelV getLabel() {
        return label;
    }

    public void setLabel(LabelV label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNumber(int number) {
        propertyModel.number = number;
        this.text = GraphNumeration.digitalToAlphabetical(number);
    }

    public String getLabelInside() {
        if (getGraph().getGraphNumeration().isNumerationDigital()) {
            return String.valueOf(propertyModel.number);
        } else {
            return text;
        }
    }
}
