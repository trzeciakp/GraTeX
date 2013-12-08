package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;

public interface ModeListener {

    public void fireModeChanged(ModeType previousMode, ModeType currentMode);
}
