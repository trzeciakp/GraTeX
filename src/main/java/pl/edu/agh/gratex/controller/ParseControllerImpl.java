package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

public class ParseControllerImpl implements ParseController {
    public static final String TEMPLATES_SEPARATOR = "%TEMPLATES";
    private EnumMap<GraphElementType, GraphElementParser> parsers = new EnumMap<GraphElementType, GraphElementParser>(GraphElementType.class);
    private GraphElementFactory graphElementFactory;

    public ParseControllerImpl(GraphElementControllersFactory factory, GraphElementFactory graphElementFactory) {
        this.graphElementFactory = graphElementFactory;
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
            List<GraphElement> elements = new LinkedList<>(graph.getElements(graphElementType));
            GraphElement.sortByDrawingPriorities(elements);
            for (GraphElement graphElement : elements) {
                result.add(parser.parseToLatex(graphElement));
            }
        }
        return result;
    }

    @Override
    public Graph parseLatexCodeWithTemplatesToGraph(List<String> codeLines) throws ParserException {
        Graph result = new Graph();
        Graph templateGraph = new Graph();
        int t = 0;
        int currentTemplateType = 0;
        boolean templates = false;
        for (String codeLine : codeLines) {
            if(codeLine.equals(TEMPLATES_SEPARATOR)) {
                templates = true;
                continue;
            }
            if(templates) {
                if(currentTemplateType < GraphElementType.values().length) {
                    GraphElementType type = GraphElementType.values()[currentTemplateType++];
                    GraphElement templateElement = tryToParse(codeLine.substring(1), templateGraph, type);
//                    templateElement.addToGraph();
                    graphElementFactory.getPropertyModelFactory().setTemplateModel(type, templateElement.getModel());
                } else {
                    throw new ParserException();
                }
            } else {
                t = incrementThroughTypesAndParse(codeLine, result,  t);
            }
        }
        return result;
    }

    @Override
    public List<String> parseTemplatesToLatexCode() {
        List<String> result = new ArrayList<>();
        result.add(TEMPLATES_SEPARATOR);
        Graph exampleGraph = graphElementFactory.createExampleGraph();
        for (GraphElementType graphElementType : GraphElementType.values()) {
            GraphElementParser parser = parsers.get(graphElementType);
            result.add("%"+parser.parseToLatex(exampleGraph.getElements(graphElementType).get(0)));
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
