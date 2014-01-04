package pl.edu.agh.gratex.model.vertex;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphNumeration;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.properties.IsLabelInside;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;

import java.awt.*;
import java.awt.geom.Area;
import java.util.LinkedList;
import java.util.List;


public class Vertex extends GraphElement {
    private VertexPropertyModel propertyModel = (VertexPropertyModel) super.propertyModel;

    private int posX;
    private int posY;
    private LabelV label = null;
    private String text;

    public Vertex(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
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
        dummy = false;
    }

    @Override
    public void removeFromGraph() {
        graph.getVertices().remove(this);
        VertexUtils.setPartOfNumeration(this, false);
        dummy = true;
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

    @Override
    public Area getArea() {
        return new Area(VertexUtils.getVertexShape(getShape(), getRadius(), posX, posY));
    }

    @Override
    public int getDrawingPriority() {
        return 2;
    }

    // ============================================
    // Getters & setters

    public int getNumber() {
        return propertyModel.getNumber();
    }

    public void setNumber(int number) {
        propertyModel.setNumber(number);
        this.text = GraphNumeration.digitalToAlphabetical(number);
    }

    public int getRadius() {
        return propertyModel.getRadius();
    }

    public void setRadius(int radius) {
        propertyModel.setRadius(radius);
    }

    public void setShape(ShapeType shape) {
        propertyModel.setShape(shape);
    }

    public ShapeType getShape() {
        return propertyModel.getShape();
    }

    public Color getVertexColor() {
        return propertyModel.getVertexColor();
    }

    public void setVertexColor(Color vertexColor) {
        propertyModel.setVertexColor(vertexColor);
    }

    public int getLineWidth() {
        return propertyModel.getLineWidth();
    }

    public void setLineWidth(int lineWidth) {
        propertyModel.setLineWidth(lineWidth);
    }

    public LineType getLineType() {
        return propertyModel.getLineType();
    }

    public void setLineType(LineType lineType) {
        propertyModel.setLineType(lineType);
    }

    public Color getLineColor() {
        return propertyModel.getLineColor();
    }

    public void setLineColor(Color lineColor) {
        propertyModel.setLineColor(lineColor);
    }

    public Color getFontColor() {
        return propertyModel.getFontColor();
    }

    public void setFontColor(Color fontColor) {
        propertyModel.setFontColor(fontColor);
    }

    public boolean isLabelInside() {
        return propertyModel.getLabelInsideENUM() == IsLabelInside.YES;
    }

    public void setLabelInside(boolean labelInside) {
        propertyModel.setLabelInside(labelInside ? IsLabelInside.YES : IsLabelInside.NO);
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

    public String getLabelInside() {
        if (getGraph().getGraphNumeration().isNumerationDigital()) {
            return String.valueOf(propertyModel.getNumber());
        } else {
            return text;
        }
    }
}
