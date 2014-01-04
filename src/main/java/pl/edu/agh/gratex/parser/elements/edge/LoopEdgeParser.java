package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.properties.LoopDirection;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LoopEdgeParser extends ParseElement {

    public static final int DIRECTION_GROUP = 1;
    private final static String REGEX = ", loop (right|above|left|below)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 1;
    public static final String STRING_FORMAT = ", loop %s";

    @Override
    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Edge edge = (Edge) element;
        if(match == null) {
            return;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        LoopDirection direction = LoopDirection.valueOf(matcher.group(DIRECTION_GROUP).toUpperCase());
        edge.setRelativeEdgeAngle(direction.getAngle());
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        if(edge.isLoop()) {
            return String.format(STRING_FORMAT, LoopDirection.getByAngle(edge.getRelativeEdgeAngle()));
        }
        return "";
    }
}
