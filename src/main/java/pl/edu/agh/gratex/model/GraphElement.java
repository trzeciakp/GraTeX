package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.draw.Drawable;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.*;
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
    }

    public void updateLocation() {
    }

    public void setModel(PropertyModel pm) {
        propertyModel.updateWithModel(pm);
    }

    public PropertyModel getModel() {
        //TODO nie bylo getCopy(), powinno byc?
        return propertyModel.getCopy();
    }

    public void draw(Graphics2D g, boolean dummy) {
        drawable.draw(this, g, dummy);
    }

    public abstract GraphElementType getType();

    public abstract void addToGraph();

    public abstract void removeFromGraph();

    public abstract List<? extends GraphElement> getConnectedElements();
}
