package pl.edu.agh.gratex.constants;

public enum ToolButtonType {
    ADD("addtool.png", "addtoolpassive.png", "Add tool", ToolType.ADD),
    REMOVE("removetool.png", "removetoolpassive.png", "Remove tool", ToolType.REMOVE),
    SELECT("selecttool.png", "selecttoolpassive.png", "Select tool", ToolType.SELECT);

    private String imageActiveName;
    private String imagePassiveName;
    private String tooltip;
    private ToolType toolType;

    ToolButtonType(String imageActiveName, String imagePassiveName, String tooltip, ToolType toolType) {
        this.imageActiveName = imageActiveName;
        this.imagePassiveName = imagePassiveName;
        this.tooltip = tooltip;
        this.toolType = toolType;
    }

    public String getImageActiveName() {
        return imageActiveName;
    }

    public String getImagePassiveName() {
        return imagePassiveName;
    }

    public String getTooltip() {
        return tooltip;
    }

    public ToolType getToolType() {
        return toolType;
    }
}
