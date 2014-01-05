package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.LineWidthParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

/**
 *
 */
public class LineWidthVertexParser extends LineWidthParseElement {

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
        Vertex vertex = (Vertex) element;
        vertex.setLineWidth((match == null ? 0 : getX(match)));
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        int lineWidth = vertex.getLineWidth();
        return (lineWidth == 0 ? "":", " + getProperty(lineWidth));
    }
}
