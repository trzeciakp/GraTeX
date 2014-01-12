package pl.edu.agh.gratex.parser.elements;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class LineColorParseElement extends ParseElement {
    public static final int GROUPS = 1;
    public static final int COLOR_GROUP = 1;
    protected static final Pattern PATTERN = Pattern.compile(LineColorParseElement.REGEX);
    private static final String REGEX = ", color=([a-zA-z]+)";
    protected ColorMapper colorMapper;

    public LineColorParseElement(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

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

    protected Color getLineColor(String match) {
        if(match == null) {
            //return colorMapper.getTemplateColor();
            return null;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        return colorMapper.getColor(matcher.group(COLOR_GROUP));
    }

    protected String getProperty(Color lineColor) {
        if(lineColor == null) {
            return "";
        }
        return ", color="+colorMapper.getColorText(lineColor);
    }
}
