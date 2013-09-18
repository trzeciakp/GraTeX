package pl.edu.agh.gratex.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class DrawingTools
{
	public static final Color	selectionColor	= new Color(72, 118, 255, 128);

	public static final int		EMPTY_LINE		= 0;
	public static final int		SOLID_LINE		= 1;
	public static final int		DASHED_LINE		= 2;
	public static final int		DOTTED_LINE		= 3;
	public static final int		DOUBLE_LINE		= 4;

	public static Color getDummyColor(Color color)
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
	}

	public static Stroke getStroke(int lineWidth, int lineType, double girth)
	{
		if (lineType == SOLID_LINE)
		{
			return new BasicStroke(lineWidth);
		}
		else if (lineType == DASHED_LINE)
		{
			if (girth == 0.0)
			{
				return new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { lineWidth + 3 }, 0);
			}
			else
			{
				float spacing = (float) (girth / (2 * (((int) (1 + Math.floor(girth / (lineWidth + 3)))) / 2)));
				return new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { spacing }, 0);
			}
		}
		else if (lineType == DOTTED_LINE)
		{
			if (girth == 0.0)
			{
				return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 1, (lineWidth * 3) / 2 + 3 }, 0);
			}
			else
			{
				float spacing = (float) (girth / Math.floor(girth / (int) ((lineWidth * 3) / 2 + 4)));
				return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 1, spacing - 1 }, 0);
			}
		}
		else
		{
			return new CompositeStroke(new BasicStroke(lineWidth + 2), new BasicStroke(lineWidth));
		}
	}
}
