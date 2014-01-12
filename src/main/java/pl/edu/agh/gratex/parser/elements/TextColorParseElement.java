package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class TextColorParseElement extends ParseElement {

    protected static final String REGEX = "(\\{\\\\textcolor\\{([^}]+)\\})?\\{([^}]+)\\}\\}?";
    protected static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 3;
    public static final int COLOR_GROUP = 2;
    public static final int TEXT_GROUP = 3;
    private ColorMapper colorMapper;

    public TextColorParseElement(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public boolean isOptional() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String regex() {
        return REGEX;
    }

    protected Color getFontColor(String match) {
        String color = getGroup(match, COLOR_GROUP);
        if(color != null) {
            return colorMapper.getColor(color);
        } else {
            return colorMapper.getTemplateColor();
        }
    }

    private String getGroup(String match, int number) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        return matcher.group(number);
    }

    protected String getText(String match) {
        return getGroup(match, TEXT_GROUP).replaceAll("\\\\%","%");
    }
    protected String getProperty(Color color, String text) {
        String escapedText = text.replaceAll("%", Matcher.quoteReplacement("\\%"));
        if (color != null) {
            return "{\\textcolor{" + colorMapper.getColorText(color) + "}{" + escapedText + "}}";
        } else {
            return "{" + escapedText + "}";
        }
    }
}
