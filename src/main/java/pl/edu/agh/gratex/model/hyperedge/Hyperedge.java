package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Hyperedge extends GraphElement {
    private HyperedgePropertyModel propertyModel = (HyperedgePropertyModel) super.propertyModel;

    private List<Vertex> connectedVertices = new LinkedList<>();
    private int jointBiasX;
    private int jointBiasY;

    public Hyperedge(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    //it should contain unique number to identify joint, any vertex cannot have the same number
    //it can be string, not necessary number
    public int getNumber() {
        //TODO
        return 1;
    }

    @Override
    public void finalizeAddingToGraph() {

    }

    @Override
    public void finalizeRemovingFromGraph() {

    }

    @Override
    public Area getArea() {
        return new Area();
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.HYPEREDGE;
    }

    @Override
    public int getDrawingPriority() {
        return 2;
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        return new ArrayList<>();
    }

    public List<Vertex> getConnectedVertices() {
        return connectedVertices;
    }

    public void setConnectedVertices(List<Vertex> connectedVertices) {
        this.connectedVertices = connectedVertices;
    }

    public int getJointBiasX() {
        return jointBiasX;
    }

    public void setJointBiasX(int jointBiasX) {
        this.jointBiasX = jointBiasX;
    }

    public int getJointBiasY() {
        return jointBiasY;
    }

    public void setJointBiasY(int jointBiasY) {
        this.jointBiasY = jointBiasY;
    }

    public Color getLineColor() {
        return propertyModel.getLineColor();
    }

    public ShapeType getJointShape() {
        return propertyModel.getJointShape();
    }

    public LineType getLineType() {
        return propertyModel.getLineType();
    }

    public int getLineWidth() {
        return propertyModel.getLineWidth();
    }

    public void setLineType(LineType lineType) {
        propertyModel.setLineType(lineType);
    }

    public Color getJointColor() {
        return propertyModel.getJointColor();
    }

    public int getJointSize() {
        return propertyModel.getJointSize();
    }

    public void setJointShape(ShapeType jointShape) {
        propertyModel.setJointShape(jointShape);
    }

    public void setJointColor(Color jointColor) {
        propertyModel.setJointColor(jointColor);
    }

    public void setLineWidth(int lineWidth) {
        propertyModel.setLineWidth(lineWidth);
    }

    public void setJointSize(int jointSize) {
        propertyModel.setJointSize(jointSize);
    }

    public void setLineColor(Color lineColor) {
        propertyModel.setLineColor(lineColor);
    }
}
