package pl.edu.agh.gratex.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LinkBoundaryParserTest {
    private static final double COEFFICIENT = 0.625;
    private static final String TEST_STRING_SIMPLE = "\\draw (0.0pt, 0.0pt) to (6.25pt, -6.25pt) [line width=0.625pt, color=black, dashed, ->, >=latex];%0,-1,3.33,4.52";
    private static final ColorMapper COLOR_MAPPER = Mockito.mock(ColorMapper.class);
    private static final GraphElementFactory FACTORY = Mockito.mock(GraphElementFactory.class);
    private static final String EXPECTED_COLOR_TEXT = "black";
    private static final Color EXPECTED_COLOR = Mockito.mock(Color.class);
    private static final Graph MOCKED_GRAPH = Mockito.mock(Graph.class);
    private static final double EXPECTED_LINE_WIDTH_TEXT = 0.625;
    public static final int EXPECTED_LINE_WIDTH = (int) (EXPECTED_LINE_WIDTH_TEXT/COEFFICIENT);
    public static final LineType EXPECTED_LINE_TYPE = LineType.DASHED;
    public static final boolean EXPECTED_IS_DIRECTED = true;
    public static final int EXPECTED_OUT_Y = 0;
    public static final int EXPECTED_OUT_X = 0;
    public static final int EXPECTED_IN_X = 10;
    public static final int EXPECTED_IN_Y = -10;
    public static final double EXPECTED_IN_ANGLE = 3.33;
    public static final double EXPECTED_OUT_ANGLE = 4.52;
    public static final ArrowType EXPECTED_ARROW_TYPE = ArrowType.FILLED;
    private static final Boundary EXPECTED_BOUNDARY_A = Mockito.mock(Boundary.class);
    private static final Boundary EXPECTED_BOUNDARY_B = Mockito.mock(Boundary.class);
    private static final int EXPECTED_BOUNDARY_A_NUMBER = 0;
    private static final int EXPECTED_BOUNDARY_B_NUMBER = -1;


    @BeforeClass
    public static void prepareColorMapper() {
        Mockito.when(COLOR_MAPPER.getColor(EXPECTED_COLOR_TEXT)).thenReturn(EXPECTED_COLOR);
        Mockito.when(COLOR_MAPPER.getColorText(EXPECTED_COLOR)).thenReturn(EXPECTED_COLOR_TEXT);
        Mockito.when(MOCKED_GRAPH.getBoundaryById(EXPECTED_BOUNDARY_A_NUMBER)).thenReturn(EXPECTED_BOUNDARY_A);
        Mockito.when(MOCKED_GRAPH.getBoundaryById(EXPECTED_BOUNDARY_B_NUMBER)).thenReturn(EXPECTED_BOUNDARY_B);
        Mockito.when(EXPECTED_BOUNDARY_A.getNumber()).thenReturn(EXPECTED_BOUNDARY_A_NUMBER);
        Mockito.when(EXPECTED_BOUNDARY_B.getNumber()).thenReturn(EXPECTED_BOUNDARY_B_NUMBER);
    }

    @Test
    public void testParseToLatex() throws Exception {
        LinkBoundary mockedLinkBoundary = Mockito.mock(LinkBoundary.class);
        Mockito.when(mockedLinkBoundary.getInPointX()).thenReturn(EXPECTED_IN_X);
        Mockito.when(mockedLinkBoundary.getInPointX()).thenReturn(EXPECTED_IN_Y);
        Mockito.when(mockedLinkBoundary.getOutPointX()).thenReturn(EXPECTED_OUT_X);
        Mockito.when(mockedLinkBoundary.getOutPointY()).thenReturn(EXPECTED_OUT_Y);
        Mockito.when(mockedLinkBoundary.getArrowType()).thenReturn(EXPECTED_ARROW_TYPE);
        Mockito.when(mockedLinkBoundary.isDirected()).thenReturn(EXPECTED_IS_DIRECTED);
        Mockito.when(mockedLinkBoundary.getLineWidth()).thenReturn(EXPECTED_LINE_WIDTH);
        Mockito.when(mockedLinkBoundary.getLineColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(mockedLinkBoundary.getLineType()).thenReturn(EXPECTED_LINE_TYPE);
        Mockito.when(mockedLinkBoundary.getBoundaryA()).thenReturn(EXPECTED_BOUNDARY_A);
        Mockito.when(mockedLinkBoundary.getBoundaryB()).thenReturn(EXPECTED_BOUNDARY_B);
        Mockito.when(mockedLinkBoundary.getInAngle()).thenReturn(EXPECTED_IN_ANGLE);
        Mockito.when(mockedLinkBoundary.getOutAngle()).thenReturn(EXPECTED_OUT_ANGLE);

        LinkBoundaryParser testObject = new LinkBoundaryParser(COLOR_MAPPER, FACTORY);

        String result = testObject.parseToLatex(mockedLinkBoundary);

        assertEquals(TEST_STRING_SIMPLE, result);
    }

    @Test
    public void testParseToGraph() throws Exception {
        LinkBoundary mockedLinkBoundary = Mockito.mock(LinkBoundary.class);
        Mockito.when(mockedLinkBoundary.getGraph()).thenReturn(MOCKED_GRAPH);
        Mockito.when(FACTORY.create(GraphElementType.LINK_BOUNDARY, MOCKED_GRAPH)).thenReturn(mockedLinkBoundary);
        LinkBoundaryParser testObject = new LinkBoundaryParser(COLOR_MAPPER, FACTORY);

        testObject.parseToGraph(TEST_STRING_SIMPLE, MOCKED_GRAPH);

        Mockito.verify(mockedLinkBoundary).setLineType(EXPECTED_LINE_TYPE);
        Mockito.verify(mockedLinkBoundary).setLineColor(EXPECTED_COLOR);
        Mockito.verify(mockedLinkBoundary).setDirected(EXPECTED_IS_DIRECTED);
        Mockito.verify(mockedLinkBoundary).setLineWidth(EXPECTED_LINE_WIDTH);
        Mockito.verify(mockedLinkBoundary).setArrowType(EXPECTED_ARROW_TYPE);
        Mockito.verify(mockedLinkBoundary).setInPointX(EXPECTED_IN_X);
        Mockito.verify(mockedLinkBoundary).setInPointY(EXPECTED_IN_Y);
        Mockito.verify(mockedLinkBoundary).setOutPointX(EXPECTED_OUT_X);
        Mockito.verify(mockedLinkBoundary).setOutPointY(EXPECTED_OUT_Y);
        Mockito.verify(mockedLinkBoundary).setBoundaryA(EXPECTED_BOUNDARY_A);
        Mockito.verify(mockedLinkBoundary).setBoundaryB(EXPECTED_BOUNDARY_B);
        Mockito.verify(mockedLinkBoundary).setInAngle(EXPECTED_IN_ANGLE);
        Mockito.verify(mockedLinkBoundary).setOutAngle(EXPECTED_OUT_ANGLE);
    }
}
