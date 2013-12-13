package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.model.graph.Graph;

public interface GeneralController {

    public ModeController getModeController();

    public ToolController getToolController();

    public SelectionController getSelectionController();

    public MouseController getMouseController();

    public OperationController getOperationController();

    public void updateWorkspace();

    public void updateMenuBarAndActions();

    public void giveFocusToLabelTextfield();

    public ModeType getMode();

    public ToolType getTool();

    public Graph getGraph();

    public void setGraph(Graph graph);

    public void resetGraph();

    public void newGraphFile();

    public void openGraphFile();

    public boolean saveGraphFile(boolean saveAs);

    public void editGraphTemplate();

    public void copyToClipboard();

    public void pasteFromClipboard();

    public void undo();

    public void redo();

    public void toggleGrid();

    public void setNumeration();

    public void parseToTeX();

    //TODO maybe it should select even if not in select mode
    public void selectAll();

    void exitApplication();

    void showAboutDialog();

    void deleteSelection();

    public void publishInfo(String entry);

    public void reportError(String message, Exception e);

    public void criticalError(String message, Exception e);

    public void resetWorkspace();
}
