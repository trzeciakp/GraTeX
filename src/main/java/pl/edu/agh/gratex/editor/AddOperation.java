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
			ControlManager.graph.getVertices().add((Vertex) element);
			((Vertex) element).setPartOfNumeration(true);
			return "Vertex added";
		}
		else if (type == 2)
		{
			ControlManager.graph.getEdges().add((Edge) element);
			return "Edge added";
		}
		else if (type == 3)
		{
			ControlManager.graph.getLabelsV().add((LabelV) element);
			((LabelV) element).getOwner().setLabel((LabelV) element);
			return "Label added to a vertex";
		}
		else
		{
			ControlManager.graph.getLabelsE().add((LabelE) element);
			((LabelE) element).getOwner().setLabel((LabelE) element);
			return "Label added to an edge";
		}
	}

	public String undoOperation()
	{
		if (type == 1)
		{
			ControlManager.graph.getVertices().remove((Vertex) element);
			((Vertex) element).setPartOfNumeration(false);
			return "Adding vertex undone";
		}
		else if (type == 2)
		{
			ControlManager.graph.getEdges().remove((Edge) element);
			return "Adding edge undone";
		}
		else if (type == 3)
		{
			ControlManager.graph.getLabelsV().remove((LabelV) element);
			((LabelV) element).getOwner().setLabel(null);
			return "Adding label undone";
		}
		else
		{
			ControlManager.graph.getLabelsE().remove((LabelE) element);
			((LabelE) element).getOwner().setLabel(null);
			return "Adding label undone";
		}
	}
}
