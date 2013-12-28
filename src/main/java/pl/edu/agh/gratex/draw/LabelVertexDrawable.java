package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

/**
 *
 */
public class LabelVertexDrawable implements Drawable {

    private SelectionController selectionController;

    public LabelVertexDrawable(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics g2d, boolean dummy) {
        LabelV labelV = (LabelV) graphElement;
        Graphics2D g = (Graphics2D) g2d.create();

        LabelVUtils.updateLocation(labelV);

        if (selectionController.selectionContains(labelV)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fill(labelV.getOutline());
        }

        g.setFont(labelV.getFont());
        g.setColor(labelV.getFontColor());
        if (dummy) {
            g.setColor(DrawingTools.getDummyColor(labelV.getFontColor()));
        }
        g.drawString(labelV.getText(), labelV.getDrawX(), labelV.getDrawY());

        g.dispose();
    }

}
