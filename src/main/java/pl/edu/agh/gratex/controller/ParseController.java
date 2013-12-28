package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.parser.*;

import java.util.List;

public interface ParseController {

    public GraphElementParser getParserByElementType(GraphElementType graphElementType);

    public VertexParser getVertexParser();
    public EdgeParser getEdgeParser();
    public LabelVertexParser getLabelVertexParser();
    public LabelEdgeParser getLabelEdgeParser();

    public List<String> parseGraphToLatexCode(Graph graph);

    public Graph parseLatexCodeToGraph(List<String> code);
}
