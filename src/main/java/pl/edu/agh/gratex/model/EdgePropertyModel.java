package pl.edu.agh.gratex.model;

import java.awt.Color;
import java.io.Serializable;

public class EdgePropertyModel extends PropertyModel implements Serializable
{
	private static final long	serialVersionUID	= 8852715176545442749L;

	public int					lineType;
	public int					lineWidth;
	public int					directed;
	public Color				lineColor;
	public int					relativeEdgeAngle;
	public int					isLoop;
	public int					arrowType;

	public EdgePropertyModel()
	{
		lineType = PropertyModel.EMPTY;
		lineWidth = PropertyModel.EMPTY;
		directed = PropertyModel.EMPTY;
		lineColor = null;
		relativeEdgeAngle = PropertyModel.EMPTY;
		isLoop = PropertyModel.NO;
		arrowType = PropertyModel.EMPTY;
	}

	public EdgePropertyModel(EdgePropertyModel pm)
	{
		copy(pm);
	}

	private void copy(EdgePropertyModel pm)
	{
		lineType = pm.lineType;
		lineWidth = pm.lineWidth;
		directed = pm.directed;
		if (pm.lineColor != null)
			lineColor = new Color(pm.lineColor.getRGB());
		else
			lineColor = null;
		relativeEdgeAngle = pm.relativeEdgeAngle;
		isLoop = pm.isLoop;
		arrowType = pm.arrowType;
	}

	public void andOperator(PropertyModel pm)
	{
		EdgePropertyModel model = (EdgePropertyModel) pm;

		if (model.lineType != lineType)
		{
			lineType = -1;
		}

		if (model.lineWidth != lineWidth)
		{
			lineWidth = -1;
		}

		if (model.directed != directed)
		{
			directed = -1;
		}

		if (lineColor != null)
		{
			if (!model.lineColor.equals(lineColor))
			{
				lineColor = null;
			}
		}

		if (model.relativeEdgeAngle != relativeEdgeAngle)
		{
			relativeEdgeAngle = -1;
		}

		if (model.isLoop != isLoop)
		{
			isLoop = -1;
		}

		if (model.arrowType != arrowType)
		{
			arrowType = -1;
		}
	}
}
