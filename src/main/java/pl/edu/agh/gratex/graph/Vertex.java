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
import pl.edu.agh.gratex.model.properties.LineType;


public class Vertex extends GraphElement implements Serializable
{
	private static final long	serialVersionUID	= -3978311311955384768L;

	// Wartości edytowalne przez użytkowanika
	private int					number;
	private int					radius;
	private int					type;
	private Color				vertexColor;
	private int					lineWidth;
	private LineType			lineType;
	private Color				lineColor;
	private Font					font;
	private Color				fontColor;
	private boolean				labelInside;

	// Wartości potrzebne do parsowania
    private int					posX;
	private int					posY;
	private LabelV				label				= null;
	private String				text;

	// Pozostałe
	private int					tempX;
	private int					tempY;

	public Vertex getCopy()
	{
		Vertex result = new Vertex();
		result.setModel(getModel());
		result.setLabelInside(false);
		result.setPosX(getPosX());
		result.setPosY(getPosY());
		if (getLabel() != null)
		{
			result.setLabel(getLabel().getCopy(result));
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
			setRadius(model.radius);
		}

		if (model.type > -1)
		{
			setType(model.type);
		}

		if (model.vertexColor != null)
		{
			setVertexColor(new Color(model.vertexColor.getRGB()));
		}

		if (model.lineWidth > -1)
		{
			setLineWidth(model.lineWidth);
		}

		if (model.lineType != LineType.EMPTY)
		{
			setLineType(model.lineType);
		}

		if (model.lineColor != null)
		{
			setLineColor(new Color(model.lineColor.getRGB()));
		}

		if (model.fontColor != null)
		{
			setFontColor(new Color(model.fontColor.getRGB()));
		}

		if (model.labelInside > -1)
		{
			setLabelInside((model.labelInside == 1));
		}
	}

