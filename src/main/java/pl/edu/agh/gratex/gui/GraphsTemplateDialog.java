package pl.edu.agh.gratex.gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.edu.agh.gratex.graph.DrawingTools;
import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.LabelE;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.model.EdgePropertyModel;
import pl.edu.agh.gratex.model.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.VertexPropertyModel;
import pl.edu.agh.gratex.property.PanelPropertyEditor;

public class GraphsTemplateDialog extends JDialog
{
	private static final long	serialVersionUID	= 3521596195996666926L;

	private Graph				graph;
	private Graph				result				= null;
	private Vertex				vertex1;
	private Vertex				vertex2;
	private Edge				edge1;
	private Edge				edge2;
	private Edge				edge3;
	private LabelV				labelV1;
	private LabelE				labelE1;
	private LabelE				labelE2;
	private LabelE				labelE3;

	private JButton				button_restoreDefaultSettings;
	private JCheckBox			checkBox_applyToAll;
	private JButton				button_save;
	private JButton				button_discard;

	private JTabbedPane			tabbedPane;
	private PanelPropertyEditor	panel_propertyEditor;
	private PanelPreview		panel_preview;
	private JLabel				label_hint;

	private JPanel				vertexPanel;
	private JPanel				edgePanel;
	private JPanel				labelVPanel;
	private JPanel				labelEPanel;

