package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.*;
import pl.edu.agh.gratex.controller.mouse.MouseController;
import pl.edu.agh.gratex.controller.mouse.MouseControllerImpl;
import pl.edu.agh.gratex.controller.operation.OperationController;
import pl.edu.agh.gratex.controller.operation.*;
import pl.edu.agh.gratex.editor.OldOperationList;
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
    private OperationController operationController;
    private ModeController modeController;
    private ToolController toolController;
    private MouseController mouseController;
    private SelectionController selectionController;
    private ParseController parseController;
    private ClipboardController clipboardController;

    private Graph graph;
    private File currentFile;

    public GeneralControllerImpl(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        modeController = new ModeControllerImpl(this);
        toolController = new ToolControllerImpl(this);
        operationController = new OperationControllerImpl(this);
        selectionController = new SelectionControllerImpl(this, modeController, toolController);
        mouseController = new MouseControllerImpl(this, modeController, toolController, selectionController, operationController);
        parseController = new ParseControllerImpl(this);
        clipboardController = new ClipboardControllerImpl();

        modeController.addModeListener(this);
        toolController.addToolListener(this);

        modeController.setMode(ModeType.VERTEX);
        toolController.setTool(ToolType.ADD);

        graph = new Graph(this);
        currentFile = null;
    }


    //===========================================
    // Listeners implementation
    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        resetWorkspace();
    }

    @Override
    public int modeUpdatePriority() {
        return 10;
    }

    @Override
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        resetWorkspace();
    }

    @Override
    public int toolUpdatePriority() {
        return 10;
    }

    //===========================================
    // GeneralController interface implementation

    @Override
    public ClipboardController getClipboardController() {
        return clipboardController;
    }

    @Override
    public ModeController getModeController() {
        return modeController;
    }

    @Override
    public ToolController getToolController() {
        return toolController;
    }

    @Override
    public SelectionController getSelectionController() {
        return selectionController;
    }

    @Override
    public MouseController getMouseController() {
        return mouseController;
    }

    @Override
    public OperationController getOperationController() {
        return operationController;
    }

    @Override
    public ParseController getParseController() {
        return parseController;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public void newGraphFile() {
        if (checkForUnsavedProgress()) {
            currentFile = null;
            graph = new Graph(this);
            ControlManager.operations = new OldOperationList(this);
            resetWorkspace();
            // TODO wykomentowalem bo wkurwialo
            // editGraphTemplate();
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
                    ControlManager.operations = new OldOperationList(this);
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
        File file = currentFile;

        if (saveAs || currentFile == null) {
            SaveFileDialog chooser;
            if (currentFile != null) {
                chooser = new SaveFileDialog(currentFile);
            } else {
                chooser = new SaveFileDialog();
            }
            file = chooser.showDialog(mainWindow);
        }

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

        return false;
    }

    //TODO it should be changed
    public void resetWorkspace() {
        selectionController.clearSelection();
        //ControlManager.updatePropertyChangeOperationStatus(false);
        //mouseController.finishMovingElement();
        //mouseController.cancelCurrentOperation();
        operationController.reportOperationEvent(null);
    }

    @Override
    public void editGraphTemplate() {
        GraphTemplateDialog gdd = new GraphTemplateDialog(mainWindow, this);
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
        selectionController.clearSelection();
        operationController.undo();
    }

    @Override
    public void redo() {
        selectionController.clearSelection();
        operationController.redo();
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
        operationController.reportOperationEvent(new GenericOperation(
                StringLiterals.INFO_GENERIC_GRID(graph.gridOn, graph.gridResolutionX, graph.gridResolutionY)));
    }

    @Override
    public void setNumeration() {
        selectionController.clearSelection();
        //ControlManager.updatePropertyChangeOperationStatus(false);
        NumerationDialog nd = new NumerationDialog(mainWindow, graph.getGraphNumeration().isNumerationDigital(),
                graph.getGraphNumeration().getStartingNumber(), Const.MAX_VERTEX_NUMBER);
        int[] result = nd.showDialog();

        if (result != null) {
            graph.getGraphNumeration().setNumerationDigital(result[0] == 0);
            graph.getGraphNumeration().setStartingNumber(result[1]);
            operationController.reportOperationEvent(new GenericOperation(
                    StringLiterals.INFO_GENERIC_NUMERATION(result[0] == 0, result[1])));
        }
    }

    @Override
    public void parseToTeX() {
        new LatexCodeDialog(mainWindow, Parser.parse(graph));
    }

    @Override
    public void showAboutDialog() {
        new AboutDialog(mainWindow);
    }

    @Override
    public void selectAll() {
        toolController.setTool(ToolType.SELECT);
        selectionController.selectAll();
        operationController.reportOperationEvent(new GenericOperation(
                StringLiterals.INFO_GENERIC_SELECT_ALL(modeController.getMode())));
    }

    @Override
    public void deleteSelection() {
        if (selectionController.selectionSize() > 0) {
            new CreationRemovalOperation(this, selectionController.getSelection(), OperationType.REMOVE_OPERATION(modeController.getMode()),
                    StringLiterals.INFO_REMOVE_ELEMENT(modeController.getMode(), selectionController.selectionSize()), false);
        }
        selectionController.clearSelection();
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

    @Override
    public void criticalError(String message, Exception e) {
        reportError(StringLiterals.MESSAGE_ERROR_CRITICAL + message, e);
        System.exit(1);
    }

    @Override
    public void exitApplication() {
        if (checkForUnsavedProgress()) {
            System.exit(0);
        }
    }

    //===========================================
    // Internal functions

    private boolean checkForUnsavedProgress() {
        // TODO this nie bedzie potrzebne, czytaj FileManager
        if (FileManager.contentChanged(this, graph, currentFile)) {
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
