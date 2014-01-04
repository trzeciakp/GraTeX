package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class DirectionEdgeParser extends ParseElement {

    private static final String REGEX = ", ->(, >=latex)?";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 1;
    public static final int ARROW_GROUP = 1;

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Edge edge = (Edge) element;
        if(match == null) {
            edge.setDirected(false);
            return;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        edge.setDirected(true);
        String group = matcher.group(ARROW_GROUP);
        if(group == null) {
            edge.setArrowType(ArrowType.BASIC);
        } else {
            edge.setArrowType(ArrowType.FILLED);
        }

    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        String result = "";
        if(edge.isDirected()) {
            result += ", ->" + (edge.getArrowType() == ArrowType.FILLED?", >=latex":"");
        }
        return result;
    }
}
