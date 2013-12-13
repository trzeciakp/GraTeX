package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.model.graph.Graph;

public interface GeneralController {

    public ModeController getModeController();

    public ToolController getToolController();

    public SelectionController getSelectionController();

    public MouseController getMouseController();

    public OperationController getOperationController();

    public Graph getGraph();

    public void resetWorkspace();

    public void updateMenuBarAndActions();

    public void giveFocusToLabelTextfield();

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

    public void showAboutDialog();

    public void selectAll();

    public void deleteSelection();

    public void publishInfo(String entry);

    public void reportError(String message, Exception e);

    public void criticalError(String message, Exception e);

    public void exitApplication();
}
