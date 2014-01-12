package pl.edu.agh.gratex.parser.elements.boundary;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.parser.elements.PositionParseElement;

/**
 *
 */
public class BoundaryPositionParseElement extends PositionParseElement {

    @Override
    public void setProperty(String match, GraphElement element) {
        Boundary boundary = (Boundary) element;
        int centerX = getX(match);
        int centerY = getY(match);
        int width = boundary.getWidth();
        int height = boundary.getHeight();
        boundary.setTopLeftX(centerX - width/2);
        boundary.setTopLeftY(centerY - height/2);
    }

    @Override
    public String getProperty(GraphElement element) {
        Boundary boundary = (Boundary) element;
        return getProperty(boundary.getTopLeftX()+boundary.getWidth()/2, boundary.getTopLeftY()+boundary.getHeight()/2);
    }
}
