package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;

import java.util.ArrayList;
import java.util.List;

public class ModeControllerImpl implements ModeController {
    private GraphElementType mode = GraphElementType.VERTEX;
    private List<ModeListener> listeners = new ArrayList<>();

    @Override
    public GraphElementType getMode() {
        return mode;
    }

    @Override
    public void setMode(GraphElementType graphElementType) {
        GraphElementType previousMode = mode;
        mode = graphElementType;
        for (ModeListener listener : listeners) {
            listener.fireModeChanged(previousMode, mode);
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
