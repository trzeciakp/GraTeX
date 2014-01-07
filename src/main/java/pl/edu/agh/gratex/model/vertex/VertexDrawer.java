package pl.edu.agh.gratex.model.vertex;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;


public class VertexDrawer implements Drawer {

    private SelectionController selectionController;

    public VertexDrawer(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics graphics) {
        Vertex vertex = (Vertex) graphElement;
        Graphics2D g = (Graphics2D) graphics.create();

        int posX = vertex.getPosX();
        int posY = vertex.getPosY();

        int tempX = 0;
        int tempY = 0;
        if (graphElement.isDummy() && vertex.getGraph().isGridOn()) {
            tempX = posX;
            tempY = posY;
            VertexUtils.adjustToGrid(vertex);
        }

        drawVertex(g, posX, posY, vertex.getShape(), vertex.getRadius(), vertex.getVertexColor(),
                vertex.getLineType(), vertex.getLineWidth(), vertex.getLineColor(), selectionController.selectionContains(vertex), vertex.isDummy());
        g.setStroke(new BasicStroke());

        if (vertex.isLabelInside()) {
            g.setColor(DrawingTools.getDrawingColor(vertex.getFontColor(), vertex.isDummy()));
            vertex.setNumber(vertex.getNumber());
            if (vertex.getLabelInside() != null) {
                g.setFont(Const.DEFAULT_FONT);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(vertex.getLabelInside(), posX - fm.stringWidth(vertex.getLabelInside()) / 2, posY + (fm.getAscent() - fm.getDescent()) / 2);
            }
        }

        if (vertex.isDummy() && vertex.getGraph().isGridOn()) {
            vertex.setPosX(tempX);
            vertex.setPosY(tempY);
        }

        g.dispose();
    }

    public static void drawVertex(Graphics2D g, int posX, int posY, ShapeType shapeType, int radius, Color vertexColor,
                                  LineType lineType, int lineWidth, Color lineColor, boolean isSelected, boolean isDummy) {
        if (lineType == LineType.NONE) {
            lineWidth = 0;
        }

        if (isSelected) {
            g.setColor(Const.SELECTION_COLOR);
            g.fill(VertexUtils.getVertexShape(shapeType, radius + lineWidth / 2 + radius / 4, posX, posY));
        }

        g.setColor(Color.white);
        g.fill(VertexUtils.getVertexShape(shapeType, radius + lineWidth / 2, posX, posY));

        g.setColor(DrawingTools.getDrawingColor(vertexColor, isDummy));
        g.fill(VertexUtils.getVertexShape(shapeType, radius + lineWidth / 2, posX, posY));

        if (lineType != LineType.NONE) {
            if (lineType == LineType.DOUBLE) {
                g.setColor(Color.white);
                g.setStroke(DrawingTools.getStroke(LineType.SOLID, lineWidth, 0.0));
                g.draw(VertexUtils.getVertexShape(shapeType, radius, posX, posY));
            }

            double perimeter = Math.PI * 2 * radius;
            switch (shapeType) {
                case TRIANGLE:
                    perimeter = Math.sqrt(3) * radius;
                    break;
                case SQUARE:
                    perimeter = Math.sqrt(2) * radius;
                    break;
                case PENTAGON:
                    perimeter = 5 * (2 * radius * Math.cos(Math.toRadians(54)));
                    break;
                case HEXAGON:
                    perimeter = 6 * radius;
                    break;
            }
            g.setColor(DrawingTools.getDrawingColor(lineColor, isDummy));
            g.setStroke(DrawingTools.getStroke(lineType, lineWidth, perimeter));
            g.draw(VertexUtils.getVertexShape(shapeType, radius, posX, posY));
        }
    }
}
