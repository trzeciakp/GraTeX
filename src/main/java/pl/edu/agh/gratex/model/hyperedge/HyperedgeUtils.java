package pl.edu.agh.gratex.model.hyperedge;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.properties.IsLabelInside;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;

public class HyperedgeUtils {
    public static boolean fitsIntoPage(Hyperedge hyperedge) {
        return !((hyperedge.getJointCenterX() - hyperedge.getJointSize() - 1 / 2 < 0) ||
                (hyperedge.getJointCenterX() + hyperedge.getJointSize() + 1 / 2 > Const.PAGE_WIDTH) ||
                (hyperedge.getJointCenterY() - hyperedge.getJointSize() - 1 / 2 < 0) ||
                (hyperedge.getJointCenterY() + hyperedge.getJointSize() + 1 / 2 > Const.PAGE_HEIGHT));
    }

    public static void updateLocation(Hyperedge hyperedge) {
        if (hyperedge.getJointHasLabel() == IsLabelInside.YES) {
            FontMetrics fm = new Canvas().getFontMetrics(Const.DEFAULT_FONT);
            int width = fm.stringWidth(hyperedge.getText());
            int height = fm.getAscent();
            int descent = fm.getDescent();

            hyperedge.setLabelDrawX(hyperedge.getJointCenterX() + 10);
            hyperedge.setLabelDrawY(hyperedge.getJointCenterY() + 10);

        }
    }
}
