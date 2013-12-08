package pl.edu.agh.gratex.constants;

public enum ActionButtonType {
    NEW_GRAPH("new.png", "New graph"),
    OPEN_GRAPH("open.png", "Open graph file"),
    SAVE_GRAPH("save.png", "Save"),
    EDIT_TEMPLATE("defaults.png", "Edit graph's template"),
    COPY_SUBGRAPH("copy.png", "Copy selected subgraph"),
    PASTE_SUBGRAPH("paste.png", "Paste copied subgraph"),
    UNDO("undo.png", "Undo"),
    REDO("redo.png", "Redo"),
    TOGGLE_GRID("grid.png", "Toggle grid on/off"),
    NUMERATION_PREFERENCE("numeration.png", "Numeration preferences"),
    SHOW_CODE("tex.png", "Show graph's LaTeX code");

    private String imageName;
    private String tooltip;

    ActionButtonType(String imageName, String tooltip) {
        this.imageName = imageName;
        this.tooltip = tooltip;
    }

    public String getImageName() {
        return imageName;
    }

    public String getTooltip() {
        return tooltip;
    }
}
