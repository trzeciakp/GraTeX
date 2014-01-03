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
import pl.edu.agh.gratex.model.properties.LabelPosition;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LabelVertexParserTest {
    private static final double COEFFICIENT = 0.625;
    public static final String TEST_STRING = "\\node at (193.75pt, -195.625pt) {\\textcolor{black}{Label}};%1,NW,10,";
    public static final double EXPECTED_TEXT_POS_X = 193.75;
    public static final int EXPECTED_POS_X = (int) (EXPECTED_TEXT_POS_X / COEFFICIENT);
    public static final double EXPECTED_TEXT_POS_Y = 195.625;
    public static final int EXPECTED_POS_Y = (int) (EXPECTED_TEXT_POS_Y / COEFFICIENT);
    public static final String EXPECTED_TEXT = "Label";

    private static final Color EXPECTED_COLOR = Mockito.mock(Color.class);
    private static final String EXPECTED_COLOR_TEXT = "black";
    private static final ColorMapper COLOR_MAPPER = Mockito.mock(ColorMapper.class);
    private static final int EXPECTED_SPACING = 10;
    private static final LabelPosition EXPECTED_POSITION = LabelPosition.NW;
    public static final Graph MOCKED_GRAPH = Mockito.mock(Graph.class);
    public static final Vertex EXPECTED_VERTEX = Mockito.mock(Vertex.class);
    private static final GraphElementFactory FACTORY = Mockito.mock(GraphElementFactory.class);

    @BeforeClass
    public static void prepareColorMapper() {
        Mockito.when(COLOR_MAPPER.getColor(EXPECTED_COLOR_TEXT)).thenReturn(EXPECTED_COLOR);
        Mockito.when(COLOR_MAPPER.getColorText(EXPECTED_COLOR)).thenReturn(EXPECTED_COLOR_TEXT);
        Mockito.when(MOCKED_GRAPH.getVertexById(1)).thenReturn(EXPECTED_VERTEX);
        Mockito.when(EXPECTED_VERTEX.getNumber()).thenReturn(1);
        Mockito.when(FACTORY.create(GraphElementType.EDGE, MOCKED_GRAPH)).thenReturn(new Edge(MOCKED_GRAPH, new EdgePropertyModel()));
        Mockito.when(FACTORY.create(GraphElementType.LABEL_EDGE, MOCKED_GRAPH)).thenReturn(new LabelE(null, MOCKED_GRAPH, new LabelEdgePropertyModel()));
        Mockito.when(FACTORY.create(GraphElementType.VERTEX, MOCKED_GRAPH)).thenReturn(new Vertex(MOCKED_GRAPH, new VertexPropertyModel()));
        Mockito.when(FACTORY.create(GraphElementType.LABEL_VERTEX, MOCKED_GRAPH)).thenReturn(new LabelV(null, MOCKED_GRAPH, new LabelVertexPropertyModel()));
    }

    @Test
    public void testUnparse() throws Exception {
        LabelVertexParser testObject = new LabelVertexParser(COLOR_MAPPER, FACTORY);

        LabelV result = (LabelV) testObject.parseToGraph(TEST_STRING, MOCKED_GRAPH);

        assertEquals(EXPECTED_POS_X, result.getPosX());
        assertEquals(EXPECTED_POS_Y, result.getPosY());
        assertEquals(EXPECTED_COLOR, result.getFontColor());
        assertEquals(EXPECTED_TEXT, result.getText());
        assertEquals(EXPECTED_VERTEX, result.getOwner());
        assertEquals(EXPECTED_SPACING, result.getSpacing());
        assertEquals(EXPECTED_POSITION, result.getLabelPosition());
    }

    @Test
    public void testParse() {
        LabelVertexParser testObject = new LabelVertexParser(COLOR_MAPPER, FACTORY);
        LabelV labelV = Mockito.mock(LabelV.class);
        Mockito.when(labelV.getText()).thenReturn(EXPECTED_TEXT);
        Mockito.when(labelV.getFontColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(labelV.getPosX()).thenReturn(EXPECTED_POS_X);
        Mockito.when(labelV.getPosY()).thenReturn(EXPECTED_POS_Y);
        Mockito.when(labelV.getOwner()).thenReturn(EXPECTED_VERTEX);
        Mockito.when(labelV.getGraph()).thenReturn(MOCKED_GRAPH);
        Mockito.when(labelV.getSpacing()).thenReturn(EXPECTED_SPACING);
        Mockito.when(labelV.getLabelPosition()).thenReturn(EXPECTED_POSITION);

        String result = testObject.parseToLatex(labelV);

        assertEquals(TEST_STRING, result);


    }
}


