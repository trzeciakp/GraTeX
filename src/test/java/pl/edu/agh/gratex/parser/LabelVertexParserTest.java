package pl.edu.agh.gratex.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LabelVertexParserTest {
    private static final double COEFFICIENT = 0.625;
    public static final String TEST_STRING = "\\node at (193.75pt, -195.625pt) {\\textcolor{black}{Label}};";
    public static final double EXPECTED_TEXT_POS_X = 193.75;
    public static final int EXPECTED_POS_X = (int) (EXPECTED_TEXT_POS_X / COEFFICIENT);
    public static final double EXPECTED_TEXT_POS_Y = 195.625;
    public static final int EXPECTED_POS_Y = (int) (EXPECTED_TEXT_POS_Y / COEFFICIENT);
    public static final String EXPECTED_TEXT = "Label";

    private static final Color EXPECTED_COLOR = Mockito.mock(Color.class);
    private static final String EXPECTED_COLOR_TEXT = "black";
    private static final ColorMapper COLOR_MAPPER = Mockito.mock(ColorMapper.class);

    @BeforeClass
    public static void prepareColorMapper() {
        Mockito.when(COLOR_MAPPER.getColor(EXPECTED_COLOR_TEXT)).thenReturn(EXPECTED_COLOR);
        Mockito.when(COLOR_MAPPER.getColorText(EXPECTED_COLOR)).thenReturn(EXPECTED_COLOR_TEXT);
    }

    @Test
    public void testUnparse() throws Exception {
        LabelVertexParser testObject = new LabelVertexParser(COLOR_MAPPER);

        LabelV result = (LabelV) testObject.unparse(TEST_STRING, Mockito.mock(Graph.class));

        assertEquals(EXPECTED_POS_X, result.getPosX());
        assertEquals(EXPECTED_POS_Y, result.getPosY());
        assertEquals(EXPECTED_COLOR, result.getFontColor());
        assertEquals(EXPECTED_TEXT, result.getText());
    }

    @Test
    public void testParse() {
        LabelVertexParser testObject = new LabelVertexParser(COLOR_MAPPER);
        LabelV labelV = Mockito.mock(LabelV.class);
        Mockito.when(labelV.getText()).thenReturn(EXPECTED_TEXT);
        Mockito.when(labelV.getFontColor()).thenReturn(EXPECTED_COLOR);
        Mockito.when(labelV.getPosX()).thenReturn(EXPECTED_POS_X);
        Mockito.when(labelV.getPosY()).thenReturn(EXPECTED_POS_Y);

        String result = testObject.parse(labelV);

        assertEquals(TEST_STRING, result);


    }
}


