package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class GraphElementParser {

    public abstract String parseToLatex(GraphElement graphElement);
    public abstract GraphElement parseToGraph(String code, Graph graph) throws ParserException;
    public abstract Pattern getPattern();
    public abstract List<ParseElement> getParseList();

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
