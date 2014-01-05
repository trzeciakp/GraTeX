package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.parser.elements.NumberParser;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.vertex.ColorVertexParser;
import pl.edu.agh.gratex.parser.elements.vertex.PositionVertexParser;
import pl.edu.agh.gratex.parser.elements.vertex.ShapeVertexParser;
import pl.edu.agh.gratex.parser.elements.vertex.SizeVertexParser;

/**
 *
 */
public class HyperedgeNumberParseElement extends NumberParser {
    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        hyperedge.setNumber(getNumber(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return getProperty(hyperedge.getNumber());
    }
}
