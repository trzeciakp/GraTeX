package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.property.PanelPropertyEditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = -7320722131326919230L;

    private PanelToolbox panel_toolbox;
    private JScrollPane scrollPane_workspace;
    private PanelWorkspace panel_workspace;
    public PanelPropertyEditor panel_propertyEditor;
    public PanelButtonContainer panel_buttonContainer;

    public MenuBar menuBar;
    public JLabel label_info;

    public MainWindow() throws Exception {
        super("GraTeX - graph code generator for LaTeX (TikZ)");
        URL url = this.getClass().getClassLoader().getResource("images/icon.png");
        setIconImage(ImageIO.read(url));

        ControlManager.passWindowHandle(this);
        initializeFrame(new GeneralControllerTmpImpl(), new ToolControllerImpl(), new ModeControllerImpl(), ControlManager.graph.pageWidth, ControlManager.graph.pageHeight);
        initializeEvents();
        updateFunctions();
        ControlManager.newGraphFile();
    }

    public void updateWorkspace() {
        //panel_workspace.updateCursor();
        panel_workspace.repaint();
    }

    public void updateFunctions() {
        panel_toolbox.repaint();
        updateWorkspace();

        String tipPart1 = null;
        String tipPart2 = null;
        String tipPart3 = null;
        String tipPart4a = null;
        String tipPart4b = null;

        switch (ControlManager.mode) {
            case 1: {
                tipPart1 = "VERTEX mode - ";
                tipPart4a = "a vertex. ";
                tipPart4b = "a vertex. ";
                break;
            }
            case 2: {
                tipPart1 = "EDGE mode - ";
                tipPart4a = "an edge. Hold down SHIFT for directed edge.";
                tipPart4b = "an edge. ";
                break;
            }
            case 3: {
                tipPart1 = "LABEL (vertex) mode - ";
                tipPart4a = "a label to a vertex. ";
                tipPart4b = "a label of a vertex. ";
                break;
            }
            case 4: {
                tipPart1 = "LABEL (edge) mode - ";
                tipPart4a = "a label to an edge. Hold down SHIFT for horizontal label.";
                tipPart4b = "a label of an edge. ";
                break;
            }
        }
        switch (ControlManager.tool) {
            case 1: {
                tipPart2 = "ADD tool. ";
                tipPart3 = "Left-click to add " + tipPart4a;
                break;
            }
            case 2: {
                tipPart2 = "REMOVE tool. ";
                tipPart3 = "Left-click to remove " + tipPart4b + "Click and drag to remove all items in the area.";
                break;
            }
            case 3: {
                tipPart2 = "SELECT tool. ";
                tipPart3 = "Left-click to select " + tipPart4b
                        + "Click and drag to select all items in the area. CTRL + click/drag to extend selection.";
                break;
            }
        }

        label_info.setText(tipPart1 + tipPart2 + tipPart3);
    }

    public void adjustSize() {
        int width = getContentPane().getWidth();
        int height = getContentPane().getHeight();

        panel_propertyEditor.setLocation(width - 208, 87);

        scrollPane_workspace.setSize(width - 328, height - 122);
        scrollPane_workspace.setViewportView(panel_workspace);

        panel_buttonContainer.setSize(width + 10, 50);

        label_info.setBounds(10, height - 36, width - 20, 36);

        menuBar.setSize(width, 25);
    }

    private void initializeEvents() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyHandler());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                adjustSize();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                ControlManager.exitApplication();
            }
        });
    }

    private void initializeFrame(GeneralController generalController, ToolController toolController, ModeController modeController, int workspacePageWidth, int workspacePageHeight) {
        setMinimumSize(new Dimension(800, 500));
        setSize(1038, 768);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        getContentPane().setLayout(null);

        scrollPane_workspace = new JScrollPane();
        scrollPane_workspace.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        panel_workspace = new PanelWorkspace(scrollPane_workspace, toolController, workspacePageWidth, workspacePageHeight);
        scrollPane_workspace.setViewportView(panel_workspace);
        panel_workspace.setLayout(null);
        scrollPane_workspace.setBounds(110, 85, 472, 344);
        getContentPane().add(scrollPane_workspace);

        panel_propertyEditor = new PanelPropertyEditor();
        panel_propertyEditor.setBounds(592, 85, 200, 380);
        panel_propertyEditor.setBorder(UIManager.getBorder("TitledBorder.border"));
        getContentPane().add(panel_propertyEditor);

        panel_buttonContainer = new PanelButtonContainer(generalController);
        panel_buttonContainer.setBounds(-5, 25, 802, 50);
        panel_buttonContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        getContentPane().add(panel_buttonContainer);

        label_info = new JLabel("");
        label_info.setBounds(10, 430, 774, 36);
        getContentPane().add(label_info);

        panel_toolbox = new PanelToolbox(toolController, modeController);
        panel_toolbox.setBounds(10, 85, 90, 344);
        getContentPane().add(panel_toolbox);

        menuBar = new MenuBar(generalController, modeController, toolController);
        menuBar.setBounds(0, 0, 0, 25);
        getContentPane().add(menuBar);
    }
}
