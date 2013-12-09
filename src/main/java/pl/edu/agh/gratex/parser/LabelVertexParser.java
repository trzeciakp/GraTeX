package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.graph.LabelV;
import pl.edu.agh.gratex.model.PropertyModel;

/**
 *
 */
public class LabelVertexParser extends GraphElementParser {
    @Override
    public String parse(GraphElement graphElement) {
        LabelV labelVertex = (LabelV) graphElement;
        String line = "\\node at (" + 0.625 * labelVertex.getPosX() + "pt, " + 0.625 * (-labelVertex.getPosY()) + "pt) ";
        if (labelVertex.getFontColor() != null)
            line += "{\\textcolor{" + PropertyModel.COLORS.get(labelVertex.getFontColor()) + "}{" + labelVertex.getText() + "}};\n";
        else
            line += "{" + labelVertex.getText() + "}";
        return line;
    }

    @Override
    public GraphElement unparse(String code, Graph graph) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
