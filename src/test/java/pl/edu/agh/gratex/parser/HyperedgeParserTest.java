package pl.edu.agh.gratex.parser;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.properties.JointLabelPosition;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class HyperedgeParserTest {
    public static final String TEST_STRING = "\\node (4) [circle, minimum size=5.0pt, fill=blue, line width=5.625pt, draw=dupa, dashed] at (195.625pt, -172.5pt) {}; \\begin{scope}[line width=1.875pt, color=olive] \\draw (1) to (4); \\draw (2) to (4); " +
            "\\node (below) at (0.0pt, 12.5pt) {test};\\end{scope}";
    public static final int JOINT_SIZE = 4;
    public static final ShapeType SHAPE_CIRCLE = ShapeType.CIRCLE;
    public static final int JOINT_NUMBER = 4;
    public static final int VERTEX_A_NUMBER = 1;
    private static final double COEFFICIENT = 0.625;
    public static final int JOINT_CENTER_X = (int) (195.625/COEFFICIENT);
    public static final int JOINT_CENTER_Y = (int) (172.5/COEFFICIENT);
    public static final int VERTEX_B_NUMBER = 2;
    public static final int LINE_WIDTH = 3;
    public static final LineType SOLID = LineType.SOLID;
    public static final Color MOCKED_BLUE = Mockito.mock(Color.class);
    public static final Color MOCKED_OLIVE = Mockito.mock(Color.class);
    public static final ColorMapper MOCKED_COLOR_MAPPER = Mockito.mock(ColorMapper.class);
    public static final GraphElementFactory MOCKED_FACTORY = Mockito.mock(GraphElementFactory.class);
    public static final Graph MOCKED_GRAPH = Mockito.mock(Graph.class);
    public static final List<Vertex> CONNECTED = new ArrayList<>();
    public static final String BLUE_COLOR_NAME = "blue";
    public static final String OLIVE_COLOR_NAME = "olive";
    public static final JointLabelPosition EXPECTED_JOINT_LABEL_POSITION = JointLabelPosition.BELOW;
    public static final double LABEL_X_TEXT = 0.0;
    public static final double LABEL_Y_TEXT = 12.5;
    public static final int LABEL_POS_X = (int) (LABEL_X_TEXT/COEFFICIENT);
    public static final int LABEL_POS_Y = (int) (-LABEL_Y_TEXT/COEFFICIENT);
    public static final String LABEL_TEXT = "test";
    public static final int JOINT_LINE_WIDTH = (int) (5.625/COEFFICIENT);
    public static final Color JOINT_LINE_COLOR = Mockito.mock(Color.class);
    public static final String JOINT_LINE_COLOR_TEXT = "dupa";
    public static final LineType JOINT_LINE_TYPE = LineType.DASHED;


    @BeforeClass
    public static void setUp() {
        Mockito.when(MOCKED_COLOR_MAPPER.getColor(BLUE_COLOR_NAME)).thenReturn(MOCKED_BLUE);
        Mockito.when(MOCKED_COLOR_MAPPER.getColor(OLIVE_COLOR_NAME)).thenReturn(MOCKED_OLIVE);
        Mockito.when(MOCKED_COLOR_MAPPER.getColor(JOINT_LINE_COLOR_TEXT)).thenReturn(JOINT_LINE_COLOR);
        Mockito.when(MOCKED_COLOR_MAPPER.getColorText(MOCKED_BLUE)).thenReturn(BLUE_COLOR_NAME);
        Mockito.when(MOCKED_COLOR_MAPPER.getColorText(MOCKED_OLIVE)).thenReturn(OLIVE_COLOR_NAME);
        Mockito.when(MOCKED_COLOR_MAPPER.getColorText(JOINT_LINE_COLOR)).thenReturn(JOINT_LINE_COLOR_TEXT);

        Vertex mockedVertexA = Mockito.mock(Vertex.class);
        Vertex mockedVertexB = Mockito.mock(Vertex.class);
        Mockito.when(mockedVertexA.getNumber()).thenReturn(VERTEX_A_NUMBER);
        Mockito.when(mockedVertexB.getNumber()).thenReturn(VERTEX_B_NUMBER);
        Mockito.when(MOCKED_GRAPH.getVertexById(VERTEX_A_NUMBER)).thenReturn(mockedVertexA);
        Mockito.when(MOCKED_GRAPH.getVertexById(VERTEX_B_NUMBER)).thenReturn(mockedVertexB);
        CONNECTED.add(mockedVertexA);
        CONNECTED.add(mockedVertexB);
    }

    @Test
    public void testParseToLatex() throws Exception {
        Hyperedge mockedHyperedge = Mockito.mock(Hyperedge.class);

        Mockito.when(mockedHyperedge.getJointSize()).thenReturn(JOINT_SIZE);
        Mockito.when(mockedHyperedge.getJointShape()).thenReturn(SHAPE_CIRCLE);
        Mockito.when(mockedHyperedge.getJointColor()).thenReturn(MOCKED_BLUE);
        Mockito.when(mockedHyperedge.getNumber()).thenReturn(JOINT_NUMBER);
        Mockito.when(mockedHyperedge.getJointCenterX()).thenReturn(JOINT_CENTER_X);
        Mockito.when(mockedHyperedge.getJointCenterY()).thenReturn(JOINT_CENTER_Y);
        Mockito.when(mockedHyperedge.getConnectedVertices()).thenReturn(CONNECTED);
        Mockito.when(mockedHyperedge.getLineWidth()).thenReturn(LINE_WIDTH);
        Mockito.when(mockedHyperedge.getLineType()).thenReturn(SOLID);
        Mockito.when(mockedHyperedge.getLineColor()).thenReturn(MOCKED_OLIVE);
        Mockito.when(mockedHyperedge.getGraph()).thenReturn(MOCKED_GRAPH);
        Mockito.when(mockedHyperedge.getJointLabelPosition()).thenReturn(EXPECTED_JOINT_LABEL_POSITION);
        Mockito.when(mockedHyperedge.getLabelPosX()).thenReturn(LABEL_POS_X);
        Mockito.when(mockedHyperedge.getLabelPosY()).thenReturn(LABEL_POS_Y);
        Mockito.when(mockedHyperedge.getText()).thenReturn(LABEL_TEXT);
        Mockito.when(mockedHyperedge.getJointLineColor()).thenReturn(JOINT_LINE_COLOR);
        Mockito.when(mockedHyperedge.getJointLineWidth()).thenReturn(JOINT_LINE_WIDTH);
        Mockito.when(mockedHyperedge.getJointLineType()).thenReturn(JOINT_LINE_TYPE);


        HyperedgeParser testObject = new HyperedgeParser(MOCKED_COLOR_MAPPER, MOCKED_FACTORY);

        String code = testObject.parseToLatex(mockedHyperedge);

        assertEquals(TEST_STRING, code);

    }

    @Test
    public void testParseToGraph() throws Exception {

        Hyperedge mockedHyperedge = Mockito.mock(Hyperedge.class);
        Mockito.when(MOCKED_FACTORY.create(GraphElementType.HYPEREDGE, MOCKED_GRAPH)).thenReturn(mockedHyperedge);
        HyperedgeParser testObject = new HyperedgeParser(MOCKED_COLOR_MAPPER, MOCKED_FACTORY);
        Mockito.when(mockedHyperedge.getGraph()).thenReturn(MOCKED_GRAPH);
        Mockito.when(mockedHyperedge.getNumber()).thenReturn(JOINT_NUMBER);

        testObject.parseToGraph(TEST_STRING, MOCKED_GRAPH);

        Mockito.verify(mockedHyperedge).setJointSize(JOINT_SIZE);
        Mockito.verify(mockedHyperedge).setJointShape(SHAPE_CIRCLE);
        Mockito.verify(mockedHyperedge).setJointColor(MOCKED_BLUE);
        Mockito.verify(mockedHyperedge).setNumber(JOINT_NUMBER);
        Mockito.verify(mockedHyperedge).setJointCenterX(JOINT_CENTER_X);
        Mockito.verify(mockedHyperedge).setJointCenterY(JOINT_CENTER_Y);
        Mockito.verify(mockedHyperedge).setConnectedVertices(Mockito.eq(CONNECTED));
        Mockito.verify(mockedHyperedge).setLineWidth(LINE_WIDTH);
        Mockito.verify(mockedHyperedge).setLineType(SOLID);
        Mockito.verify(mockedHyperedge).setLineColor(MOCKED_OLIVE);
        Mockito.verify(mockedHyperedge).setText(LABEL_TEXT);
        Mockito.verify(mockedHyperedge).setLabelPosX(LABEL_POS_X);
        Mockito.verify(mockedHyperedge).setLabelPosY(LABEL_POS_Y);
        Mockito.verify(mockedHyperedge).setText(LABEL_TEXT);

    }
}
