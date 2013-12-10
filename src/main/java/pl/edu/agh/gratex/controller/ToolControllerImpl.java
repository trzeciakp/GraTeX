package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.view.ControlManager;
import pl.edu.agh.gratex.constants.ToolType;

import java.util.ArrayList;
import java.util.List;

public class ToolControllerImpl implements ToolController {
    private List<ToolListener> listeners = new ArrayList<>();

    @Override
    public void setTool(ToolType toolType) {
        ToolType previousToolType = ControlManager.getTool();
        ControlManager.changeTool(toolType);
        for (ToolListener toolListener : listeners) {
            toolListener.fireToolChanged(previousToolType, toolType);
        }
    }

    @Override
    public ToolType getTool() {
        return ControlManager.getTool();
    }

    @Override
    public void addToolListener(ToolListener toolListener) {
        listeners.add(toolListener);
    }

    @Override
    public void removeToolListener(ToolListener toolListener) {
        listeners.remove(toolListener);
    }
}
