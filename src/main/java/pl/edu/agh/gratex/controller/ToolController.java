package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.gui.ToolType;

/**
 *
 */
public interface ToolController {

    public void setTool(ToolType toolType);

    public ToolType getTool();

    public void addToolListener(ToolListener toolListener);

    public void removeToolListener(ToolListener toolListener);
}
