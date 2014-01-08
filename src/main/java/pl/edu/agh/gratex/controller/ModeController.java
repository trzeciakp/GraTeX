package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;

public interface ModeController {

    public ModeType getMode();
    public void setMode(ModeType graphElementType);
    public void addModeListener(ModeListener modeListener);
    public void removeModeListener(ModeListener modeListener);
}
