package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.mouse.*;
import pl.edu.agh.gratex.parser.*;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ColorMapperTmpImpl;

/**
 *
 */
public class GraphElementControllersFactoryImpl implements GraphElementControllersFactory {
    private GeneralController generalController;
    private ColorMapper colorMapper;

    public GraphElementControllersFactoryImpl(GeneralController generalController, ColorMapper colorMapper) {
        this.generalController = generalController;
        this.colorMapper = colorMapper;
    }

    @Override
    public GraphElementParser createGraphElementParser(GraphElementType type) {
        switch (type) {
            case VERTEX:
                return new VertexParser(colorMapper);
            case EDGE:
                return new EdgeParser(colorMapper);
            case LABEL_VERTEX:
                return new LabelVertexParser(colorMapper);
            case LABEL_EDGE:
                return new LabelEdgeParser(colorMapper);
        }
        return null;
    }

    @Override
    public GraphElementMouseController createGraphElementMouseController(GraphElementType type) {
        switch (type) {
            case VERTEX:
                return new VertexMouseControllerImpl(generalController);
            case EDGE:
                return new EdgeMouseControllerImpl(generalController);
            case LABEL_VERTEX:
                return new LabelVertexMouseControllerImpl(generalController);
            case LABEL_EDGE:
                return new LabelEdgeMouseControllerImpl(generalController);
        }
        return null;
    }
}
