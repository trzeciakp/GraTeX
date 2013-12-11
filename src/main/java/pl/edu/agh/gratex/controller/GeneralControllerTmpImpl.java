package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.editor.OperationList;
import pl.edu.agh.gratex.editor.TemplateChangeOperation;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.utils.FileManager;
import pl.edu.agh.gratex.view.*;

import javax.swing.*;
import java.io.File;

public class GeneralControllerTmpImpl implements GeneralController, ToolListener, ModeListener {
    private MainWindow mainWindow;
    private Graph graph;
    private File currentFile;
    private ModeType mode;
    private ToolType tool;

    public GeneralControllerTmpImpl(MainWindow mainWindow, ModeController modeController, ToolController toolController) {
        this.mainWindow = mainWindow;
        graph = new Graph();
        currentFile = null;
        modeController.addModeListener(this);
        toolController.addToolListener(this);
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    //===========================================
    // Listeners implementation

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        mode = currentMode;
        ControlManager.applyChange();
    }

    @Override
    public int modeUpdatePriority() {
        return 10;
    }

    @Override
    public void toolChanged(ToolType previousToolType, ToolType currentToolType) {
        tool = currentToolType;
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
        graph = new Graph();
    }

    @Override
    public void newGraphFile() {
        if (checkForUnsavedProgress()) {
            currentFile = null;
            resetGraph();
            ControlManager.operations = new OperationList();
            ControlManager.applyChange();
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
                    ControlManager.operations = new OperationList();
                    ControlManager.applyChange();
                    publishInfo(StringLiterals.INFO_GRAPH_OPEN_OK);
                } else {
                    publishInfo(StringLiterals.INFO_GRAPH_OPEN_FAIL);
                    reportError(StringLiterals.MESSAGE_ERROR_OPEN_GRAPH);
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
                    reportError(StringLiterals.MESSAGE_ERROR_SAVE_GRAPH);
                }
            }
        }
        return false;
    }

    @Override
    public void editGraphTemplate() {
        GraphsTemplateDialog gdd = new GraphsTemplateDialog(mainWindow);
        Graph templateGraph = gdd.displayDialog();

        if (templateGraph != null) {
            ControlManager.operations.addNewOperation(new TemplateChangeOperation(mainWindow.getGeneralController().getGraph(), templateGraph));
            mainWindow.getGeneralController().publishInfo(ControlManager.operations.redo());
        }
    }

    @Override
    public void copyToClipboard() {
        ControlManager.copyToClipboard();
    }

    @Override
    public void pasteFromClipboard() {
        ControlManager.pasteFromClipboard();
    }

    @Override
    public void undo() {
        ControlManager.undo();
    }

    @Override
    public void redo() {
        ControlManager.redo();
    }

    @Override
    public void toggleGrid() {
        ControlManager.toggleGrid();
    }

    @Override
    public void setNumeration() {
        ControlManager.setNumeration();
    }

    @Override
    public void parseToTeX() {
        ControlManager.parseToTeX();
    }

    @Override
    public void selectAll() {
        ControlManager.selectAll();
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
        ControlManager.deleteSelection();
    }

    @Override
    public void publishInfo(String entry) {
        mainWindow.label_info.setText(entry);
    }

    @Override
    public void reportError(String message) {
        JOptionPane.showMessageDialog(mainWindow, message, StringLiterals.TITLE_ERROR_DIALOG, JOptionPane.ERROR_MESSAGE);
    }

    //===========================================
    // Internal functions

    private boolean checkForUnsavedProgress() {
        if (FileManager.contentChanged(graph, currentFile)) {
            Object[] options = {"Save", "Don't save", "Cancel"};
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
}
