package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.model.GraphElement;

import java.util.Collection;
import java.util.List;

public interface SelectionController {
    public List<GraphElement> getSelection();
    public int getSize();
    public boolean contains(Object o);
    public void clearSelection();
    public void selectAll();
    public void addToSelection(Collection<? extends GraphElement> elements, boolean controlDown);
    public void addToSelection(GraphElement element, boolean controlDown);
}
