package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.IsLabelInside;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexDrawer;
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
        HyperedgeUtils.updateLocation(hyperedge);
        Graphics2D g = (Graphics2D) graphics.create();

        int middleX = hyperedge.getJointCenterX();
        int middleY = hyperedge.getJointCenterY();
        int lineWidth = hyperedge.getLineWidth();

        if (selectionController.selectionContains(hyperedge)) {
            g.setColor(Const.SELECTION_COLOR);
            g.setStroke(new BasicStroke(lineWidth * 2 + 5));
            for (Vertex vertex : hyperedge.getConnectedVertices()) {
                g.drawLine(vertex.getPosX(), vertex.getPosY(), middleX, middleY);
            }
        }

        g.setColor(DrawingTools.getDrawingColor(hyperedge.getLineColor(), hyperedge.isDummy()));
        g.setStroke(DrawingTools.getStroke(hyperedge.getLineType(), lineWidth, 0.0));
        for (Vertex vertex : hyperedge.getConnectedVertices()) {
            g.drawLine(vertex.getPosX(), vertex.getPosY(), middleX, middleY);
        }

        VertexDrawer.drawVertex(g, middleX, middleY, hyperedge.getJointShape(), hyperedge.getJointSize(), hyperedge.getJointColor(), hyperedge.getJointLineType(),
                hyperedge.getJointLineWidth(), hyperedge.getJointLineColor(), selectionController.selectionContains(hyperedge), hyperedge.isDummy());

        if (hyperedge.getJointHasLabel() == IsLabelInside.YES) {
            g.setColor(hyperedge.getJointLabelColor());
            g.drawString(hyperedge.getText(), hyperedge.getLabelDrawX(), hyperedge.getLabelDrawY());
        }

        g.dispose();
    }
}
