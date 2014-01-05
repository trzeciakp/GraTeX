package pl.edu.agh.gratex.parser.elements.boundary;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class BoundaryResolutionParseElement extends ParseElement {
    private static final String REGEX_NUMBER = "(\\d+\\.?\\d*)";
    private static final String REGEX = "minimum width=" + REGEX_NUMBER + "pt, minimum height="+REGEX_NUMBER+"pt";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 2;
    public static final int WIDTH_GROUP = 1;
    public static final int HEIGHT_GROUP = 2;
    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Boundary boundary = (Boundary) element;

        boundary.setWidth(getGroupFromMatch(match, WIDTH_GROUP));
        boundary.setHeight(getGroupFromMatch(match, HEIGHT_GROUP));
    }

    @Override
    public String getProperty(GraphElement element) {
        Boundary boundary = (Boundary) element;

        return "minimum width="+boundary.getWidth()*COEFFICIENT+"pt, minimum height="+boundary.getHeight()*COEFFICIENT+"pt";
    }

    @Override
    public int groups() {
        return super.groups()+ GROUPS;
    }

    private int getGroupFromMatch(String match, int group) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        //TODO
        return (int) (Double.parseDouble(matcher.group(group))/COEFFICIENT);
    }
}
