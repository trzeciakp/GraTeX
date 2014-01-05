package pl.edu.agh.gratex.model.boundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

/**
 *
 */
public class BoundaryDrawer implements Drawer {
    private SelectionController selectionController;

    public BoundaryDrawer(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics graphics) {
        Boundary boundary = (Boundary) graphElement;
        Graphics2D g = (Graphics2D) graphics.create();
        int topLeftX = boundary.getTopLeftX();
        int topLeftY = boundary.getTopLeftY();
        int width = boundary.getWidth();
        int height = boundary.getHeight();

        LineType lineType = boundary.getLineType();
        int lineWidth = boundary.getLineWidth();
        Color lineColor = boundary.getLineColor();
        Color fillColor = boundary.getFillColor();

        if (selectionController.selectionContains(boundary)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fillRect(topLeftX - lineWidth / 2 - 5, topLeftY - lineWidth / 2 - 5, width + lineWidth + 10, height + lineWidth + 10);
        }

        g.setColor(DrawingTools.getDrawingColor(fillColor, boundary.isDummy()));
        g.fillRect(topLeftX, topLeftY, width, height);

        g.setColor(DrawingTools.getDrawingColor(lineColor, boundary.isDummy()));
        g.setStroke(DrawingTools.getStroke(lineType, lineWidth, 2 * (width + height)));
        g.drawRect(topLeftX, topLeftY, width, height);

        g.dispose();
    }
}