package pl.edu.agh.gratex.controller;

public interface GeneralController {
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
}
