package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.model.GraphElement;

import java.awt.*;

/**
 *
 */
public interface Drawer {
    void draw(GraphElement graphElement, Graphics graphics);
}
