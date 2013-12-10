package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.view.ControlManager;

import java.util.ArrayList;
import java.util.List;

//TODO get rid of using ControlManager. Use Field of type GraphElementType instead.
public class ModeControllerTmpImpl implements ModeController {
    private List<ModeListener> listeners = new ArrayList<>();

    @Override
    public ModeType getMode() {
        return ControlManager.getMode();
    }

    @Override
    public void setMode(ModeType modeType) {
        ModeType previousMode = ControlManager.getMode();
        for(ModeListener modeListener : listeners) {
            modeListener.fireModeChanged(previousMode, modeType);
        }
        ControlManager.changeMode(modeType);
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
