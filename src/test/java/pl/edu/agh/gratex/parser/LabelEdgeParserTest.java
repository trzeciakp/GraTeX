package pl.edu.agh.gratex.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.properties.LabelHorizontalPlacement;
import pl.edu.agh.gratex.model.properties.LabelTopPlacement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LabelEdgeParserTest {
    private static final double COEFFICIENT = 0.625;
    private static final String TEST_STRING = "\\node at (35.0pt, -88.125pt) {\\textcolor{black}{Label}};%1,BELOW,50,LEVEL,10,";
    private static final ColorMapper COLOR_MAPPER = Mockito.mock(ColorMapper.class);
    private static final String EXPECTED_COLOR_TEXT = "black";
    private static final Color EXPECTED_COLOR = Mockito.mock(Color.class);
    public static final Graph MOCKED_GRAPH = Mockito.mock(Graph.class);
    public static final Edge EXPECTED_EDGE = Mockito.mock(Edge.class);
    public static final double EXPECTED_TEXT_POS_X = 35.0;
    private static final int EXPECTED_POS_X = (int) (EXPECTED_TEXT_POS_X / COEFFICIENT);
    private static final double EXPECTED_TEXT_POS_Y = 88.125;
    public static final int EXPECTED_POS_Y = (int) (EXPECTED_TEXT_POS_Y / COEFFICIENT);
    public static final String EXPECTED_TEXT = "Label";
    public static final LabelHorizontalPlacement EXPECTED_HORIZONTAL_PLACEMENT = LabelHorizontalPlacement.LEVEL;
    private static final int EXPECTED_SPACING = 10;
    private static final LabelTopPlacement EXPECTED_TOP_PLACEMENT = LabelTopPlacement.BELOW;
    public static final int EXPECTED_POSITION = 50;
    private static final GraphElementFactory FACTORY = Mockito.mock(GraphElementFactory.class);

    @BeforeClass
    public static void prepareColorMapper() {
        Mockito.when(COLOR_MAPPER.getColor(EXPECTED_COLOR_TEXT)).thenReturn(EXPECTED_COLOR);
        Mockito.when(COLOR_MAPPER.getColorText(EXPECTED_COLOR)).thenReturn(EXPECTED_COLOR_TEXT);
        Mockito.when(MOCKED_GRAPH.getEdgeById(1)).thenReturn(EXPECTED_EDGE);
        Mockito.when(EXPECTED_EDGE.getNumber()).thenReturn(1);
        Mockito.when(FACTORY.create(GraphElementType.EDGE, MOCKED_GRAPH)).thenReturn(new Edge(MOCKED_GRAPH));
        Mockito.when(FACTORY.create(GraphElementType.LABEL_EDGE, MOCKED_GRAPH)).thenReturn(new LabelE(null, MOCKED_GRAPH));
        Mockito.when(FACTORY.create(GraphElementType.VERTEX, MOCKED_GRAPH)).thenReturn(new Vertex(MOCKED_GRAPH));
        Mockito.when(FACTORY.create(GraphElementType.LABEL_VERTEX, MOCKED_GRAPH)).thenReturn(new LabelV(null, MOCKED_GRAPH));
    }

    @Test
    public void testParse() throws Exception {
        LabelEdgeParser testObject = new LabelEdgeParser(COLOR_MAPPER, FACTORY);

        LabelE result = (LabelE) testObject.parseToGraph(TEST_STRING, MOCKED_GRAPH);

        assertEquals(EXPECTED_POS_X, result.getPosX());
        assertEquals(EXPECTED_POS_Y, result.getPosY());
        assertEquals(EXPECTED_COLOR, result.getFontColor());
        assertEquals(EXPECTED_TEXT, result.getText());
        assertEquals(EXPECTED_EDGE, result.getOwner());
        assertEquals(EXPECTED_HORIZONTAL_PLACEMENT, result.getHorizontalPlacement());
        assertEquals(EXPECTED_SPACING, result.getSpacing());
        assertEquals(EXPECTED_TOP_PLACEMENT, result.getTopPlacement());
        assertEquals(EXPECTED_POSITION, result.getPosition());
    }

    @Test
    public void testUnparse() throws Exception {
        LabelEdgeParser testObject = new LabelEdgeParser(COLOR_MAPPER, FACTORY);
        LabelE labelE = Mockito.mock(LabelE.class);
        Mockito.when(labelE.getText()).thenReturn(EXPECTED_TEXT);
        Mockito.when(labelE.getFontColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(labelE.getPosX()).thenReturn(EXPECTED_POS_X);
        Mockito.when(labelE.getPosY()).thenReturn(EXPECTED_POS_Y);
        Mockito.when(labelE.getOwner()).thenReturn(EXPECTED_EDGE);
        Mockito.when(labelE.getGraph()).thenReturn(MOCKED_GRAPH);
        Mockito.when(labelE.getSpacing()).thenReturn(EXPECTED_SPACING);
        Mockito.when(labelE.getPosition()).thenReturn(EXPECTED_POSITION);
        Mockito.when(labelE.getTopPlacement()).thenReturn(EXPECTED_TOP_PLACEMENT);
        Mockito.when(labelE.getHorizontalPlacement()).thenReturn(EXPECTED_HORIZONTAL_PLACEMENT);

        String result = testObject.parseToLatex(labelE);

        assertEquals(TEST_STRING, result);
    }
}
