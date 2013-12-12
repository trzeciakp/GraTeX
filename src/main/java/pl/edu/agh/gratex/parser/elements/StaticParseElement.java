package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.GraphElement;

/**
 *
 */
public class StaticParseElement extends ParseElement {

    private String text;
    private String regex;
    private boolean optional;

    public StaticParseElement(String text, boolean optional) {

        this.text = text;
        this.optional = optional;

    }

    @Override
    public boolean isOptional() {
        return optional;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String regex() {
        return "\\Q"+text+"\\E";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProperty(GraphElement element) {
        return text;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
