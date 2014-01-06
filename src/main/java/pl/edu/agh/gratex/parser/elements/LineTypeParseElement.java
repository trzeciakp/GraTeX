package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.properties.LineType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class LineTypeParseElement extends ParseElement {
    public static final int GROUPS = 1;
    public static final int LINE_TYPE_GROUP = 1;
    private static final String REGEX = ", (dashed|dotted|double)";
    private static final Pattern PATTERN = Pattern.compile(LineTypeParseElement.REGEX);

    @Override
    public boolean isOptional() {
        return true;
    }

    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    protected LineType getLineType(String match) {
        if(match == null) {
            return  LineType.SOLID;
        } else {
            Matcher matcher = PATTERN.matcher(match);
            matcher.matches();
            return LineType.valueOf(matcher.group(LINE_TYPE_GROUP).toUpperCase());
        }
    }

    protected String getProperty(LineType lineType) {
        if(LineType.SOLID != lineType) {
            return ", "+lineType.toString();
        } else {
            return "";
        }
    }
}
