package pl.edu.agh.gratex.model.boundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Boundary extends GraphElement {
    private int topLeftX;
    private int topLeftY;
    private int width;
    private int height;
    private int number = minNumberCounter--;
    private static int minNumberCounter = Const.MIN_VERTEX_NUMBER - 1;

    private BoundaryPropertyModel propertyModel = (BoundaryPropertyModel) super.propertyModel;

    public Boundary(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.BOUNDARY;
    }


    @Override
    public List<GraphElement> getConnectedElements() {
        List<GraphElement> result = new LinkedList<>();
        result.addAll(GraphUtils.getAdjacentLinks(graph, this));
        return result;
    }

    @Override
    public int getTypeDrawingPriority() {
        return -100000000;
    }

    @Override
    public Area getArea() {
        return new Area(new Rectangle(topLeftX, topLeftY, width, height));
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        if(number <= minNumberCounter) {
            minNumberCounter = number--;
        }
    }

    public int getTopLeftX() {
        return topLeftX;
    }

    public void setTopLeftX(int topLeftX) {
        this.topLeftX = topLeftX;
    }

    public int getTopLeftY() {
        return topLeftY;
    }

    public void setTopLeftY(int topLeftY) {
        this.topLeftY = topLeftY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setFillColor(Color fillColor) {
        propertyModel.setFillColor(fillColor);
    }

    public LineType getLineType() {
        return propertyModel.getLineType();
    }

    public void setLineType(LineType lineType) {
        propertyModel.setLineType(lineType);
    }

    public int getLineWidth() {
        return propertyModel.getLineWidth();
    }

    public void setLineWidth(int lineWidth) {
        propertyModel.setLineWidth(lineWidth);
    }

    public Color getLineColor() {
        return propertyModel.getLineColor();
    }

    public void setLineColor(Color lineColor) {
        propertyModel.setLineColor(lineColor);
    }

    public Color getFillColor() {
        return propertyModel.getFillColor();
    }
}
