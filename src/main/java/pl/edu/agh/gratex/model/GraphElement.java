package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.draw.Drawable;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.view.Application;

import java.awt.*;
import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public abstract class GraphElement implements Serializable {
    protected String latexCode = "";

    protected Graph graph;

    private Drawable drawable;

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    protected GraphElement(Graph graph) {
        this.graph = graph;
    }

    public String getLatexCode() {
        return latexCode;
    }

    public void setLatexCode(String latexCode) {
        this.latexCode = latexCode;
    }

    public void draw(Graphics2D g, boolean dummy) {
        drawable.draw(this, g, dummy);
    }

    public abstract void setModel(PropertyModel pm);

    public abstract PropertyModel getModel();

    public abstract GraphElementType getType();

    public abstract Graph getGraph();

    public abstract void addToGraph(String code);

    public abstract void removeFromGraph();

    public abstract List<? extends GraphElement> getConnectedElements();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphElement)) {
            return false;
        } else {
            return ((GraphElement) obj).getLatexCode().equals(getLatexCode());
        }
    }

    @Override
    public int hashCode() {
        return getLatexCode().hashCode();
    }
}
