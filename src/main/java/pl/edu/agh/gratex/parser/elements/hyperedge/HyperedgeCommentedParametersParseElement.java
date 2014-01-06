package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.hyperedge.HyperedgePropertyModel;
import pl.edu.agh.gratex.model.properties.IsJointDisplay.IsJointDisplay;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class HyperedgeCommentedParametersParseElement extends ParseElement {

    private static final String REGEX = "%hidden";
    public static final int GROUPS = 1;
    public static final int ANGLE_GROUP = 1;
    public static final String STRING_FORMAT = "%hidden";
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
        Hyperedge hyperedge = (Hyperedge) element;
        if(match == null) {
            hyperedge.setIsJointDisplay(IsJointDisplay.VISIBLE);
        } else {
            hyperedge.setIsJointDisplay(IsJointDisplay.HIDDEN);
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return hyperedge.getIsJointDisplay() == IsJointDisplay.HIDDEN ? STRING_FORMAT : "";
    }
}
