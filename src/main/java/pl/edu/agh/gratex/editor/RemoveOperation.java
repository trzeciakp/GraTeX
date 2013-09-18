package pl.edu.agh.gratex.editor;


import java.util.Iterator;
import java.util.LinkedList;

import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.gui.ControlManager;

public class RemoveOperation extends Operation
{
	private LinkedList<GraphElement>	elements;
	private int							type;
	private LinkedList<Edge>			connectedEdges;

	public RemoveOperation(LinkedList<GraphElement> _elements)
	{
		type = ControlManager.mode;
		elements = new LinkedList<GraphElement>(_elements);
	}

	public RemoveOperation(GraphElement _element)
	{
		type = ControlManager.mode;
		elements = new LinkedList<GraphElement>();
		elements.add(_element);
	}

	public String doOperation()
	{
		String result = null;
		if (type == 1)
		{
			if (elements.size() > 1)
			{
				connectedEdges = new LinkedList<Edge>();
				Iterator<GraphElement> it = elements.listIterator();
				Vertex temp = null;
				while (it.hasNext())
				{
					temp = (Vertex) it.next();
					temp.setPartOfNumeration(false);
					connectedEdges.addAll(ControlManager.graph.getAdjacentEdges(temp));
					ControlManager.graph.edges.removeAll(connectedEdges);
				}
				ControlManager.graph.vertices.removeAll(elements);
				result = elements.size() + " vertices removed";
			}
			else
			{
				connectedEdges = ControlManager.graph.getAdjacentEdges((Vertex) elements.get(0));
				ControlManager.graph.edges.removeAll(connectedEdges);
				ControlManager.graph.vertices.remove((Vertex) elements.get(0));
				((Vertex) elements.get(0)).setPartOfNumeration(false);
				result = "Vertex removed";
			}
		}
		else if (type == 2)
		{
			ControlManager.graph.edges.removeAll(elements);
			if (elements.size() > 1)
			{
				result = elements.size() + " edges removed";
			}
			else
			{
				result = "Edge removed";
			}
		}
		else if (type == 3)
		{
			if (elements.size() > 1)
			{
				Iterator<GraphElement> it = elements.listIterator();
				while (it.hasNext())
				{
					((LabelV) it.next()).owner.label = null;
				}
				result = elements.size() + " labels removed";
			}
			else
			{
				((LabelV) elements.getFirst()).owner.label = null;
				result = "Label removed";
			}
			ControlManager.graph.labelsV.removeAll(elements);
		}
		else
		{
			if (elements.size() > 1)
			{
				Iterator<GraphElement> it = elements.listIterator();
				while (it.hasNext())
				{
					((LabelE) it.next()).owner.label = null;
				}
				result = elements.size() + " labels removed";
			}
			else
			{
				((LabelE) elements.getFirst()).owner.label = null;
				result = "Label removed";
			}
			ControlManager.graph.labelsV.removeAll(elements);
		}

		return result;
	}

	public String undoOperation()
	{
		String result = null;

		if (type == 1)
		{
			if (elements.size() > 1)
			{
				Iterator<GraphElement> it = elements.listIterator();
				Vertex temp = null;
				while (it.hasNext())
				{
					temp = (Vertex) it.next();
					ControlManager.graph.vertices.add(temp);
					temp.setPartOfNumeration(true);
				}
				result = "Removing multiple vertices undone";
			}
			else
			{
				ControlManager.graph.vertices.add((Vertex) elements.get(0));
				((Vertex) elements.get(0)).setPartOfNumeration(true);
				result = "Removing vertex undone";
			}
			ControlManager.graph.edges.addAll(connectedEdges);
		}
		else if (type == 2)
		{
			if (elements.size() > 1)
			{
				Iterator<GraphElement> it = elements.listIterator();
				while (it.hasNext())
				{
					ControlManager.graph.edges.add((Edge) it.next());
				}
				result = "Removing multiple edges undone";
			}
			else
			{
				ControlManager.graph.edges.add((Edge) elements.get(0));
				result = "Removing edge undone";
			}
		}
		else if (type == 3)
		{
			if (elements.size() > 1)
			{
				Iterator<GraphElement> it = elements.listIterator();
				LabelV temp = null;
				while (it.hasNext())
				{
					temp = (LabelV) it.next();
					ControlManager.graph.labelsV.add(temp);
					temp.owner.label = temp;
				}
				result = "Removing multiple labels undone";
			}
			else
			{
				ControlManager.graph.labelsV.add((LabelV) elements.get(0));
				((LabelV) elements.getFirst()).owner.label = (LabelV) elements.getFirst();
				result = "Removing label undone";
			}
		}
		else
		{
			if (elements.size() > 1)
			{
				Iterator<GraphElement> it = elements.listIterator();
				LabelE temp = null;
				while (it.hasNext())
				{
					temp = (LabelE) it.next();
					ControlManager.graph.labelsE.add(temp);
					temp.owner.label = temp;
				}
				result = "Removing multiple labels undone";
			}
			else
			{
				ControlManager.graph.labelsE.add((LabelE) elements.get(0));
				((LabelE) elements.getFirst()).owner.label = (LabelE) elements.getFirst();
				result = "Removing label undone";
			}
		}
		return result;
	}
}