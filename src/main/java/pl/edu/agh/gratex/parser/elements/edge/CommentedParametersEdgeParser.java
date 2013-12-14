package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class CommentedParametersEdgeParser extends ParseElement {

    private static final String REGEX = "%(-?\\d+)";
    public static final int GROUPS = 1;
    public static final int ANGLE_GROUP = 1;
    public static final String STRING_FORMAT = "%%%d";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isOptional() {
        return true;
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
        if(match == null) {
            edge.setRelativeEdgeAngle(0);
            return;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        int angle = Integer.parseInt(matcher.group(ANGLE_GROUP));
        edge.setRelativeEdgeAngle(angle);
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        if(edge.getRelativeEdgeAngle() == 0) {
            return "";
        }
        return String.format(STRING_FORMAT, edge.getRelativeEdgeAngle());
    }
}
