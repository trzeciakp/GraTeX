package pl.edu.agh.gratex.parser;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.hyperedge.*;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;
import pl.edu.agh.gratex.parser.elements.hyperedge.HyperedgeLabelParseElement;

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
        init();
    }

    @Override
    public List<ParseElement> createParseList() {
        List<ParseElement> parseList = new ArrayList<>();
        parseList.add(new StaticParseElement("\\node ", false));
        parseList.add(new HyperedgeNumberParseElement());
        parseList.add(new StaticParseElement(" [", false));
        parseList.add(new HyperedgeJointShapeParseElement());
        parseList.add(new HyperedgeJointSizeParseElement());
        parseList.add(new HyperedgeJointColorParseElement(colorMapper));
        parseList.add(new HyperedgeJointLineWidthParseElement());
        parseList.add(new HyperedgeJointLineColorTypeParseElement(colorMapper));
        parseList.add(new StaticParseElement("] at ", false));
        parseList.add(new HyperedgeJointPositionParseElement());
        parseList.add(new StaticParseElement("{}; \\begin{scope}[", false));
        parseList.add(new HyperedgeLineWidthParseElement());
        parseList.add(new HyperedgeLineTypeParseElement());
        parseList.add(new HyperedgeLineColorParseElement(colorMapper));
        parseList.add(new StaticParseElement("]", false));
        parseList.add(new HyperedgeVerticesParseElement());
        parseList.add(new HyperedgeLabelParseElement(colorMapper));
        parseList.add(new StaticParseElement("\\end{scope}", false));
        parseList.add(new HyperedgeCommentedParametersParseElement());
        return parseList;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.HYPEREDGE;
    }

  /*  public static void main(String[] args) {
        Pattern testP = Pattern.compile("(b)?((a[0-9]a)+)(test)");
        Pattern testP2 = Pattern.compile("(a([0-9])(a))");
        String[] testS = { "ba1atest", "ba2aa3atest", "a4aa5atest"};
        for (String s : testS) {
            test(s, testP2);
        }
    }

    private static void test(String toTest, Pattern pattern) {
        Matcher matcher = pattern.matcher(toTest);
        matcher.matches();
        for(int i = 0; i < matcher.groupCount()+1; i++) {
            System.out.println(i+": "+matcher.group(i));
        }
        while(matcher.find()) {
           System.out.println(matcher.group(2));
           System.out.println(matcher.group(2));
        }
        //System.out.println(matcher.groupCount());
        System.out.println();
    }*/
}
