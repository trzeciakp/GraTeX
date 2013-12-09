package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.graph.Vertex;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;

/**
 *
 */
public class VertexParser extends GraphElementParser {
    @Override
    public String parse(GraphElement graphElement) {
        Vertex vertex = (Vertex) graphElement;
        String line = "\\node (" + vertex.getNumber() + ") ";
        if (vertex.getShape() == 1) {
            line += "[circle"; //ksztalt
        } else {
            line += "[regular polygon, regular polygon sides=" + (vertex.getShape() + 1);
        }
        line += ", minimum size=" + (1.25 * vertex.getRadius()) + "pt"; //wielkosc
        if (vertex.getVertexColor() != null)
            line += ", fill=" + PropertyModel.COLORS.get(vertex.getVertexColor());
        line += ", line width=" + 0.625 * vertex.getLineWidth() + "pt";
        if (vertex.getLineType() != LineType.NONE) {
            line += ", draw";
            if (vertex.getLineColor() != null)
                line += "=" + PropertyModel.COLORS.get(vertex.getLineColor());
            if (vertex.getLineType() == LineType.DASHED)
                line += ", dashed";
            else if (vertex.getLineType() == LineType.DOTTED)
                line += ", dotted";
            else if (vertex.getLineType() == LineType.DOUBLE)
                line += ", double";
        }
        line += "] at (" + 0.625 * vertex.getPosX() + "pt, " + 0.625 * (-vertex.getPosY()) + "pt) ";
        if (vertex.isLabelInside()) {
            if (vertex.getFontColor() != null)
                line += "{\\textcolor{" + PropertyModel.COLORS.get(vertex.getFontColor()) + "}{" + vertex.getNumber() + "}};\n";
            else
                line += "{" + vertex.getNumber() + "}";
        } else
            line += " {};\n";
        return line;
    }

    @Override
    public GraphElement unparse(String code, Graph graph) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
