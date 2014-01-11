package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.boundary.*;

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
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\begin{scope}[on background layer] ", false));
        parseList.add(new StaticParseElement("\\node ", false));
        parseList.add(new BoundaryNumberParseElement());
        parseList.add(new StaticParseElement(" [", false));
        parseList.add(new BoundaryResolutionParseElement());
        parseList.add(new BoundaryLineWidthParseElement());
        parseList.add(new BoundaryLineColorTypeParseElement(colorMapper));
        parseList.add(new BoundaryFillColorParseElement(colorMapper));
        parseList.add(new StaticParseElement("] at ", false));
        parseList.add(new BoundaryPositionParseElement());
        parseList.add(new StaticParseElement("{};\\end{scope}", false));
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.BOUNDARY;
    }
}
