package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.*;
import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public abstract class GraphElement implements Serializable {
    protected String latexCode = "";
    protected Graph graph;

    protected GraphElement(Graph graph) {
        this.graph = graph;
    }

    public String getLatexCode() {
        return latexCode;
    }

    public void setLatexCode(String latexCode) {
        this.latexCode = latexCode;
    }

    public GraphElement getCopy() {
        try {
            String code = graph.getGeneralController().getParseController().getParserByElementType(getType()).parseToLatex(this);
            return graph.getGeneralController().getParseController().getParserByElementType(getType()).parseToGraph(code, graph);
        } catch (Exception e) {
            e.printStackTrace();
            graph.getGeneralController().criticalError("Parser error", e);
            return null;
        }
    }

    public abstract void draw(Graphics2D g, boolean dummy);

    public abstract void setModel(PropertyModel pm);

    public abstract PropertyModel getModel();

    public abstract GraphElementType getType();

    public abstract Graph getGraph();

    public abstract void addToGraph();

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
