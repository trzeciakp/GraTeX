package pl.edu.agh.gratex.gui;


import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.ModeController;
import pl.edu.agh.gratex.controller.ModeControllerImpl;
import pl.edu.agh.gratex.graph.*;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.property.PanelPropertyEditor;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GraphsTemplateDialog extends JDialog {
    private static final long serialVersionUID = 3521596195996666926L;

    private Graph graph;
    private Graph result = null;
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

    public GraphsTemplateDialog(MainWindow parent) {
        super(parent, StringLiterals.TITLE_GRAPH_TEMPLATE_EDITOR, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(768, 511);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        ModeController modeController = new ModeControllerImpl();
        initGraph();
        initializeFrame(modeController);
        initializeEvents(modeController);
    }

    public Graph displayDialog() {
        setVisible(true);
        return result;
    }

    private void updateModel(PropertyModel model) {
        if (model instanceof VertexPropertyModel) {
            ((VertexPropertyModel) model).number = -1;
            vertex1.setModel((VertexPropertyModel) model);
            graph.setVertexDefaultModel((VertexPropertyModel) vertex1.getModel());
            graph.getVertexDefaultModel().number = -1;
        } else if (model instanceof EdgePropertyModel) {
            ((EdgePropertyModel) model).relativeEdgeAngle = -1;
            edge1.setModel((EdgePropertyModel) model);
            edge2.setModel((EdgePropertyModel) model);
            edge3.setModel((EdgePropertyModel) model);
            graph.setEdgeDefaultModel((EdgePropertyModel) edge1.getModel());
            graph.getEdgeDefaultModel().relativeEdgeAngle = -1;
        } else if (model instanceof LabelVertexPropertyModel) {
            labelV1.setModel((LabelVertexPropertyModel) model);
            graph.setLabelVDefaultModel((LabelVertexPropertyModel) labelV1.getModel());
            graph.getLabelVDefaultModel().text = null;
        } else {
            labelE1.setModel((LabelEdgePropertyModel) model);
            labelE2.setModel((LabelEdgePropertyModel) model);
            labelE3.setModel((LabelEdgePropertyModel) model);
            graph.setLabelEDefaultModel((LabelEdgePropertyModel) labelE1.getModel());
            graph.getLabelEDefaultModel().text = null;
        }

        refreshModels();
    }

    private void refreshModels() {
        if (tabbedPane.getSelectedIndex() == 0) {
            panel_propertyEditor.setModel(graph.getVertexDefaultModel());
        } else if (tabbedPane.getSelectedIndex() == 1) {
            panel_propertyEditor.setModel(graph.getEdgeDefaultModel());
        } else if (tabbedPane.getSelectedIndex() == 2) {
            panel_propertyEditor.setModel(graph.getLabelVDefaultModel());
        } else if (tabbedPane.getSelectedIndex() == 3) {
            panel_propertyEditor.setModel(graph.getLabelEDefaultModel());
        }
    }

    private void initializeEvents(final ModeController modeController) {
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_preview);
                ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(label_hint);
                ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_propertyEditor);

                //panel_propertyEditor.setMode(tabbedPane.getSelectedIndex() + 1);
                //TODO
                modeController.setMode(ModeType.values()[tabbedPane.getSelectedIndex()]);
                refreshModels();
            }
        });

        button_restoreDefaultSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graph.initDefaultModels();
                graph.getEdgeDefaultModel().isLoop = -1;
                graph.getEdgeDefaultModel().relativeEdgeAngle = -1;
                graph.getLabelVDefaultModel().text = null;
                graph.getLabelEDefaultModel().text = null;
                vertex1.setModel(graph.getVertexDefaultModel());
                edge1.setModel(graph.getEdgeDefaultModel());
                edge1.setRelativeEdgeAngle(300);
                edge2.setModel(graph.getEdgeDefaultModel());
                edge2.setRelativeEdgeAngle(180);
                edge3.setModel(graph.getEdgeDefaultModel());
                edge3.setRelativeEdgeAngle(0);
                labelV1.setModel(graph.getLabelVDefaultModel());
                labelE1.setModel(graph.getLabelEDefaultModel());
                labelE2.setModel(graph.getLabelEDefaultModel());

                panel_preview.repaint();
            }
        });

        button_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox_applyToAll.isSelected()) {
                    if (0 != JOptionPane.showConfirmDialog(null,
                            StringLiterals.MESSAGE_CONFIRM_GLOBAL_APPLY, StringLiterals.TITLE_CONFIRM_DIALOG,
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
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

        button_discard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                result = null;
                setVisible(false);
                dispose();
            }
        });
    }

    private void initGraph() {
        graph = new Graph();
        graph.gridOn = false;

        graph.setVertexDefaultModel(new VertexPropertyModel(ControlManager.graph.getVertexDefaultModel()));
        graph.setEdgeDefaultModel(new EdgePropertyModel(ControlManager.graph.getEdgeDefaultModel()));
        graph.getEdgeDefaultModel().isLoop = -1;
        graph.getEdgeDefaultModel().relativeEdgeAngle = -1;
        graph.setLabelVDefaultModel(new LabelVertexPropertyModel(ControlManager.graph.getLabelVDefaultModel()));
        graph.getLabelVDefaultModel().text = null;
        graph.setLabelEDefaultModel(new LabelEdgePropertyModel(ControlManager.graph.getLabelEDefaultModel()));
        graph.getLabelEDefaultModel().text = null;

        vertex1 = new Vertex();
        vertex1.setModel(graph.getVertexDefaultModel());
        vertex1.updateNumber(1);
        vertex1.setPosX(240);
        vertex1.setPosY(190);
        graph.getVertices().add(vertex1);

        vertex2 = new Vertex();
        vertex2.setModel(graph.getVertexDefaultModel());
        vertex2.updateNumber(2);
        vertex2.setType(1);
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

        edge1 = new Edge();
        edge1.setModel(graph.getEdgeDefaultModel());
        edge1.setVertexA(vertex2);
        edge1.setVertexB(vertex1);
        edge1.setRelativeEdgeAngle(300);
        graph.getEdges().add(edge1);

        edge2 = new Edge();
        edge2.setModel(graph.getEdgeDefaultModel());
        edge2.setVertexA(vertex1);
        edge2.setVertexB(vertex1);
        edge2.setRelativeEdgeAngle(180);
        graph.getEdges().add(edge2);

        edge3 = new Edge();
        edge3.setModel(graph.getEdgeDefaultModel());
        edge3.setVertexA(vertex1);
        edge3.setVertexB(vertex2);
        edge3.setRelativeEdgeAngle(0);
        graph.getEdges().add(edge3);

        labelV1 = new LabelV(vertex1);
        vertex1.setLabel(labelV1);
        labelV1.setModel(graph.getLabelVDefaultModel());
        graph.getLabelsV().add(labelV1);

        labelE1 = new LabelE(edge1);
        edge1.setLabel(labelE1);
        labelE1.setModel(graph.getLabelEDefaultModel());
        graph.getLabelsE().add(labelE1);

        labelE2 = new LabelE(edge2);
        edge2.setLabel(labelE2);
        labelE2.setModel(graph.getLabelEDefaultModel());
        graph.getLabelsE().add(labelE2);

        labelE3 = new LabelE(edge3);
        edge3.setLabel(labelE3);
        labelE3.setModel(graph.getLabelEDefaultModel());
        graph.getLabelsE().add(labelE3);
    }

    private void initializeFrame(ModeController modeController) {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
        tabbedPane.setBounds(0, 0, 752, 427);
        getContentPane().add(tabbedPane);

        label_hint = new JLabel(StringLiterals.LABEL_TEMPLATE_EDITOR_HEADER);
        label_hint.setHorizontalAlignment(SwingConstants.CENTER);
        label_hint.setBounds(222, 10, 524, 20);
        panel_preview = new PanelPreview(graph);
        panel_preview.setBounds(222, 42, 520, 343);

        panel_propertyEditor = new PanelPropertyEditor(modeController) {
            private static final long serialVersionUID = 4008480073440306159L;

            public void valueChanged(PropertyModel model) {
                updateModel(model);
                panel_preview.repaint();
            }
        };
        panel_propertyEditor.setBounds(10, 10, 200, 380);
        panel_propertyEditor.setBorder(UIManager.getBorder("TitledBorder.border"));
        panel_propertyEditor.setEnabled(true);
        panel_propertyEditor.disableLabelEdition();
        //panel_propertyEditor.setMode(1);
        panel_propertyEditor.setModel(graph.getVertexDefaultModel());

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
}
