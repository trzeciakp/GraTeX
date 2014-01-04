package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.GraphElement;

import java.awt.*;

/**
 *
 */
public class BoundaryDrawable implements Drawable {
    public BoundaryDrawable(SelectionController selectionController) {
    }

    @Override
    public void draw(GraphElement graphElement, Graphics g, boolean dummy) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawString("DUUUPA", 10, 10);
    }
}
