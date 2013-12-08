package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.graph.GraphElementType;
import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.gui.ToolType;

/**
 *
 */
public class GeneralControllerTmpImpl implements GeneralController {

    @Override
    public void newGraphFile() {
        ControlManager.newGraphFile();
    }

    @Override
    public void openGraphFile() {
        ControlManager.openGraphFile();
    }

    @Override
    public boolean saveGraphFile(boolean saveAs) {
        return ControlManager.saveGraphFile(saveAs);
    }

    @Override
    public void editGraphTemplate() {
        ControlManager.editGraphTemplate();
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
    public void changeMode(GraphElementType graphElementType) {
        //TODO
        ControlManager.changeMode(graphElementType.ordinal()+1);
    }

    @Override
    public void exitApplication() {
        ControlManager.exitApplication();
    }

    @Override
    public void showAboutDialog() {
        ControlManager.showAboutDialog();
    }

    @Override
    public void deleteSelection() {
        ControlManager.deleteSelection();
    }

    @Override
    public void changeTool(ToolType toolType) {
        //TODO
        ControlManager.changeTool(toolType.ordinal() + 1);
    }

    @Override
    public ToolType getTool() {
        return ToolType.values()[ControlManager.tool - 1];
    }

    @Override
    public Object getMode() {
        return GraphElementType.values()[ControlManager.mode - 1];
    }
}
