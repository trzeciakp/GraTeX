package pl.edu.agh.gratex.model.boundary;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Boundary extends GraphElement {

    private int leftCornerX;
    private int leftCornerY;
    private int width;
    private int height;

    private BoundaryPropertyModel propertyModel = (BoundaryPropertyModel) super.propertyModel;

    public Boundary(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    @Override
    public Area getArea() {
        return new Area(new Rectangle(leftCornerX, leftCornerY, width, height));
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

    public int getLeftCornerX() {
        return leftCornerX;
    }

    public void setLeftCornerX(int leftCornerX) {
        this.leftCornerX = leftCornerX;
    }

    public int getLeftCornerY() {
        return leftCornerY;
    }

    public void setLeftCornerY(int leftCornerY) {
        this.leftCornerY = leftCornerY;
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
}
