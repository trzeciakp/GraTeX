package pl.edu.agh.gratex.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.GeneralControllerImpl;
import pl.edu.agh.gratex.view.propertyPanel.PanelPropertyEditor;
import javax.swing.border.LineBorder;
import java.awt.Color;

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

        generalController = new GeneralControllerImpl(this);

        try {
            URL url = this.getClass().getClassLoader().getResource("images/icon.png");
            setIconImage(ImageIO.read(url));
        } catch (Exception e) {
            generalController.criticalError(StringLiterals.MESSAGE_ERROR_GET_RESOURCE, e);
        }

        initializeFrame();
        initializeEvents();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyHandler(generalController));

        generalController.getModeController().setMode(ModeType.VERTEX);
        generalController.getToolController().setTool(ToolType.ADD);
        generalController.newGraphFile();
    }

    // TODO Trzeba zmienic layout Mainwindow na jakis typu border (on akurat chyba sie nada idealnie) i to pojdzie do piachu
    public void adjustSize() {
        int width = getContentPane().getWidth();
        int height = getContentPane().getHeight();

        panel_propertyEditor.setLocation(width - 208, 87);

        scrollPane_workspace.setSize(width - 328, height - 122);
        scrollPane_workspace.setViewportView(panel_workspace);

        panel_buttonContainer.setSize(width + 10, 50);

        label_infoDisplay.setBounds(10, height - 36, width - 20, 36);

        menuBar.setSize(width, 25);
    }

    private void initializeEvents() {
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent arg0) {
//                adjustSize();
//            }
//        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                generalController.exitApplication();
            }
        });
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
        label_infoDisplay.setPreferredSize(new Dimension(0, 36));
        label_infoDisplay.setBorder(new EmptyBorder(0, 10, 0, 10));
        getContentPane().add(label_infoDisplay, BorderLayout.SOUTH);

        panel_propertyEditor = new PanelPropertyEditor(generalController, generalController.getModeController(), generalController.getOperationController(), generalController.getSelectionController());
        panel_propertyEditor.setPreferredSize(new Dimension(200, 0));
        panel_propertyEditor.setBorder(UIManager.getBorder("TitledBorder.border"));
        getContentPane().add(panel_propertyEditor, BorderLayout.EAST);

        panel_buttonContainer = new PanelButtonContainer(generalController, generalController.getSelectionController());
        panel_buttonContainer.setPreferredSize(new Dimension(0, 50));
        panel_buttonContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        getContentPane().add(panel_buttonContainer, BorderLayout.NORTH);

        panel_toolbox = new PanelToolbox(generalController.getToolController(), generalController.getModeController());
        panel_toolbox.setBorder(null);
        panel_toolbox.setPreferredSize(new Dimension(100, 0));
        getContentPane().add(panel_toolbox, BorderLayout.WEST);

        menuBar = new MenuBar(generalController, generalController.getModeController(), generalController.getToolController(), generalController.getSelectionController());
        setJMenuBar(menuBar);
    }
}
