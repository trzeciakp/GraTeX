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
		vertexStartModel = ControlManager.graph.vertexDefaultModel;
		edgeStartModel = ControlManager.graph.edgeDefaultModel;
		labelVStartModel = ControlManager.graph.labelVDefaultModel;
		labelEStartModel = ControlManager.graph.labelEDefaultModel;

		vertexEndModel = endGraphState.vertexDefaultModel;
		edgeEndModel = endGraphState.edgeDefaultModel;
		labelVEndModel = endGraphState.labelVDefaultModel;
		labelEEndModel = endGraphState.labelEDefaultModel;
		applyToAll = endGraphState.gridOn;
	}

	public String doOperation()
	{
		ControlManager.graph.vertexDefaultModel = vertexEndModel;
		ControlManager.graph.edgeDefaultModel = edgeEndModel;
		ControlManager.graph.labelVDefaultModel = labelVEndModel;
		ControlManager.graph.labelEDefaultModel = labelEEndModel;

		if (applyToAll)
		{
			if (ControlManager.graph.vertices.size() > 0)
			{
				vertexChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.vertices), -1);
				vertexChange.setEndModel(vertexEndModel);
				vertexChange.doOperation();
			}

			if (ControlManager.graph.edges.size() > 0)
			{
				edgeChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.edges), -1);
				edgeChange.setEndModel(edgeEndModel);
				edgeChange.doOperation();
			}

			if (ControlManager.graph.labelsV.size() > 0)
			{
				labelVChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.labelsV), -1);
				labelVChange.setEndModel(labelVEndModel);
				labelVChange.doOperation();
			}

			if (ControlManager.graph.labelsE.size() > 0)
			{
				labelEChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.graph.labelsE), -1);
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
		ControlManager.graph.vertexDefaultModel = vertexStartModel;
		ControlManager.graph.edgeDefaultModel = edgeStartModel;
		ControlManager.graph.labelVDefaultModel = labelVStartModel;
		ControlManager.graph.labelEDefaultModel = labelEStartModel;

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
