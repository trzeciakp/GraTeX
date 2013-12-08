package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;

import java.util.ArrayList;
import java.util.List;

public class ModeControllerImpl implements ModeController {
    private ModeType mode = ModeType.VERTEX;
    private List<ModeListener> listeners = new ArrayList<>();

    @Override
    public ModeType getMode() {
        return mode;
    }

    @Override
    public void setMode(ModeType modeType) {
        ModeType previousMode = mode;
        mode = modeType;
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
