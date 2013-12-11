package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;

public interface ModeListener {
    public void modeChanged(ModeType previousMode, ModeType currentMode);
    public int modeUpdatePriority();
}
