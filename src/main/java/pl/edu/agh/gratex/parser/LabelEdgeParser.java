package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.PropertyModel;

/**
 *
 */
public class LabelEdgeParser extends GraphElementParser {
    @Override
    public String parse(GraphElement graphElement) {
        LabelE labelEdge = (LabelE) graphElement;
        String line = "\\node at (" + 0.625 * labelEdge.getPosX() + "pt, " + 0.625 * (-labelEdge.getPosY()) + "pt) ";
        if (labelEdge.getAngle() > 0)
            line += "[rotate=" + labelEdge.getAngle() + "] ";
        if (labelEdge.getFontColor() != null)
            line += "{\\textcolor{" + PropertyModel.COLORS.get(labelEdge.getFontColor()) + "}{" + labelEdge.getText() + "}};\n";
        else
            line += "{" + labelEdge.getText() + "}";
        return line;
    }

    @Override
    public GraphElement unparse(String code, Graph graph) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
