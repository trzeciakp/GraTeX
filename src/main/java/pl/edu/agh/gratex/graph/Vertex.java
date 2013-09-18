package pl.edu.agh.gratex.graph;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.io.Serializable;

import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.VertexPropertyModel;


public class Vertex extends GraphElement implements Serializable
{
	private static final long	serialVersionUID	= -3978311311955384768L;

	// Wartości edytowalne przez użytkowanika
	private int					number;
	public int					radius;
	public int					type;
	public Color				vertexColor;
	public int					lineWidth;
	public int					lineType;
	public Color				lineColor;
	public Font					font;
	public Color				fontColor;
	public boolean				labelInside;

	// Wartości potrzebne do parsowania
	public int					posX;
	public int					posY;
	public LabelV				label				= null;
	public String				text;

	// Pozostałe
	private int					tempX;
	private int					tempY;

	public Vertex getCopy()
	{
		Vertex result = new Vertex();
		result.setModel(getModel());
		result.labelInside = false;
		result.posX = posX;
		result.posY = posY;
		if (label != null)
		{
			result.label = label.getCopy(result);
		}

		return result;
	}

	public int getNumber()
	{
		return number;
	}

	public void setModel(PropertyModel pm)
	{
		VertexPropertyModel model = (VertexPropertyModel) pm;

		if (model.number > -1)
		{
			setPartOfNumeration(false);
			updateNumber(model.number);
			setPartOfNumeration(true);
		}

		if (model.radius > -1)
		{
			radius = model.radius;
		}

		if (model.type > -1)
		{
			type = model.type;
		}

		if (model.vertexColor != null)
		{
			vertexColor = new Color(model.vertexColor.getRGB());
		}

		if (model.lineWidth > -1)
		{
			lineWidth = model.lineWidth;
		}

		if (model.lineType > -1)
		{
			lineType = model.lineType;
		}

		if (model.lineColor != null)
		{
			lineColor = new Color(model.lineColor.getRGB());
		}

		if (model.fontColor != null)
		{
			fontColor = new Color(model.fontColor.getRGB());
		}

		if (model.labelInside > -1)
		{
			labelInside = (model.labelInside == 1);
		}
	}

	public PropertyModel getModel()
	{
		VertexPropertyModel result = new VertexPropertyModel();

		result.number = number;
		result.radius = radius;
		result.type = type;
		result.vertexColor = new Color(vertexColor.getRGB());
		result.lineWidth = lineWidth;
		result.lineType = lineType;
		result.lineColor = new Color(lineColor.getRGB());
		result.fontColor = new Color(fontColor.getRGB());
		result.labelInside = 0;
		if (labelInside)
		{
			result.labelInside = 1;
		}

		return result;
	}

	public void updateNumber(int _number) // Sprawdzić z poziomu property editora czy ControlManager.graph.usedNumber[number] = false; aby było unikatowe
	{
		number = _number;
		if (ControlManager.graph.digitalNumeration)
		{
			text = Integer.toString(number);
		}
		else
		{
			text = Utilities.getABC(number);
		}
	}

	public void setPartOfNumeration(boolean flag)
	{
		ControlManager.graph.usedNumber[number] = flag;
	}

	public void adjustToGrid()
	{
		posX = ((posX + (ControlManager.graph.gridResolutionX / 2)) / ControlManager.graph.gridResolutionX) * ControlManager.graph.gridResolutionX;
		posY = ((posY + (ControlManager.graph.gridResolutionY / 2)) / ControlManager.graph.gridResolutionY) * ControlManager.graph.gridResolutionY;
	}

	public boolean collides(Vertex vertex)
	{
		Area area = new Area(Utilities.getVertexShape(type + 1, radius, posX, posY));
		area.intersect(new Area(Utilities.getVertexShape(vertex.type + 1, vertex.radius, vertex.posX, vertex.posY)));
		return !area.isEmpty();
	}

