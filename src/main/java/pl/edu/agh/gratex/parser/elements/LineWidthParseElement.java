package pl.edu.agh.gratex.parser.elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class LineWidthParseElement extends ParseElement {
    protected static final double COEFFICIENT = 0.625;

    private static final String REGEX_NUMBER = "(\\d+\\.?\\d*)";
    private static final String REGEX = "line width=" + REGEX_NUMBER + "pt";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 1;
    public static final int X_GROUP = 1;

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
        return super.groups()+ GROUPS;
    }

    protected int getX(String match) {
        return getGroupFromMatch(match, X_GROUP);
    }

    private int getGroupFromMatch(String match, int group) {
        Matcher matcher = Pattern.compile(regex()).matcher(match);
        matcher.matches();
        //TODO
        return (int) (Double.parseDouble(matcher.group(group))/COEFFICIENT);
    }

    protected String getProperty(int x) {
        return "line width="+x*COEFFICIENT+"pt";
    }
}
