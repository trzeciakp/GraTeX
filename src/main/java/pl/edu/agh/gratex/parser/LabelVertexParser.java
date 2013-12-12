package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.labelvertex.LabelVertexPositionParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.labelvertex.LabelVertexTextColorParseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
public class LabelVertexParser extends GraphElementParser {

    List<ParseElement> parseList = new ArrayList<>();
    private final Pattern pattern;

    public LabelVertexParser(ColorMapper colorMapper) {
        parseList.add(new StaticParseElement("\\node at ", false));
        parseList.add(new LabelVertexPositionParseElement());
        parseList.add(new LabelVertexTextColorParseElement(colorMapper));
        parseList.add(new StaticParseElement(";", false));
        pattern = evaluatePattern();
    }

    @Override
    public String parse(GraphElement graphElement) {
        return super.parseUsingParseList(graphElement);
    }

    @Override
    public GraphElement unparse(String code, Graph graph) throws ParserException {
        LabelV result = new LabelV(null, graph);
        unparseUsingParseList(code, result);
        return result;
    }

    public List<ParseElement> getParseList() {
        return parseList;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
