package pl.edu.agh.gratex.parser.elements.boundary;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.parser.elements.NumberParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

/**
 *
 */
public class BoundaryNumberParseElement extends NumberParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        Boundary boundary = (Boundary) element;
        boundary.setNumber(getNumber(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Boundary boundary = (Boundary) element;
        return getProperty(boundary.getNumber());
    }
}
