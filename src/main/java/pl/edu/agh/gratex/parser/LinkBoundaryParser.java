package pl.edu.agh.gratex.parser;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.link.*;

import java.util.ArrayList;
import java.util.List;

public class LinkBoundaryParser extends GraphElementParser {
    private ColorMapper colorMapper;

    public LinkBoundaryParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;

        init();
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\draw ", false));
        parseList.add(new LinkBoundaryInPositionParseElement());
        parseList.add(new StaticParseElement("to ", false));
        parseList.add(new LinkBoundaryOutPositionParseElement());
        parseList.add(new StaticParseElement("[", false));
        parseList.add(new LinkBoundaryLineWidthParseElement());
        parseList.add(new LinkBoundaryLineColorParseElement(colorMapper));
        parseList.add(new LinkBoundaryLineTypeParseElement());
        parseList.add(new LinkBoundaryDirectionParseElement());
        parseList.add(new StaticParseElement("];", false));
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LINK_BOUNDARY;
    }
}
