package pl.edu.agh.gratex.graph;

import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class DrawingTools
{
	public static final Color	selectionColor	= new Color(72, 118, 255, 128);

	public static Color getDummyColor(Color color)
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
	}

	public static Stroke getStroke(int lineWidth, LineType lineType, double girth)
	{
        switch (lineType) {
            case SOLID:
			    return new BasicStroke(lineWidth);
            case DASHED:
                if (girth == 0.0)
                {
                    return new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { lineWidth + 3 }, 0);
                }
                else
                {
                    float spacing = (float) (girth / (2 * (((int) (1 + Math.floor(girth / (lineWidth + 3)))) / 2)));
                    return new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { spacing }, 0);
                }
            case DOTTED:
                if (girth == 0.0)
                {
                    return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 1, (lineWidth * 3) / 2 + 3 }, 0);
                }
                else
                {
                    float spacing = (float) (girth / Math.floor(girth / (int) ((lineWidth * 3) / 2 + 4)));
                    return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 1, spacing - 1 }, 0);
                }
            case DOUBLE:
			    return new CompositeStroke(new BasicStroke(lineWidth + 2), new BasicStroke(lineWidth));
            default:
                //TODO  maybe exception? if empty
                return null;
        }
	}
}
