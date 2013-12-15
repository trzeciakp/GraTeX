package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.*;
import java.io.Serializable;


@SuppressWarnings("serial")
public abstract class GraphElement implements Serializable {
    private String latexCode = "";

    public String getLatexCode() {
        return latexCode;
    }

    public void setLatexCode(String latexCode) {
        this.latexCode = latexCode;
    }

    public abstract void draw(Graphics2D g, boolean dummy);

    public abstract void setModel(PropertyModel pm);

    public abstract PropertyModel getModel();

    public abstract GraphElementType getType();

    public abstract Graph getGraph();

    public void remove() {
        getGraph().getElements(getType()).remove(this);
    }

    /*@Override
    public boolean equals(Object obj) {
        System.out.println("obj = [" + obj + "]");
        if (!(obj instanceof GraphElement))
        {
            return false;
        }
        else
        {
            System.out.println("obj = [" + ((GraphElement) obj).getLatexCode() + "] " + getLatexCode());
            return ((GraphElement) obj).getLatexCode().equals(getLatexCode());
        }
    }

    @Override
    public int hashCode()
    {
        return getLatexCode().hashCode();
    }*/
}
