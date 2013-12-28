package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.mouse.GraphElementMouseController;
import pl.edu.agh.gratex.parser.GraphElementParser;

/**
 *
 */
public interface GraphElementControllersFactory {
    GraphElementParser createGraphElementParser(GraphElementType type);
    GraphElementMouseController createGraphElementMouseController(GraphElementType type);
}
