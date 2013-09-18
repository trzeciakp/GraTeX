package pl.edu.agh.gratex.graph;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.EdgePropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;


public class Edge extends GraphElement implements Serializable
{
	private static final long	serialVersionUID	= -7941761380307220731L;

	// Wartości edytowalne przez użytkowanika
	public int					lineType;
	public int					lineWidth;
	public boolean				directed;										// Z shiftem rysujemy skierowaną	
	public int					arrowType;
	public Color				lineColor;
	public int					relativeEdgeAngle;

	// Wartości potrzebne do parsowania
	public Vertex				vertexA;
	public Vertex				vertexB;
	public LabelE				label;
	public int					inAngle;
	public int					outAngle;

	// Pozostałe
	private int					arrowLengthFactor	= 2;						// Nakład na długość grota względem grubości linii - ustawienie z poziomu kodu
	private int					arrowLengthBasic	= 10;						// Podstawowa długość grota - ustawienie z poziomu kodu
	public Point				arcMiddle			= new Point();
	public int					arcRadius;
	public Point				outPoint;
	public Point				inPoint;
	public Arc2D.Double			arc;
	public int[]				arrowLine1			= null;
	public int[]				arrowLine2			= null;

	public Edge getCopy(LinkedList<Vertex> vertices)
	{
		Vertex _vertexA = null;
		Vertex _vertexB = null;
		Iterator<Vertex> itv = vertices.listIterator();
		Vertex tempV = null;
		while (itv.hasNext())
		{
			tempV = itv.next();
			if (tempV.posX == vertexA.posX && tempV.posY == vertexA.posY)
			{
				_vertexA = tempV;
			}
			if (tempV.posX == vertexB.posX && tempV.posY == vertexB.posY)
			{
				_vertexB = tempV;
			}
		}

		Edge result = new Edge();
		result.setModel(getModel());
		if (_vertexA == null || _vertexB == null)
		{
			return null;
		}
		result.vertexA = _vertexA;
		result.vertexB = _vertexB;

		if (label != null)
		{
			result.label = label.getCopy(result);
		}

		return result;
	}

	public void setModel(PropertyModel pm)
	{
		EdgePropertyModel model = (EdgePropertyModel) pm;

		if (model.lineType > -1)
		{
			lineType = model.lineType;
		}

		if (model.lineWidth > -1)
		{
			lineWidth = model.lineWidth;
		}

		if (model.directed > -1)
		{
			directed = (model.directed == 1);
		}

		if (model.lineColor != null)
		{
			lineColor = new Color(model.lineColor.getRGB());
		}

		if (model.relativeEdgeAngle > -1)
		{
			relativeEdgeAngle = model.relativeEdgeAngle;
		}

		if (model.arrowType > -1)
		{
			arrowType = model.arrowType;
		}
	}

	public PropertyModel getModel()
	{
		EdgePropertyModel result = new EdgePropertyModel();

		result.lineWidth = lineWidth;
		result.lineType = lineType;
		result.arrowType = arrowType;
		result.lineColor = new Color(lineColor.getRGB());
		result.relativeEdgeAngle = relativeEdgeAngle;
		if (vertexA == vertexB)
		{
			result.isLoop = pl.edu.agh.gratex.model.PropertyModel.YES;
		}
		else
			result.isLoop = pl.edu.agh.gratex.model.PropertyModel.NO;
		result.directed = 0;
		if (directed)
		{
			result.directed = 1;
		}

		return result;
	}

