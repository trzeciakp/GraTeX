package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.properties.JointLabelPosition;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class HyperedgeLabelRelativePositionParseElement extends ParseElement {
    public static final int GROUPS = 1;
    public static final int POSITION_GROUP = 1;
    private static final String REGEX = "\\((above|below|left|right|inside)\\)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public String regex() {
        return REGEX;
    }
    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        hyperedge.setJointLabelPosition(JointLabelPosition.valueOf(matcher.group(POSITION_GROUP).toUpperCase()));
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return "(" +hyperedge.getJointLabelPosition().name().toLowerCase()+")";
    }
}
