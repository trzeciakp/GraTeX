package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;

import java.awt.*;

/**
 *
 */
public class HyperedgeDrawer implements Drawer {
    private SelectionController selectionController;

    public HyperedgeDrawer(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics g) {

    }
}
