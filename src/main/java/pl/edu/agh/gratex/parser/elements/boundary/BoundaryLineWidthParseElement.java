package pl.edu.agh.gratex.parser.elements.boundary;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.parser.elements.LineWidthParseElement;

/**
 *
 */
public class BoundaryLineWidthParseElement extends LineWidthParseElement {

    @Override
    public String regex() {
        return ", " + super.regex();
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Boundary boundary = (Boundary) element;
        //boundary.setLineWidth((match == null ? 0 : getX(match)));
        boundary.setLineWidth(getX(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Boundary boundary = (Boundary) element;
        int lineWidth = boundary.getLineWidth();
        //return (lineWidth == 0 ? "":", " + getProperty(lineWidth));
        return ", " + getProperty(lineWidth);
    }
}