package pl.edu.agh.gratex.model.boundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

/**
 *
 */
public class EditedBoundaryDrawer implements Drawer {
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

        g.setColor(Const.SELECTION_COLOR);
        g.fillRect(topLeftX - lineWidth / 2 - 5, topLeftY - lineWidth / 2 - 5, width + lineWidth + 10, height + lineWidth + 10);

        g.setColor(fillColor);
        g.fillRect(topLeftX, topLeftY, width, height);

        int cornerWidth = (int) (width * Const.BOUNDARY_CORNER_LENGTH_FACTOR);
        int cornerHeight = (int) (height * Const.BOUNDARY_CORNER_LENGTH_FACTOR);

        drawCorner(g, topLeftX, topLeftY, cornerWidth, cornerHeight, lineColor, fillColor);
        drawCorner(g, topLeftX + width, topLeftY, -cornerWidth, cornerHeight, lineColor, fillColor);
        drawCorner(g, topLeftX + width, topLeftY + height, -cornerWidth, -cornerHeight, lineColor, fillColor);
        drawCorner(g, topLeftX, topLeftY + height, cornerWidth, -cornerHeight, lineColor, fillColor);

        g.setColor(lineColor);
        g.setStroke(DrawingTools.getStroke(lineType, lineWidth, 2 * (width + height)));
        g.drawRect(topLeftX, topLeftY, width, height);

        g.dispose();
    }

    private void drawCorner(Graphics2D g, int outsideX, int outsideY, int width, int height, Color backgroundColor, Color arrowColor) {
        g.setColor(backgroundColor);
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
        g.drawLine(arrowTipX, arrowTipY, arrowTipX + width / 4, arrowTipY);
        g.drawLine(arrowTipX, arrowTipY, arrowTipX, arrowTipY + height / 4);
    }
}
