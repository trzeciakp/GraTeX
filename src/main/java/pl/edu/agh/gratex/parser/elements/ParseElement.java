package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.GraphElement;

/**
 *
 */
public abstract class ParseElement {

    public abstract boolean isOptional();
    public abstract String regex();
    public abstract void setProperty(String match, GraphElement element);
    public abstract String getProperty(GraphElement element);
    public int groups() {
        return 1;
    }
}
