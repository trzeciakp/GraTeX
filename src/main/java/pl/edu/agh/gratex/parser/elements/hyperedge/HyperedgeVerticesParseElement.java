package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class HyperedgeVerticesParseElement extends ParseElement {
    private static final String SINGLE_LINE_REGEX = "( \\\\draw \\((\\d+)\\) to \\((\\d+)\\);)";
    private static final String REGEX = "(" + SINGLE_LINE_REGEX + "+)";
    private static final String SINGLE_LINE_FORMAT = " \\draw (%d) to (%d);";
    private static final Pattern SINGLE_LINE_PATTERN = Pattern.compile(SINGLE_LINE_REGEX);
    private static final int GROUPS = 4;
    private static final int VERTEX_A_GROUP = 2;
    private static final int VERTEX_B_GROUP = 3;

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        int jointNumber = hyperedge.getNumber();
        Matcher matcher = SINGLE_LINE_PATTERN.matcher(match);
        List<Vertex> connectedVertices = new ArrayList<>();
        while(matcher.find()) {
           Vertex foundVertex = tryToFindVertex(matcher, VERTEX_A_GROUP, jointNumber, element.getGraph());
           if(foundVertex == null) {
               foundVertex = tryToFindVertex(matcher, VERTEX_B_GROUP, jointNumber, element.getGraph());
           }
           connectedVertices.add(foundVertex);
        }
        hyperedge.setConnectedVertices(connectedVertices);
    }

    private Vertex tryToFindVertex(Matcher matcher, int group, int jointNumber, Graph graph) {
        String foundGroup = matcher.group(group);
        int suspectedVertexNumber = Integer.parseInt(foundGroup);
        if(suspectedVertexNumber == jointNumber) {
            return null;
        }
        return graph.getVertexById(suspectedVertexNumber);
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        List<Vertex> connectedVertices = hyperedge.getConnectedVertices();
        StringBuilder result = new StringBuilder();
        for (Vertex vertex : connectedVertices) {
            result.append(String.format(SINGLE_LINE_FORMAT, vertex.getNumber(), hyperedge.getNumber()));
        }
        return result.toString();
    }
}
