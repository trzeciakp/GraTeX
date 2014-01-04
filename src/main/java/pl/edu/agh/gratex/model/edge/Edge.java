package pl.edu.agh.gratex.model.edge;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.*;
import java.util.List;


public class Edge extends GraphElement {
    private EdgePropertyModel propertyModel = (EdgePropertyModel) super.propertyModel;

    private Vertex vertexA;
    private Vertex vertexB;
    private LabelE label;
    private int inAngle;
    private int outAngle;

    private Point arcMiddle = new Point();
    private int arcRadius;
    private Point outPoint;
    private Point inPoint;
    private Arc2D.Double arc;
    private int[] arrowLine1 = null;
    private int[] arrowLine2 = null;

    public Edge(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    public boolean isLoop() {
        return (vertexA != null && vertexA == vertexB);
    }

    @Override
    public PropertyModel getModel() {
        EdgePropertyModel pm = (EdgePropertyModel) propertyModel.getCopy();
        pm.setLoop(isLoop() ? PropertyModel.YES : PropertyModel.NO);
        return pm;
    }

    @Override
    public void updateLocation() {
        EdgeUtils.updateLocation(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (inAngle != edge.inAngle) return false;
        if (outAngle != edge.outAngle) return false;
        if (getRelativeEdgeAngle() != edge.getRelativeEdgeAngle()) return false;
        if (vertexA != null ? !vertexA.equals(edge.vertexA) : edge.vertexA != null) return false;
        if (vertexB != null ? !vertexB.equals(edge.vertexB) : edge.vertexB != null) return false;

        return true;
    }

    public int getNumber() {
        int result = vertexA != null ? vertexA.getNumber() : 0;
        result = 31 * result + (vertexB != null ? vertexB.getNumber() : 0);
        result = 31 * result + inAngle;
        result = 31 * result + outAngle;
        return result;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.EDGE;
    }

    public void drawLabel(Graphics2D g, boolean dummy) {
        if (getLabel() != null) {
            getLabel().draw(g, dummy);
        }
    }

    @Override
    public void addToGraph() {
        graph.getEdges().add(this);
        updateLocation();
    }

    @Override
    public void removeFromGraph() {
        getGraph().getEdges().remove(this);
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        List<GraphElement> result = new LinkedList<>();
        if (label != null) {
            result.add(label);
        }
        return result;
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

    public boolean isDirected() {
        return propertyModel.getDirected() == PropertyModel.YES;
    }

    public void setDirected(boolean directed) {
        propertyModel.setDirected(directed ? PropertyModel.YES : PropertyModel.NO);
    }

    public int getArrowType() {
        return propertyModel.getArrowType();
    }

    public void setArrowType(int arrowType) {
        propertyModel.setArrowType(arrowType);
    }

    public void setArrowType(ArrowType arrowType) {
        propertyModel.setArrowType(arrowType.getValue());
    }

    public ArrowType getArrowTypeENUM() {
        return ArrowType.values()[propertyModel.getArrowType() + 1];
    }

    public Color getLineColor() {
        return propertyModel.getLineColor();
    }

    public void setLineColor(Color lineColor) {
        propertyModel.setLineColor(lineColor);
    }

    public int getRelativeEdgeAngle() {
        return propertyModel.getRelativeEdgeAngle();
    }

    public void setRelativeEdgeAngle(int relativeEdgeAngle) {
        propertyModel.setRelativeEdgeAngle(relativeEdgeAngle);
    }

    public Vertex getVertexA() {
        return vertexA;
    }

    public void setVertexA(Vertex vertexA) {
        this.vertexA = vertexA;
    }

    public Vertex getVertexB() {
        return vertexB;
    }

    public void setVertexB(Vertex vertexB) {
        this.vertexB = vertexB;
    }

    public LabelE getLabel() {
        return label;
    }

    public void setLabel(LabelE label) {
        this.label = label;
    }

    public int getInAngle() {
        return inAngle;
    }

    public void setInAngle(int inAngle) {
        this.inAngle = inAngle;
    }

    public int getOutAngle() {
        return outAngle;
    }

    public void setOutAngle(int outAngle) {
        this.outAngle = outAngle;
    }

    public Point getArcMiddle() {
        return arcMiddle;
    }

    public void setArcMiddle(Point arcMiddle) {
        this.arcMiddle = arcMiddle;
    }

    public int getArcRadius() {
        return arcRadius;
    }

    public void setArcRadius(int arcRadius) {
        this.arcRadius = arcRadius;
    }

    public Point getOutPoint() {
        return outPoint;
    }

    public void setOutPoint(Point outPoint) {
        this.outPoint = outPoint;
    }

    public Point getInPoint() {
        return inPoint;
    }

    public void setInPoint(Point inPoint) {
        this.inPoint = inPoint;
    }

    public Arc2D.Double getArc() {
        return arc;
    }

    public void setArc(Arc2D.Double arc) {
        this.arc = arc;
    }

    public int[] getArrowLine1() {
        return arrowLine1;
    }

    public void setArrowLine1(int[] arrowLine1) {
        this.arrowLine1 = arrowLine1;
    }

    public int[] getArrowLine2() {
        return arrowLine2;
    }

    public void setArrowLine2(int[] arrowLine2) {
        this.arrowLine2 = arrowLine2;
    }
}
