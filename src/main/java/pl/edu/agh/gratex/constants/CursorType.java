package pl.edu.agh.gratex.constants;

public enum CursorType {
    ADD(ToolType.ADD, "images/addtoolcursor.png", "add"),
    REMOVE(ToolType.REMOVE, "images/removetoolcursor.png", "remove"),
    SELECT(ToolType.SELECT, "images/selecttoolcursor.png", "select");

    private ToolType toolType;
    private String imageName;
    private String description;

    CursorType(ToolType toolType, String imageName, String description) {
        this.toolType = toolType;
        this.imageName = imageName;
        this.description = description;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public String getImageName() {
        return imageName;
    }

    public String getDescription() {
        return description;
    }
}
