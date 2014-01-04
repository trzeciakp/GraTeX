package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.model.GraphElement;

import java.awt.*;

/**
 *
 */
public interface Drawable {
    void draw(GraphElement graphElement, Graphics g);
}
