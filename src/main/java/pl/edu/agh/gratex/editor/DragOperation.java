package pl.edu.agh.gratex.editor;

import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;

public class DragOperation extends Operation
{
	private int				startPosX;
	private int				startPosY;
	private int				endPosX;
	private int				endPosY;

	private int				startAngle;
	private int				endAngle;

	private int				startBias;
	private int				endBias;
	private boolean			startLabelEHorizontal;
	private boolean			endLabelEHorizontal;

	private int				startRelativeEdgeAngle;
	private int				endRelativeEdgeAngle;
	private boolean			startEdgeDirection;
	private boolean			endEdgeDirection;
	private Vertex			disjointedVertex;
	private Vertex			endVertex;
	private boolean			isDisjointedVertexA;

	private GraphElement	draggedGraphElement;

	public DragOperation(GraphElement _draggedGraphElement)
	{
		draggedGraphElement = _draggedGraphElement;
	}

	public boolean changeMade()
	{
		if (draggedGraphElement instanceof Vertex)
		{
			return (startPosX != endPosX || startPosY != endPosY);
		}
		else if (draggedGraphElement instanceof Edge)
		{
			return (startRelativeEdgeAngle != endRelativeEdgeAngle);
		}
		else if (draggedGraphElement instanceof LabelV)
		{
			return (startAngle != endAngle);
		}
		else
		{
			return (startBias != endBias || startLabelEHorizontal != endLabelEHorizontal);
		}
	}

	public void setStartPos(int x, int y)
	{
		startPosX = x;
		startPosY = y;
	}

	public void setEndPos(int x, int y)
	{
		endPosX = x;
		endPosY = y;
	}

	public void setStartAngle(int _angle)
	{
		startAngle = _angle;
	}

	public void setEndAngle(int _angle)
	{
		endAngle = _angle;
	}

	public void setLabelEStartState(LabelE labelE)
	{
		startBias = labelE.position;
		startLabelEHorizontal = labelE.horizontalPlacement;
	}

	public void setLabelEEndState(LabelE labelE)
	{
		endBias = labelE.position;
		endLabelEHorizontal = labelE.horizontalPlacement;
	}

	public void setEdgeStartState(Edge edge, boolean _isDisjointedVertexA)
	{
		startRelativeEdgeAngle = edge.relativeEdgeAngle;
		startEdgeDirection = edge.directed;
		isDisjointedVertexA = _isDisjointedVertexA;
		if (isDisjointedVertexA)
		{
			disjointedVertex = edge.vertexA;
		}
		else
		{
			disjointedVertex = edge.vertexB;
		}
	}

	public void setEdgeEndState(Edge edge)
	{
		endRelativeEdgeAngle = edge.relativeEdgeAngle;
		endEdgeDirection = edge.directed;
		if (isDisjointedVertexA)
		{
			endVertex = edge.vertexA;
		}
		else
		{
			endVertex = edge.vertexB;
		}
	}

	public void restoreEdgeStartState()
	{
		((Edge) draggedGraphElement).directed = startEdgeDirection;
		((Edge) draggedGraphElement).relativeEdgeAngle = startRelativeEdgeAngle;
		if (isDisjointedVertexA)
		{
			((Edge) draggedGraphElement).vertexA = disjointedVertex;
		}
		else
		{
			((Edge) draggedGraphElement).vertexB = disjointedVertex;
		}
	}

	public Vertex getDisjointedVertex()
	{
		return disjointedVertex;
	}

	public boolean draggingA()
	{
		return isDisjointedVertexA;
	}

	public void restoreDisjointedVertex()
	{
		((Edge) draggedGraphElement).directed = startEdgeDirection;
		if (isDisjointedVertexA)
		{
			((Edge) draggedGraphElement).vertexA = disjointedVertex;
		}
		else
		{
			((Edge) draggedGraphElement).vertexB = disjointedVertex;
		}
	}

	public void setEndVertex(Vertex vertex)
	{
		endVertex = vertex;
	}

	public String doOperation()
	{
		String result = null;

		if (draggedGraphElement instanceof Vertex)
		{
			((Vertex) draggedGraphElement).posX = endPosX;
			((Vertex) draggedGraphElement).posY = endPosY;
			result = "Vertex moved";
		}
		else if (draggedGraphElement instanceof Edge)
		{
			((Edge) draggedGraphElement).directed = endEdgeDirection;
			((Edge) draggedGraphElement).relativeEdgeAngle = endRelativeEdgeAngle;
			if (isDisjointedVertexA)
			{
				((Edge) draggedGraphElement).vertexA = endVertex;
			}
			else
			{
				((Edge) draggedGraphElement).vertexB = endVertex;
			}
			result = "Edge moved";
		}
		else if (draggedGraphElement instanceof LabelV)
		{
			((LabelV) draggedGraphElement).position = endAngle;
			result = "Label moved";
		}
		else if (draggedGraphElement instanceof LabelE)
		{
			((LabelE) draggedGraphElement).position = endBias;
			((LabelE) draggedGraphElement).horizontalPlacement = endLabelEHorizontal;
			result = "Label moved";
		}
		return result;
	}

	public String undoOperation()
	{
		String result = null;

		if (draggedGraphElement instanceof Vertex)
		{
			((Vertex) draggedGraphElement).posX = startPosX;
			((Vertex) draggedGraphElement).posY = startPosY;
			result = "Vertex moved back";
		}
		else if (draggedGraphElement instanceof Edge)
		{
			restoreEdgeStartState();
			result = "Edge moved back";
		}
		else if (draggedGraphElement instanceof LabelV)
		{
			((LabelV) draggedGraphElement).position = startAngle;
			result = "Label moved back";
		}
		else if (draggedGraphElement instanceof LabelE)
		{
			((LabelE) draggedGraphElement).position = startBias;
			((LabelE) draggedGraphElement).horizontalPlacement = startLabelEHorizontal;
			result = "Label moved back";
		}
		return result;
	}
}
