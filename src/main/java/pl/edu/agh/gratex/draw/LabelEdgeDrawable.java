package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEUtils;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

/**
 *
 */
public class LabelEdgeDrawable implements Drawable {
    @Override
    public void draw(GraphElement graphElement, Graphics g2d, boolean dummy) {
        LabelE labelE = (LabelE) graphElement;
        Graphics2D g = (Graphics2D) g2d.create();

        LabelEUtils.updatePosition(labelE);

        if (labelE.getGraph().getGeneralController().getSelectionController().selectionContains(labelE)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fillPolygon(labelE.getOutline());
        }

        g.setColor(labelE.getFontColor());
        if (dummy) {
            g.setColor(DrawingTools.getDummyColor(labelE.getFontColor()));
        }
        g.setFont(labelE.getFont());
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
