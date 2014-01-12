package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class PositionParseElement extends ParseElement {
    protected static final double COEFFICIENT = 0.625;
    private static final String REGEX_PREFIX = "\\(";
    private static final String REGEX_NUMBER = "(-?\\d+\\.?\\d*)";
    private static final String REGEX_MIDFIX = "pt, ";
    private static final String REGEX_SUFFIX = "pt\\) ";
    private static final String REGEX = REGEX_PREFIX + REGEX_NUMBER + REGEX_MIDFIX + REGEX_NUMBER + REGEX_SUFFIX;
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 2;
    public static final int X_GROUP = 1;
    public static final int Y_GROUP = 2;

    @Override
    public boolean isOptional() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public int groups() {
        return super.groups()+ GROUPS;
    }

    protected int getX(String match) {
        return getGroupFromMatch(match, X_GROUP);
    }

    protected int getY(String match) {
        return -getGroupFromMatch(match, Y_GROUP);
    }

    private int getGroupFromMatch(String match, int group) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        return getIntFromDoublePt(matcher.group(group));
    }

    protected String getProperty(int x, int y) {
        return "("+COEFFICIENT*x+"pt, "+ COEFFICIENT*(-y)+"pt) ";
    }
}
