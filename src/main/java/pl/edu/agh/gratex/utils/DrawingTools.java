package pl.edu.agh.gratex.utils;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;

public class DrawingTools {
    public static Color getDrawingColor(Color color, boolean dummy) {
        if (dummy) {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
        } else {
            return new Color(color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    public static Color getBoundaryCornerColor(Color color) {
        float hsb[] = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        float newBrightness = hsb[2] < 0.5f ?
                hsb[2] + Const.BOUNDARY_COLOR_BRIGHTNESS_DIFFERENCE :
                hsb[2] - Const.BOUNDARY_COLOR_BRIGHTNESS_DIFFERENCE;

        return Color.getHSBColor(hsb[0], hsb[1], newBrightness);
    }

    public static Stroke getStroke(LineType lineType, int lineWidth, double perimeter) {
        switch (lineType) {
            case NONE:
                return new BasicStroke(0);
            case SOLID:
                return new BasicStroke(lineWidth);
            case DASHED:
                if (perimeter == 0.0) {
                    return new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{lineWidth + 3}, 0);
                } else {
                    float spacing = (float) (perimeter / (2 * (((int) (1 + Math.floor(perimeter / (lineWidth + 3)))) / 2)));
                    return new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{spacing}, 0);
                }
            case DOTTED:
                if (perimeter == 0.0) {
                    return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{1, (lineWidth * 3) / 2 + 3}, 0);
                } else {
                    float spacing = (float) (perimeter / Math.floor(perimeter / ((lineWidth * 3) / 2 + 4)));
                    return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{1, spacing - 1}, 0);
                }
            case DOUBLE:
                return new CompositeStroke(new BasicStroke(lineWidth + 2), new BasicStroke(lineWidth));
            default:
                return null;
        }
    }
}
