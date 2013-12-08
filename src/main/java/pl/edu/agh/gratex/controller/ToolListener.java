package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.gui.ToolType;

/**
 *
 */
public interface ToolListener {

    public void fireToolChanged(ToolType previousToolType, ToolType currentToolType);
}
