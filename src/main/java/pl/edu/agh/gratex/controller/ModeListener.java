package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.graph.GraphElementType;

/**
 *
 */
public interface ModeListener {

    public void fireModeChanged(GraphElementType previousMode, GraphElementType currentMode);
}
