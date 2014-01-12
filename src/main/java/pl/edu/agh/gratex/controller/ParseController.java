package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.parser.*;

import java.util.List;

public interface ParseController {

    public GraphElementParser getParserByElementType(GraphElementType graphElementType);

    public List<String> parseGraphToLatexCode(Graph graph);

    public Graph parseLatexCodeWithTemplatesToGraph(List<String> code) throws ParserException;

    public List<String> parseTemplatesToLatexCode();
}
