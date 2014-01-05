package pl.edu.agh.gratex.model.boundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
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

        int tempX = 0;
        int tempY = 0;
        int tempWidth = 0;
        int tempHeight = 0;
        if (graphElement.isDummy() && boundary.getGraph().isGridOn()) {
            tempX = boundary.getTopLeftX();
            tempY = boundary.getTopLeftY();
            tempWidth = boundary.getWidth();
            tempHeight = boundary.getHeight();
            BoundaryUtils.adjustToGrid(boundary);
        }

        int topLeftX = boundary.getTopLeftX();
        int topLeftY = boundary.getTopLeftY();
        int width = boundary.getWidth();
        int height = boundary.getHeight();

        LineType lineType = boundary.getLineType();
        int lineWidth = boundary.getLineWidth();
        if (lineType == LineType.NONE) {
            lineWidth = 0;
        }
        Color lineColor = boundary.getLineColor();
        Color fillColor = boundary.getFillColor();

        if (selectionController.selectionContains(boundary)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fillRect(topLeftX - lineWidth / 2 - 7, topLeftY - lineWidth / 2 - 7, width + lineWidth + 14, height + lineWidth + 14);
        }

        g.setColor(DrawingTools.getDrawingColor(fillColor, boundary.isDummy()));
        g.fillRect(topLeftX, topLeftY, width, height);

        if (selectionController.selectionContains(boundary)) {
            int cornerWidth = (int) (width * Const.BOUNDARY_CORNER_LENGTH_FACTOR);
            int cornerHeight = (int) (height * Const.BOUNDARY_CORNER_LENGTH_FACTOR);

            drawCorner(g, topLeftX, topLeftY, cornerWidth, cornerHeight, fillColor);
            drawCorner(g, topLeftX + width, topLeftY, -cornerWidth, cornerHeight, fillColor);
            drawCorner(g, topLeftX + width, topLeftY + height, -cornerWidth, -cornerHeight, fillColor);
            drawCorner(g, topLeftX, topLeftY + height, cornerWidth, -cornerHeight, fillColor);
        }

        g.setStroke(new BasicStroke(1));
        g.setColor(DrawingTools.getDrawingColor(lineColor, boundary.isDummy()));
        g.setStroke(DrawingTools.getStroke(lineType, lineWidth, 2 * (width + height)));
        g.drawRect(topLeftX, topLeftY, width, height);

        if (boundary.isDummy() && boundary.getGraph().isGridOn()) {
            boundary.setTopLeftX(tempX);
            boundary.setTopLeftY(tempY);
            boundary.setWidth(tempWidth);
            boundary.setHeight(tempHeight);
        }

        g.dispose();
    }


    private void drawCorner(Graphics2D g, int outsideX, int outsideY, int width, int height, Color arrowColor) {
        g.setColor(DrawingTools.getBoundaryCornerColor(arrowColor));
        int x = Math.min(outsideX, outsideX + width);
        int y = Math.min(outsideY, outsideY + height);
        g.fillRect(x, y, Math.abs(width), Math.abs(height));

        int arrowTipX = (int) (outsideX + width * (1 - Const.BOUNDARY_ARROW_SIZE_FACTOR) / 2.0);
        int arrowTipY = (int) (outsideY + height * (1 - Const.BOUNDARY_ARROW_SIZE_FACTOR) / 2.0);

        int arrowEndX = (int) (outsideX + width * (1 + Const.BOUNDARY_ARROW_SIZE_FACTOR) / 2.0);
        int arrowEndY = (int) (outsideY + height * (1 + Const.BOUNDARY_ARROW_SIZE_FACTOR) / 2.0);

        g.setColor(arrowColor);
        int lineWidth = (2500 + Math.abs(width * height)) / 2500;
        g.setStroke(DrawingTools.getStroke(LineType.SOLID, lineWidth, 0.0));
        g.drawLine(arrowTipX + (int) (Math.signum(width) * lineWidth / 2), arrowTipY + (int) (Math.signum(height) * lineWidth / 2), arrowEndX, arrowEndY);
        g.drawLine(arrowTipX, arrowTipY, arrowTipX + (int) (width * Const.BOUNDARY_ARROW_HEAD_SIZE_FACTOR), arrowTipY);
        g.drawLine(arrowTipX, arrowTipY, arrowTipX, arrowTipY + (int) (height * Const.BOUNDARY_ARROW_HEAD_SIZE_FACTOR));
    }
}
