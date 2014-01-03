package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.ParseController;
import pl.edu.agh.gratex.draw.Drawable;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.view.Application;

import java.awt.*;
import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public abstract class GraphElement implements Serializable {
    protected Graph graph;
    protected Drawable drawable;

    public Graph getGraph() {
        return graph;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    protected GraphElement(Graph graph) {
        this.graph = graph;
    }

    public void updateLocation() {
    }

    public void draw(Graphics2D g, boolean dummy) {
        drawable.draw(this, g, dummy);
    }

    public abstract void setModel(PropertyModel pm);

    public abstract PropertyModel getModel();

    public abstract GraphElementType getType();

    public abstract void addToGraph();

    public abstract void removeFromGraph();

    public abstract List<? extends GraphElement> getConnectedElements();
}
