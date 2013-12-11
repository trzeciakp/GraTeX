package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.view.propertyPanel.PanelPropertyEditor;

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

    private GeneralController generalController;
    private ModeController modeController;
    private ToolController toolController;
    private SelectionController selectionController;
    private MouseController mouseController;

    public MenuBar menuBar;
    public JLabel label_info;

    public MainWindow() throws Exception {
        super(StringLiterals.TITLE_MAIN_WINDOW);
        URL url = this.getClass().getClassLoader().getResource("images/icon.png");
        setIconImage(ImageIO.read(url));

        ControlManager.passWindowHandle(this);

        modeController = new ModeControllerTmpImpl();
        toolController = new ToolControllerImpl();
        mouseController = new MouseControllerImpl(this, modeController, toolController);
        selectionController = new SelectionControllerImpl(modeController, toolController);
        generalController = new GeneralControllerImpl(this, mouseController, selectionController, modeController, toolController);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyHandler(mouseController));

        initializeFrame();
        initializeEvents();

        modeController.setMode(ModeType.VERTEX);
        toolController.setTool(ToolType.ADD);
        generalController.newGraphFile();

        updateFunctions();
    }

    public void updateWorkspace() {
        //panel_workspace.updateCursor();
        panel_workspace.repaint();
    }

    public void updateFunctions() {
        panel_toolbox.repaint();
        updateWorkspace();
        label_info.setText(StringLiterals.INFO_MODE_AND_TOOL(generalController.getMode(), generalController.getTool()));
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
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                adjustSize();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                generalController.exitApplication();
            }
        });
    }

    private void initializeFrame() {
        setMinimumSize(new Dimension(800, 500));
        setSize(1038, 768);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        getContentPane().setLayout(null);

        scrollPane_workspace = new JScrollPane();
        scrollPane_workspace.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        panel_workspace = new PanelWorkspace(scrollPane_workspace, toolController, mouseController);
        scrollPane_workspace.setViewportView(panel_workspace);
        panel_workspace.setLayout(null);
        scrollPane_workspace.setBounds(110, 85, 472, 344);
        getContentPane().add(scrollPane_workspace);

        panel_propertyEditor = new PanelPropertyEditor(modeController);
        panel_propertyEditor.setBounds(592, 85, 200, 380);
        panel_propertyEditor.setBorder(UIManager.getBorder("TitledBorder.border"));
        getContentPane().add(panel_propertyEditor);

        panel_buttonContainer = new PanelButtonContainer(generalController, mouseController);
        panel_buttonContainer.setBounds(-5, 25, 802, 50);
        panel_buttonContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        getContentPane().add(panel_buttonContainer);

        label_info = new JLabel("");
        label_info.setBounds(10, 430, 774, 36);
        getContentPane().add(label_info);

        panel_toolbox = new PanelToolbox(toolController, modeController);
        panel_toolbox.setBounds(10, 85, 90, 344);
        getContentPane().add(panel_toolbox);

        menuBar = new MenuBar(generalController, mouseController, modeController, toolController);
        menuBar.setBounds(0, 0, 0, 25);
        getContentPane().add(menuBar);
    }

    public GeneralController getGeneralController() {
        return generalController;
    }

    public ModeController getModeController() {
        return modeController;
    }

    public ToolController getToolController() {
        return toolController;
    }

    public SelectionController getSelectionController() {
        return selectionController;
    }

    public MouseController getMouseController() {
        return mouseController;
    }
}
