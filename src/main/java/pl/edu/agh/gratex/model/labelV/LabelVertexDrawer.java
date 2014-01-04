package pl.edu.agh.gratex.model.labelV;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.Drawer;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

/**
 *
 */
public class LabelVertexDrawer implements Drawer {

    private SelectionController selectionController;

    public LabelVertexDrawer(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics g2d) {
        LabelV labelV = (LabelV) graphElement;
        Graphics2D g = (Graphics2D) g2d.create();

        labelV.updateLocation();

        if (selectionController.selectionContains(labelV)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fill(labelV.getOutline());
        }

        g.setFont(Const.DEFAULT_FONT);
        g.setColor(labelV.getFontColor());
        if (graphElement.isDummy()) {
            g.setColor(DrawingTools.getDummyColor(labelV.getFontColor()));
        }
        g.drawString(labelV.getText(), labelV.getDrawX(), labelV.getDrawY());

        g.dispose();
    }

}
