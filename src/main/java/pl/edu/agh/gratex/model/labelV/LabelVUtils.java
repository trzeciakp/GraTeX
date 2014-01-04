package pl.edu.agh.gratex.model.labelV;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.*;
import java.awt.geom.Point2D;

public class LabelVUtils {
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

        Point exitPoint = Geometry.calculateEdgeExitPoint(labelV.getOwner(), (450 - 45 * labelV.getLabelPosition().getValue()) % 360);

        double drawPosX = 0.0;
        double drawPosY = 0.0;

        switch (labelV.getLabelPosition().getValue()) {
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
}
