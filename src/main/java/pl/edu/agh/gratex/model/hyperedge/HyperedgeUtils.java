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

            int middleX = hyperedge.getJointCenterX();
            int middleY = hyperedge.getJointCenterY();

            int spacing = Const.HYPEREDGE_JOINT_LABEL_MARGIN + hyperedge.getJointSize() + hyperedge.getJointLineWidth() / 2;

            switch (hyperedge.getJointLabelPosition()) {
                case ABOVE: {
                    hyperedge.setLabelPosX(middleX);
                    hyperedge.setLabelPosY(middleY - spacing - height / 2);
                    hyperedge.setLabelDrawX(middleX - width / 2);
                    hyperedge.setLabelDrawY(middleY - spacing - descent);
                    break;
                }
                case RIGHT: {
                    hyperedge.setLabelPosX(middleX + spacing + width / 2);
                    hyperedge.setLabelPosY(middleY);
                    hyperedge.setLabelDrawX(middleX + spacing);
                    hyperedge.setLabelDrawY(middleY - descent + height / 2);
                    break;
                }
                case BELOW: {
                    hyperedge.setLabelPosX(middleX);
                    hyperedge.setLabelPosY(middleY + spacing + height / 2);
                    hyperedge.setLabelDrawX(middleX - width / 2);
                    hyperedge.setLabelDrawY(middleY - descent + spacing + height);
                    break;
                }
                case LEFT: {
                    hyperedge.setLabelPosX(middleX - spacing - width /2);
                    hyperedge.setLabelPosY(middleY);
                    hyperedge.setLabelDrawX(middleX - spacing - width);
                    hyperedge.setLabelDrawY(middleY - descent + height / 2);
                    break;
                }
                case INSIDE: {
                    hyperedge.setLabelPosX(middleX);
                    hyperedge.setLabelPosY(middleY);
                    hyperedge.setLabelDrawX(middleX - width / 2);
                    hyperedge.setLabelDrawY(middleY - descent + height / 2);
                    break;
                }
            }
        }
    }
}
