package pl.edu.agh.gratex.model.edge;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.properties.IsDirected;
import pl.edu.agh.gratex.model.properties.IsLoop;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
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
        pm.setLoop(isLoop() ? IsLoop.YES : IsLoop.NO);
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

    @Override
    public GraphElementType getType() {
        return GraphElementType.EDGE;
    }

    @Override
    public void finalizeAddingToGraph() {
    }

    @Override
    public void finalizeRemovingFromGraph() {
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        List<GraphElement> result = new LinkedList<>();
        if (label != null) {
            result.add(label);
        }
        return result;
    }

    @Override
    public Area getArea() {
        Shape edgePath = arc;
        if (vertexA != vertexB && getRelativeEdgeAngle() == 0) {
            Path2D path = new Path2D.Double();
            path.moveTo(inPoint.x, inPoint.y);
            path.lineTo(outPoint.x, outPoint.y);
            edgePath = path;
        }
        return new Area(new BasicStroke(2 * Const.EDGE_SELECTION_MARGIN + getLineWidth()).createStrokedShape(edgePath));
    }

    @Override
    public int getTypeDrawingPriority() {
        return 100000000;
    }


    public int getNumber() {
        int result = vertexA != null ? vertexA.getNumber() : 0;
        result = 31 * result + (vertexB != null ? vertexB.getNumber() : 0);
        result = 31 * result + inAngle;
        result = 31 * result + outAngle;
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
        return propertyModel.getDirected() == IsDirected.YES;
    }

    public void setDirected(boolean directed) {
        propertyModel.setDirected(directed ? IsDirected.YES : IsDirected.NO);
    }

    public void setArrowType(ArrowType arrowType) {
        propertyModel.setArrowType(arrowType);
    }

    public ArrowType getArrowType() {
        return propertyModel.getArrowType();
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
