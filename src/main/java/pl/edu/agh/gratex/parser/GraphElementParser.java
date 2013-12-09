package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.GraphElement;

/**
 *
 */
public abstract class GraphElementParser {

    public abstract String parse(GraphElement graphElement);
    public abstract GraphElement unparse(String code, Graph graph) throws ParserException;
}
