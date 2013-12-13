package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ToolType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToolControllerImpl implements ToolController, Serializable {
    private GeneralController generalController;

    private ToolType tool;
    private List<ToolListener> listeners;

    public ToolControllerImpl(GeneralController generalController) {
        this.generalController = generalController;
        this.tool = ToolType.ADD;
        this.listeners = new ArrayList<>();
    }

    @Override
    public GeneralController getGeneralController() {
        return generalController;
    }

    @Override
    public void setTool(ToolType newTool) {
        ToolType previousToolType = tool;
        for (ToolListener toolListener : listeners) {
            toolListener.toolChanged(previousToolType, newTool);
        }
        tool = newTool;
    }

    @Override
    public ToolType getTool() {
        return tool;
    }

    @Override
    public void addToolListener(ToolListener toolListener) {
        listeners.add(toolListener);
        sortListeners();
    }

    @Override
    public void removeToolListener(ToolListener toolListener) {
        listeners.remove(toolListener);
        sortListeners();
    }

    private void sortListeners() {
        Collections.sort(listeners, new Comparator<ToolListener>() {
            public int compare(ToolListener l1, ToolListener l2) {
                return Integer.compare(l1.toolUpdatePriority(), l2.toolUpdatePriority());
            }
        });
    }
}
