package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.draw.Drawable;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.List;


public abstract class GraphElement {
    protected Graph graph;
    protected Drawable drawable;
    protected PropertyModel propertyModel;

    public Graph getGraph() {
        return graph;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    protected GraphElement(Graph graph, PropertyModel propertyModel) {
        this.graph = graph;
        this.propertyModel = propertyModel;
        this.propertyModel.setOwner(this);
    }

    public void updateLocation() {
    }

    public void setModel(PropertyModel pm) {
        propertyModel.updateWithModel(pm);
    }

    public PropertyModel getModel() {
        return propertyModel.getCopy();
    }

    public void draw(Graphics2D g, boolean dummy) {
        drawable.draw(this, g, dummy);
    }

    public boolean contains(int x, int y) {
        return getArea().contains(x, y);
    }

    public boolean intersects(Rectangle selectionArea) {
        return getArea().intersects(selectionArea);
    }

    public abstract Area getArea();

    public abstract GraphElementType getType();

    public abstract void addToGraph();

    public abstract void removeFromGraph();

    public abstract List<? extends GraphElement> getConnectedElements();
}
