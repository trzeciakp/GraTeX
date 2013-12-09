package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.graph.Edge;
import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;

/**
 *
 */
public class EdgeParser extends GraphElementParser {
    @Override
    public String parse(GraphElement graphElement) {
        Edge edge = (Edge) graphElement;
        String line = "\\draw [line width=" + 0.625 * edge.getLineWidth();
        if (edge.isDirected()) {
            line += ", ->";
            if (edge.getArrowType() == ArrowType.FILLED.getValue()) line += ", >=latex";
        }
        if (edge.getLineType() == LineType.DASHED)
            line += ", dashed";
        else if (edge.getLineType() == LineType.DOTTED)
            line += ", dotted";
        else if (edge.getLineType() == LineType.DOUBLE)
            line += ", double";
        if (edge.getLineColor() != null)
            line += ", color=" + PropertyModel.COLORS.get(edge.getLineColor());
        //petla
        if (edge.getVertexA() == edge.getVertexB()) {
            line += ", loop ";
            switch (edge.getRelativeEdgeAngle()) {
                case 0:
                    line += "right";
                    break;
                case 90:
                    line += "above";
                    break;
                case 180:
                    line += "left";
                    break;
                case 270:
                    line += "below";
                    break;
            }
            line += "] (" + edge.getVertexA().getNumber() + ") to (" + edge.getVertexB().getNumber() + ");\n";
        } else {
            line += "]";
            line += " (" + edge.getVertexA().getNumber() + ") to ";
            if (edge.getRelativeEdgeAngle() > 0)
                line += " [in=" + edge.getInAngle() + ", out=" + edge.getOutAngle() + "]";

            line += " (" + edge.getVertexB().getNumber() + ");\n";
        }
        return line;
    }

    @Override
    public GraphElement unparse(String code, Graph graph) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
