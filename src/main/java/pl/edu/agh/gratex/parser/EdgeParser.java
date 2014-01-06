package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.edge.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EdgeParser extends GraphElementParser {

    private ColorMapper colorMapper;

    public EdgeParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;
        init();
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\draw [", false));
        parseList.add(new EdgeLineWidthParseElement());
        parseList.add(new EdgeDirectionParseElement());
        parseList.add(new EdgeLineTypeParseElement());
        parseList.add(new EdgeLineColorParseElement(colorMapper));
        parseList.add(new EdgeLoopParseElement());
        parseList.add(new StaticParseElement("] ", false));
        parseList.add(new EdgeVerticesParseElement());
        parseList.add(new StaticParseElement(";", false));
        parseList.add(new EdgeCommentedParametersParseElement());
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.EDGE;
    }
}
