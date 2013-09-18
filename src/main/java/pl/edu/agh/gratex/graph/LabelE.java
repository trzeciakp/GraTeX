package pl.edu.agh.gratex.graph;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;

import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;


public class LabelE extends GraphElement implements Serializable
{
	private static final long	serialVersionUID	= 7816741486248743237L;
	
	// Wartości edytowalne przez użytkowanika
	public String	text;
	public Font		font	= new Font("Cambria", Font.PLAIN, 16);
	public Color	fontColor;
	public int		position;										// Procent przesunięcia na krawędzie
	public int		spacing;										// odleglość napisu od krawędzi
	public boolean	topPlacement;									// Czy etykieta jest nad krawędzią
	public boolean	horizontalPlacement;							// Czy etykieta jest zawsze poziomo (nie = równolegle do krawędzi)

	// Wartości potrzebne do parsowania
	public int		posX;											// Środek stringa x
	public int		posY;											// Środek stringa y
	public int		angle;											// Nachylenie (0 stopni gdy pozioma)

	// Pozostałe
	public Edge		owner;
	public Polygon	outline;
	public int		drawX;
	public int		drawY;

	public LabelE(Edge element)
	{
		owner = element;
		text = "Label";
	}

	public LabelE getCopy(Edge owner)
	{
		LabelE result = new LabelE(owner);
		result.setModel(getModel());

		return result;
	}

	public void setModel(PropertyModel pm)
	{
		LabelEdgePropertyModel model = (LabelEdgePropertyModel) pm;

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

		if (model.topPlacement > -1)
		{
			topPlacement = (model.topPlacement == 1);
		}

		if (model.horizontalPlacement > -1)
		{
			horizontalPlacement = (model.horizontalPlacement == 1);
		}
	}

