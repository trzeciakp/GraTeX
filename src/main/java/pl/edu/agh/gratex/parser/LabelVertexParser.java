package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.labelvertex.CommentedParametersLabelVertexParser;
import pl.edu.agh.gratex.parser.elements.labelvertex.LabelVertexPositionParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.labelvertex.LabelVertexTextColorParseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
public class LabelVertexParser extends GraphElementParser {
    private ColorMapper colorMapper;

    public LabelVertexParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;
        init();
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\node at ", false));
        parseList.add(new LabelVertexPositionParseElement());
        parseList.add(new LabelVertexTextColorParseElement(colorMapper));
        parseList.add(new StaticParseElement(";", false));
        parseList.add(new CommentedParametersLabelVertexParser());
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LABEL_VERTEX;
    }
}
