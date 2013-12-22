package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class VerticesEdgeParser extends ParseElement {
    private static final String VERTEX_REGEX = "\\((\\d+)\\)";
    private static final String REGEX = VERTEX_REGEX + " to (\\[[^]]+\\] )?" + VERTEX_REGEX;
    public static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 3;
    public static final int VERTEX_A_NUMBER = 1;
    private static final int VERTEX_B_NUMBER = 3;
    public static final String STRING_FORMAT = "(%d) to%s (%d)";
    public static final String IN_OUT_FORMAT = " [in=%d, out=%d]";

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
        Edge edge = (Edge) element;
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        Vertex vertexA = getVertexByNumber(matcher.group(VERTEX_A_NUMBER), element.getGraph());
        Vertex vertexB = getVertexByNumber(matcher.group(VERTEX_B_NUMBER), element.getGraph());
        if(vertexA != null && vertexB != null) {
            edge.setVertexA(vertexA);
            edge.setVertexB(vertexB);
        } else {
            //TODO
        }
    }

    private Vertex getVertexByNumber(String number, Graph graph) {
        int vertexNumber = Integer.parseInt(number);
        return graph.getVertexById(vertexNumber);
    }


    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        String angles = (!edge.isLoop() && edge.getRelativeEdgeAngle() > 0?
                String.format(IN_OUT_FORMAT,edge.getInAngle(), edge.getOutAngle()):"");
        return String.format(STRING_FORMAT, edge.getVertexA().getNumber(), angles, edge.getVertexB().getNumber());
    }
}
