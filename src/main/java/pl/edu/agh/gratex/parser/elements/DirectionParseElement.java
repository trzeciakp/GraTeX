package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.properties.ArrowType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class DirectionParseElement extends ParseElement {
    public static final int GROUPS = 1;
    public static final int ARROW_GROUP = 1;
    private static final String REGEX = ", ->(, >=latex)?";
    private static final Pattern PATTERN = Pattern.compile(DirectionParseElement.REGEX);

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

    protected boolean getIsDirected(String match) {
        if(match == null) {
            return false;
        } else {
            return true;
        }
    }

    protected ArrowType getArrowType(String match) {
        if(match == null) {
            return null;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String group = matcher.group(ARROW_GROUP);
        if(group == null) {
            return ArrowType.BASIC;
        } else {
            return ArrowType.FILLED;
        }
    }

    protected String getProperty(boolean directed, ArrowType arrowType) {
        String result = "";
        if(directed) {
            result += ", ->" + (arrowType == ArrowType.FILLED?", >=latex":"");
        }
        return result;
    }
}