	protected JRootPane createRootPane()
	{
		ActionListener actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				setVisible(false);
				dispose();
			}
		};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	public GraphsTemplateDialog(MainWindow parent)
	{
		super(parent, "Graph's template editor", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(768, 511);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		initGraph();
		initializeFrame();
		initializeEvents();
	}

	public Graph displayDialog()
	{
		setVisible(true);
		return result;
	}

	private void updateModel(PropertyModel model)
	{
		if (model instanceof VertexPropertyModel)
		{
			((VertexPropertyModel) model).number = -1;
			vertex1.setModel((VertexPropertyModel) model);
			graph.vertexDefaultModel = (VertexPropertyModel) vertex1.getModel();
			graph.vertexDefaultModel.number = -1;
		}
		else if (model instanceof EdgePropertyModel)
		{
			((EdgePropertyModel) model).relativeEdgeAngle = -1;
			edge1.setModel((EdgePropertyModel) model);
			edge2.setModel((EdgePropertyModel) model);
			edge3.setModel((EdgePropertyModel) model);
			graph.edgeDefaultModel = (EdgePropertyModel) edge1.getModel();
			graph.edgeDefaultModel.relativeEdgeAngle = -1;
		}
		else if (model instanceof LabelVertexPropertyModel)
		{
			labelV1.setModel((LabelVertexPropertyModel) model);
			graph.labelVDefaultModel = (LabelVertexPropertyModel) labelV1.getModel();
			graph.labelVDefaultModel.text = null;
		}
		else
		{
			labelE1.setModel((LabelEdgePropertyModel) model);
			labelE2.setModel((LabelEdgePropertyModel) model);
			labelE3.setModel((LabelEdgePropertyModel) model);
			graph.labelEDefaultModel = (LabelEdgePropertyModel) labelE1.getModel();
			graph.labelEDefaultModel.text = null;
		}

		refreshModels();
	}

	private void refreshModels()
	{
		if (tabbedPane.getSelectedIndex() == 0)
		{
			panel_propertyEditor.setModel(graph.vertexDefaultModel);
		}
		else if (tabbedPane.getSelectedIndex() == 1)
		{
			panel_propertyEditor.setModel(graph.edgeDefaultModel);
		}
		else if (tabbedPane.getSelectedIndex() == 2)
		{
			panel_propertyEditor.setModel(graph.labelVDefaultModel);
		}
		else if (tabbedPane.getSelectedIndex() == 3)
		{
			panel_propertyEditor.setModel(graph.labelEDefaultModel);
		}
	}

	private void initializeEvents()
	{
		tabbedPane.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_preview);
				((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(label_hint);
				((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_propertyEditor);

				panel_propertyEditor.setMode(tabbedPane.getSelectedIndex() + 1);
				refreshModels();
			}
		});

		button_restoreDefaultSettings.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				graph.initDefaultModels();
				graph.edgeDefaultModel.isLoop = -1;
				graph.edgeDefaultModel.relativeEdgeAngle = -1;
				graph.labelVDefaultModel.text = null;
				graph.labelEDefaultModel.text = null;
				vertex1.setModel(graph.vertexDefaultModel);
				edge1.setModel(graph.edgeDefaultModel);
				edge1.relativeEdgeAngle = 300;
				edge2.setModel(graph.edgeDefaultModel);
				edge2.relativeEdgeAngle = 180;
				edge3.setModel(graph.edgeDefaultModel);
				edge3.relativeEdgeAngle = 0;
				labelV1.setModel(graph.labelVDefaultModel);
				labelE1.setModel(graph.labelEDefaultModel);
				labelE2.setModel(graph.labelEDefaultModel);

				panel_preview.repaint();
			}
		});

		button_save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (checkBox_applyToAll.isSelected())
				{
					if (0 != JOptionPane.showConfirmDialog(null,
							"Current settings will be applied to ALL existing elements of the graph.\nDo you wish to continue?", "Confirm decision",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
					{
						return;
					}

					graph.gridOn = true;

				}
				refreshModels();
				result = graph;
				setVisible(false);
				dispose();
			}
		});

		button_discard.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				result = null;
				setVisible(false);
				dispose();
			}
		});
	}

	private void initGraph()
	{
		graph = new Graph();
		graph.gridOn = false;

		graph.vertexDefaultModel = new VertexPropertyModel(ControlManager.graph.vertexDefaultModel);
		graph.edgeDefaultModel = new EdgePropertyModel(ControlManager.graph.edgeDefaultModel);
		graph.edgeDefaultModel.isLoop = -1;
		graph.edgeDefaultModel.relativeEdgeAngle = -1;
		graph.labelVDefaultModel = new LabelVertexPropertyModel(ControlManager.graph.labelVDefaultModel);
		graph.labelVDefaultModel.text = null;
		graph.labelEDefaultModel = new LabelEdgePropertyModel(ControlManager.graph.labelEDefaultModel);
		graph.labelEDefaultModel.text = null;

		vertex1 = new Vertex();
		vertex1.setModel(graph.vertexDefaultModel);
		vertex1.updateNumber(1);
		vertex1.posX = 240;
		vertex1.posY = 190;
		graph.vertices.add(vertex1);

		vertex2 = new Vertex();
		vertex2.setModel(graph.vertexDefaultModel);
		vertex2.updateNumber(2);
		vertex2.type = 1;
		vertex2.vertexColor = new Color(200, 200, 200);
		vertex2.lineType = DrawingTools.SOLID_LINE;
		vertex2.lineWidth = 1;
		vertex2.lineColor = Color.black;
		vertex2.fontColor = Color.black;
		vertex2.labelInside = true;
		vertex2.radius = 15;
		vertex2.posX = 490;
		vertex2.posY = 313;
		graph.vertices.add(vertex2);

		edge1 = new Edge();
		edge1.setModel(graph.edgeDefaultModel);
		edge1.vertexA = vertex2;
		edge1.vertexB = vertex1;
		edge1.relativeEdgeAngle = 300;
		graph.edges.add(edge1);

		edge2 = new Edge();
		edge2.setModel(graph.edgeDefaultModel);
		edge2.vertexA = vertex1;
		edge2.vertexB = vertex1;
		edge2.relativeEdgeAngle = 180;
		graph.edges.add(edge2);

		edge3 = new Edge();
		edge3.setModel(graph.edgeDefaultModel);
		edge3.vertexA = vertex1;
		edge3.vertexB = vertex2;
		edge3.relativeEdgeAngle = 0;
		graph.edges.add(edge3);

		labelV1 = new LabelV(vertex1);
		vertex1.label = labelV1;
		labelV1.setModel(graph.labelVDefaultModel);
		graph.labelsV.add(labelV1);

		labelE1 = new LabelE(edge1);
		edge1.label = labelE1;
		labelE1.setModel(graph.labelEDefaultModel);
		graph.labelsE.add(labelE1);

		labelE2 = new LabelE(edge2);
		edge2.label = labelE2;
		labelE2.setModel(graph.labelEDefaultModel);
		graph.labelsE.add(labelE2);

		labelE3 = new LabelE(edge3);
		edge3.label = labelE3;
		labelE3.setModel(graph.labelEDefaultModel);
		graph.labelsE.add(labelE3);
	}

	private void initializeFrame()
	{
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		tabbedPane.setBounds(0, 0, 752, 427);
		getContentPane().add(tabbedPane);

		label_hint = new JLabel("Edit graph properties, according to which new elements will be created.");
		label_hint.setHorizontalAlignment(SwingConstants.CENTER);
		label_hint.setBounds(222, 10, 524, 20);
		panel_preview = new PanelPreview(graph);
		panel_preview.setBounds(222, 42, 520, 343);

		panel_propertyEditor = new PanelPropertyEditor()
		{
			private static final long	serialVersionUID	= 4008480073440306159L;

			public void valueChanged(PropertyModel model)
			{
				updateModel(model);
				panel_preview.repaint();
			}
		};
		panel_propertyEditor.setBounds(10, 10, 200, 380);
		panel_propertyEditor.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_propertyEditor.setEnabled(true);
		panel_propertyEditor.disableLabelEdition();
		panel_propertyEditor.setMode(1);
		panel_propertyEditor.setModel(graph.vertexDefaultModel);

		vertexPanel = new JPanel();
		vertexPanel.setLayout(null);
		tabbedPane.addTab("Vertex", null, vertexPanel, null);

		vertexPanel.add(panel_propertyEditor);
		vertexPanel.add(label_hint);
		vertexPanel.add(panel_preview);

		edgePanel = new JPanel();
		edgePanel.setLayout(null);
		tabbedPane.addTab("Edge", null, edgePanel, null);

		labelVPanel = new JPanel();
		labelVPanel.setLayout(null);
		tabbedPane.addTab("Label (V)", null, labelVPanel, null);

		labelEPanel = new JPanel();
		labelEPanel.setLayout(null);
		tabbedPane.addTab("Label (E)", null, labelEPanel, null);

		button_discard = new JButton("Discard");
		button_discard.setToolTipText("Discard all changes");
		button_discard.setBounds(656, 433, 90, 34);
		getContentPane().add(button_discard);

		button_save = new JButton("Save");
		button_save.setToolTipText("Save changes");
		button_save.setBounds(554, 433, 90, 34);
		getContentPane().add(button_save);

		button_restoreDefaultSettings = new JButton("Restore default settings");
		button_restoreDefaultSettings.setToolTipText("Reset values to default");
		button_restoreDefaultSettings.setBounds(6, 433, 170, 34);
		getContentPane().add(button_restoreDefaultSettings);

		checkBox_applyToAll = new JCheckBox("Apply to existing items");
		checkBox_applyToAll.setBounds(393, 433, 149, 34);
		checkBox_applyToAll.setToolTipText("Above settings will be applied to all existing elements of the graph");
		getContentPane().add(checkBox_applyToAll);
	}
}
