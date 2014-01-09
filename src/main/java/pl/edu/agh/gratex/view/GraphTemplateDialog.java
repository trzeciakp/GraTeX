package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.PropertyModelFactory;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundaryUtils;
import pl.edu.agh.gratex.model.properties.LabelPosition;
import pl.edu.agh.gratex.model.vertex.Vertex;
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
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class GraphTemplateDialog extends JDialog implements ModeListener {
    private GeneralController generalController;
    private GraphElementFactory graphElementFactory;

    // This dialog needs its own ModeController, so that it does not change mode globally
    private ModeController modeController;

    private Graph graph;
    private boolean result;

    private JButton button_restoreDefaultSettings;
    private JCheckBox checkBox_applyToAll;
    private JButton button_save;
    private JButton button_discard;

    private JTabbedPane tabbedPane;
    private PanelPropertyEditor panel_propertyEditor;
    private PanelPreview panel_preview;
    private JLabel label_hint;

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
        modeController = new ModeControllerImpl();
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
        PropertyModel currentModel = currentModels.get(modelType);
        currentModel.updateWithModel(model);
        //to refresh fields which should be enabled/disabled
        panel_propertyEditor.setModel(currentModel);
    }

    private void setDefaultModelsToAllGraphElements() {
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
                setDefaultModelsToAllGraphElements();
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
        graph.setGridOn(false);

        Vertex vertex1 = (Vertex) graphElementFactory.create(GraphElementType.VERTEX, graph);
        vertex1.setNumber(1);
        VertexUtils.setPartOfNumeration(vertex1, true);
        vertex1.setPosX(120);
        vertex1.setPosY(255);
        graph.addElement(vertex1);

        Vertex vertex2 = (Vertex) graphElementFactory.create(GraphElementType.VERTEX, graph);
        vertex2.setNumber(2);
        VertexUtils.setPartOfNumeration(vertex2, true);
        vertex2.setPosX(400);
        vertex2.setPosY(255);
        graph.addElement(vertex2);

        Vertex vertex3 = (Vertex) graphElementFactory.create(GraphElementType.VERTEX, graph);
        vertex3.setNumber(3);
        VertexUtils.setPartOfNumeration(vertex3, true);
        vertex3.setPosX(260);
        vertex3.setPosY(90);
        graph.addElement(vertex3);

        Edge edge1 = (Edge) graphElementFactory.create(GraphElementType.EDGE, graph);
        edge1.setVertexA(vertex2);
        edge1.setVertexB(vertex3);
        edge1.setRelativeEdgeAngle(330);
        graph.addElement(edge1);

        Edge edge2 = (Edge) graphElementFactory.create(GraphElementType.EDGE, graph);
        edge2.setVertexA(vertex1);
        edge2.setVertexB(vertex1);
        edge2.setRelativeEdgeAngle(90);
        graph.addElement(edge2);

        Edge edge3 = (Edge) graphElementFactory.create(GraphElementType.EDGE, graph);
        edge3.setVertexA(vertex1);
        edge3.setVertexB(vertex2);
        edge3.setRelativeEdgeAngle(0);
        graph.addElement(edge3);

        LabelV labelV1 = (LabelV) graphElementFactory.create(GraphElementType.LABEL_VERTEX, graph);
        labelV1.setOwner(vertex2);
        labelV1.setLabelPosition(LabelPosition.SE);
        vertex1.setLabel(labelV1);
        graph.addElement(labelV1);

        LabelE labelE1 = (LabelE) graphElementFactory.create(GraphElementType.LABEL_EDGE, graph);
        labelE1.setOwner(edge1);
        labelE1.setPosition(35);
        edge1.setLabel(labelE1);
        graph.addElement(labelE1);

        LabelE labelE2 = (LabelE) graphElementFactory.create(GraphElementType.LABEL_EDGE, graph);
        labelE2.setOwner(edge2);
        edge2.setLabel(labelE2);
        graph.addElement(labelE2);

        LabelE labelE3 = (LabelE) graphElementFactory.create(GraphElementType.LABEL_EDGE, graph);
        labelE3.setOwner(edge3);
        labelE3.setPosition(35);
        edge3.setLabel(labelE3);
        graph.addElement(labelE3);

        Hyperedge hyperedge = (Hyperedge) graphElementFactory.create(GraphElementType.HYPEREDGE, graph);
        List<Vertex> connectedVertices = new LinkedList<>();
        connectedVertices.add(vertex1);
        connectedVertices.add(vertex2);
        connectedVertices.add(vertex3);
        hyperedge.setConnectedVertices(connectedVertices);
        hyperedge.autoCenterJoint();
        graph.addElement(hyperedge);

                Boundary boundary1 = (Boundary) graphElementFactory.create(GraphElementType.BOUNDARY, graph);
        boundary1.setTopLeftX(40);
        boundary1.setTopLeftY(140);
        boundary1.setWidth(440);
        boundary1.setHeight(173);
        graph.addElement(boundary1);

        Boundary boundary2 = (Boundary) graphElementFactory.create(GraphElementType.BOUNDARY, graph);
        boundary2.setTopLeftX(40);
        boundary2.setTopLeftY(30);
        boundary2.setWidth(140);
        boundary2.setHeight(60);
        graph.addElement(boundary2);

        System.out.println(LinkBoundaryUtils.getExitPointFromBoundary(boundary2, 0));
        System.out.println(LinkBoundaryUtils.getExitPointFromBoundary(boundary2, 90));
        System.out.println(LinkBoundaryUtils.getExitPointFromBoundary(boundary2, 180));
        System.out.println(LinkBoundaryUtils.getExitPointFromBoundary(boundary2, 270));

        LinkBoundary linkBoundary = (LinkBoundary) graphElementFactory.create(GraphElementType.LINK_BOUNDARY, graph);
        linkBoundary.setBoundaryA(boundary1);
        linkBoundary.setOutAngle(305);
        linkBoundary.setBoundaryB(boundary2);
        linkBoundary.setInAngle(180);
        graph.addElement(linkBoundary);

        for (GraphElement graphElement : graph.getAllElements()) {
            graphElement.setDummy(false);
        }
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
        panel_propertyEditor.disableUnnecessaryFields();
        panel_propertyEditor.setModel(currentModels.get(GraphElementType.VERTEX));

        for (ModeType modeType : ModeType.values()) {
            JPanel panel = new JPanel();
            panel.setLayout(null);
            tabbedPane.addTab(modeType.toString(), null, panel, null);
        }

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
        checkBox_applyToAll.setBounds(363, 433, 179, 34);
        checkBox_applyToAll.setToolTipText(StringLiterals.TOOLTIP_TEMPLATE_EDITOR_GLOBAL_APPLY);
        getContentPane().add(checkBox_applyToAll);

        ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_preview);
        ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(label_hint);
        ((JPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).add(panel_propertyEditor);
        modeController.setMode(ModeType.values()[tabbedPane.getSelectedIndex()]);
    }

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        panel_propertyEditor.setModel(currentModels.get(currentMode.getRelatedElementType()));
    }

    @Override
    public int modeUpdatePriority() {
        return 10;
    }
}
