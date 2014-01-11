package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LinkBoundaryCommentedParametersParseElement extends ParseElement {

    private static final String REGEX_NUMBER = "(-?\\d+)";
    private static final String REGEX = "%"+REGEX_NUMBER+","+REGEX_NUMBER;
    public static final int GROUPS = 2;
    public static final int BOUNDARY_A_GROUP = 1;
    public static final int BOUNDARY_B_GROUP = 2;
    public static final String STRING_FORMAT = "%%%d,%d";
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
        LinkBoundary linkBoundary = (LinkBoundary) element;
        if(match == null) {
            return;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        linkBoundary.setBoundaryA(groupTextToBoundary(matcher, BOUNDARY_A_GROUP, linkBoundary.getGraph()));
        linkBoundary.setBoundaryB(groupTextToBoundary(matcher, BOUNDARY_B_GROUP, linkBoundary.getGraph()));
    }

    private Boundary groupTextToBoundary(Matcher matcher, int group, Graph graph) {
        int id = Integer.parseInt(matcher.group(group));
        return graph.getBoundaryById(id);
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        Boundary boundaryA = linkBoundary.getBoundaryA();
        Boundary boundaryB = linkBoundary.getBoundaryB();
        if(boundaryA != null && boundaryB != null) {
            return String.format(STRING_FORMAT, boundaryA.getNumber(), boundaryB.getNumber());
        }
        return "";
    }
}
