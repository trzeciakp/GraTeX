package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.controller.mouse.*;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.*;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

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
            case BOUNDARY:
                return new BoundaryParser(colorMapper, graphElementFactory);
            case HYPEREDGE:
                return new HyperedgeParser(colorMapper, graphElementFactory);
        }
        return null;
    }

    @Override
    public GraphElementMouseController createGraphElementMouseController(ModeType type) {
        switch (type) {
            case VERTEX:
                return new VertexMouseControllerImpl(generalController, graphElementFactory, type.getRelatedElementType());
            case EDGE:
                return new EdgeMouseControllerImpl(generalController, graphElementFactory, type.getRelatedElementType());
            case LABEL_VERTEX:
                return new LabelVertexMouseControllerImpl(generalController, graphElementFactory, type.getRelatedElementType());
            case LABEL_EDGE:
                return new LabelEdgeMouseControllerImpl(generalController, graphElementFactory, type.getRelatedElementType());
            case BOUNDARY:
                return new BoundaryMouseController(generalController, graphElementFactory, type.getRelatedElementType());
            case HYPEREDGE:
                return new HyperedgeMouseControllerImpl(generalController, graphElementFactory, type.getRelatedElementType());
        }
        return null;
    }
}
