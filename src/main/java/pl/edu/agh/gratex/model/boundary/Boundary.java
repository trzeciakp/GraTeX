package pl.edu.agh.gratex.model.boundary;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Boundary extends GraphElement {
    // TODO zmienilbym na topLeftX
    private int topLeftX;
    private int topLeftY;
    private int width;
    private int height;

    private BoundaryPropertyModel propertyModel = (BoundaryPropertyModel) super.propertyModel;

    public Boundary(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    @Override
    public Area getArea() {
        return new Area(new Rectangle(topLeftX, topLeftY, width, height));
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.BOUNDARY;
    }

    @Override
    public int getDrawingPriority() {
        return 0;
    }

    @Override
    public void finalizeAddingToGraph() {
    }

    @Override
    public void finalizeRemovingFromGraph() {
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        return new ArrayList<>();
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
