package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.graph.GraphElementType;
import pl.edu.agh.gratex.gui.ControlManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
//TODO get rid of using ControlManager. Use Field of type GraphElementType instead.
public class ModeControllerTmpImpl implements ModeController {
    private List<ModeListener> listeners = new ArrayList<>();

    @Override
    public GraphElementType getMode() {
        return ControlManager.getMode();
    }

    @Override
    public void setMode(GraphElementType graphElementType) {
        GraphElementType previousMode = ControlManager.getMode();
        ControlManager.changeMode(graphElementType.ordinal() + 1);
        for(ModeListener modeListener : listeners) {
            modeListener.fireModeChanged(previousMode, graphElementType);
        }
    }

    @Override
    public void addModeListener(ModeListener modeListener) {
        listeners.add(modeListener);
    }

    @Override
    public void removeModeListener(ModeListener modeListener) {
        listeners.remove(modeListener);
    }
}
