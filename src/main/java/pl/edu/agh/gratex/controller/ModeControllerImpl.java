package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModeControllerImpl implements ModeController, Serializable {
    private GeneralController generalController;

    private ModeType mode = ModeType.VERTEX;
    private List<ModeListener> listeners = new ArrayList<>();

    public ModeControllerImpl(GeneralController generalController) {
        this.generalController = generalController;
    }

    @Override
    public GeneralController getGeneralController() {
        return generalController;
    }

    @Override
    public ModeType getMode() {
        return mode;
    }

    @Override
    public void setMode(ModeType newMode) {
        ModeType previousMode = mode;
        mode = newMode;
        for (ModeListener modeListener : listeners) {
            modeListener.modeChanged(previousMode, newMode);
        }
    }

    @Override
    public void addModeListener(ModeListener modeListener) {
        listeners.add(modeListener);
        sortListeners();
    }

    @Override
    public void removeModeListener(ModeListener modeListener) {
        listeners.remove(modeListener);
        sortListeners();
    }

    private void sortListeners() {
        Collections.sort(listeners, new Comparator<ModeListener>() {
            public int compare(ModeListener l1, ModeListener l2) {
                return Integer.compare(l1.modeUpdatePriority(), l2.modeUpdatePriority());
            }
        });
    }
}
