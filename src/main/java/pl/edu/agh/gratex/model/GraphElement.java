package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public abstract class GraphElement {
    private static int elementIdCounter = 0;

    protected Graph graph;
    protected Drawer drawer;
    protected PropertyModel propertyModel;
    protected boolean dummy = true;
    protected int elementID;

    public Graph getGraph() {
        return graph;
    }

    public void addToGraph() {
        graph.addElement(this);
        updateLocation();
        dummy = false;
        finalizeAddingToGraph();
    }

    public void removeFromGraph() {
        graph.removeElement(this);
        dummy = true;
        finalizeRemovingFromGraph();
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    protected GraphElement(Graph graph, PropertyModel propertyModel) {
        this.graph = graph;
        this.propertyModel = propertyModel;
        this.propertyModel.setOwner(this);
        this.elementID = elementIdCounter++;
    }

    public void updateLocation() {
    }

    public void setModel(PropertyModel pm) {
        propertyModel.updateWithModel(pm);
    }

    public PropertyModel getModel() {
        return propertyModel.getCopy();
    }

    public void draw(Graphics2D g) {
        drawer.draw(this, g);
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    public boolean contains(int x, int y) {
        return getArea().contains(x, y);
    }

    public boolean intersects(Rectangle selectionArea) {
        return getArea().intersects(selectionArea);
    }

    public int getDrawingPriority() {
        return getTypeDrawingPriority() + elementID;
    }

    public void finalizeAddingToGraph() {
    }

    public void finalizeRemovingFromGraph() {
    }

    public List<GraphElement> getConnectedElements() {
        return new LinkedList<>();
    }

    public abstract GraphElementType getType();

    public abstract int getTypeDrawingPriority();

    public abstract Area getArea();

    public static final void sortByDrawingPriorities(List<GraphElement> elements) {
        Collections.sort(elements, new Comparator<GraphElement>() {
            public int compare(GraphElement el1, GraphElement el2) {
                return Integer.compare(el1.getDrawingPriority(), el2.getDrawingPriority());
            }
        });
    }
}
