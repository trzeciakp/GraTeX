package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.*;

/**
 *
 */
public class VertexDrawable implements Drawable {

    private SelectionController selectionController;

    public VertexDrawable(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement element, Graphics graphics, boolean dummy) {
        Vertex vertex = (Vertex) element;
        Graphics2D g = (Graphics2D) graphics.create();
        Graph graph = vertex.getGraph();

        int posX = vertex.getPosX();
        int posY = vertex.getPosY();
        int shape = vertex.getShape();
        int radius = vertex.getRadius();
        int lineWidth = vertex.getLineWidth();
        LineType lineType = vertex.getLineType();
        Color lineColor = vertex.getLineColor();
        Color vertexColor = vertex.getVertexColor();

        int tempX = 0;
        int tempY = 0;
        if (dummy && graph.isGridOn()) {
            tempX = posX;
            tempY = posY;
            VertexUtils.adjustToGrid(vertex);
        }

        if (selectionController.selectionContains(vertex)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fill(Geometry.getVertexShape(shape + 1, radius + lineWidth / 2 + radius / 4, posX, posY));
        }

        if (lineWidth > 0 && lineType != LineType.NONE) {
            g.setColor(Color.white);
            g.fill(Geometry.getVertexShape(shape + 1, radius + lineWidth / 2, posX, posY));
            g.setColor(vertexColor);
            if (dummy) {
                g.setColor(DrawingTools.getDummyColor(vertexColor));
            }
            // TODO Tutaj chyba wystarczy najpierw namalowac na bialo gruba linie, a na tym podwojna, a nie tak kombonowac na jana ze srednica wierzcholka
            // TODO Czyli przerobic metode CompositeStroke.createStrokenShape().
            if (lineType == LineType.DOUBLE) {
                Shape innerOutline = Geometry.getVertexShape(shape + 1, radius - 2 - (lineWidth * 23) / 16, posX, posY);
                if (shape == ShapeType.CIRCLE.getValue()) {
                    innerOutline = Geometry.getVertexShape(shape + 1, radius - 2 - (lineWidth * 9) / 8, posX, posY);
                }
                if (shape == ShapeType.TRIANGLE.getValue()) {
                    innerOutline = Geometry.getVertexShape(shape + 1, radius - 4 - (lineWidth * 11) / 5, posX, posY);
                }
                if (shape == ShapeType.SQUARE.getValue()) {
                    innerOutline = Geometry.getVertexShape(shape + 1, radius - 3 - (lineWidth * 13) / 8, posX, posY);
                }

                g.fill(innerOutline);

                g.setColor(lineColor);
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(lineColor));
                }
                g.setStroke(DrawingTools.getStroke(lineWidth, LineType.SOLID, 0.0));
                g.draw(Geometry.getVertexShape(shape + 1, radius, posX, posY));
                g.draw(innerOutline);
            } else {
                Shape vertexShape = Geometry.getVertexShape(shape + 1, radius, posX, posY);
                g.fill(vertexShape);

                g.setColor(lineColor);
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(lineColor));
                }

                double girth = Math.PI * 2 * radius;
                if (shape == 2) {
                    girth = Math.sqrt(3) * radius;
                }
                if (shape == 3) {
                    girth = Math.sqrt(2) * radius;
                }
                if (shape == 4) {
                    girth = 2 * radius * Math.cos(Math.toRadians(54));
                }
                if (shape == 5) {
                    girth = radius;
                }

                g.setStroke(DrawingTools.getStroke(lineWidth, lineType, girth));
                g.draw(vertexShape);
            }
            g.setStroke(new BasicStroke());
        }

        if (vertex.isLabelInside()) {
            g.setColor(vertex.getFontColor());
            if (dummy) {
                g.setColor(DrawingTools.getDummyColor(vertex.getFontColor()));
            }
            vertex.setNumber(vertex.getNumber());
            if (vertex.getLabelInside() != null) {
                g.setFont(Const.DEFAULT_FONT);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(vertex.getLabelInside(), posX - fm.stringWidth(vertex.getLabelInside()) / 2, posY + (fm.getAscent() - fm.getDescent()) / 2);
            }
        }

        if (dummy && graph.isGridOn()) {
            vertex.setPosX(tempX);
            vertex.setPosY(tempY);
        }

        g.dispose();
    }
}
