package pl.edu.agh.gratex.model.linkBoundary;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

public class LinkBoundaryDrawer implements Drawer {
    private SelectionController selectionController;

    public LinkBoundaryDrawer(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        LinkBoundary link = (LinkBoundary) graphElement;
        link.updateLocation();

        int outPointX = link.getOutPointX();
        int outPointY = link.getOutPointY();
        int inPointX = link.getInPointX();
        int inPointY = link.getInPointY();

        if (selectionController.selectionContains(link)) {
            g.setColor(Const.SELECTION_COLOR);
            Stroke drawingStroke = new BasicStroke(link.getLineWidth() + 2 * Const.EDGE_SELECTION_MARGIN);
            g.setStroke(drawingStroke);
            g.drawLine(outPointX, outPointY, inPointX, inPointY);
        }

        g.setColor(DrawingTools.getDrawingColor(link.getLineColor(), link.isDummy()));
        g.setStroke(DrawingTools.getStroke(link.getLineType(), link.getLineWidth(), 0.0));
        g.drawLine(outPointX, outPointY, inPointX, inPointY);

        if (link.isDirected()) {
            g.setColor(DrawingTools.getDrawingColor(link.getLineColor(), link.isDummy()));
            g.setStroke(new BasicStroke(link.getLineWidth()));
            if (link.getArrowType() == ArrowType.BASIC) {
                g.drawLine(link.getArrowLine1()[0], link.getArrowLine1()[1], link.getArrowLine1()[2], link.getArrowLine1()[3]);
                g.drawLine(link.getArrowLine2()[0], link.getArrowLine2()[1], link.getArrowLine2()[2], link.getArrowLine2()[3]);
            } else {
                g.fillPolygon(new Polygon(new int[]{link.getArrowLine1()[0], link.getArrowLine1()[2], link.getArrowLine2()[0]},
                        new int[]{link.getArrowLine1()[1], link.getArrowLine1()[3], link.getArrowLine2()[1]}, 3));
            }
        }
    }
}

