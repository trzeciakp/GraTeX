package pl.edu.agh.gratex.model.vertex;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphNumeration;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Area;

public class VertexUtils {
    public static void updateNumber(Vertex vertex, int number) {
        vertex.setNumber(number);
        //vertex.setText(GraphNumeration.digitalToAlphabetical(number));
        /*if (vertex.getGraph().getGraphNumeration().isNumerationDigital()) {
            vertex.setText(Integer.toString(number));
        } else {
            vertex.setText(GraphNumeration.digitalToAlphabetical(number));
        } */
    }

    public static void setPartOfNumeration(Vertex vertex, boolean flag) {
        vertex.getGraph().getGraphNumeration().setUsed(vertex.getNumber(), flag);
    }

    public static void adjustToGrid(Vertex vertex) {
        Graph graph = vertex.getGraph();
        vertex.setPosX(((vertex.getPosX() + (graph.gridResolutionX / 2)) / graph.gridResolutionX) * graph.gridResolutionX);
        vertex.setPosY(((vertex.getPosY() + (graph.gridResolutionY / 2)) / graph.gridResolutionY) * graph.gridResolutionY);
    }

    public static boolean collides(Vertex vertex1, Vertex vertex2) {
        Area area = new Area(Geometry.getVertexShape(vertex1.getShape() + 1, vertex1.getRadius(), vertex1.getPosX(), vertex1.getPosY()));
        area.intersect(new Area(Geometry.getVertexShape(vertex2.getShape() + 1, vertex2.getRadius(), vertex2.getPosX(), vertex2.getPosY())));
        return !area.isEmpty();
    }

    public static boolean intersects(Vertex vertex, int x, int y) {
        Area area = new Area(Geometry.getVertexShape(vertex.getShape() + 1, vertex.getRadius(), vertex.getPosX(), vertex.getPosY()));
        return area.contains(x, y);
    }


    public static boolean fitsIntoPage(Vertex vertex) {
        return !((vertex.getPosX() - vertex.getRadius() - vertex.getLineWidth() / 2 < 0) || (vertex.getPosX() + vertex.getRadius() + vertex.getLineWidth() / 2 > Const.PAGE_WIDTH)
                || (vertex.getPosY() - vertex.getRadius() - vertex.getLineWidth() / 2 < 0) || (vertex.getPosY() + vertex.getRadius() + vertex.getLineWidth() / 2 > Const.PAGE_HEIGHT));
    }
/*
    public static void drawVertex(Vertex vertex, Graphics2D graphics, boolean dummy) {
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
        if (dummy && graph.gridOn) {
            tempX = posX;
            tempY = posY;
            adjustToGrid(vertex);
        }

        if (graph.getGeneralController().getSelectionController().selectionContains(vertex)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fill(Geometry.getVertexShape(shape + 1, radius + lineWidth / 2 + radius / 4, posX, posY));
        }

        g.setColor(vertexColor);
        if (dummy) {
            g.setColor(DrawingTools.getDummyColor(vertexColor));
        }

        if (lineWidth > 0 && lineType != LineType.NONE) {
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

                g.setColor(Color.white);
                g.fill(Geometry.getVertexShape(shape + 1, radius + lineWidth / 2, posX, posY));

                g.setColor(vertexColor);
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(vertexColor));
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
            updateNumber(vertex, vertex.getNumber());
            if (vertex.getLabelInside() != null) {
                g.setFont(vertex.getFont());
                FontMetrics fm = g.getFontMetrics();
                g.drawString(vertex.getLabelInside(), posX - fm.stringWidth(vertex.getLabelInside()) / 2, posY + (fm.getAscent() - fm.getDescent()) / 2);
            }
        }

        if (dummy && graph.gridOn) {
            vertex.setPosX(tempX);
            vertex.setPosY(tempY);
        }

        g.dispose();
    }*/
}
