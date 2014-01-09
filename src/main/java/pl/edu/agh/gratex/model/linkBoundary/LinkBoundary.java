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
    private int outAngle;
    private int outPointX;
    private int outPointY;

    private Boundary boundaryB;
    private int inAngle;
    private int inPointX;
    private int inPointY;


    public LinkBoundary(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LINK_BOUNDARY;
    }

    @Override
    public int getTypeDrawingPriority() {
        return 0;
    }

    @Override
    public Area getArea() {
        Path2D path = new Path2D.Double();
        path.moveTo(outPointX, outPointY);
        path.lineTo(inPointX, inPointY);
        return new Area(new BasicStroke(2 * Const.EDGE_SELECTION_MARGIN + getLineWidth()).createStrokedShape(path));
    }

    public Boundary getBoundaryA() {
        return boundaryA;
    }

    public void setBoundaryA(Boundary boundaryA) {
        this.boundaryA = boundaryA;
    }

    public int getOutAngle() {
        return outAngle;
    }

    public void setOutAngle(int outAngle) {
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

    public int getInAngle() {
        return inAngle;
    }

    public void setInAngle(int inAngle) {
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

    public void setInPointY(int inPointY) {
        this.inPointY = inPointY;
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

    public void setDirected(IsDirected directed) {
        propertyModel.setDirected(directed);
    }

    public IsDirected getDirected() {
        return propertyModel.getDirected();
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
