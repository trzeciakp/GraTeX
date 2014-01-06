package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.parser.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class ParseControllerImpl implements ParseController {
    private EnumMap<GraphElementType, GraphElementParser> parsers = new EnumMap<GraphElementType, GraphElementParser>(GraphElementType.class);

    public ParseControllerImpl(GraphElementControllersFactory factory) {
        for (GraphElementType type : GraphElementType.values()) {
            parsers.put(type, factory.createGraphElementParser(type));
        }
    }

    @Override
    public GraphElementParser getParserByElementType(GraphElementType graphElementType) {
        return parsers.get(graphElementType);
    }

    @Override
    public List<String> parseGraphToLatexCode(Graph graph) {
        List<String> result = new ArrayList<>();
        for (GraphElementType graphElementType : GraphElementType.values()) {
            GraphElementParser parser = parsers.get(graphElementType);
            List<GraphElement> elements = graph.getElements(graphElementType);
            GraphElement.sortByDrawingPriorities(elements);
            for (GraphElement graphElement : elements) {
                result.add(parser.parseToLatex(graphElement));
            }
        }
        return result;
    }

    @Override
    public Graph parseLatexCodeToGraph(List<String> codeLines) {
        Graph result = new Graph();
        int t = 0;
        for (String codeLine : codeLines) {
            try {
                t = incrementThroughTypesAndParse(codeLine, result,  t);
            } catch (ParserException e) {
                //TODO
                //TODO
                //TODO
                //TODO
                e.printStackTrace();
            }
        }
        return result;
    }

    private int incrementThroughTypesAndParse(String codeLine, Graph graph, int t) throws ParserException {
        GraphElementType[] types = GraphElementType.values();
        while(t < types.length) {
            try {
                GraphElement ge = tryToParse(codeLine, graph, types[t]);
                ge.addToGraph();
                return t;
            } catch (ParserException e) {
                t++;
            }
        }
        throw new ParserException();
    }

    private GraphElement tryToParse(String code, Graph graph, GraphElementType type) throws ParserException {
        return parsers.get(type).parseToGraph(code, graph);
    }
}
