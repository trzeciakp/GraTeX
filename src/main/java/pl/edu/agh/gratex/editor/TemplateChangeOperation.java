package pl.edu.agh.gratex.editor;


import java.util.LinkedList;

import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.EdgePropertyModel;
import pl.edu.agh.gratex.model.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.VertexPropertyModel;


public class TemplateChangeOperation extends Operation
{
	private boolean						applyToAll;

	private VertexPropertyModel			vertexStartModel;
	private EdgePropertyModel			edgeStartModel;
	private LabelVertexPropertyModel	labelVStartModel;
	private LabelEdgePropertyModel		labelEStartModel;

	private VertexPropertyModel			vertexEndModel;
	private EdgePropertyModel			edgeEndModel;
	private LabelVertexPropertyModel	labelVEndModel;
	private LabelEdgePropertyModel		labelEEndModel;

	private PropertyChangeOperation		vertexChange	= null;
	private PropertyChangeOperation		edgeChange		= null;
	private PropertyChangeOperation		labelVChange	= null;
	private PropertyChangeOperation		labelEChange	= null;

	public TemplateChangeOperation(Graph initialGraphState, Graph endGraphState)
	{
		vertexStartModel = ControlManager.graph.getVertexDefaultModel();
		edgeStartModel = ControlManager.graph.getEdgeDefaultModel();
		labelVStartModel = ControlManager.graph.getLabelVDefaultModel();
		labelEStartModel = ControlManager.graph.getLabelEDefaultModel();

		vertexEndModel = endGraphState.getVertexDefaultModel();
		edgeEndModel = endGraphState.getEdgeDefaultModel();
		labelVEndModel = endGraphState.getLabelVDefaultModel();
		labelEEndModel = endGraphState.getLabelEDefaultModel();
		applyToAll = endGraphState.gridOn;
	}

	public String doOperation()
	{
		ControlManager.graph.setVertexDefaultModel(vertexEndModel);
		ControlManager.graph.setEdgeDefaultModel(edgeEndModel);
		ControlManager.graph.setLabelVDefaultModel(labelVEndModel);
		ControlManager.graph.setLabelEDefaultModel(labelEEndModel);

		if (applyToAll)
		{
			if (ControlManager.graph.getVertices().size() > 0)
			{
				vertexChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.getVertices()), -1);
				vertexChange.setEndModel(vertexEndModel);
				vertexChange.doOperation();
			}

			if (ControlManager.graph.getEdges().size() > 0)
			{
				edgeChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.getEdges()), -1);
				edgeChange.setEndModel(edgeEndModel);
				edgeChange.doOperation();
			}

			if (ControlManager.graph.getLabelsV().size() > 0)
			{
				labelVChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.getLabelsV()), -1);
				labelVChange.setEndModel(labelVEndModel);
				labelVChange.doOperation();
			}

			if (ControlManager.graph.getLabelsE().size() > 0)
			{
				labelEChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.getLabelsE()), -1);
				labelEChange.setEndModel(labelEEndModel);
				labelEChange.doOperation();
			}
		}

		if (applyToAll)
		{
			return "Template changed and applied globally";
		}
		else
		{
			return "Template changed";
		}
	}

	public String undoOperation()
	{
		ControlManager.graph.setVertexDefaultModel(vertexStartModel);
		ControlManager.graph.setEdgeDefaultModel(edgeStartModel);
		ControlManager.graph.setLabelVDefaultModel(labelVStartModel);
		ControlManager.graph.setLabelEDefaultModel(labelEStartModel);

		if (applyToAll)
		{
			if (vertexChange != null)
			{
				vertexChange.undoOperation();
			}
			if (edgeChange != null)
			{
				edgeChange.undoOperation();
			}
			if (labelVChange != null)
			{
				labelVChange.undoOperation();
			}
			if (labelEChange != null)
			{
				labelEChange.undoOperation();
			}
		}

		if (applyToAll)
		{
			return "Template change and global applying undone";
		}
		else
		{
			return "Template change undone";
		}
	}
}
