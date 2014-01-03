package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEUtils;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

/**
 *
 */
public class LabelEdgeDrawable implements Drawable {

    private SelectionController selectionController;

    public LabelEdgeDrawable(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics g2d, boolean dummy) {
        LabelE labelE = (LabelE) graphElement;
        Graphics2D g = (Graphics2D) g2d.create();

        labelE.updateLocation();

        if (selectionController.selectionContains(labelE)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fillPolygon(labelE.getOutline());
        }

        g.setColor(labelE.getFontColor());
        if (dummy) {
            g.setColor(DrawingTools.getDummyColor(labelE.getFontColor()));
        }
        g.setFont(Const.DEFAULT_FONT);
        if (labelE.isHorizontalPlacement()) {
            g.drawString(labelE.getText(), labelE.getDrawX(), labelE.getDrawY());
        } else {
            g.translate(labelE.getDrawX(), labelE.getDrawY());
            g.rotate(Math.toRadians(360 - labelE.getAngle()), 0, 0);
            g.drawString(labelE.getText(), 0, 0);
        }

        g.dispose();
    }

}
