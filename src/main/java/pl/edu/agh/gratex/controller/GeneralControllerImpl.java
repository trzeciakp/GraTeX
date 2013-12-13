package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.editor.OperationList;
import pl.edu.agh.gratex.editor.RemoveOperation;
import pl.edu.agh.gratex.editor.TemplateChangeOperation;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.parser.Parser;
import pl.edu.agh.gratex.utils.FileManager;
import pl.edu.agh.gratex.view.*;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;

public class GeneralControllerImpl implements GeneralController, ToolListener, ModeListener, Serializable {
    private MainWindow mainWindow;
    private ModeController modeController;
    private ToolController toolController;
    private SelectionController selectionController;
    private MouseController mouseController;

    private Graph graph;
    private File currentFile;

    private ModeType mode = ModeType.VERTEX;
    private ToolType tool = ToolType.ADD;

    public GeneralControllerImpl(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        modeController = new ModeControllerTmpImpl(this);
        toolController = new ToolControllerImpl(this);
        mouseController = new MouseControllerImpl(this, modeController, toolController);
        selectionController = new SelectionControllerImpl(this, modeController, toolController);

        modeController.addModeListener(this);
        toolController.addToolListener(this);

        graph = new Graph(this);
        currentFile = null;
    }

    //===========================================
    // Listeners implementation

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

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        mode = currentMode;
        resetWorkspace();
    }

    @Override
    public int modeUpdatePriority() {
        return 10;
    }

    @Override
    public void toolChanged(ToolType previousToolType, ToolType currentToolType) {
        tool = currentToolType;
        resetWorkspace();
    }

    @Override
    public int toolUpdatePriority() {
        return 10;
    }