	public PropertyModel getModel()
	{
		LabelEdgePropertyModel result = new LabelEdgePropertyModel();

		result.text = new String(text);
		result.fontColor = new Color(fontColor.getRGB());
		result.position = position;
		result.spacing = spacing;
		result.topPlacement = 0;
		result.isLoop = PropertyModel.NO;
		if (owner.vertexA == owner.vertexB)
		{
			result.isLoop = PropertyModel.YES;
		}
		if (topPlacement)
		{
			result.topPlacement = 1;
		}
		result.horizontalPlacement = 0;
		if (horizontalPlacement)
		{
			result.horizontalPlacement = 1;
		}

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

		double ellipseShortRadius = 0.75;

		if (owner.vertexA == owner.vertexB)
		{
			horizontalPlacement = true;
			topPlacement = true;
			if (position < 34)
			{
				position = 25;
			}
			else if (position < 67)
			{
				position = 50;
			}
			else
			{
				position = 75;
			}

			double offsetRate = 0.75;
			if (owner.vertexA.type == 2)
			{
				offsetRate = 0.375;
				if (owner.relativeEdgeAngle == 270)
				{
					ellipseShortRadius /= 2;
				}
			}
			else if (owner.vertexA.type == 3)
			{
				offsetRate = 0.5;
			}
			else if (owner.vertexA.type == 4)
			{
				offsetRate = 0.4375;
			}
			else if (owner.vertexA.type == 5)
			{
				offsetRate = 0.625;
			}

			int r = owner.vertexA.radius + owner.vertexA.lineWidth / 2;
			switch (owner.relativeEdgeAngle)
			{
				case 0:
				{
					if (position == 25)
					{
						posX = owner.vertexA.posX + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius));
						posY = owner.vertexA.posY - height / 2 - spacing - (int) Math.round(r * ellipseShortRadius / 2);
					}
					else if (position == 50)
					{
						posX = owner.vertexA.posX + spacing + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2));
						posY = owner.vertexA.posY;
					}
					else
					{
						posX = owner.vertexA.posX + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius));
						posY = owner.vertexA.posY + height / 2 + spacing + (int) Math.round(r * ellipseShortRadius / 2);
					}
					break;
				}
				case 90:
				{
					if (position == 25)
					{
						posX = owner.vertexA.posX - spacing - width / 2 - (int) Math.round(r * ellipseShortRadius / 2);
						posY = owner.vertexA.posY - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius));
					}
					else if (position == 50)
					{
						posX = owner.vertexA.posX;
						posY = owner.vertexA.posY - spacing - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2));
					}
					else
					{
						posX = owner.vertexA.posX + spacing + width / 2 + (int) Math.round(r * ellipseShortRadius / 2);
						posY = owner.vertexA.posY - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius));
					}
					break;
				}
				case 180:
				{
					if (position == 25)
					{
						posX = owner.vertexA.posX - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius));
						posY = owner.vertexA.posY + spacing + height / 2 + (int) Math.round(r * ellipseShortRadius / 2);
					}
					else if (position == 50)
					{
						posX = owner.vertexA.posX - spacing - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2));
						posY = owner.vertexA.posY;
					}
					else
					{
						posX = owner.vertexA.posX - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius));
						posY = owner.vertexA.posY - spacing - height / 2 - (int) Math.round(r * ellipseShortRadius / 2);
					}
					break;
				}
				case 270:
				{
					if (position == 25)
					{
						posX = owner.vertexA.posX + spacing + width / 2 + (int) Math.round(r * ellipseShortRadius / 2);
						posY = owner.vertexA.posY + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius));
					}
					else if (position == 50)
					{
						posX = owner.vertexA.posX;
						posY = owner.vertexA.posY + spacing + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2));
					}
					else
					{
						posX = owner.vertexA.posX - spacing - width / 2 - (int) Math.round(r * ellipseShortRadius / 2);
						posY = owner.vertexA.posY + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius));
						;
					}
					break;
				}
			}
			drawX = posX - width / 2;
			drawY = posY + height / 2 - descent;
			int[] xp = new int[] { drawX, drawX + width, drawX + width, drawX };
			int[] yp = new int[] { posY - height / 2, posY - height / 2, posY + height / 2, posY + height / 2 };
			outline = new Polygon(xp, yp, 4);
		}
		else
		{
			if (owner.relativeEdgeAngle == 0) // straight edge
			{
				int ax = owner.inPoint.x;
				int ay = owner.inPoint.y;
				int bx = owner.outPoint.x;
				int by = owner.outPoint.y;
				int dx = bx - ax;
				int dy = by - ay;

				double distance = Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));

				double baselinePart = (owner.lineWidth + spacing) / distance;
				double ascentPart = (owner.lineWidth + spacing + height) / distance;

				Point baseline1p1 = new Point((int) Math.round(ax + dy * baselinePart), (int) Math.round(ay - dx * baselinePart));
				Point baseline1p2 = new Point((int) Math.round(bx + dy * baselinePart), (int) Math.round(by - dx * baselinePart));
				Point baseline2p1 = new Point((int) Math.round(ax - dy * baselinePart), (int) Math.round(ay + dx * baselinePart));
				Point baseline2p2 = new Point((int) Math.round(bx - dy * baselinePart), (int) Math.round(by + dx * baselinePart));

				Point ascent1p1 = new Point((int) Math.round(ax + dy * ascentPart), (int) Math.round(ay - dx * ascentPart));
				Point ascent1p2 = new Point((int) Math.round(bx + dy * ascentPart), (int) Math.round(by - dx * ascentPart));
				Point ascent2p1 = new Point((int) Math.round(ax - dy * ascentPart), (int) Math.round(ay + dx * ascentPart));
				Point ascent2p2 = new Point((int) Math.round(bx - dy * ascentPart), (int) Math.round(by + dx * ascentPart));

				Point startPoint = null;
				Point endPoint = null;
				if (topPlacement)
				{
					if (baseline2p1.x < baseline2p2.x)
					{
						startPoint = baseline2p1;
						endPoint = baseline2p2;
						if (ax < bx)
						{
							startPoint = baseline1p1;
							endPoint = baseline1p2;
						}
					}
					else
					{
						startPoint = baseline2p2;
						endPoint = baseline2p1;
						if (ax < bx)
						{
							startPoint = baseline1p2;
							endPoint = baseline1p1;
						}
					}
				}
				else
				{
					if (ascent2p1.x < ascent2p2.x)
					{
						startPoint = ascent2p1;
						endPoint = ascent2p2;
						if (ax >= bx)
						{
							startPoint = ascent1p1;
							endPoint = ascent1p2;
						}
					}
					else
					{
						startPoint = ascent2p2;
						endPoint = ascent2p1;
						if (ax >= bx)
						{
							startPoint = ascent1p2;
							endPoint = ascent1p1;
						}
					}
				}

				if (horizontalPlacement)
				{
					if (!topPlacement)
					{
						if (baseline2p1.x < baseline2p2.x)
						{
							startPoint = baseline2p1;
							endPoint = baseline2p2;
							if (ax > bx)
							{
								startPoint = baseline1p1;
								endPoint = baseline1p2;
							}
						}
						else
						{
							startPoint = baseline2p2;
							endPoint = baseline2p1;
							if (ax > bx)
							{
								startPoint = baseline1p2;
								endPoint = baseline1p1;
							}
						}
					}

					Point stringClosestCorner = new Point((int) Math.round(startPoint.x
							+ (position * (distance) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
							+ (position * (distance) * (endPoint.y - startPoint.y) / (100 * distance))));

					if (topPlacement)
					{
						if ((ax - bx) * (ay - by) > 0)
						{
							drawX = stringClosestCorner.x;
							drawY = stringClosestCorner.y - descent;
						}
						else
						{
							drawX = stringClosestCorner.x - width;

							drawY = stringClosestCorner.y - descent;
						}
						posY = stringClosestCorner.y - height / 2;
					}
					else
					{
						if ((ax - bx) * (ay - by) > 0)
						{
							drawX = stringClosestCorner.x - width;
							drawY = stringClosestCorner.y - descent + height;
						}
						else
						{
							drawX = stringClosestCorner.x;
							drawY = stringClosestCorner.y - descent + height;
						}
						posY = stringClosestCorner.y + height / 2;
					}

					if (height % 2 == 1)
					{
						height++;
					}
					angle = 0;
					posX = drawX + width / 2;
					int[] xp = new int[] { drawX, drawX + width, drawX + width, drawX };
					int[] yp = new int[] { posY - height / 2, posY - height / 2, posY + height / 2, posY + height / 2 };
					outline = new Polygon(xp, yp, 4);
				}
				else
				{

					Point stringStartBottom = new Point((int) Math.round(startPoint.x
							+ (position * (distance - width) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
							+ (position * (distance - width) * (endPoint.y - startPoint.y) / (100 * distance))));

					Point stringEndBottom = new Point((int) Math.round(startPoint.x
							+ ((width + (position * (distance - width) / 100)) * (endPoint.x - startPoint.x) / distance)),
							(int) Math.round(startPoint.y
									+ ((width + (position * (distance - width) / 100)) * (endPoint.y - startPoint.y) / distance)));

					Point stringStartTop = null;
					Point stringEndTop = null;

					if (startPoint.y < endPoint.y)
					{
						stringStartTop = new Point(stringStartBottom.x + Math.abs(baseline1p1.x - ascent1p1.x), stringStartBottom.y
								- Math.abs(baseline1p1.y - ascent1p1.y));

						stringEndTop = new Point(stringEndBottom.x + Math.abs(baseline1p1.x - ascent1p1.x), stringEndBottom.y
								- Math.abs(baseline1p1.y - ascent1p1.y));

						drawX = stringStartBottom.x + (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.x - ascent1p1.x));
						drawY = stringStartBottom.y - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.y - ascent1p1.y));
					}
					else
					{
						stringStartTop = new Point(stringStartBottom.x - Math.abs(baseline1p1.x - ascent1p1.x), stringStartBottom.y
								- Math.abs(baseline1p1.y - ascent1p1.y));

						stringEndTop = new Point(stringEndBottom.x - Math.abs(baseline1p1.x - ascent1p1.x), stringEndBottom.y
								- Math.abs(baseline1p1.y - ascent1p1.y));

						drawX = stringStartBottom.x - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.x - ascent1p1.x));
						drawY = stringStartBottom.y - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.y - ascent1p1.y));
					}

					int[] xp = new int[] { stringStartBottom.x, stringEndBottom.x, stringEndTop.x, stringStartTop.x };
					int[] yp = new int[] { stringStartBottom.y, stringEndBottom.y, stringEndTop.y, stringStartTop.y };
					outline = new Polygon(xp, yp, 4);

					posX = (stringStartBottom.x + stringEndTop.x) / 2;
					posY = (stringStartBottom.y + stringEndTop.y) / 2;

					double angled = Math
							.toDegrees(Math.asin((stringStartBottom.y - stringEndBottom.y) / stringStartBottom.distance(stringEndBottom)));
					if (angled < 0)
					{
						angled += 360;
					}

					angle = (int) Math.round(angled);
				}
			}
			else
			// curved edge
			{
				if (horizontalPlacement)
				{
					double r = 0.0;
					if (topPlacement)
					{
						r = spacing + owner.arcRadius;
					}
					else
					{
						r = owner.arcRadius - spacing;
					}
					double startAngle = (Math.toDegrees(Math.atan2(owner.outPoint.x - owner.arcMiddle.x, owner.outPoint.y - owner.arcMiddle.y)) + 270) % 360;
					double endAngle = (Math.toDegrees(Math.atan2(owner.inPoint.x - owner.arcMiddle.x, owner.inPoint.y - owner.arcMiddle.y)) + 270) % 360;
					double stringAngle = 0.0;
					if (owner.relativeEdgeAngle <= 60)
					{
						stringAngle = (360 + startAngle - (position * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
					}
					else
					{
						stringAngle = (startAngle + (position * ((endAngle - startAngle + 360) % 360)) / 100) % 360;
					}

					posX = owner.arcMiddle.x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle)));
					posY = owner.arcMiddle.y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle)));

					angle = 0;
					if (!topPlacement)
					{
						stringAngle = (stringAngle + 180) % 360;
					}
					if (stringAngle < 90)
					{
						drawX = posX;
						drawY = posY - descent;
						posX += width / 2;
						posY -= height / 2;
					}
					else if (stringAngle < 180)
					{
						drawX = posX - width;
						drawY = posY - descent;
						posX -= width / 2;
						posY -= height / 2;
					}
					else if (stringAngle < 270)
					{
						drawX = posX - width;
						drawY = posY + height - descent;
						posX -= width / 2;
						posY += height / 2;
					}
					else
					{
						drawX = posX;
						drawY = posY + height - descent;
						posX += width / 2;
						posY += height / 2;
					}

					int xpoints[] = new int[] { posX - width / 2, posX + width / 2, posX + width / 2, posX - width / 2 };
					int ypoints[] = new int[] { posY - height / 2, posY - height / 2, posY + height / 2, posY + height / 2 };
					outline = new Polygon(xpoints, ypoints, 4);
				}
				else
				{
					double r = 0.0;
					if (topPlacement)
					{
						r = spacing + height / 2 + owner.arcRadius;
					}
					else
					{
						r = owner.arcRadius - spacing - height / 2;
					}
					double alpha = Math.toDegrees(Math.atan(width / (2 * r)));
					double startAngle = (Math.toDegrees(Math.atan2(owner.outPoint.x - owner.arcMiddle.x, owner.outPoint.y - owner.arcMiddle.y)) + 270) % 360;
					double endAngle = (Math.toDegrees(Math.atan2(owner.inPoint.x - owner.arcMiddle.x, owner.inPoint.y - owner.arcMiddle.y)) + 270) % 360;
					double stringAngle = 0.0;
					if (owner.relativeEdgeAngle <= 60)
					{
						startAngle = (startAngle - alpha) % 360;
						endAngle = (endAngle + alpha) % 360;
						stringAngle = (360 + startAngle - (position * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
					}
					else
					{
						startAngle = (startAngle + alpha) % 360;
						endAngle = (endAngle - alpha) % 360;
						stringAngle = startAngle + (position * ((endAngle - startAngle + 360) % 360)) / 100;
					}

					angle = (int) Math.round((stringAngle + 270) % 360);
					if (angle > 90 && angle < 271)
					{
						angle = (180 + angle) % 360;
					}

					posX = owner.arcMiddle.x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle)));
					posY = owner.arcMiddle.y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle)));

					Point middle = new Point(posX, posY);
					Point draw = new Point(posX - width / 2, posY + height / 2 - descent);
					draw = Utilities.rotatePoint(middle, draw, angle);
					Point p1 = new Point(posX - width / 2, posY - height / 2);
					p1 = Utilities.rotatePoint(middle, p1, angle);
					Point p2 = new Point(posX + width / 2, posY - height / 2);
					p2 = Utilities.rotatePoint(middle, p2, angle);
					Point p3 = new Point(posX + width / 2, posY + height / 2);
					p3 = Utilities.rotatePoint(middle, p3, angle);
					Point p4 = new Point(posX - width / 2, posY + height / 2);
					p4 = Utilities.rotatePoint(middle, p4, angle);

					drawX = draw.x;
					drawY = draw.y;
					int xpoints[] = new int[] { p1.x, p2.x, p3.x, p4.x };
					int ypoints[] = new int[] { p1.y, p2.y, p3.y, p4.y };
					outline = new Polygon(xpoints, ypoints, 4);
				}
			}
		}
	}

	public void draw(Graphics2D g2d, boolean dummy)
	{
		Graphics2D g = (Graphics2D) g2d.create();

		updatePosition(g);

		if (ControlManager.selection.contains(this))
		{
			g.setColor(DrawingTools.selectionColor);
			g.fillPolygon(outline);
		}

		g.setColor(fontColor);
		if (dummy)
		{
			g.setColor(DrawingTools.getDummyColor(fontColor));
		}
		g.setFont(font);
		if (horizontalPlacement)
		{
			g.drawString(text, drawX, drawY);
		}
		else
		{
			g.translate(drawX, drawY);
			g.rotate(Math.toRadians(360 - angle), 0, 0);
			g.drawString(text, 0, 0);
		}

		g.dispose();
	}
}
