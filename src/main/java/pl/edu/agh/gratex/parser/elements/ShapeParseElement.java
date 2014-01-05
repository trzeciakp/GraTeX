package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.properties.ShapeType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class ShapeParseElement extends ParseElement {
    public static final int GROUPS = 2;
    public static final int SIDES_GROUP = 2;
    public static final int CIRCLE_POLYGON_GROUP = 1;
    private static final String REGEX = "(circle|regular polygon, regular polygon sides=(\\d+))";
    private static final Pattern PATTERN = Pattern.compile(ShapeParseElement.REGEX);

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

    protected ShapeType getShapeType(String match) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String group = matcher.group(SIDES_GROUP);
        if(group != null) {
            int sides = Integer.parseInt(group);
            return ShapeType.getShapeBySidesNumber(sides);
        }
        if(matcher.group(CIRCLE_POLYGON_GROUP) != null) {
            return ShapeType.CIRCLE;
        }
    }

    protected String getProperty(ShapeType shape) {
        return (shape == ShapeType.CIRCLE?"circle":"regular polygon, regular polygon sides="+shape.getSides());
    }
}
