package pl.edu.agh.gratex.editor;

import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.gui.ControlManager;

public class AddOperation extends Operation
{
	private GraphElement	element;
	private int				type;

	public AddOperation(GraphElement _element)
	{
		type = ControlManager.mode;
		element = _element;
	}

	public String doOperation()
	{
		if (type == 1)
		{
			ControlManager.graph.vertices.add((Vertex) element);
			((Vertex) element).setPartOfNumeration(true);
			return "Vertex added";
		}
		else if (type == 2)
		{
			ControlManager.graph.edges.add((Edge) element);
			return "Edge added";
		}
		else if (type == 3)
		{
			ControlManager.graph.labelsV.add((LabelV) element);
			((LabelV) element).owner.label = (LabelV) element;
			return "Label added to a vertex";
		}
		else
		{
			ControlManager.graph.labelsE.add((LabelE) element);
			((LabelE) element).owner.label = (LabelE) element;
			return "Label added to an edge";
		}
	}

	public String undoOperation()
	{
		if (type == 1)
		{
			ControlManager.graph.vertices.remove((Vertex) element);
			((Vertex) element).setPartOfNumeration(false);
			return "Adding vertex undone";
		}
		else if (type == 2)
		{
			ControlManager.graph.edges.remove((Edge) element);
			return "Adding edge undone";
		}
		else if (type == 3)
		{
			ControlManager.graph.labelsV.remove((LabelV) element);
			((LabelV) element).owner.label = null;
			return "Adding label undone";
		}
		else
		{
			ControlManager.graph.labelsE.remove((LabelE) element);
			((LabelE) element).owner.label = null;
			return "Adding label undone";
		}
	}
}
