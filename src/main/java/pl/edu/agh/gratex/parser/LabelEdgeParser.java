package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.labeledge.LabelEdgeCommentedParametersParseElement;
import pl.edu.agh.gratex.parser.elements.labeledge.LabelEdgePositionParseElement;
import pl.edu.agh.gratex.parser.elements.labeledge.LabelEdgeRotationParseElement;
import pl.edu.agh.gratex.parser.elements.labeledge.LabelEdgeTextColorParseElement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LabelEdgeParser extends GraphElementParser {
    private ColorMapper colorMapper;

    public LabelEdgeParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;
        init();
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\node at ", false));
        parseList.add(new LabelEdgePositionParseElement());
        parseList.add(new LabelEdgeRotationParseElement());
        parseList.add(new LabelEdgeTextColorParseElement(colorMapper));
        parseList.add(new StaticParseElement(";", false));
        parseList.add(new LabelEdgeCommentedParametersParseElement());
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LABEL_EDGE;
    }
}

