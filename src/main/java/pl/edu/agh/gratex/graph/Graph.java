package pl.edu.agh.gratex.graph;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.EdgePropertyModel;
import pl.edu.agh.gratex.model.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.VertexPropertyModel;


public class Graph implements Serializable
{
	private static final long	serialVersionUID	= 6647099578243878702L;
	
	public LinkedList<Vertex>		vertices;
	public LinkedList<Edge>			edges;
	public LinkedList<LabelV>		labelsV;
	public LinkedList<LabelE>		labelsE;

	public VertexPropertyModel		vertexDefaultModel;
	public EdgePropertyModel		edgeDefaultModel;
	public LabelVertexPropertyModel	labelVDefaultModel;
	public LabelEdgePropertyModel	labelEDefaultModel;

	public int						gridResolutionX		= 20;
	public int						gridResolutionY		= 20;
	public boolean					gridOn;

	public int						pageWidth			= 672;
	public int						pageHeight			= 880;

	public boolean[]				usedNumber;
	public int						startNumber			= 1;
	public int						maxNumber			= 703;	// ZZ tyle wynosi, 3 litery to za dużo
	public boolean					digitalNumeration	= true;

	public Graph()
	{
		vertices = new LinkedList<Vertex>();
		edges = new LinkedList<Edge>();
		labelsV = new LinkedList<LabelV>();
		labelsE = new LinkedList<LabelE>();

		initDefaultModels();

		usedNumber = new boolean[maxNumber];
		for (int i = 0; i < maxNumber; i++)
		{
			usedNumber[i] = false;
		}
		if (startNumber < 1 || startNumber >= maxNumber)
		{
			startNumber = 1;
		}
	}

	public int getNextFreeNumber()
	{
		for (int i = startNumber; i < maxNumber; i++)
		{
			if (!usedNumber[i])
			{
				return i;
			}
		}
		startNumber = 1;
		return 1;
	}

	public LinkedList<GraphElement> getIntersectingElements(Rectangle area)
	{
		LinkedList<GraphElement> result = new LinkedList<GraphElement>();
		Area rect = new Area(area);

		if (ControlManager.mode == ControlManager.VERTEX_MODE)
		{
			Iterator<Vertex> itv = vertices.listIterator();
			Vertex temp = null;
			while (itv.hasNext())
			{
				temp = itv.next();
				Area outline = new Area(Utilities.getVertexShape(temp.type + 1, temp.radius, temp.posX, temp.posY));
				outline.intersect(rect);
				if (!outline.isEmpty())
				{
					result.add(temp);
				}
			}
		}

		else if (ControlManager.mode == ControlManager.EDGE_MODE)
		{
			Iterator<Edge> ite = edges.listIterator();
			Edge temp = null;
			while (ite.hasNext())
			{
				temp = ite.next();
				if (Utilities.checkArcRectangleIntersection(temp, area))
				{
					result.add(temp);
				}
			}
		}

		else if (ControlManager.mode == ControlManager.LABEL_V_MODE)
		{
			Iterator<LabelV> itlv = labelsV.listIterator();
			LabelV temp = null;
			while (itlv.hasNext())
			{
				temp = itlv.next();
				if (temp.outline.intersects(area))
				{
					result.add(temp);
				}
			}
		}

		else if (ControlManager.mode == ControlManager.LABEL_E_MODE)
		{
			Iterator<LabelE> itle = labelsE.listIterator();
			LabelE temp = null;
			while (itle.hasNext())
			{
				temp = itle.next();
				if (temp.outline.intersects(area))
				{
					result.add(temp);
				}
			}
		}

		return result;
	}

	public Vertex getVertexFromPosition(int x, int y)
	{
		Iterator<Vertex> itv = vertices.listIterator();
		Vertex temp = null;
		while (itv.hasNext())
		{
			temp = itv.next();
			if (temp.intersects(x, y))
			{
				return temp;
			}
		}
		return null;
	}

	public boolean vertexCollision(Vertex vertex)
	{
		Iterator<Vertex> itv = vertices.listIterator();
		while (itv.hasNext())
		{
			if (itv.next().collides(vertex))
			{
				return true;
			}
		}
		return false;
	}

