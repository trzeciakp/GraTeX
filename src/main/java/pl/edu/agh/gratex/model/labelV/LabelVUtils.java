package pl.edu.agh.gratex.model.labelV;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.*;

public class LabelVUtils {
    public static boolean intersects(LabelV labelV, int x, int y) {
        return labelV.getOutline().contains(x, y);
    }

    public static void updatePosition(LabelV labelV, Graphics2D g) {
        g.setFont(labelV.getFont());
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(labelV.getText());
        int height = fm.getAscent();
        int descent = fm.getDescent();
        int spacing = labelV.getSpacing();

        Point exitPoint = Geometry.calculateEdgeExitPoint(labelV.getOwner(), (450 - 45 * labelV.getPosition()) % 360);

        double drawPosX = 0.0;
        double drawPosY = 0.0;

        switch (labelV.getPosition()) {
            case 0: {
                drawPosX = exitPoint.x;
                drawPosY = exitPoint.y - spacing - height / 2;
                break;
            }
            case 1: {
                drawPosX = exitPoint.x + spacing / 1.4142 + width / 2;
                drawPosY = exitPoint.y - spacing / 1.4142 - height / 2;
                break;
            }
            case 2: {
                drawPosX = exitPoint.x + spacing + width / 2;
                drawPosY = exitPoint.y;
                break;
            }
            case 3: {
                drawPosX = exitPoint.x + spacing / 1.4142 + width / 2;
                drawPosY = exitPoint.y + spacing / 1.4142 + height / 2;
                break;
            }
            case 4: {
                drawPosX = exitPoint.x;
                drawPosY = exitPoint.y + spacing + height / 2 + 0.5;
                break;
            }
            case 5: {
                drawPosX = exitPoint.x - spacing / 1.4142 - width / 2;
                drawPosY = exitPoint.y + spacing / 1.4142 + height / 2;
                break;
            }
            case 6: {
                drawPosX = exitPoint.x - spacing - width / 2;
                drawPosY = exitPoint.y;
                break;
            }
            case 7: {
                drawPosX = exitPoint.x - spacing / 1.4142 - width / 2;
                drawPosY = exitPoint.y - spacing / 1.4142 - height / 2;
                break;
            }

        }
        labelV.setPosX((int) drawPosX);
        labelV.setPosY((int) drawPosY);
        labelV.setDrawX((int) (drawPosX - width / 2));
        labelV.setDrawY((int) (drawPosY - descent + height / 2));
        labelV.setOutline(new Rectangle(labelV.getPosX() - width / 2, labelV.getPosY() - height / 2, width, height));
    }

    public static void draw(LabelV labelV, Graphics2D g2d, boolean dummy) {
        Graphics2D g = (Graphics2D) g2d.create();

        updatePosition(labelV, g);

        if (labelV.getGraph().getGeneralController().getSelectionController().selectionContains(labelV)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fillRect(labelV.getOutline().x, labelV.getOutline().y, labelV.getOutline().width, labelV.getOutline().height);
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
