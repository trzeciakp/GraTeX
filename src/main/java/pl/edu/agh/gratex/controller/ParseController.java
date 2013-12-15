package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.parser.*;

public interface ParseController {
    public GeneralController getGeneralController();

    public GraphElementParser getParserByElementType(GraphElementType graphElementType);

    public VertexParser getVertexParser();
    public EdgeParser getEdgeParser();
    public LabelVertexParser getLabelVertexParser();
    public LabelEdgeParser getLabelEdgeParser();
}
