package pl.edu.agh.gratex.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.operation.OperationController;
import pl.edu.agh.gratex.controller.operation.OperationControllerImpl;
import pl.edu.agh.gratex.view.propertyPanel.PanelPropertyEditor;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

    private PanelToolbox panel_toolbox;
    private JScrollPane scrollPane_workspace;
    private PanelWorkspace panel_workspace;
    private PanelPropertyEditor panel_propertyEditor;
    private PanelButtonContainer panel_buttonContainer;

    private GeneralController generalController;

    private MenuBar menuBar;
    private InfoDisplay label_infoDisplay;

    public MainWindow() {
        super(StringLiterals.TITLE_MAIN_WINDOW);

        ModeController modeController1 = new ModeControllerImpl();
        ToolController toolController1 = new ToolControllerImpl();
        OperationController operationController = new OperationControllerImpl();
        SelectionController selectionController = new SelectionControllerImpl(modeController1, toolController1, operationController);
        generalController = new GeneralControllerImpl(this, modeController1, toolController1, operationController, selectionController);

        setIconImage(Application.loadImage("icon.png"));

        initializeFrame();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyHandler(generalController));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                generalController.exitApplication();
            }
        });

        generalController.getModeController().setMode(ModeType.VERTEX);
        generalController.getToolController().setTool(ToolType.ADD);
        generalController.newGraphFile();
    }

    private void initializeFrame() {
        setMinimumSize(new Dimension(Const.MAIN_WINDOW_MIN_WIDTH, Const.MAIN_WINDOW_MIN_HEIGHT));
        setSize(Const.MAIN_WINDOW_DEFAULT_WIDTH, Const.MAIN_WINDOW_DEFAULT_HEIGHT);
        setPreferredSize(new Dimension(Const.MAIN_WINDOW_DEFAULT_WIDTH, Const.MAIN_WINDOW_DEFAULT_HEIGHT));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        scrollPane_workspace = new JScrollPane();
        scrollPane_workspace.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        panel_workspace = new PanelWorkspace(scrollPane_workspace, generalController);
        scrollPane_workspace.setViewportView(panel_workspace);
        panel_workspace.setLayout(null);
        getContentPane().add(scrollPane_workspace, BorderLayout.CENTER);

        label_infoDisplay = new InfoDisplay(generalController);
        label_infoDisplay.setPreferredSize(new Dimension(0, 30));
        label_infoDisplay.setBorder(new EmptyBorder(0, 10, 0, 10));
        getContentPane().add(label_infoDisplay, BorderLayout.SOUTH);

        panel_propertyEditor = new PanelPropertyEditor(generalController, generalController.getModeController(), generalController.getOperationController(), generalController.getSelectionController());
        panel_propertyEditor.setPreferredSize(new Dimension(220, 0));
        getContentPane().add(panel_propertyEditor, BorderLayout.EAST);

        panel_buttonContainer = new PanelButtonContainer(generalController, generalController.getSelectionController());
        panel_buttonContainer.setPreferredSize(new Dimension(0, 50));
        panel_buttonContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        getContentPane().add(panel_buttonContainer, BorderLayout.NORTH);

        panel_toolbox = new PanelToolbox(generalController.getToolController(), generalController.getModeController());
        panel_toolbox.setBorder(null);
        panel_toolbox.setPreferredSize(new Dimension(120, 0));
        getContentPane().add(panel_toolbox, BorderLayout.WEST);

        menuBar = new MenuBar(generalController, generalController.getModeController(), generalController.getToolController(), generalController.getSelectionController());
        setJMenuBar(menuBar);

        setVisible(true);
    }
}
