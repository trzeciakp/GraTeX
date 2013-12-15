package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.model.GraphElement;

import java.util.List;

/**
 *
 */
public interface SelectionListener {
    public void selectionChanged(List<? extends GraphElement> collection);
}
