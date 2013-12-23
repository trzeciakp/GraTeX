package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.labelvertex.CommentedParametersLabelVertexParser;
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
        parseList.add(new CommentedParametersLabelVertexParser());
        pattern = evaluatePattern();
    }

    @Override
    public String parseToLatex(GraphElement graphElement) {
        return super.parseToLatexUsingParseList(graphElement);
    }

    @Override
    public LabelV parseToGraph(String code, Graph graph) throws ParserException {
        LabelV labelV = new LabelV(null, graph);
        parseToGraphUsingParseList(code, labelV);
        return labelV;
    }

    public List<ParseElement> getParseList() {
        return parseList;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
