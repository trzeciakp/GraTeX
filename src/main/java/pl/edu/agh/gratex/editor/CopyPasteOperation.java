package pl.edu.agh.gratex.editor;


import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.gui.ControlManager;

public class CopyPasteOperation extends Operation
{
	public LinkedList<Vertex>	vertices;
	public LinkedList<Edge>		edges;
	public LinkedList<LabelV>	labelsV;
	public LinkedList<LabelE>	labelsE;

	public int					minX		= 10000;
	public int					minY		= 10000;
	public int					minNumber	= 10000;

	public int					targetX		= 0;
	public int					targetY		= 0;

	public int					biasX		= 0;
	public int					biasY		= 0;

	public boolean				pasting		= false;

	public CopyPasteOperation(LinkedList<GraphElement> selectedVertices)
	{
		vertices = new LinkedList<Vertex>();
		edges = new LinkedList<Edge>();
		labelsV = new LinkedList<LabelV>();
		labelsE = new LinkedList<LabelE>();

		Iterator<GraphElement> it = selectedVertices.listIterator();
		Vertex tempV = null;
		while (it.hasNext())
		{
			tempV = (Vertex) it.next();
			vertices.add(tempV.getCopy());
			if (vertices.getLast().label != null)
			{
				labelsV.add(vertices.getLast().label);
			}

			if (tempV.posX < minX)
			{
				minX = tempV.posX;
				minY = tempV.posY;
			}
			if (tempV.getNumber() < minNumber)
			{
				minNumber = tempV.getNumber();
			}
		}

		Iterator<Edge> ite = ControlManager.graph.edges.listIterator();
		Edge tempE = null;
		while (ite.hasNext())
		{
			tempE = ite.next();
			Edge edge = tempE.getCopy(vertices);
			if (edge != null)
			{
				edges.add(edge);
				if (edges.getLast().label != null)
				{
					labelsE.add(edges.getLast().label);
				}
			}
		}
	}

	public void startPasting()
	{
		pasting = true;
		biasX = 0;
		biasY = 0;
	}

	public void calculatePosition()
	{
		Iterator<Vertex> itv = vertices.listIterator();
		Vertex tempV = null;
		while (itv.hasNext())
		{
			tempV = itv.next();
			tempV.posX += -biasX + targetX - minX;
			tempV.posY += -biasY + targetY - minY;
		}
		biasX = targetX - minX;
		biasY = targetY - minY;
	}

	public boolean fitsIntoPosition()
	{
		Iterator<Vertex> itvd = vertices.listIterator();
		Iterator<Vertex> itv = null;
		Vertex tempVD = null;

		while (itvd.hasNext())
		{
			tempVD = itvd.next();
			itv = ControlManager.graph.vertices.listIterator();
			while (itv.hasNext())
			{
				if (tempVD.collides(itv.next()))
				{
					return false;
				}
			}

			if (!tempVD.fitsIntoPage())
			{
				return false;
			}
		}

		return true;
	}

	public void drawDummySubgraph(Graphics2D g)
	{
		Iterator<Edge> ite = edges.listIterator();
		while (ite.hasNext())
		{
			ite.next().draw(g, true);
		}

		Iterator<Vertex> itv = vertices.listIterator();
		while (itv.hasNext())
		{
			itv.next().draw(g, true);
		}

		ite = edges.listIterator();
		while (ite.hasNext())
		{
			ite.next().drawLabel(g, true);
		}

		itv = vertices.listIterator();
		while (itv.hasNext())
		{
			itv.next().drawLabel(g, true);
		}
	}

	public CopyPasteOperation getCopy()
	{
		return new CopyPasteOperation(new LinkedList<GraphElement>(vertices));
	}

	public String doOperation()
	{
		ControlManager.graph.vertices.addAll(vertices);
		ControlManager.graph.edges.addAll(edges);
		ControlManager.graph.labelsV.addAll(labelsV);
		ControlManager.graph.labelsE.addAll(labelsE);

		Collections.sort(vertices, new VertexOrderComparator());
		Iterator<Vertex> itv = vertices.listIterator();
		Vertex tempV = null;
		while (itv.hasNext())
		{
			tempV = itv.next();
			tempV.updateNumber(ControlManager.graph.getNextFreeNumber());
			tempV.setPartOfNumeration(true);
			tempV.labelInside = (ControlManager.graph.vertexDefaultModel.labelInside == 1);
			if (ControlManager.graph.gridOn)
			{
				tempV.adjustToGrid();
			}
		}

		minNumber = 10000;
		itv = vertices.listIterator();
		while (itv.hasNext())
		{
			tempV = itv.next();
			if (tempV.getNumber() < minNumber)
			{
				minNumber = tempV.getNumber();
			}
		}

		return "Subgraph pasted";
	}

	public String undoOperation()
	{
		ControlManager.graph.vertices.removeAll(vertices);
		ControlManager.graph.edges.removeAll(edges);
		ControlManager.graph.labelsV.removeAll(labelsV);
		ControlManager.graph.labelsE.removeAll(labelsE);

		Iterator<Vertex> itv = vertices.listIterator();
		while (itv.hasNext())
		{
			itv.next().setPartOfNumeration(false);
		}

		return "Pasting subgraph undone";
	}
}
