package pl.edu.agh.gratex.model.linkBoundary;

import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;

import java.awt.*;

public class LinkBoundaryDrawer implements Drawer {
    private SelectionController selectionController;

    public LinkBoundaryDrawer(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics graphics) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