	public boolean intersects(int x, int y)
	{
		Point2D va = new Point(vertexA.posX, vertexA.posY);
		Point2D vb = new Point(vertexB.posX, vertexB.posY);
		Point2D click = new Point(x, y);

		if (vertexA == vertexB)
		{
			int r = vertexA.radius + vertexA.lineWidth / 2;
			double dx = arcMiddle.x - x;
			double dy = arcMiddle.y - y;
			double a = r * 0.75;
			double b = r * 0.375;
			if (relativeEdgeAngle == 90 || relativeEdgeAngle == 270)
			{
				a = r * 0.375;
				b = r * 0.75;
			}

			double distance = (dx * dx) / (a * a) + (dy * dy) / (b * b);
			return Math.abs(distance - 1) < 1;
		}
		else
		{
			if (relativeEdgeAngle != 0)
			{
				if (Math.abs(click.distance(arcMiddle) - arcRadius) < 10 + lineWidth / 2)
				{
					double clickAngle = (Math.toDegrees(Math.atan2(click.getX() - arcMiddle.x, click.getY() - arcMiddle.y)) + 270) % 360;
					if (arc.extent < 0)
					{
						double endAngle = (arc.start + arc.extent + 720) % 360;

						return (clickAngle - endAngle + 720) % 360 < (arc.start - endAngle + 720) % 360;
					}
					else
					{
						return (arc.extent) % 360 > (clickAngle - arc.start + 720) % 360;
					}
				}
			}
			else
			{
				if ((Math.min(vertexA.posX, vertexB.posX) <= x && Math.max(vertexA.posX, vertexB.posX) >= x)
						|| (Math.min(vertexA.posY, vertexB.posY) <= y && Math.max(vertexA.posY, vertexB.posY) >= y))
				{
					if (click.distance(va) > vertexA.radius && click.distance(vb) > vertexB.radius)
					{
						double distance = Line2D.ptLineDist((double) vertexA.posX, (double) vertexA.posY, (double) vertexB.posX,
								(double) vertexB.posY, (double) x, (double) y);
						return distance < 12;
					}
				}
			}
		}

		return false;
	}

	private void calculateInOutPoints()
	{
		double beta = (Math.toDegrees(Math.atan2(vertexB.posX - vertexA.posX, vertexB.posY - vertexA.posY))) + 270;
		outPoint = Utilities.calculateEdgeExitPoint(vertexA, relativeEdgeAngle + beta);
		inPoint = Utilities.calculateEdgeExitPoint(vertexB, 180 - relativeEdgeAngle + beta);
		outAngle = (int) Math.round(Math.toDegrees(Math.atan2(outPoint.x - vertexA.posX, outPoint.y - vertexA.posY)) + 270) % 360;
		inAngle = (int) Math.round(Math.toDegrees(Math.atan2(inPoint.x - vertexB.posX, inPoint.y - vertexB.posY)) + 270) % 360;
	}

	private void calculateArcParameters()
	{
		if (relativeEdgeAngle != 0)
		{
			double mx = (outPoint.x + inPoint.x) / 2;
			double my = (outPoint.y + inPoint.y) / 2;
			double dx = (inPoint.x - outPoint.x) / 2;
			double dy = (inPoint.y - outPoint.y) / 2;
			arcMiddle.x = (int) Math.round(mx - dy * Math.cos(Math.toRadians(relativeEdgeAngle)) / Math.sin(Math.toRadians(relativeEdgeAngle)));
			arcMiddle.y = (int) Math.round(my + dx * Math.cos(Math.toRadians(relativeEdgeAngle)) / Math.sin(Math.toRadians(relativeEdgeAngle)));
			arcRadius = (int) Math.round(Math.sqrt((arcMiddle.x - outPoint.x) * (arcMiddle.x - outPoint.x) + (arcMiddle.y - outPoint.y)
					* (arcMiddle.y - outPoint.y)));
		}
	}