	public LinkedList<Edge> getAdjacentEdges(Vertex vertex)
	{
		LinkedList<Edge> result = new LinkedList<Edge>();

		Iterator<Edge> ite = edges.listIterator();
		Edge temp = null;
		while (ite.hasNext())
		{
			temp = ite.next();
			if (temp.vertexA == vertex || temp.vertexB == vertex)
			{
				result.add(temp);
			}
		}
		return result;
	}

	public Edge getEdgeFromPosition(int x, int y)
	{
		Iterator<Edge> ite = edges.listIterator();
		Edge temp = null;
		while (ite.hasNext())
		{
			temp = ite.next();
			if (temp.intersects(x, y))
			{
				return temp;
			}
		}
		return null;
	}

	public LabelV getLabelVFromPosition(int x, int y)
	{
		Iterator<LabelV> itlv = labelsV.listIterator();
		LabelV temp = null;
		while (itlv.hasNext())
		{
			temp = itlv.next();
			if (temp.intersects(x, y))
			{
				return temp;
			}
		}
		return null;
	}

	public LabelE getLabelEFromPosition(int x, int y)
	{
		Iterator<LabelE> itle = labelsE.listIterator();
		LabelE temp = null;
		while (itle.hasNext())
		{
			temp = itle.next();
			if (temp.intersects(x, y))
			{
				return temp;
			}
		}
		return null;
	}

	public void drawAll(Graphics2D g)
	{
		Iterator<Edge> ite = edges.listIterator();
		while (ite.hasNext())
		{
			ite.next().draw(g, false);
		}

		Iterator<Vertex> itv = vertices.listIterator();
		while (itv.hasNext())
		{
			itv.next().draw(g, false);
		}

		ite = edges.listIterator();
		while (ite.hasNext())
		{
			ite.next().drawLabel(g, false);
		}

		itv = vertices.listIterator();
		while (itv.hasNext())
		{
			itv.next().drawLabel(g, false);
		}
	}

	public void adjustVerticesToGrid()
	{
		Iterator<Vertex> itv = vertices.listIterator();
		while (itv.hasNext())
		{
			itv.next().adjustToGrid();
		}
	}

	public void deleteUnusedLabels()
	{
		Iterator<LabelV> itlv = labelsV.listIterator();
		while (itlv.hasNext())
		{
			if (itlv.next().owner.label == null)
			{
				itlv.remove();
			}
		}

		Iterator<LabelE> itle = labelsE.listIterator();
		while (itle.hasNext())
		{
			if (itle.next().owner.label == null)
			{
				itle.remove();
			}
		}
	}

	public void initDefaultModels()
	{
		vertexDefaultModel = new VertexPropertyModel();
		vertexDefaultModel.number = -1;
		vertexDefaultModel.radius = 40;
		vertexDefaultModel.type = 1;
		vertexDefaultModel.vertexColor = new Color(new Float(1), new Float(0.5), new Float(0));
		vertexDefaultModel.lineType = 1;
		vertexDefaultModel.lineWidth = 1;
		vertexDefaultModel.lineColor = Color.black;
		vertexDefaultModel.fontColor = Color.black;
		vertexDefaultModel.labelInside = 1;

		edgeDefaultModel = new EdgePropertyModel();
		edgeDefaultModel.lineType = DrawingTools.SOLID_LINE;
		edgeDefaultModel.lineWidth = 1;
		edgeDefaultModel.directed = 0;
		edgeDefaultModel.lineColor = Color.black;
		edgeDefaultModel.relativeEdgeAngle = 0;

		labelVDefaultModel = new LabelVertexPropertyModel();
		labelVDefaultModel.text = "Label";
		labelVDefaultModel.fontColor = Color.black;
		labelVDefaultModel.position = 0;
		labelVDefaultModel.spacing = 5;

		labelEDefaultModel = new LabelEdgePropertyModel();
		labelEDefaultModel.text = "Label";
		labelEDefaultModel.fontColor = Color.black;
		labelEDefaultModel.position = 50;
		labelEDefaultModel.spacing = 5;
		labelEDefaultModel.topPlacement = 1;
		labelEDefaultModel.horizontalPlacement = 0;
	}
}