//===========================================
    // GeneralController interface implementation

    @Override
    public ModeType getMode() {
        return mode;
    }

    @Override
    public ToolType getTool() {
        return tool;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void resetGraph() {
        graph = new Graph(this);
    }

    @Override
    public void newGraphFile() {
        if (checkForUnsavedProgress()) {
            currentFile = null;
            resetGraph();
            ControlManager.operations = new OperationList(this);
            resetWorkspace();
            editGraphTemplate();
        }
    }

    @Override
    public void openGraphFile() {
        if (checkForUnsavedProgress()) {
            OpenFileDialog chooser;
            if (currentFile != null) {
                chooser = new OpenFileDialog(currentFile);
            } else {
                chooser = new OpenFileDialog();
            }
            File file = chooser.showDialog(mainWindow);
            if (file != null) {

                Graph newGraph;
                if ((newGraph = FileManager.openFile(file)) != null) {
                    currentFile = file;
                    GraphUtils.deleteUnusedLabels(newGraph);
                    graph = newGraph;
                    ControlManager.operations = new OperationList(this);
                    resetWorkspace();
                    publishInfo(StringLiterals.INFO_GRAPH_OPEN_OK);
                } else {
                    publishInfo(StringLiterals.INFO_GRAPH_OPEN_FAIL);
                    reportError(StringLiterals.MESSAGE_ERROR_OPEN_GRAPH, null);
                }
            }
        }
    }

    @Override
    public boolean saveGraphFile(boolean saveAs) {
        if (!saveAs && currentFile != null) {
            return FileManager.saveFile(graph, currentFile);
        } else {
            SaveFileDialog chooser;
            if (currentFile != null) {
                chooser = new SaveFileDialog(currentFile);
            } else {
                chooser = new SaveFileDialog();
            }
            File file = chooser.showDialog(mainWindow);

            if (file != null) {
                if (FileManager.saveFile(graph, file)) {
                    publishInfo(StringLiterals.INFO_GRAPH_SAVE_OK);
                    currentFile = file;
                    return true;
                } else {
                    publishInfo(StringLiterals.INFO_GRAPH_SAVE_FAIL);
                    reportError(StringLiterals.MESSAGE_ERROR_SAVE_GRAPH, null);
                }
            }
        }
        return false;
    }

    @Override
    public void editGraphTemplate() {
        GraphsTemplateDialog gdd = new GraphsTemplateDialog(mainWindow, this);
        Graph templateGraph = gdd.displayDialog();

        if (templateGraph != null) {
            ControlManager.operations.addNewOperation(new TemplateChangeOperation(this, graph, templateGraph));
            publishInfo(ControlManager.operations.redo());
        }
    }

    @Override
    public void copyToClipboard() {
        mouseController.copyToClipboard();
    }

    @Override
    public void pasteFromClipboard() {
        mouseController.pasteFromClipboard();
    }

    @Override
    public void undo() {
        publishInfo(ControlManager.operations.undo());
        mainWindow.updateWorkspace();
        selectionController.clearSelection();
        ControlManager.updatePropertyChangeOperationStatus(false);
    }

    @Override
    public void redo() {
        publishInfo(ControlManager.operations.redo());
        mainWindow.updateWorkspace();
        selectionController.clearSelection();
    }

    @Override
    public void toggleGrid() {
        if (graph.gridOn) {
            graph.gridOn = false;
        } else {
            GridDialog gd = new GridDialog(mainWindow, graph.gridResolutionX, graph.gridResolutionY);
            int[] result = gd.showDialog();
            if (result != null) {
                graph.gridOn = true;
                graph.gridResolutionX = result[0];
                graph.gridResolutionY = result[1];
                GraphUtils.adjustVerticesToGrid(graph);
            }
        }
        mainWindow.updateFunctions();
    }

    @Override
    public void setNumeration() {
        selectionController.clearSelection();
        ControlManager.updatePropertyChangeOperationStatus(false);
        NumerationDialog nd = new NumerationDialog(mainWindow, graph.getGraphNumeration().isNumerationDigital(),
                graph.getGraphNumeration().getStartingNumber(), Const.MAX_VERTEX_NUMBER);
        int[] result = nd.showDialog();

        if (result != null) {
            graph.getGraphNumeration().setNumerationDigital(result[0] == 0);
            graph.getGraphNumeration().setStartingNumber(result[1]);
        }

        mainWindow.updateFunctions();
    }

    @Override
    public void parseToTeX() {
        new LatexCodeDialog(mainWindow, Parser.parse(graph));
    }

    @Override
    public void selectAll() {
        selectionController.selectAll();
        ControlManager.updatePropertyChangeOperationStatus(true);
        mainWindow.updateWorkspace();
    }

    @Override
    public void exitApplication() {
        if (checkForUnsavedProgress()) {
            System.exit(0);
        }
    }

    @Override
    public void showAboutDialog() {
        new AboutDialog(mainWindow);
    }

    @Override
    public void deleteSelection() {
        if (selectionController.selectionSize() > 0) {
            ControlManager.operations.addNewOperation(new RemoveOperation(this, selectionController.getSelection()));
            publishInfo(ControlManager.operations.redo());
            mainWindow.updateFunctions();
        }
        selectionController.clearSelection();
        ControlManager.updatePropertyChangeOperationStatus(false);
    }

    @Override
    public void publishInfo(String entry) {
        mainWindow.publishInfo(entry);
    }

    @Override
    public void reportError(String message, Exception e) {
        String fullMessage = message + (e == null ? "" : "\n\n" + e.toString());
        JOptionPane.showMessageDialog(mainWindow, fullMessage, StringLiterals.TITLE_ERROR_DIALOG, JOptionPane.ERROR_MESSAGE);
    }

    //===========================================
    // Internal functions

    private boolean checkForUnsavedProgress() {
        // TODO this nie bedzie potrzebne, czytaj FileManager
        if (FileManager.contentChanged(this, graph, currentFile)) {
            Object[] options = {"Save", "Don't save", "Cancel"};
            System.out.println(" " + mainWindow);
            int option = JOptionPane.showOptionDialog(mainWindow, "There have been changes since last save.\n"
                    + "Would you like to save your graph now?", "Unsaved progress", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            if (option == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (option == JOptionPane.NO_OPTION) {
                return true;
            } else {
                return saveGraphFile(true);
            }
        } else {
            return true;
        }
    }

    public void resetWorkspace() {
        selectionController.clearSelection();
        ControlManager.updatePropertyChangeOperationStatus(false);
        mouseController.finishMovingElement();
        mouseController.resetCurrentOperation();
        mainWindow.updateFunctions();
    }

    @Override
    public void updateMenuBarAndActions() {
        mainWindow.updateMenuBarAndActions();
    }

    @Override
    public void giveFocusToLabelTextfield() {
        mainWindow.giveFocusToLabelTextfield();
    }

    @Override
    public void updateWorkspace() {
        mainWindow.updateWorkspace();
    }

    @Override
    public void criticalError(String message, Exception e) {
        reportError(StringLiterals.MESSAGE_ERROR_CRITICAL + message, e);
        System.exit(1);
    }
}
