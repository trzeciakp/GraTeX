package pl.edu.agh.gratex.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.parser.elements.ColorMapper;

import java.awt.*;

/**
 *
 */
public class LabelEdgeParserTest {
    private static final String TEST_STRING = "\\node at (35.0pt, -88.125pt) {\\textcolor{black}{Label}};";
    private static final ColorMapper COLOR_MAPPER = Mockito.mock(ColorMapper.class);
    private static final String EXPECTED_COLOR_TEXT = "black";
    private static final Color EXPECTED_COLOR = Mockito.mock(Color.class);

    @BeforeClass
    public static void prepareColorMapper() {
        Mockito.when(COLOR_MAPPER.getColor(EXPECTED_COLOR_TEXT)).thenReturn(EXPECTED_COLOR);
        Mockito.when(COLOR_MAPPER.getColorText(EXPECTED_COLOR)).thenReturn(EXPECTED_COLOR_TEXT);
    }

    @Test
    public void testParse() throws Exception {

        LabelEdgeParser testParser = new LabelEdgeParser(COLOR_MAPPER);
    }

    @Test
    public void testUnparse() throws Exception {

    }
}
