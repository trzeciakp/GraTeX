package pl.edu.agh.gratex.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class VertexParserTest {
    private static final double COEFFICIENT = 0.625;
    private static final ColorMapper COLOR_MAPPER = Mockito.mock(ColorMapper.class);
    private static final String EXPECTED_COLOR_TEXT = "black";
    private static final Color EXPECTED_COLOR = Mockito.mock(Color.class);
    private static final Graph MOCKED_GRAPH = Mockito.mock(Graph.class);
    private static final Vertex MOCKED_VERTEX = Mockito.mock(Vertex.class);

    public static final LineType EXPECTED_DEFAULT_LINE_TYPE = LineType.SOLID;

    private static final String TEST_STRING_SIMPLE = "\\node (1) [circle, minimum size=50.0pt, fill=black, line width=0.625pt, draw=black, dashed] at (71.25pt, -56.25pt) {\\textcolor{black}{1}};";

    private static final String TEST_STRING_FULL = "\\node (1) [regular polygon, regular polygon sides=3, minimum size=50.0pt, fill=black, line width=0.625pt, draw=black, dashed] at (71.25pt, -56.25pt) {\\textcolor{black}{1}};";

    private static final double EXPECTED_LINE_WIDTH_FULL_TEXT = 0.625;
    public static final int EXPECTED_LINE_WIDTH_FULL = (int) (EXPECTED_LINE_WIDTH_FULL_TEXT/COEFFICIENT);
    private static final ShapeType EXPECTED_SHAPE_TYPE_FULL = ShapeType.TRIANGLE;
    private static final double EXPECTED_SIZE_IN_PT_FULL = 50.0;
    private static final double EXPECTED_POS_X_IN_PT_FULL = 71.25;
    private static final double EXPECTED_POS_Y_IN_PT_FULL = 56.25;
    public static final int EXPECTED_SIZE_FULL = (int) (EXPECTED_SIZE_IN_PT_FULL/COEFFICIENT);
    private static final int EXPECTED_NUMBER_FULL = 1;
    private static final LineType EXPECTED_LINE_TYPE_FULL = LineType.DASHED;


    @BeforeClass
    public static void prepareColorMapper() {
        Mockito.when(COLOR_MAPPER.getColor(EXPECTED_COLOR_TEXT)).thenReturn(EXPECTED_COLOR);
        Mockito.when(COLOR_MAPPER.getColorText(EXPECTED_COLOR)).thenReturn(EXPECTED_COLOR_TEXT);
    }

    @Test
    public void testToLatexFull() throws Exception {
        Mockito.when(MOCKED_VERTEX.getNumber()).thenReturn(EXPECTED_NUMBER_FULL);
        Mockito.when(MOCKED_VERTEX.getShapeENUM()).thenReturn(EXPECTED_SHAPE_TYPE_FULL);
        Mockito.when(MOCKED_VERTEX.getRadius()).thenReturn(getIntFromPt(EXPECTED_SIZE_IN_PT_FULL/2));
        Mockito.when(MOCKED_VERTEX.getVertexColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(MOCKED_VERTEX.getLineWidth()).thenReturn(EXPECTED_LINE_WIDTH_FULL);
        Mockito.when(MOCKED_VERTEX.getLineColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(MOCKED_VERTEX.getLineType()).thenReturn(EXPECTED_LINE_TYPE_FULL);
        Mockito.when(MOCKED_VERTEX.getPosX()).thenReturn(getIntFromPt(EXPECTED_POS_X_IN_PT_FULL));
        Mockito.when(MOCKED_VERTEX.getPosY()).thenReturn(getIntFromPt(EXPECTED_POS_Y_IN_PT_FULL));
        Mockito.when(MOCKED_VERTEX.getFontColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(MOCKED_VERTEX.isLabelInside()).thenReturn(true);

        VertexParser testObject = new VertexParser(COLOR_MAPPER);

        String result = testObject.parseToLatex(MOCKED_VERTEX);

        assertEquals(TEST_STRING_FULL, result);
    }

    @Test
    public void testUnparse() throws Exception {
        VertexParser testObject = new VertexParser(COLOR_MAPPER);

        Vertex vertex = testObject.parseToGraph(TEST_STRING_FULL, MOCKED_GRAPH);

        assertEquals(EXPECTED_NUMBER_FULL, vertex.getNumber());
        assertEquals(EXPECTED_SHAPE_TYPE_FULL, vertex.getShapeENUM());
        assertEquals(getIntFromPt(EXPECTED_SIZE_IN_PT_FULL/2), vertex.getRadius());
        assertEquals(getIntFromPt(EXPECTED_POS_X_IN_PT_FULL), vertex.getPosX());
        assertEquals(getIntFromPt(EXPECTED_POS_Y_IN_PT_FULL), vertex.getPosY());
        assertEquals(EXPECTED_COLOR, vertex.getVertexColor());
        assertEquals(EXPECTED_COLOR, vertex.getFontColor());
        assertEquals(EXPECTED_COLOR, vertex.getLineColor());
        assertEquals(EXPECTED_LINE_WIDTH_FULL, vertex.getLineWidth());
        assertEquals(EXPECTED_LINE_TYPE_FULL, vertex.getLineType());
        assertEquals(true, vertex.isLabelInside());

    }

    private int getIntFromPt(double pt) {
        return (int) (pt/COEFFICIENT);
    }
}
