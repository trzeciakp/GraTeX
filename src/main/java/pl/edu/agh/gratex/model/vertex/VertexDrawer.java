package pl.edu.agh.gratex.model.vertex;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

/**
 *
 */
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
        ShapeType shapeType = vertex.getShape();
        int radius = vertex.getRadius();
        int lineWidth = vertex.getLineWidth();
        LineType lineType = vertex.getLineType();
        Color lineColor = vertex.getLineColor();
        Color vertexColor = vertex.getVertexColor();

        int tempX = 0;
        int tempY = 0;
        if (graphElement.isDummy() && vertex.getGraph().isGridOn()) {
            tempX = posX;
            tempY = posY;
            VertexUtils.adjustToGrid(vertex);
        }

        if (selectionController.selectionContains(vertex)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fill(VertexUtils.getVertexShape(shapeType, radius + lineWidth / 2 + radius / 4, posX, posY));
        }

        if (lineWidth > 0 && lineType != LineType.NONE) {
            g.setColor(Color.white);
            g.fill(VertexUtils.getVertexShape(shapeType, radius + lineWidth / 2, posX, posY));
            g.setColor(vertexColor);
            if (graphElement.isDummy()) {
                g.setColor(DrawingTools.getDummyColor(vertexColor));
            }
            if (lineType == LineType.DOUBLE) {
                Shape innerOutline = VertexUtils.getVertexShape(shapeType, radius - 2 - (lineWidth * 23) / 16, posX, posY);
                if (shapeType == ShapeType.CIRCLE) {
                    innerOutline = VertexUtils.getVertexShape(shapeType, radius - 2 - (lineWidth * 9) / 8, posX, posY);
                }
                if (shapeType == ShapeType.TRIANGLE) {
                    innerOutline = VertexUtils.getVertexShape(shapeType, radius - 4 - (lineWidth * 11) / 5, posX, posY);
                }
                if (shapeType == ShapeType.SQUARE) {
                    innerOutline = VertexUtils.getVertexShape(shapeType, radius - 3 - (lineWidth * 13) / 8, posX, posY);
                }

                g.fill(innerOutline);

                g.setColor(lineColor);
                if (graphElement.isDummy()) {
                    g.setColor(DrawingTools.getDummyColor(lineColor));
                }
                g.setStroke(DrawingTools.getStroke(lineWidth, LineType.SOLID, 0.0));
                g.draw(VertexUtils.getVertexShape(shapeType, radius, posX, posY));
                g.draw(innerOutline);
            } else {
                Shape vertexShape = VertexUtils.getVertexShape(shapeType, radius, posX, posY);
                g.fill(vertexShape);

                g.setColor(lineColor);
                if (graphElement.isDummy()) {
                    g.setColor(DrawingTools.getDummyColor(lineColor));
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

                g.setStroke(DrawingTools.getStroke(lineWidth, lineType, perimeter));
                g.draw(vertexShape);
            }
            g.setStroke(new BasicStroke());
        }

        if (vertex.isLabelInside()) {
            g.setColor(vertex.getFontColor());
            if (graphElement.isDummy()) {
                g.setColor(DrawingTools.getDummyColor(vertex.getFontColor()));
            }
            vertex.setNumber(vertex.getNumber());
            if (vertex.getLabelInside() != null) {
                g.setFont(Const.DEFAULT_FONT);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(vertex.getLabelInside(), posX - fm.stringWidth(vertex.getLabelInside()) / 2, posY + (fm.getAscent() - fm.getDescent()) / 2);
            }
        }

        if (graphElement.isDummy() && vertex.getGraph().isGridOn()) {
            vertex.setPosX(tempX);
            vertex.setPosY(tempY);
        }

        g.dispose();
    }
}
