package pl.edu.agh.gratex.parser.elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class SizeParseElement extends ParseElement {
    public static final int GROUPS = 1;
    public static final int SIZE_GROUP = 1;
    private static final String REGEX = ", minimum size=(\\d+\\.?\\d*)pt";
    private static final Pattern PATTERN = Pattern.compile(SizeParseElement.REGEX);

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
        return super.groups() + GROUPS;
    }

    protected int getRadius(String match) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        int size = getIntFromDoublePt(matcher.group(SIZE_GROUP))/2;
        return size;
    }

    protected String getProperty(int radius) {
        return ", minimum size="+getDoublePt(2* radius);
    }
}
