package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.vertex.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class VertexParser extends GraphElementParser {
    private ColorMapper colorMapper;

    public VertexParser(ColorMapper colorMapper, GraphElementFactory graphElementFactory) {
        super(graphElementFactory);
        this.colorMapper = colorMapper;
        init();
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\node ", false));
        parseList.add(new VertexNumberParseElement());
        parseList.add(new StaticParseElement(" [", false));
        parseList.add(new VertexShapeParseElement());
        parseList.add(new VertexSizeParseElement());
        parseList.add(new VertexColorParseElement(colorMapper));
        //parseList.add(new StaticParseElement(", ", false));
        parseList.add(new VertexLineWidthParseElement());
        parseList.add(new VertexLineColorTypeParseElement(colorMapper));
        parseList.add(new StaticParseElement("] at ", false));
        parseList.add(new VertexPositionParseElement());
        parseList.add(new VertexTextColorParseElement(colorMapper));
        parseList.add(new StaticParseElement(";", false));
        //parseList.add(new CommentedParametersVertexParser());
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.VERTEX;
    }
}
