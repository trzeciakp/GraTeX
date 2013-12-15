package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.parser.*;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ColorMapperTmpImpl;

public class ParseControllerImpl implements ParseController {
    GeneralController generalController;
    ColorMapper colorMapper = new ColorMapperTmpImpl();
    VertexParser vertexParser = new VertexParser(colorMapper);
    EdgeParser edgeParser = new EdgeParser(colorMapper);
    LabelVertexParser labelVertexParser = new LabelVertexParser(colorMapper);
    LabelEdgeParser labelEdgeParser = new LabelEdgeParser(colorMapper);

    public ParseControllerImpl(GeneralController generalController) {
        this.generalController = generalController;
    }

    @Override
    public GeneralController getGeneralController() {
        return generalController;
    }

    @Override
    public GraphElementParser getParserByElementType(GraphElementType graphElementType) {
        switch (graphElementType)
        {
            case VERTEX:
                return vertexParser;
            case EDGE:
                return edgeParser;
            case LABEL_VERTEX:
                return labelVertexParser;
            case LABEL_EDGE:
                return labelEdgeParser;
            default:
                return null;
        }
    }

    @Override
    public VertexParser getVertexParser() {
        return vertexParser;
    }

    @Override
    public EdgeParser getEdgeParser() {
        return edgeParser;
    }

    @Override
    public LabelVertexParser getLabelVertexParser() {
        return labelVertexParser;
    }

    @Override
    public LabelEdgeParser getLabelEdgeParser() {
        return labelEdgeParser;
    }
}
