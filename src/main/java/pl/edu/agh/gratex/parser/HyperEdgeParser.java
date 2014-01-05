package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.vertex.NumberVertexParser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HyperedgeParser extends GraphElementParser {
    private ColorMapper colorMapper;

    public HyperedgeParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\node ", false));
        parseList.add(new NumberVertexParser());
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.HYPEREDGE;
    }
}
