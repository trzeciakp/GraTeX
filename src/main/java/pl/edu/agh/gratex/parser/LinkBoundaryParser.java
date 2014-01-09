package pl.edu.agh.gratex.parser;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.List;

public class LinkBoundaryParser extends GraphElementParser {
    private ColorMapper colorMapper;

    public LinkBoundaryParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;
        // TODO wylaczylem bo by sie program nie wlaczyl
        //init();
    }

    @Override
    public List<ParseElement> createParseList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LINK_BOUNDARY;
    }
}
