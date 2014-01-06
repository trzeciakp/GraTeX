package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.*;
import pl.edu.agh.gratex.controller.mouse.MouseController;
import pl.edu.agh.gratex.controller.mouse.MouseControllerImpl;
import pl.edu.agh.gratex.controller.operation.*;
import pl.edu.agh.gratex.model.DrawerFactory;
import pl.edu.agh.gratex.model.DrawerFactoryImpl;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.parser.elements.ColorMapperTmpImpl;
import pl.edu.agh.gratex.utils.FileManager;
import pl.edu.agh.gratex.view.*;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class GeneralControllerImpl implements GeneralController, ToolListener, ModeListener {
    private MainWindow mainWindow;
    private OperationController operationController;
    private ModeController modeController;
    private ToolController toolController;
    private MouseController mouseController;
    private SelectionController selectionController;
    private ParseController parseController;

    private Graph graph;
    private GraphElementFactory graphElementFactory;
    private final FileManager fileManager;

    public GeneralControllerImpl(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        modeController = new ModeControllerImpl(this);
        toolController = new ToolControllerImpl(this);
        operationController = new OperationControllerImpl(this);
        selectionController = new SelectionControllerImpl(this, modeController, toolController);
        DrawerFactory drawerFactory = new DrawerFactoryImpl(selectionController);
        PropertyModelFactory propertyModelFactory = new PropertyModelFactoryImpl();
        graphElementFactory = new GraphElementFactoryImpl(drawerFactory, propertyModelFactory);

        GraphElementControllersFactory elementControllersFactory = new GraphElementControllersFactoryImpl(this, new ColorMapperTmpImpl(), graphElementFactory);
        parseController = new ParseControllerImpl(elementControllersFactory);
        fileManager = new FileManager(parseController);
        mouseController = new MouseControllerImpl(this, modeController, toolController, selectionController, operationController, elementControllersFactory);

        modeController.addModeListener(this);
        toolController.addToolListener(this);

        modeController.setMode(ModeType.VERTEX);
        toolController.setTool(ToolType.ADD);

        graph = new Graph();
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
            graph = new Graph();
            resetWorkspace();
            editGraphTemplate();
        }
    }

    @Override
    public void openGraphFile() {
        if (checkForUnsavedProgress()) {
            OpenFileDialog chooser;
            File currentFile = fileManager.getCurrentFile();
            if (currentFile != null) {
                chooser = new OpenFileDialog(currentFile);
            } else {
                chooser = new OpenFileDialog();
            }
            File file = chooser.showDialog(mainWindow);

            if (file != null) {
                Graph newGraph;
                if ((newGraph = fileManager.openFile(file)) != null) {
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
        File file = fileManager.getCurrentFile();

        if (saveAs || file == null) {
            SaveFileDialog chooser;
            if (file != null) {
                chooser = new SaveFileDialog(file);
            } else {
                chooser = new SaveFileDialog();
            }
            file = chooser.showDialog(mainWindow);
        }

        if (file != null) {
            if (fileManager.saveFile(file, graph)) {
                operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_GRAPH_SAVE_OK));
                return true;
            } else {
                operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_GRAPH_SAVE_FAIL));
                Application.reportError(StringLiterals.MESSAGE_ERROR_SAVE_GRAPH, null);
            }
        }
        return false;
    }

    @Override
    public void editGraphTemplate() {
        GraphTemplateDialog gdd = new GraphTemplateDialog(mainWindow, this, graphElementFactory);
        boolean applyToAll = gdd.displayDialog();

        if(applyToAll) {
            List<GraphElement> elements = graph.getAllElements();
            AlterationOperation operation = new AlterationOperation(this, elements, OperationType.TEMPLATE_GLOBAL_APPLY, StringLiterals.INFO_TEMPLATE_APPLIED_GLOBALLY);
            PropertyModelFactory propertyModelFactory = graphElementFactory.getPropertyModelFactory();
            for (GraphElement element : elements) {
                element.setModel(propertyModelFactory.createTemplateModel(element.getType()));
            }
            operation.finish();
        } else {
            operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_TEMPLATE_CHANGE));
        }
        resetWorkspace();
    }

    @Override
    public void duplicateSubgraph() {
        mouseController.duplicateSubgraph();
    }

    @Override
    public void undo() {
        resetWorkspace();
        operationController.undo();
    }

    @Override
    public void redo() {
        resetWorkspace();
        operationController.redo();
    }

    @Override
    public void toggleGrid() {
        if (graph.isGridOn()) {
            graph.setGridOn(false);
        } else {
            GridDialog gd = new GridDialog(mainWindow, graph.getGridResolutionX(), graph.getGridResolutionY());
            int[] result = gd.showDialog();
            if (result != null) {
                graph.setGridOn(true);
                graph.setGridResolutionX(result[0]);
                graph.setGridResolutionY(result[1]);
                GraphUtils.adjustElementsToGrid(graph);
            }
        }
        operationController.reportOperationEvent(new GenericOperation(
                StringLiterals.INFO_GENERIC_GRID(graph.isGridOn(), graph.getGridResolutionX(), graph.getGridResolutionY())));
        resetWorkspace();
    }

    @Override
    public void setNumeration() {
        NumerationDialog nd = new NumerationDialog(mainWindow, graph.getGraphNumeration().isNumerationDigital(),
                graph.getGraphNumeration().getStartingNumber(), Const.MAX_VERTEX_NUMBER);
        int[] result = nd.showDialog();

        if (result != null) {
            graph.getGraphNumeration().setNumerationDigital(result[0] == 0);
            graph.getGraphNumeration().setStartingNumber(result[1]);
            operationController.reportOperationEvent(new GenericOperation(
                    StringLiterals.INFO_GENERIC_NUMERATION(result[0] == 0, result[1])));
        }
        resetWorkspace();
    }

    @Override
    public void parseToTeX() {
        resetWorkspace();
        new LatexCodeDialog(mainWindow, parseController.parseGraphToLatexCode(graph));
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
        if(fileManager.hasContentChanged(graph)) {
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

    private void resetWorkspace() {
        selectionController.clearSelection();
        mouseController.cancelCurrentOperation();
        operationController.reportOperationEvent(null);
    }
}
