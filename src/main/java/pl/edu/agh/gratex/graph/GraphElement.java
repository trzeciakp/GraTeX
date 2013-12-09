package pl.edu.agh.gratex.graph;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;
import java.io.Serializable;


public abstract class GraphElement implements Serializable {
    private static final long serialVersionUID = 633609989731960865L;

    public abstract void draw(Graphics2D g, boolean dummy);

    public abstract void setModel(PropertyModel pm);

    public abstract PropertyModel getModel();

    public abstract GraphElementType getType();

    public abstract Graph getGraph();

    public void remove() {
        getGraph().getElements(getType()).remove(this);
    }
}
