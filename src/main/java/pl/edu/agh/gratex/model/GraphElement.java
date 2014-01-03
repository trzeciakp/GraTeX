package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.draw.Drawable;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.*;
import java.util.List;


@SuppressWarnings("serial")
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

    protected GraphElement(Graph graph) {
        this.graph = graph;
        if (graph != null) {
            propertyModel = graph.getPropertyModelFactory().create(getType());
        } else {
            propertyModel = new PropertyModelFactory().create(getType());
        }
        propertyModel.setOwner(this);
    }

    public void updateLocation() {
    }

    public void setModel(PropertyModel pm) {
        propertyModel.updateWithModel(pm);
    }

    public PropertyModel getModel() {
        return propertyModel;
    }

    public void draw(Graphics2D g, boolean dummy) {
        drawable.draw(this, g, dummy);
    }

    public abstract GraphElementType getType();

    public abstract void addToGraph();

    public abstract void removeFromGraph();

    public abstract List<? extends GraphElement> getConnectedElements();
}
