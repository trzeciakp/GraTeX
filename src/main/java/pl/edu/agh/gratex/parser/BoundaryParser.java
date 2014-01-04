package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BoundaryParser extends GraphElementParser {
    private ColorMapper colorMapper;

    public BoundaryParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;
        init();
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> result = new ArrayList<>();
        return result;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.BOUNDARY;
    }
}