	public boolean intersects(int x, int y)
	{
		Area area = new Area(Utilities.getVertexShape(type + 1, radius, posX, posY));
		return area.contains(x, y);
	}

	public boolean fitsIntoPage()
	{
		return !((posX - radius - lineWidth / 2 < 0) || (posX + radius + lineWidth / 2 > ControlManager.graph.pageWidth)
				|| (posY - radius - lineWidth / 2 < 0) || (posY + radius + lineWidth / 2 > ControlManager.graph.pageHeight));
	}

	public void draw(Graphics2D g2d, boolean dummy)
	{
		Graphics2D g = (Graphics2D) g2d.create();

		if (dummy && ControlManager.graph.gridOn)
		{
			tempX = posX;
			tempY = posY;
			adjustToGrid();
		}

		if (ControlManager.selection.contains(this))
		{
			g.setColor(DrawingTools.selectionColor);
			g.fill(Utilities.getVertexShape(type + 1, radius + lineWidth / 2 + radius / 4, posX, posY));
		}

		g.setColor(vertexColor);
		if (dummy)
		{
			g.setColor(DrawingTools.getDummyColor(vertexColor));
		}

		if (lineWidth > 0 && lineType != PropertyModel.NONE)
		{
			if (lineType == DrawingTools.DOUBLE_LINE)
			{
				Shape innerOutline = Utilities.getVertexShape(type + 1, radius - 2 - (lineWidth * 23) / 16, posX, posY);
				if (type == PropertyModel.CIRCLE)
				{
					innerOutline = Utilities.getVertexShape(type + 1, radius - 2 - (lineWidth * 9) / 8, posX, posY);
				}
				if (type == PropertyModel.TRIANGLE)
				{
					innerOutline = Utilities.getVertexShape(type + 1, radius - 4 - (lineWidth * 11) / 5, posX, posY);
				}
				if (type == PropertyModel.SQUARE)
				{
					innerOutline = Utilities.getVertexShape(type + 1, radius - 3 - (lineWidth * 13) / 8, posX, posY);
				}
				
				g.setColor(Color.white);
				g.fill(Utilities.getVertexShape(type + 1, radius + lineWidth / 2, posX, posY));
				
				g.setColor(vertexColor);
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(vertexColor));
				}
				g.fill(innerOutline);
				
				g.setColor(lineColor);
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(lineColor));
				}
				g.setStroke(DrawingTools.getStroke(lineWidth, PropertyModel.SOLID, 0.0));
				g.draw(Utilities.getVertexShape(type + 1, radius, posX, posY));
				g.draw(innerOutline);
			}

			else
			{
				Shape vertexShape = Utilities.getVertexShape(type + 1, radius, posX, posY);
				g.fill(vertexShape);

				g.setColor(lineColor);
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(lineColor));
				}

				double girth = Math.PI * 2 * radius;
				if (type == 2)
				{
					girth = Math.sqrt(3) * radius;
				}
				if (type == 3)
				{
					girth = Math.sqrt(2) * radius;
				}
				if (type == 4)
				{
					girth = 2 * radius * Math.cos(Math.toRadians(54));
				}
				if (type == 5)
				{
					girth = radius;
				}

				g.setStroke(DrawingTools.getStroke(lineWidth, lineType, girth));
				g.draw(vertexShape);
			}
			g.setStroke(new BasicStroke());
		}

		if (labelInside)
		{
			g.setColor(fontColor);
			if (dummy)
			{
				g.setColor(DrawingTools.getDummyColor(fontColor));
			}
			updateNumber(number);
			if (text != null)
			{
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
				g.drawString(text, posX - fm.stringWidth(text) / 2, posY + (fm.getAscent() - fm.getDescent()) / 2);
			}
		}

		if (dummy && ControlManager.graph.gridOn)
		{
			posX = tempX;
			posY = tempY;
		}

		g.dispose();
	}

	public void drawLabel(Graphics2D g, boolean dummy)
	{
		if (label != null)
		{
			label.draw(g, dummy);
		}
	}
}
