package pl.edu.agh.gratex.model.labelV;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.*;
import java.awt.geom.Point2D;

public class LabelVUtils {
    // Returns true if (x, y) is in the area occupied by the label
    public static boolean intersects(LabelV labelV, int x, int y) {
        return labelV.getOutline().contains(x, y);
    }

    // Calculates the position of LabelV (N, S, W, E etc) according to vertex and cursor location
    public static int getPositionFromCursorLocation(Vertex owner, int mouseX, int mouseY) {
        Point2D p1 = new Point(owner.getPosX(), owner.getPosY());
        Point2D p2 = new Point(mouseX, mouseY);
        double angle = Math.toDegrees(Math.asin((mouseX - owner.getPosX()) / p1.distance(p2)));
        if (mouseY < owner.getPosY()) {
            if (mouseX < owner.getPosX()) {
                angle += 360;
            }
        } else {
            angle = 180 - angle;
        }
        return ((int) Math.abs(Math.ceil((angle - 22.5) / 45))) % 8;
    }


    // Calculates the location of LabelV and attributes needed for parsing
    public static void updateLocation(LabelV labelV) {
        FontMetrics fm = new Canvas().getFontMetrics(Const.DEFAULT_FONT);
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
/*

    public static void draw(LabelV labelV, Graphics2D g2d, boolean dummy) {
        Graphics2D g = (Graphics2D) g2d.createEmptyModel();

        updateLocation(labelV);

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

*/
}
