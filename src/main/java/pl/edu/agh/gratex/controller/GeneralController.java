package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.controller.mouse.MouseController;
import pl.edu.agh.gratex.controller.operation.OperationController;
import pl.edu.agh.gratex.model.graph.Graph;

public interface GeneralController {

    public ModeController getModeController();

    public ToolController getToolController();

    public SelectionController getSelectionController();

    public MouseController getMouseController();

    public OperationController getOperationController();

    public ParseController getParseController();

    public Graph getGraph();

    public void resetWorkspace();

    public void newGraphFile();

    public void openGraphFile();

    public boolean saveGraphFile(boolean saveAs);

    public void editGraphTemplate();

    public void duplicateSubgraph();

    public void undo();

    public void redo();

    public void toggleGrid();

    public void setNumeration();

    public void parseToTeX();

    public void showAboutDialog();

    public void selectAll();

    public void deleteSelection();

    public void exitApplication();
}
