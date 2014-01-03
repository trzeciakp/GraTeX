package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.PropertyModelFactory;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.view.propertyPanel.PanelPropertyEditor;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EnumMap;

@SuppressWarnings("serial")
public class GraphTemplateDialog extends JDialog implements ModeListener {
    private GeneralController generalController;
    private GraphElementFactory graphElementFactory;

    // This dialog needs its own ModeController, so that it does not change mode globally
    private ModeController modeController;

    private Graph graph;
    private boolean result;

    //TODO convert fields to local
    private Vertex vertex1;
    private Vertex vertex2;
    private Edge edge1;
    private Edge edge2;
    private Edge edge3;
    private LabelV labelV1;
    private LabelE labelE1;
    private LabelE labelE2;
    private LabelE labelE3;

    private JButton button_restoreDefaultSettings;
    private JCheckBox checkBox_applyToAll;
    private JButton button_save;
    private JButton button_discard;

    private JTabbedPane tabbedPane;
    private PanelPropertyEditor panel_propertyEditor;
    private PanelPreview panel_preview;
    private JLabel label_hint;

    private JPanel vertexPanel;
    private JPanel edgePanel;
    private JPanel labelVPanel;
    private JPanel labelEPanel;

    private EnumMap<GraphElementType, PropertyModel> currentModels = new EnumMap<>(GraphElementType.class);

    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }

    public GraphTemplateDialog(MainWindow parent, GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(parent, StringLiterals.TITLE_GRAPH_TEMPLATE_EDITOR, true);
        this.generalController = generalController;

        this.graphElementFactory = graphElementFactory;
        modeController = new ModeControllerImpl(generalController);
        modeController.addModeListener(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(768, 511);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        PropertyModelFactory propertyModelFactory = graphElementFactory.getPropertyModelFactory();
        for (GraphElementType graphElementType : GraphElementType.values()) {
            currentModels.put(graphElementType, propertyModelFactory.createTemplateModel(graphElementType).getCopy());
        }

        initGraph();
        initializeFrame();
        initializeEvents();
    }

    public boolean displayDialog() {
        setVisible(true);
        return result;
    }

    private void updateModel(PropertyModel model, GraphElementType modelType) {
        for (GraphElement graphElement : graph.getElements(modelType)) {
            graphElement.setModel(model);
        }
        currentModels.get(modelType).updateWithModel(model);

        /*if (model instanceof VertexPropertyModel) {
            ((VertexPropertyModel) model).setNumber(-1);
            vertex1.setModel(model);
            graph.setVertexDefaultModel((VertexPropertyModel) vertex1.getModel());
            graph.getVertexDefaultModel().setNumber(-1);
        } else if (model instanceof EdgePropertyModel) {
            ((EdgePropertyModel) model).setRelativeEdgeAngle(-1);
            edge1.setModel(model);
            edge2.setModel(model);
            edge3.setModel(model);
            graph.setEdgeDefaultModel((EdgePropertyModel) edge1.getModel());
            graph.getEdgeDefaultModel().setRelativeEdgeAngle(-1);
        } else if (model instanceof LabelVertexPropertyModel) {
            labelV1.setModel(model);
            graph.setLabelVDefaultModel((LabelVertexPropertyModel) labelV1.getModel());
            graph.getLabelVDefaultModel().setText(null);
        } else {
            labelE1.setModel(model);
            labelE2.setModel(model);
            labelE3.setModel(model);
            graph.setLabelEDefaultModel((LabelEdgePropertyModel) labelE1.getModel());
            graph.getLabelEDefaultModel().setText(null);
        } */
    }

    private void setDefaultsModelsToAllGraphElements() {
        PropertyModelFactory propertyModelFactory = graphElementFactory.getPropertyModelFactory();
        for (GraphElement graphElement : graph.getAllElements()) {
            PropertyModel model = propertyModelFactory.createDefaultModel(graphElement.getType()).getCopy();
            graphElement.setModel(model);
            currentModels.put(graphElement.getType(), model);
        }
    }

    private void setTemplateModelsToAllGraphElements() {
        PropertyModelFactory propertyModelFactory = graphElementFactory.getPropertyModelFactory();
        for (GraphElement graphElement : graph.getAllElements()) {
            PropertyModel model = propertyModelFactory.createTemplateModel(graphElement.getType());
            graphElement.setModel(model);
        }
    }

    private void initializeEvents() {
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_preview);
                ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(label_hint);
                ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_propertyEditor);
                modeController.setMode(ModeType.values()[tabbedPane.getSelectedIndex()]);
            }
        });

        button_restoreDefaultSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //graph.initDefaultModels();
                //TODO czy potrzebne?
                /*graph.getEdgeDefaultModel().setLoop(-1);
                graph.getEdgeDefaultModel().setRelativeEdgeAngle(-1);
                graph.getLabelVDefaultModel().setText(null);
                graph.getLabelEDefaultModel().setText(null);*/
                edge1.setRelativeEdgeAngle(300);
                edge2.setRelativeEdgeAngle(180);
                edge3.setRelativeEdgeAngle(0);
                /*vertex1.setModel(graph.getVertexDefaultModel());
                edge1.setModel(graph.getEdgeDefaultModel());
                edge2.setModel(graph.getEdgeDefaultModel());
                edge3.setModel(graph.getEdgeDefaultModel());
                labelV1.setModel(graph.getLabelVDefaultModel());
                labelE1.setModel(graph.getLabelEDefaultModel());
                labelE2.setModel(graph.getLabelEDefaultModel());    */
                setDefaultsModelsToAllGraphElements();
                panel_preview.repaint();
                modeController.setMode(ModeType.values()[tabbedPane.getSelectedIndex()]);
            }
        });

        button_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox_applyToAll.isSelected()) {
                    if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(null,
                            StringLiterals.MESSAGE_CONFIRM_GLOBAL_APPLY, StringLiterals.TITLE_CONFIRM_DIALOG,
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                        return;
                    }
                    result = true;
                } else {
                    result = false;
                }
                PropertyModelFactory propertyModelFactory = graphElementFactory.getPropertyModelFactory();
                for (GraphElementType graphElementType : GraphElementType.values()) {
                    propertyModelFactory.setTemplateModel(graphElementType, currentModels.get(graphElementType));
                }

                setVisible(false);
                dispose();
            }
        });

        button_discard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                result = false;
                setVisible(false);
                dispose();
            }
        });
    }

    private void initGraph() {
        graph = new Graph();
        graph.gridOn = false;

        /*
        graph.setVertexDefaultModel((VertexPropertyModel) generalController.getGraph().getVertexDefaultModel().getCopy());
        graph.setEdgeDefaultModel((EdgePropertyModel) generalController.getGraph().getEdgeDefaultModel().getCopy());
        graph.getEdgeDefaultModel().setLoop(-1);
        graph.getEdgeDefaultModel().setRelativeEdgeAngle(-1);
        graph.setLabelVDefaultModel((LabelVertexPropertyModel) generalController.getGraph().getLabelVDefaultModel().getCopy());
        graph.getLabelVDefaultModel().setText(null);
        graph.setLabelEDefaultModel((LabelEdgePropertyModel) generalController.getGraph().getLabelEDefaultModel().getCopy());
        graph.getLabelEDefaultModel().setText(null);*/

        vertex1 = (Vertex) graphElementFactory.create(GraphElementType.VERTEX, graph);
        VertexUtils.updateNumber(vertex1, 1);
        vertex1.setPosX(240);
        vertex1.setPosY(190);
        graph.getVertices().add(vertex1);
        //vertex1.setModel(graph.getVertexDefaultModel());

        vertex2 = (Vertex) graphElementFactory.create(GraphElementType.VERTEX, graph);
        VertexUtils.updateNumber(vertex1, 2);
        vertex2.setShape(1);
        vertex2.setVertexColor(new Color(200, 200, 200));
        vertex2.setLineType(LineType.SOLID);
        vertex2.setLineWidth(1);
        vertex2.setLineColor(Color.black);
        vertex2.setFontColor(Color.black);
        vertex2.setLabelInside(true);
        vertex2.setRadius(15);
        vertex2.setPosX(490);
        vertex2.setPosY(313);
        graph.getVertices().add(vertex2);
        //vertex2.setModel(graph.getVertexDefaultModel());

        edge1 = (Edge) graphElementFactory.create(GraphElementType.EDGE, graph);
        //edge1.setModel(graph.getEdgeDefaultModel());
        edge1.setVertexA(vertex2);
        edge1.setVertexB(vertex1);
        edge1.setRelativeEdgeAngle(300);
        graph.getEdges().add(edge1);

        edge2 = (Edge) graphElementFactory.create(GraphElementType.EDGE, graph);
//        edge2.setModel(graph.getEdgeDefaultModel());
        edge2.setVertexA(vertex1);
        edge2.setVertexB(vertex1);
        edge2.setRelativeEdgeAngle(180);
        graph.getEdges().add(edge2);

        edge3 = (Edge) graphElementFactory.create(GraphElementType.EDGE, graph);
//        edge3.setModel(graph.getEdgeDefaultModel());
        edge3.setVertexA(vertex1);
        edge3.setVertexB(vertex2);
        edge3.setRelativeEdgeAngle(0);
        graph.getEdges().add(edge3);

        labelV1 = (LabelV) graphElementFactory.create(GraphElementType.LABEL_VERTEX, graph);
        labelV1.setOwner(vertex1);
        vertex1.setLabel(labelV1);
//        labelV1.setModel(graph.getLabelVDefaultModel());
        graph.getLabelsV().add(labelV1);

        labelE1 = (LabelE) graphElementFactory.create(GraphElementType.LABEL_EDGE, graph);
        labelE1.setOwner(edge1);
        edge1.setLabel(labelE1);
//        labelE1.setModel(graph.getLabelEDefaultModel());
        graph.getLabelsE().add(labelE1);

        labelE2 = (LabelE) graphElementFactory.create(GraphElementType.LABEL_EDGE, graph);
        labelE2.setOwner(edge2);
        edge2.setLabel(labelE2);
//        labelE2.setModel(graph.getLabelEDefaultModel());
        graph.getLabelsE().add(labelE2);

        labelE3 = (LabelE) graphElementFactory.create(GraphElementType.LABEL_EDGE, graph);
        labelE3.setOwner(edge3);
        edge3.setLabel(labelE3);
//        labelE3.setModel(graph.getLabelEDefaultModel());
        graph.getLabelsE().add(labelE3);

        setTemplateModelsToAllGraphElements();
    }

    private void initializeFrame() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        tabbedPane.setBounds(0, 0, 752, 427);
        getContentPane().add(tabbedPane);

        label_hint = new JLabel(StringLiterals.LABEL_TEMPLATE_EDITOR_HEADER);
        label_hint.setHorizontalAlignment(SwingConstants.CENTER);
        label_hint.setBounds(222, 10, 524, 20);
        panel_preview = new PanelPreview(graph);
        panel_preview.setBounds(222, 42, 520, 343);

        panel_propertyEditor = new PanelPropertyEditor(generalController, modeController, null, null) {
            public void valueChanged(PropertyModel model) {
                updateModel(model, modeController.getMode().getRelatedElementType());
                panel_preview.repaint();
            }
        };
        panel_propertyEditor.setBounds(10, 10, 200, 380);
        panel_propertyEditor.setBorder(UIManager.getBorder("TitledBorder.border"));
        panel_propertyEditor.setEnabled(true);
        panel_propertyEditor.disableLabelEdition();
        panel_propertyEditor.setModel(currentModels.get(GraphElementType.VERTEX));

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

        button_discard = new JButton(StringLiterals.BUTTON_TEMPLATE_EDITOR_DISCARD);
        button_discard.setToolTipText(StringLiterals.TOOLTIP_TEMPLATE_EDITOR_DISCARD);
        button_discard.setBounds(656, 433, 90, 34);
        getContentPane().add(button_discard);

        button_save = new JButton(StringLiterals.BUTTON_TEMPLATE_EDITOR_SAVE);
        button_save.setToolTipText(StringLiterals.TOOLTIP_TEMPLATE_EDITOR_SAVE);
        button_save.setBounds(554, 433, 90, 34);
        getContentPane().add(button_save);

        button_restoreDefaultSettings = new JButton(StringLiterals.BUTTON_TEMPLATE_EDITOR_RESTORE_DEFAULT);
        button_restoreDefaultSettings.setToolTipText(StringLiterals.TOOLTIP_TEMPLATE_EDITOR_RESTORE_DEFAULT);
        button_restoreDefaultSettings.setBounds(6, 433, 170, 34);
        getContentPane().add(button_restoreDefaultSettings);

        checkBox_applyToAll = new JCheckBox(StringLiterals.CHECKBOX_TEMPLATE_EDITOR_GLOBAL_APPLY);
        checkBox_applyToAll.setBounds(393, 433, 149, 34);
        checkBox_applyToAll.setToolTipText(StringLiterals.TOOLTIP_TEMPLATE_EDITOR_GLOBAL_APPLY);
        getContentPane().add(checkBox_applyToAll);
    }

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        panel_propertyEditor.setModel(currentModels.get(currentMode.getRelatedElementType()));
        /*switch(currentMode)
        {
            case VERTEX:
                panel_propertyEditor.setModel(graph.getVertexDefaultModel());
                break;
            case EDGE:
                panel_propertyEditor.setModel(graph.getEdgeDefaultModel());
                break;
            case LABEL_VERTEX:
                panel_propertyEditor.setModel(graph.getLabelVDefaultModel());
                break;
            case LABEL_EDGE:
                panel_propertyEditor.setModel(graph.getLabelEDefaultModel());
                break;
        }                */
    }

    @Override
    public int modeUpdatePriority() {
        return 10;
    }
}
