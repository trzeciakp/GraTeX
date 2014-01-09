package pl.edu.agh.gratex.model.linkBoundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.IsDirected;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;


public class LinkBoundary extends GraphElement {
    private LinkBoundaryPropertyModel propertyModel = (LinkBoundaryPropertyModel) super.propertyModel;

    private Boundary boundaryA;
    private double outAngle;
    private int outPointX;
    private int outPointY;

    private Boundary boundaryB;
    private double inAngle;
    private int inPointX;
    private int inPointY;

    private int[] arrowLine1 = null;
    private int[] arrowLine2 = null;


    public LinkBoundary(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LINK_BOUNDARY;
    }

    @Override
    public int getTypeDrawingPriority() {
        return -100000000;
    }

    @Override
    public Area getArea() {
        Path2D path = new Path2D.Double();
        path.moveTo(outPointX, outPointY);
        path.lineTo(inPointX, inPointY);
        return new Area(new BasicStroke(2 * Const.EDGE_SELECTION_MARGIN + getLineWidth()).createStrokedShape(path));
    }

    @Override
    public void updateLocation() {
        LinkBoundaryUtils.updateLocation(this);
    }

    public Boundary getBoundaryA() {
        return boundaryA;
    }

    public void setBoundaryA(Boundary boundaryA) {
        this.boundaryA = boundaryA;
    }

    public double getOutAngle() {
        return outAngle;
    }

    public void setOutAngle(double outAngle) {
        this.outAngle = outAngle;
    }

    public int getOutPointX() {
        return outPointX;
    }

    public void setOutPointX(int outPointX) {
        this.outPointX = outPointX;
    }

    public int getOutPointY() {
        return outPointY;
    }

    public void setOutPointY(int outPointY) {
        this.outPointY = outPointY;
    }

    public Boundary getBoundaryB() {
        return boundaryB;
    }

    public void setBoundaryB(Boundary boundaryB) {
        this.boundaryB = boundaryB;
    }

    public double getInAngle() {
        return inAngle;
    }

    public void setInAngle(double inAngle) {
        this.inAngle = inAngle;
    }

    public int getInPointX() {
        return inPointX;
    }

    public void setInPointX(int inPointX) {
        this.inPointX = inPointX;
    }

    public int getInPointY() {
        return inPointY;
    }

    public int[] getArrowLine1() {
        return arrowLine1;
    }

    public void setInPointY(int inPointY) {
        this.inPointY = inPointY;
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

    public void setLineType(LineType lineType) {
        propertyModel.setLineType(lineType);
    }

    public int getLineWidth() {
        return propertyModel.getLineWidth();
    }

    public void setArrowType(ArrowType arrowType) {
        propertyModel.setArrowType(arrowType);
    }

    public void setLineWidth(int lineWidth) {
        propertyModel.setLineWidth(lineWidth);
    }

    public LineType getLineType() {
        return propertyModel.getLineType();
    }

    public boolean isDirected() {
        return propertyModel.getDirected() == IsDirected.YES;
    }

    public void setDirected(boolean directed) {
        propertyModel.setDirected(directed ? IsDirected.YES : IsDirected.NO);
    }

    public ArrowType getArrowType() {
        return propertyModel.getArrowType();
    }

    public void setLineColor(Color lineColor) {
        propertyModel.setLineColor(lineColor);
    }

    public Color getLineColor() {
        return propertyModel.getLineColor();
    }
}
