package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.vertex.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
public class VertexParser extends GraphElementParser {
    private final Pattern pattern;
    private List<ParseElement> parseList = new ArrayList<>();

    public VertexParser(ColorMapper colorMapper) {
        parseList.add(new StaticParseElement("\\node ", false));
        parseList.add(new NumberVertexParser());
        parseList.add(new StaticParseElement(" [", false));
        parseList.add(new ShapeVertexParser());
        parseList.add(new SizeVertexParser());
        parseList.add(new ColorVertexParser(colorMapper));
        parseList.add(new StaticParseElement(", ", false));
        parseList.add(new LineWidthVertexParser());
        parseList.add(new LineColorTypeVertexParser(colorMapper));
        parseList.add(new StaticParseElement("] at ", false));
        parseList.add(new PositionVertexParser());
        parseList.add(new TextColorVertexParser(colorMapper));
        parseList.add(new StaticParseElement(";", false));
        //parseList.add(new CommentedParametersVertexParser());
        pattern = evaluatePattern();
    }

    @Override
    public String parseToLatex(GraphElement graphElement) {
        return super.parseToLatexUsingParseList(graphElement);
    }

    @Override
    public Vertex parseToGraph(String code, Graph graph) throws ParserException {
        Vertex result = new Vertex(graph);
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