	private void updateArrowPosition()
	{
		int ax = 0;
		int ay = 0;
		int bx = inPoint.x;
		int by = inPoint.y;

		if (vertexA == vertexB)
		{
			ax = 2 * bx - vertexA.posX;
			ay = 2 * by - vertexA.posY;
		}
		else if (relativeEdgeAngle != 0)
		{
			int x1 = bx - (arcMiddle.y - by);
			int y1 = by + (arcMiddle.x - bx);
			int x2 = bx + (arcMiddle.y - by);
			int y2 = by - (arcMiddle.x - bx);
			if (Point.distance(x1, y1, vertexB.posX, vertexB.posY) > Point.distance(x2, y2, vertexB.posX, vertexB.posY))
			{
				ax = x1;
				ay = y1;
			}
			else
			{
				ax = x2;
				ay = y2;
			}
		}
		else
		{
			ax = vertexA.posX;
			ay = vertexA.posY;
		}

		int dx = bx - ax;
		int dy = by - ay;

		int[] p1 = new int[] { ax - dy, ay + dx };
		int[] p2 = new int[] { ax + dy, ay - dx };
		
		if (arrowType == PropertyModel.FILLED)
		{

			p1 = new int[] { ax - dy / 2, ay + dx / 2};
			p2 = new int[] { ax + dy / 2, ay - dx / 2};
		}

		double part = (arrowLengthBasic + arrowLengthFactor * lineWidth)
				/ Math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]));
		
		p1[0] = bx - (int) Math.round(part * (bx - p1[0]));
		p1[1] = by - (int) Math.round(part * (by - p1[1]));
		p2[0] = bx - (int) Math.round(part * (bx - p2[0]));
		p2[1] = by - (int) Math.round(part * (by - p2[1]));

		arrowLine1 = new int[] { p1[0], p1[1], bx, by };
		arrowLine2 = new int[] { p2[0], p2[1], bx, by };
	}

	private void drawAngleVisualisation(Graphics2D g)
	{
		g.setColor(Color.gray);
		g.setStroke(DrawingTools.getStroke(2, DrawingTools.DASHED_LINE, 0.0));
		g.drawLine(vertexA.posX, vertexA.posY, vertexB.posX, vertexB.posY);

		Point m = new Point((vertexA.posX + vertexB.posX) / 2, (vertexA.posY + vertexB.posY) / 2);
		double d = 2 * m.distance(vertexA.posX, vertexB.posY);

		Point p1 = new Point((int) Math.round(vertexA.posX + (d / (2.5 * vertexA.radius)) * (outPoint.x - vertexA.posX)),
				(int) Math.round(vertexA.posY + (d / (2.5 * vertexA.radius)) * (outPoint.y - vertexA.posY)));
		Point p2 = new Point((int) Math.round(vertexB.posX + (d / (2.5 * vertexB.radius)) * (inPoint.x - vertexB.posX)),
				(int) Math.round(vertexB.posY + (d / (2.5 * vertexB.radius)) * (inPoint.y - vertexB.posY)));
		g.drawLine(vertexA.posX, vertexA.posY, p1.x, p1.y);
		g.drawLine(vertexB.posX, vertexB.posY, p2.x, p2.y);

		g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
		FontMetrics fm = g.getFontMetrics();
		String alphaText = Character.toString('\u03B1');

		int arcAngle = relativeEdgeAngle;
		if (arcAngle > 180)
		{
			arcAngle -= 360;
			alphaText = '-' + Character.toString('\u03B1');
		}
		double angle = Math.toRadians(outAngle - arcAngle / 2);
		int x = vertexA.posX - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
		int y = vertexA.posY + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
		g.drawString(alphaText, x, y);
		angle = Math.toRadians(inAngle + arcAngle / 2);
		x = vertexB.posX - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
		y = vertexB.posY + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
		g.drawString(alphaText, x, y);
		g.draw(new Arc2D.Double(vertexA.posX - (d / 3), vertexA.posY - (d / 3), d / 1.5, d / 1.5, outAngle, -arcAngle, Arc2D.OPEN));
		g.draw(new Arc2D.Double(vertexB.posX - (d / 3), vertexB.posY - (d / 3), d / 1.5, d / 1.5, inAngle, arcAngle, Arc2D.OPEN));
	}

	public void updatePosition()
	{
		if (vertexA != vertexB)
		{
			calculateInOutPoints();
			calculateArcParameters();

			if (relativeEdgeAngle != 0)
			{
				int x1 = outPoint.x;
				int y1 = outPoint.y;
				int x2 = inPoint.x;
				int y2 = inPoint.y;
				int x0 = arcMiddle.x;
				int y0 = arcMiddle.y;

				double x = x0 - arcRadius;
				double y = y0 - arcRadius;
				double width = 2 * arcRadius;
				double height = 2 * arcRadius;
				double startAngle = (Math.toDegrees(Math.atan2(x1 - x0, y1 - y0)) + 270) % 360;
				double endAngle = (Math.toDegrees(Math.atan2(x2 - x0, y2 - y0)) + 270) % 360;

				if (relativeEdgeAngle > 180)
				{
					endAngle = (endAngle - startAngle + 360) % 360;
				}
				else
				{
					endAngle = ((endAngle - startAngle + 360) % 360) - 360;
				}

				arc = new Arc2D.Double(x, y, width, height, startAngle, endAngle, Arc2D.OPEN);
			}
		}

		else
		{
			int r = vertexA.radius + vertexA.lineWidth / 2;
			double x = 0;
			double y = 0;
			double width = r * 1.5;
			double height = r * 0.75;
			if (relativeEdgeAngle == 90 || relativeEdgeAngle == 270)
			{
				width = r * 0.75;
				height = r * 1.5;
			}

			double offsetRate = 0.75;
			double arrowPosHorizRate = 0.965;
			double arrowPosVertRate = 0.265;

			if (vertexA.type == 2)
			{
				offsetRate = 0.375;
				arrowPosHorizRate = 0.84;
				arrowPosVertRate = 0.325;

				if (relativeEdgeAngle == 90)
				{
					arrowPosHorizRate = 0.595;
					arrowPosVertRate = 0.26;
				}
				else if (relativeEdgeAngle == 180)
				{
					arrowPosHorizRate = 0.475;
					arrowPosVertRate = 0.195;
				}
				else if (relativeEdgeAngle == 270)
				{
					arrowPosHorizRate = 0.505;
					arrowPosVertRate = 0.15;
					width /= 2;
					height /= 2;
				}
			}

			else if (vertexA.type == 3)
			{
				offsetRate = 0.5;
				arrowPosHorizRate = 0.72;
				arrowPosVertRate = 0.265;
			}

			else if (vertexA.type == 4)
			{
				offsetRate = 0.4375;
				arrowPosHorizRate = 0.755;
				arrowPosVertRate = 0.295;

				if (relativeEdgeAngle == 90)
				{
					arrowPosHorizRate = 0.795;
					arrowPosVertRate = 0.305;
				}
				else if (relativeEdgeAngle == 180)
				{
					arrowPosHorizRate = 0.93;
					arrowPosVertRate = 0.355;
				}
				else if (relativeEdgeAngle == 270)
				{
					arrowPosHorizRate = 0.815;
					arrowPosVertRate = 0.335;
				}
			}

			else if (vertexA.type == 5)
			{
				offsetRate = 0.625;
				arrowPosHorizRate = 0.85;
				arrowPosVertRate = 0.27;
				if (relativeEdgeAngle == 90 || relativeEdgeAngle == 270)
				{
					arrowPosHorizRate = 0.88;
					arrowPosVertRate = 0.283;
				}
			}

			switch (relativeEdgeAngle)
			{
				case 0:
				{
					x = vertexA.posX + r * offsetRate;
					y = vertexA.posY - height / 2;
					inPoint = new Point((int) Math.round(vertexA.posX + r * arrowPosHorizRate), (int) Math.round(vertexA.posY + r * arrowPosVertRate));
					outPoint = new Point((int) Math.round(vertexA.posX + r * arrowPosHorizRate),
							(int) Math.round(vertexA.posY - r * arrowPosVertRate));
					break;
				}
				case 90:
				{
					x = vertexA.posX - width / 2;
					y = vertexA.posY - r * (offsetRate + height / r);
					inPoint = new Point((int) Math.round(vertexA.posX + r * arrowPosVertRate), (int) Math.round(vertexA.posY - r * arrowPosHorizRate));
					outPoint = new Point((int) Math.round(vertexA.posX - r * arrowPosVertRate),
							(int) Math.round(vertexA.posY - r * arrowPosHorizRate));
					break;
				}
				case 180:
				{
					x = vertexA.posX - r * (offsetRate + width / r);
					y = vertexA.posY - height / 2;
					inPoint = new Point((int) Math.round(vertexA.posX - r * arrowPosHorizRate), (int) Math.round(vertexA.posY - r * arrowPosVertRate));
					outPoint = new Point((int) Math.round(vertexA.posX - r * arrowPosHorizRate),
							(int) Math.round(vertexA.posY + r * arrowPosVertRate));
					break;
				}
				case 270:
				{
					x = vertexA.posX - width / 2;
					y = vertexA.posY + r * offsetRate;
					inPoint = new Point((int) Math.round(vertexA.posX - r * arrowPosVertRate), (int) Math.round(vertexA.posY + r * arrowPosHorizRate));
					outPoint = new Point((int) Math.round(vertexA.posX + r * arrowPosVertRate),
							(int) Math.round(vertexA.posY + r * arrowPosHorizRate));
					break;
				}
			}
			arcMiddle = new Point((int) Math.round(x + width / 2), (int) Math.round(y + height / 2));
			arc = new Arc2D.Double(x, y, width, height, 0, 360, Arc2D.OPEN);
		}

		if (directed)
		{
			updateArrowPosition();
		}
	}

	public void draw(Graphics2D g2d, boolean dummy)
	{
		Graphics2D g = (Graphics2D) g2d.create();

		updatePosition();

		if (vertexA != vertexB)
		{
			if (relativeEdgeAngle != 0)
			{
				if (ControlManager.selection.contains(this))
				{
					g.setColor(DrawingTools.selectionColor);
					Stroke drawingStroke = new BasicStroke(lineWidth * 2 + 5);
					g.setStroke(drawingStroke);
					g.draw(arc);
				}

				g.setColor(lineColor);
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(lineColor));
				}
				g.setStroke(DrawingTools.getStroke(lineWidth, lineType, 0.0));
				g.draw(arc);
			}
			else
			{
				if (ControlManager.selection.contains(this))
				{
					g.setColor(DrawingTools.selectionColor);
					Stroke drawingStroke = new BasicStroke(lineWidth * 2 + 5);
					g.setStroke(drawingStroke);
					g.drawLine(vertexA.posX, vertexA.posY, vertexB.posX, vertexB.posY);
				}

				g.setColor(lineColor);
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(lineColor));
				}
				g.setStroke(DrawingTools.getStroke(lineWidth, lineType, 0.0));
				g.drawLine(vertexA.posX, vertexA.posY, vertexB.posX, vertexB.posY);
			}
			if ((ControlManager.selection.contains(this) || ControlManager.currentlyAddedEdge == this) && relativeEdgeAngle != 0)
			{
				drawAngleVisualisation(g);
			}
		}

		else
		{
			if (ControlManager.selection.contains(this))
			{
				g.setColor(DrawingTools.selectionColor);
				Stroke drawingStroke = new BasicStroke(lineWidth * 2 + 5);
				g.setStroke(drawingStroke);
				g.draw(arc);
			}

			g.setColor(lineColor);
			if (dummy)
			{
				g.setColor(DrawingTools.getDummyColor(lineColor));
			}
			g.setStroke(DrawingTools.getStroke(lineWidth, lineType, 0.0));
			g.draw(arc);
		}

		if (directed)
		{
			if (arrowType == PropertyModel.BASIC)
			{
				if (ControlManager.selection.contains(this))
				{
					g.setColor(DrawingTools.selectionColor);
					Stroke drawingStroke = new BasicStroke(lineWidth * 2 + 5);
					g.setStroke(drawingStroke);
					g.drawLine(arrowLine1[0], arrowLine1[1], arrowLine1[2], arrowLine1[3]);
					g.drawLine(arrowLine2[0], arrowLine2[1], arrowLine2[2], arrowLine2[3]);
				}

				g.setColor(lineColor);
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(lineColor));
				}
				g.setStroke(new BasicStroke(lineWidth));
				g.drawLine(arrowLine1[0], arrowLine1[1], arrowLine1[2], arrowLine1[3]);
				g.drawLine(arrowLine2[0], arrowLine2[1], arrowLine2[2], arrowLine2[3]);
			}
			else
			{
				if (ControlManager.selection.contains(this))
				{
					g.setColor(DrawingTools.selectionColor);
					Stroke drawingStroke = new BasicStroke(lineWidth * 2 + 5);
					g.setStroke(drawingStroke);
					g.fillPolygon(new Polygon(new int[] {arrowLine1[0], arrowLine1[2], arrowLine2[0]}, new int[] {arrowLine1[1], arrowLine1[3], arrowLine2[1]}, 3));
				}

				g.setColor(lineColor);
				if (dummy)
				{
					g.setColor(DrawingTools.getDummyColor(lineColor));
				}
				g.setStroke(new BasicStroke(lineWidth));
				g.fillPolygon(new Polygon(new int[] {arrowLine1[0], arrowLine1[2], arrowLine2[0]}, new int[] {arrowLine1[1], arrowLine1[3], arrowLine2[1]}, 3));
			}
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
