package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;

import java.util.Collection;
import java.util.List;

public interface SelectionController {
    public List<GraphElement> getSelection();
    public int selectionSize();
    public boolean selectionContains(GraphElement element);
    public void clearSelection();
    public void selectAll(Graph graph);
    public void addToSelection(Collection<? extends GraphElement> elements, boolean controlDown);
    public void addToSelection(GraphElement element, boolean controlDown);
    public void repeatSelection();
    public void addListener(SelectionListener listener);
    public void removeListener(SelectionListener listener);
}
