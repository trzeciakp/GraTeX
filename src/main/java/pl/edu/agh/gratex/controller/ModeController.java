package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.graph.GraphElementType;

/**
 *
 */
public interface ModeController {

    public GraphElementType getMode();

    public void setMode(GraphElementType graphElementType);
    public void addModeListener(ModeListener modeListener);

    public void removeModeListener(ModeListener modeListener);
}
