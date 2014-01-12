package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.GraphElement;

import java.util.regex.Pattern;

/**
 *
 */
public abstract class ParseElement {

    public static final double COEFFICIENT = 0.625;
    protected Pattern PATTERN;

    public ParseElement() {
        init();
    }

    protected void init() {
        PATTERN = Pattern.compile(regex());
    }

    public abstract boolean isOptional();
    public abstract String regex();
    public abstract void setProperty(String match, GraphElement element);
    public abstract String getProperty(GraphElement element);
    public int groups() {
        return 1;
    }
    protected int getIntFromDoublePt(String doubleString) {
        double x = Double.parseDouble(doubleString);
        int result = (int) (x/ COEFFICIENT);
        return result;
    }

    protected String getDoublePt(int x) {
        return x*COEFFICIENT+"pt";
    }

    protected Pattern getPattern() {
        return PATTERN;
    }
}
