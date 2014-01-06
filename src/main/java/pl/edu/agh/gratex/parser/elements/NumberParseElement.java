package pl.edu.agh.gratex.parser.elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class NumberParseElement extends ParseElement {
    public static final int GROUPS = 1;
    public static final int NUMBER_GROUP = 1;
    public static final String STRING_FORMAT = "(%d)";
    private static final String REGEX = "\\((\\d+)\\)";
    private static final Pattern PATTERN = Pattern.compile(NumberParseElement.REGEX);

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

    protected int getNumber(String match) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        return Integer.parseInt(matcher.group(NUMBER_GROUP));
    }

    protected String getProperty(int number) {
        return String.format(STRING_FORMAT, number);
    }
}
