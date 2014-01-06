package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.PositionParseElement;

/**
 *
 */
public class VertexPositionParseElement extends PositionParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        Vertex vertex = (Vertex) element;
        vertex.setPosX(getX(match));
        vertex.setPosY(getY(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        return getProperty(vertex.getPosX(), vertex.getPosY());
    }
}
