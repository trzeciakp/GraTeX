package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.*;
import pl.edu.agh.gratex.controller.mouse.MouseController;
import pl.edu.agh.gratex.controller.mouse.MouseControllerImpl;
import pl.edu.agh.gratex.controller.operation.*;
import pl.edu.agh.gratex.draw.DrawableFactory;
import pl.edu.agh.gratex.draw.DrawableFactoryImpl;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.GraphElementFactoryImpl;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.parser.Parser;
import pl.edu.agh.gratex.parser.elements.ColorMapperTmpImpl;
import pl.edu.agh.gratex.utils.FileManager;
import pl.edu.agh.gratex.view.*;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;
import java.util.List;

public class GeneralControllerImpl implements GeneralController, ToolListener, ModeListener, Serializable {
    private MainWindow mainWindow;
    private OperationController operationController;
    private ModeController modeController;
    private ToolController toolController;
    private MouseController mouseController;
    private SelectionController selectionController;
    private ParseController parseController;

    private Graph graph;
    private File currentFile;
    private GraphElementFactory graphElementFactory;
    private final FileManager fileManager;

    public GeneralControllerImpl(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        modeController = new ModeControllerImpl(this);
        toolController = new ToolControllerImpl(this);
        operationController = new OperationControllerImpl(this);
        selectionController = new SelectionControllerImpl(this, modeController, toolController);
        DrawableFactory drawableFactory = new DrawableFactoryImpl(selectionController);
        graphElementFactory = new GraphElementFactoryImpl(drawableFactory);

        GraphElementControllersFactory elementControllersFactory = new GraphElementControllersFactoryImpl(this, new ColorMapperTmpImpl(), graphElementFactory);
        parseController = new ParseControllerImpl(elementControllersFactory);
        fileManager = new FileManager(parseController);
        mouseController = new MouseControllerImpl(this, modeController, toolController, selectionController, operationController, elementControllersFactory);

        modeController.addModeListener(this);
        toolController.addToolListener(this);

        modeController.setMode(ModeType.VERTEX);
        toolController.setTool(ToolType.ADD);

        graph = new Graph();
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
            graph = new Graph();
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
                if ((newGraph = fileManager.openFile(file)) != null) {
                    currentFile = file;
                    GraphUtils.deleteUnusedLabels(newGraph);
                    graph = newGraph;
                    resetWorkspace();
                    operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_GRAPH_OPEN_OK));
                } else {
                    operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_GRAPH_OPEN_FAIL));
                    Application.reportError(StringLiterals.MESSAGE_ERROR_OPEN_GRAPH, null);
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
                operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_GRAPH_SAVE_OK));
                currentFile = file;
                return true;
            } else {
                operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_GRAPH_SAVE_FAIL));
                Application.reportError(StringLiterals.MESSAGE_ERROR_SAVE_GRAPH, null);
            }
        }

        return false;
    }

    //TODO it should be changed
    public void resetWorkspace() {
        selectionController.clearSelection();
        //mouseController.finishMovingElement();
        //mouseController.cancelCurrentOperation();
        operationController.reportOperationEvent(null);
    }

    @Override
    public void editGraphTemplate() {
        GraphTemplateDialog gdd = new GraphTemplateDialog(mainWindow, this, graphElementFactory);
        Graph templateGraph = gdd.displayDialog();

        if (templateGraph != null) {
            graph.setVertexDefaultModel(templateGraph.getVertexDefaultModel());
            graph.setEdgeDefaultModel(templateGraph.getEdgeDefaultModel());
            graph.setLabelVDefaultModel(templateGraph.getLabelVDefaultModel());
            graph.setLabelEDefaultModel(templateGraph.getLabelEDefaultModel());

            if (templateGraph.gridOn) {
                List<GraphElement> elements = graph.getAllElements();
                AlterationOperation operation = new AlterationOperation(this, elements, OperationType.TEMPLATE_GLOBAL_APPLY, StringLiterals.INFO_TEMPLATE_APPLIED_GLOBALLY);
                // TODO Na razie robie tak żeby działało, może kiedyś uda się to ładniej napisać
                for (GraphElement graphElement : elements) {
                    switch (graphElement.getType()) {
                        case VERTEX:
                            graphElement.setModel(templateGraph.getVertexDefaultModel());
                            break;
                        case EDGE:
                            graphElement.setModel(templateGraph.getEdgeDefaultModel());
                            break;
                        case LABEL_VERTEX:
                            graphElement.setModel(templateGraph.getLabelVDefaultModel());
                            break;
                        case LABEL_EDGE:
                            graphElement.setModel(templateGraph.getLabelEDefaultModel());
                            break;
                    }
                }
                operation.finish();
            } else {
                operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_TEMPLATE_CHANGE));
            }
        }
    }

    @Override
    public void duplicateSubgraph() {
        mouseController.duplicateSubgraph();
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
    public void exitApplication() {
        if (checkForUnsavedProgress()) {
            System.exit(0);
        }
    }

    //===========================================
    // Internal functions

    private boolean checkForUnsavedProgress() {
        // TODO this nie bedzie potrzebne, czytaj FileManager
        if(fileManager.hasContentChanged(graph)) {
            System.out.println(mainWindow);
            Object[] options = {"Save", "Don't save", "Cancel"};
            int option = JOptionPane.showOptionDialog(null, "There have been changes since last save.\n"
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
