package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.vertex.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        parseList.add(new NumberVertexParser());
        parseList.add(new StaticParseElement(" [", false));
        parseList.add(new ShapeVertexParser());
        parseList.add(new SizeVertexParser());
        parseList.add(new ColorVertexParser(colorMapper));
        parseList.add(new StaticParseElement(", ", false));
        parseList.add(new LineWidthVertexParser());
        parseList.add(new LineColorTypeVertexParser(colorMapper));
        parseList.add(new StaticParseElement("] at ", false));
        parseList.add(new PositionVertexParser());
        parseList.add(new TextColorVertexParser(colorMapper));
        parseList.add(new StaticParseElement(";", false));
        //parseList.add(new CommentedParametersVertexParser());
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.VERTEX;
    }
}
