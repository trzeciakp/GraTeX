package pl.edu.agh.gratex.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EdgeParserTest {
    private static final double COEFFICIENT = 0.625;
    private static final String TEST_STRING_SIMPLE = "\\draw [line width=0.625pt, color=black] (1) to (2);";
    private static final String TEST_STRING_IN_OUT = "\\draw [line width=0.625pt, color=black] (1) to [in=286, out=346] (2);%300";
    private static final ColorMapper COLOR_MAPPER = Mockito.mock(ColorMapper.class);
    private static final GraphElementFactory FACTORY = Mockito.mock(GraphElementFactory.class);
    private static final String EXPECTED_COLOR_TEXT = "black";
    private static final Color EXPECTED_COLOR = Mockito.mock(Color.class);
    private static final Graph MOCKED_GRAPH = Mockito.mock(Graph.class);
    public static final int VERTEX_A_NUMBER = 1;
    public static final int VERTEX_B_NUMBER = 2;
    private static final Vertex EXPECTED_VERTEX_A = Mockito.mock(Vertex.class);
    private static final Vertex EXPECTED_VERTEX_B = Mockito.mock(Vertex.class);
    private static final double EXPECTED_LINE_WIDTH_TEXT = 0.625;
    public static final int EXPECTED_LINE_WIDTH = (int) (EXPECTED_LINE_WIDTH_TEXT/COEFFICIENT);
    private static final int EXPECTED_ANGLE = 0;
    private static final int EXPECTED_ANGLE_IN_OUT = 300;
    public static final LineType EXPECTED_DEFAULT_LINE_TYPE = LineType.SOLID;


    @BeforeClass
    public static void prepareColorMapper() {
        Mockito.when(COLOR_MAPPER.getColor(EXPECTED_COLOR_TEXT)).thenReturn(EXPECTED_COLOR);
        Mockito.when(COLOR_MAPPER.getColorText(EXPECTED_COLOR)).thenReturn(EXPECTED_COLOR_TEXT);
        Mockito.when(MOCKED_GRAPH.getVertexById(VERTEX_A_NUMBER)).thenReturn(EXPECTED_VERTEX_A);
        Mockito.when(MOCKED_GRAPH.getVertexById(VERTEX_B_NUMBER)).thenReturn(EXPECTED_VERTEX_B);
        Mockito.when(EXPECTED_VERTEX_A.getNumber()).thenReturn(VERTEX_A_NUMBER);
        Mockito.when(EXPECTED_VERTEX_B.getNumber()).thenReturn(VERTEX_B_NUMBER);

        Mockito.when(FACTORY.create(GraphElementType.EDGE, MOCKED_GRAPH)).thenReturn(new Edge(MOCKED_GRAPH, new EdgePropertyModel()));
        Mockito.when(FACTORY.create(GraphElementType.LABEL_EDGE, MOCKED_GRAPH)).thenReturn(new LabelE(null, MOCKED_GRAPH, new LabelEdgePropertyModel()));
        Mockito.when(FACTORY.create(GraphElementType.VERTEX, MOCKED_GRAPH)).thenReturn(new Vertex(MOCKED_GRAPH, new VertexPropertyModel()));
        Mockito.when(FACTORY.create(GraphElementType.LABEL_VERTEX, MOCKED_GRAPH)).thenReturn(new LabelV(null, MOCKED_GRAPH, new LabelVertexPropertyModel()));
    }

    @Test
    public void testParseToGraphSimple() throws Exception {
        EdgeParser testObject = new EdgeParser(COLOR_MAPPER, FACTORY);

        Edge result = (Edge) testObject.parseToGraph(TEST_STRING_SIMPLE, MOCKED_GRAPH);

        assertEquals(EXPECTED_COLOR, result.getLineColor());
        assertEquals(EXPECTED_VERTEX_A, result.getVertexA());
        assertEquals(EXPECTED_VERTEX_B, result.getVertexB());
        assertEquals(EXPECTED_LINE_WIDTH, result.getLineWidth());
        assertEquals(EXPECTED_ANGLE, result.getRelativeEdgeAngle());
    }

    @Test
    public void testParseToLatexSimple() throws Exception {
        EdgeParser testObject = new EdgeParser(COLOR_MAPPER, FACTORY);
        Edge edge = Mockito.mock(Edge.class);
        Mockito.when(edge.getLineColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(edge.getRelativeEdgeAngle()).thenReturn(EXPECTED_ANGLE);
        Mockito.when(edge.getVertexA()).thenReturn(EXPECTED_VERTEX_A);
        Mockito.when(edge.getVertexB()).thenReturn(EXPECTED_VERTEX_B);
        Mockito.when(edge.getGraph()).thenReturn(MOCKED_GRAPH);
        Mockito.when(edge.getLineWidth()).thenReturn(EXPECTED_LINE_WIDTH);
        Mockito.when(edge.getLineType()).thenReturn(EXPECTED_DEFAULT_LINE_TYPE);

        String result = testObject.parseToLatex(edge);

        assertEquals(TEST_STRING_SIMPLE, result);
    }


    @Test
    public void testParseToGraphInOut() throws Exception {
        EdgeParser testObject = new EdgeParser(COLOR_MAPPER, FACTORY);

        Edge result = (Edge) testObject.parseToGraph(TEST_STRING_IN_OUT, MOCKED_GRAPH);

        assertEquals(EXPECTED_COLOR, result.getLineColor());
        assertEquals(EXPECTED_VERTEX_A, result.getVertexA());
        assertEquals(EXPECTED_VERTEX_B, result.getVertexB());
        assertEquals(EXPECTED_LINE_WIDTH, result.getLineWidth());
        assertEquals(EXPECTED_ANGLE_IN_OUT, result.getRelativeEdgeAngle());
    }

    @Test
    public void testParseToLatexInOut() throws Exception {
        EdgeParser testObject = new EdgeParser(COLOR_MAPPER, FACTORY);
        Edge edge = Mockito.mock(Edge.class);
        Mockito.when(edge.getLineColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(edge.getRelativeEdgeAngle()).thenReturn(EXPECTED_ANGLE);
        Mockito.when(edge.getVertexA()).thenReturn(EXPECTED_VERTEX_A);
        Mockito.when(edge.getVertexB()).thenReturn(EXPECTED_VERTEX_B);
        Mockito.when(edge.getGraph()).thenReturn(MOCKED_GRAPH);
        Mockito.when(edge.getLineWidth()).thenReturn(EXPECTED_LINE_WIDTH);
        Mockito.when(edge.getLineType()).thenReturn(EXPECTED_DEFAULT_LINE_TYPE);

        String result = testObject.parseToLatex(edge);

        assertEquals(TEST_STRING_SIMPLE, result);
    }
}
