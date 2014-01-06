package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.utils.DrawingTools;

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
    public void draw(GraphElement graphElement, Graphics graphics) {
        Hyperedge hyperedge = (Hyperedge) graphElement;
        Graphics2D g = (Graphics2D) graphics.create();

        int middleX = hyperedge.getJointCenterX();
        int middleY = hyperedge.getJointCenterY();

        int lineWidth = hyperedge.getLineWidth();
        LineType lineType = hyperedge.getLineType();
        if (lineType == LineType.NONE) {
            lineWidth = 0;
        }
        Color lineColor = hyperedge.getLineColor();
        ShapeType jointShape = hyperedge.getJointShape();
        int jointRadius = hyperedge.getJointSize();
        Color jointColor = hyperedge.getJointColor();

        if (selectionController.selectionContains(hyperedge)) {
            g.setColor(Const.SELECTION_COLOR);
            g.setStroke(new BasicStroke(lineWidth * 2 + 5));
            for (Vertex vertex : hyperedge.getConnectedVertices()) {
                g.drawLine(vertex.getPosX(), vertex.getPosY(), middleX, middleY);
            }

            g.setStroke(new BasicStroke(1));
            Shape joint = VertexUtils.getVertexShape(jointShape, jointRadius + 1 + jointRadius / 4, middleX, middleY);
            g.fill(joint);
        }

        g.setColor(DrawingTools.getDrawingColor(lineColor, hyperedge.isDummy()));
        g.setStroke(DrawingTools.getStroke(lineType, lineWidth, 0.0));
        for (Vertex vertex : hyperedge.getConnectedVertices()) {
            g.drawLine(vertex.getPosX(), vertex.getPosY(), middleX, middleY);
        }

        Shape joint = VertexUtils.getVertexShape(jointShape, jointRadius, middleX, middleY);
        g.setColor(Color.white);
        g.fill(joint);
        g.setColor(DrawingTools.getDrawingColor(jointColor, hyperedge.isDummy()));
        g.fill(joint);
        g.setColor(DrawingTools.getDrawingColor(Color.black, hyperedge.isDummy()));
        g.setStroke(new BasicStroke(1));
        g.draw(joint);

        g.dispose();
    }
}
