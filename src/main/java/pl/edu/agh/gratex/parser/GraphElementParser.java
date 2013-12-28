package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class GraphElementParser {

    private GraphElementFactory graphElementFactory;
    private Pattern pattern;
    private List<ParseElement> parseList;


    protected GraphElementParser(GraphElementFactory graphElementFactory) {
        this.graphElementFactory = graphElementFactory;
    }

    public void init() {
        this.parseList = createParseList();
        this.pattern = evaluatePattern();
    }

    public abstract List<ParseElement> createParseList();

    public abstract GraphElementType getType();

    public String parseToLatex(GraphElement graphElement) {
        return parseToLatexUsingParseList(graphElement);
    }

    public GraphElement parseToGraph(String code, Graph graph) throws ParserException {
        GraphElement graphElement = graphElementFactory.create(getType(), graph);
        parseToGraphUsingParseList(code, graphElement);
        return graphElement;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public List<ParseElement> getParseList() {
        return parseList;
    }

    public GraphElementFactory getGraphElementFactory() {
        return graphElementFactory;
    }

    public void setGraphElementFactory(GraphElementFactory graphElementFactory) {
        this.graphElementFactory = graphElementFactory;
    }

    protected void parseToGraphUsingParseList(String code, GraphElement graphElement) throws ParserException {
        Matcher matcher = getPattern().matcher(code);
        int group = 1;
        if(matcher.matches()) {
            for (ParseElement parseElement : getParseList()) {
                String foundGroup = matcher.group(group);
                parseElement.setProperty(foundGroup, graphElement);
                group += parseElement.groups();
            }
        } else {
            throw new ParserException();
        }
    }

    protected String parseToLatexUsingParseList(GraphElement graphElement) {
        StringBuilder sb = new StringBuilder();
        for (ParseElement parseElement : getParseList()) {
            sb.append(parseElement.getProperty(graphElement));
        }
        return sb.toString();

    }

    protected Pattern evaluatePattern() {
        StringBuilder regex = new StringBuilder();
        for (ParseElement parseElement : getParseList()) {
            regex.append("(").append(parseElement.regex()).append(")");
            if(parseElement.isOptional()) {
                regex.append("?");
            }
        }
        return Pattern.compile(regex.toString());
    }

    public void updateElementWithCode(GraphElement graphElement, String code) throws ParserException {
        parseToGraphUsingParseList(code, graphElement);
        graphElement.setLatexCode(code);
    }
}
