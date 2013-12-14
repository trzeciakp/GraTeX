package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.edge.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
public class EdgeParser extends GraphElementParser {
    private static final String BEGINING = "\\draw ";
    private final Pattern pattern;
    private List<ParseElement> parseList = new ArrayList<>();

    public EdgeParser(ColorMapper colorMapper) {
        parseList.add(new StaticParseElement("\\draw [", false));
        parseList.add(new LineWidthEdgeParser());
        parseList.add(new DirectionEdgeParser());
        parseList.add(new LineTypeEdgeParser());
        parseList.add(new LineColorEdgeParser(colorMapper));
        parseList.add(new LoopEdgeParser());
        parseList.add(new StaticParseElement("] ", false));
        parseList.add(new VertexesEdgeParser());
        parseList.add(new StaticParseElement(";", false));
        parseList.add(new CommentedParametersEdgeParser());
        pattern = evaluatePattern();
    }

    @Override
    public String parseToLatex(GraphElement graphElement) {
        return super.parseToLatexUsingParseList(graphElement);
    }

    @Override
    public Edge parseToGraph(String code, Graph graph) throws ParserException {
        Edge result = new Edge(graph);
        parseToGraphUsingParseList(code, result);
        return result;
    }

    public List<ParseElement> getParseList() {
        return parseList;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