	public PropertyModel getModel()
	{
		VertexPropertyModel result = new VertexPropertyModel();

		result.number = number;
		result.radius = getRadius();
		result.type = getType();
		result.vertexColor = new Color(getVertexColor().getRGB());
		result.lineWidth = getLineWidth();
		result.lineType = getLineType();
		result.lineColor = new Color(getLineColor().getRGB());
		result.fontColor = new Color(getFontColor().getRGB());
		result.labelInside = 0;
		if (isLabelInside())
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
			setText(Integer.toString(number));
		}
		else
		{
			setText(Utilities.getABC(number));
		}
	}

	public void setPartOfNumeration(boolean flag)
	{
		ControlManager.graph.usedNumber[number] = flag;
	}

	public void adjustToGrid()
	{
		setPosX(((getPosX() + (ControlManager.graph.gridResolutionX / 2)) / ControlManager.graph.gridResolutionX) * ControlManager.graph.gridResolutionX);
		setPosY(((getPosY() + (ControlManager.graph.gridResolutionY / 2)) / ControlManager.graph.gridResolutionY) * ControlManager.graph.gridResolutionY);
	}

	public boolean collides(Vertex vertex)
	{
		Area area = new Area(Utilities.getVertexShape(getType() + 1, getRadius(), getPosX(), getPosY()));
		area.intersect(new Area(Utilities.getVertexShape(vertex.getType() + 1, vertex.getRadius(), vertex.getPosX(), vertex.getPosY())));
		return !area.isEmpty();
	}

	public boolean intersects(int x, int y)
	{
		Area area = new Area(Utilities.getVertexShape(getType() + 1, getRadius(), getPosX(), getPosY()));
		return area.contains(x, y);
	}

	public boolean fitsIntoPage()
	{
		return !((getPosX() - getRadius() - getLineWidth() / 2 < 0) || (getPosX() + getRadius() + getLineWidth() / 2 > ControlManager.graph.pageWidth)
				|| (getPosY() - getRadius() - getLineWidth() / 2 < 0) || (getPosY() + getRadius() + getLineWidth() / 2 > ControlManager.graph.pageHeight));
	}

	public void draw(Graphics2D g2d, boolean dummy)
	{
		Graphics2D g = (Graphics2D) g2d.create();

		if (dummy && ControlManager.graph.gridOn)
		{
			tempX = getPosX();
			tempY = getPosY();
			adjustToGrid();
		}

		if (ControlManager.selection.contains(this))
		{
			g.setColor(DrawingTools.selectionColor);
			g.fill(Utilities.getVertexShape(getType() + 1, getRadius() + getLineWidth() / 2 + getRadius() / 4, getPosX(), getPosY()));
		}

		g.setColor(getVertexColor());
		if (dummy)
		{
			g.setColor(DrawingTools.getDummyColor(getVertexColor()));
		}

		if (getLineWidth() > 0 && getLineType() != LineType.NONE)
		{
			if (getLineType() == LineType.DOUBLE)
			{
				Shape innerOutline = Utilities.getVertexShape(getType() + 1, getRadius() - 2 - (getLineWidth() * 23) / 16, getPosX(), getPosY());
				if (getType() == pl.edu.agh.gratex.model.properties.Shape.CIRCLE.getValue())
				{
					innerOutline = Utilities.getVertexShape(getType() + 1, getRadius() - 2 - (getLineWidth() * 9) / 8, getPosX(), getPosY());
				}
				if (getType() == pl.edu.agh.gratex.model.properties.Shape.TRIANGLE.getValue())
				{
					innerOutline = Utilities.getVertexShape(getType() + 1, getRadius() - 4 - (getLineWidth() * 11) / 5, getPosX(), getPosY());
				}
				if (getType() == pl.edu.agh.gratex.model.properties.Shape.SQUARE.getValue())
				{
					innerOutline = Utilities.getVertexShape(getType() + 1, getRadius() - 3 - (getLineWidth() * 13) / 8, getPosX(), getPosY());
				}
				
				g.setColor(Color.white);
				g.fill(Utilities.getVertexShape(getType() + 1, getRadius() + getLineWidth() / 2, getPosX(), getPosY()));
				
				g.setColor(getVertexColor());
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(getVertexColor()));
				}
				g.fill(innerOutline);
				
				g.setColor(getLineColor());
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(getLineColor()));
				}
				g.setStroke(DrawingTools.getStroke(getLineWidth(), LineType.SOLID, 0.0));
				g.draw(Utilities.getVertexShape(getType() + 1, getRadius(), getPosX(), getPosY()));
				g.draw(innerOutline);
			}

			else
			{
				Shape vertexShape = Utilities.getVertexShape(getType() + 1, getRadius(), getPosX(), getPosY());
				g.fill(vertexShape);

				g.setColor(getLineColor());
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(getLineColor()));
				}

				double girth = Math.PI * 2 * getRadius();
				if (getType() == 2)
				{
					girth = Math.sqrt(3) * getRadius();
				}
				if (getType() == 3)
				{
					girth = Math.sqrt(2) * getRadius();
				}
				if (getType() == 4)
				{
					girth = 2 * getRadius() * Math.cos(Math.toRadians(54));
				}
				if (getType() == 5)
				{
					girth = getRadius();
				}

				g.setStroke(DrawingTools.getStroke(getLineWidth(), getLineType(), girth));
				g.draw(vertexShape);
			}
			g.setStroke(new BasicStroke());
		}

		if (isLabelInside())
		{
			g.setColor(getFontColor());
			if (dummy)
			{
				g.setColor(DrawingTools.getDummyColor(getFontColor()));
			}
			updateNumber(number);
			if (getText() != null)
			{
				g.setFont(getFont());
				FontMetrics fm = g.getFontMetrics();
				g.drawString(getText(), getPosX() - fm.stringWidth(getText()) / 2, getPosY() + (fm.getAscent() - fm.getDescent()) / 2);
			}
		}

		if (dummy && ControlManager.graph.gridOn)
		{
			setPosX(tempX);
			setPosY(tempY);
		}

		g.dispose();
	}

	public void drawLabel(Graphics2D g, boolean dummy)
	{
		if (getLabel() != null)
		{
			getLabel().draw(g, dummy);
		}
	}

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Color getVertexColor() {
        return vertexColor;
    }

    public void setVertexColor(Color vertexColor) {
        this.vertexColor = vertexColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public boolean isLabelInside() {
        return labelInside;
    }

    public void setLabelInside(boolean labelInside) {
        this.labelInside = labelInside;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public LabelV getLabel() {
        return label;
    }

    public void setLabel(LabelV label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
