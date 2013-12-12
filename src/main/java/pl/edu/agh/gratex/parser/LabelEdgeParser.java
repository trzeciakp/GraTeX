package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.labeledge.LabelEdgePositionParser;
import pl.edu.agh.gratex.parser.elements.labeledge.LabelEdgeRotationParser;
import pl.edu.agh.gratex.parser.elements.labeledge.LabelEdgeTextColorParseElement;
import pl.edu.agh.gratex.parser.elements.labelvertex.LabelVertexPositionParseElement;
import pl.edu.agh.gratex.parser.elements.labelvertex.LabelVertexTextColorParseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
public class LabelEdgeParser extends GraphElementParser {
    private final Pattern pattern;
    private List<ParseElement> parseList = new ArrayList<>();

    public LabelEdgeParser(ColorMapper colorMapper) {
        parseList.add(new StaticParseElement("\\node at ", false));
        parseList.add(new LabelEdgePositionParser());
        parseList.add(new LabelEdgeRotationParser());
        parseList.add(new LabelEdgeTextColorParseElement(colorMapper));
        parseList.add(new StaticParseElement(";", false));
        pattern = evaluatePattern();
    }

    @Override
    public String parse(GraphElement graphElement) {
        return super.parseUsingParseList(graphElement);
        /*LabelE labelEdge = (LabelE) graphElement;
        String line = "\\node at (" + 0.625 * labelEdge.getPosX() + "pt, " + 0.625 * (-labelEdge.getPosY()) + "pt) ";
        if (labelEdge.getAngle() > 0)
            line += "[rotate=" + labelEdge.getAngle() + "] ";
        if (labelEdge.getFontColor() != null)
            line += "{\\textcolor{" + PropertyModel.COLORS.get(labelEdge.getFontColor()) + "}{" + labelEdge.getText() + "}};\n";
        else
            line += "{" + labelEdge.getText() + "}";
        return line;*/
    }

    @Override
    public GraphElement unparse(String code, Graph graph) throws ParserException {
        LabelE labelE = new LabelE(null, graph);
        unparseUsingParseList(code, labelE);
        return labelE;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public List<ParseElement> getParseList() {
        return parseList;
    }
}
