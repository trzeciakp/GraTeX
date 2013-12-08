package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.gui.ControlManager;

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
}
