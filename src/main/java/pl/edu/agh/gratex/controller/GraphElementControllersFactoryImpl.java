package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.controller.mouse.*;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.*;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ColorMapperTmpImpl;

/**
 *
 */
public class GraphElementControllersFactoryImpl implements GraphElementControllersFactory {
    private GeneralController generalController;
    private ColorMapper colorMapper;
    private GraphElementFactory graphElementFactory;

    public GraphElementControllersFactoryImpl(GeneralController generalController, ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        this.generalController = generalController;
        this.colorMapper = colorMapper;
        this.graphElementFactory = graphElementFactory;
    }

    @Override
    public GraphElementParser createGraphElementParser(GraphElementType type) {
        switch (type) {
            case VERTEX:
                return new VertexParser(colorMapper, graphElementFactory);
            case EDGE:
                return new EdgeParser(colorMapper, graphElementFactory);
            case LABEL_VERTEX:
                return new LabelVertexParser(colorMapper, graphElementFactory);
            case LABEL_EDGE:
                return new LabelEdgeParser(colorMapper, graphElementFactory);
        }
        return null;
    }

    @Override
    public GraphElementMouseController createGraphElementMouseController(ModeType type) {
        switch (type) {
            case VERTEX:
                return new VertexMouseControllerImpl(generalController, graphElementFactory);
            case EDGE:
                return new EdgeMouseControllerImpl(generalController, graphElementFactory);
            case LABEL_VERTEX:
                return new LabelVertexMouseControllerImpl(generalController, graphElementFactory);
            case LABEL_EDGE:
                return new LabelEdgeMouseControllerImpl(generalController, graphElementFactory);
        }
        return null;
    }
}
