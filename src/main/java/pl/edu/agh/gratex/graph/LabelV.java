package pl.edu.agh.gratex.graph;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;


public class LabelV extends GraphElement implements Serializable
{
	private static final long	serialVersionUID	= 5054682946344977073L;
	
	// Wartości edytowalne przez użytkowanika
	public String		text;
	public Font			font	= new Font("Cambria", Font.PLAIN, 16);
	public Color		fontColor;
	public int			position;										// 0-N; 1-NE; 2-E; ...; 7 - NW;
	public int			spacing;										// odleglość napisu od obrysu wierzchołka	

	// Wartości potrzebne do parsowania
	public int			posX;											// \ Środek stringa
	public int			posY;											// / Środek stringa

	// Pozostałe
	public Vertex		owner;
	public int			drawX;
	public int			drawY;
	public Rectangle	outline;

	public LabelV(Vertex element)
	{
		owner = element;
		text = "Label";
	}

	public LabelV getCopy(Vertex owner)
	{
		LabelV result = new LabelV(owner);
		result.setModel(getModel());

		return result;
	}

	public void setModel(PropertyModel pm)
	{
		LabelVertexPropertyModel model = (LabelVertexPropertyModel) pm;

		if (model.text != null)
		{
			text = new String(model.text);
		}

		if (model.fontColor != null)
		{
			fontColor = new Color(model.fontColor.getRGB());
		}

		if (model.position > -1)
		{
			position = model.position;
		}

		if (model.spacing > -1)
		{
			spacing = model.spacing;
		}
	}

	public PropertyModel getModel()
	{
		LabelVertexPropertyModel result = new LabelVertexPropertyModel();

		result.text = new String(text);
		result.fontColor = new Color(fontColor.getRGB());
		result.position = position;
		result.spacing = spacing;

		return result;
	}

	public boolean intersects(int x, int y)
	{
		return outline.contains(x, y);
	}

	public void updatePosition(Graphics2D g)
	{
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(text);
		int height = fm.getAscent();
		int descent = fm.getDescent();

		Point exitPoint = Utilities.calculateEdgeExitPoint(owner, (450 - 45 * position) % 360);

		double dposX = 0.0;
		double dposY = 0.0;

		switch (position)
		{
			case 0:
			{
				dposX = exitPoint.x;
				dposY = exitPoint.y - spacing - height / 2;
				break;
			}
			case 1:
			{
				dposX = exitPoint.x + spacing / 1.4142 + width / 2;
				dposY = exitPoint.y - spacing / 1.4142 - height / 2;
				break;
			}
			case 2:
			{
				dposX = exitPoint.x + spacing + width / 2;
				dposY = exitPoint.y;
				break;
			}
			case 3:
			{
				dposX = exitPoint.x + spacing / 1.4142 + width / 2;
				dposY = exitPoint.y + spacing / 1.4142 + height / 2;
				break;
			}
			case 4:
			{
				dposX = exitPoint.x;
				dposY = exitPoint.y + spacing + height / 2 + 0.5;
				break;
			}
			case 5:
			{
				dposX = exitPoint.x - spacing / 1.4142 - width / 2;
				dposY = exitPoint.y + spacing / 1.4142 + height / 2;
				break;
			}
			case 6:
			{
				dposX = exitPoint.x - spacing - width / 2;
				dposY = exitPoint.y;
				break;
			}
			case 7:
			{
				dposX = exitPoint.x - spacing / 1.4142 - width / 2;
				dposY = exitPoint.y - spacing / 1.4142 - height / 2;
				break;
			}

		}
		posX = (int) dposX;
		posY = (int) dposY;
		drawX = (int) (dposX - width / 2);
		drawY = (int) (dposY - descent + height / 2);
		outline = new Rectangle(posX - width / 2, posY - height / 2, width, height);
	}

	public void draw(Graphics2D g2d, boolean dummy)
	{
		Graphics2D g = (Graphics2D) g2d.create();

		updatePosition(g);

		if (ControlManager.selection.contains(this))
		{
			g.setColor(DrawingTools.selectionColor);
			g.fillRect(outline.x, outline.y, outline.width, outline.height);
		}

		g.setFont(font);
		g.setColor(fontColor);
		if (dummy)
		{
			g.setColor(DrawingTools.getDummyColor(fontColor));
		}
		g.drawString(text, drawX, drawY);

		g.dispose();
	}
}
