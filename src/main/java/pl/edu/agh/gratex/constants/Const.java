package pl.edu.agh.gratex.constants;

import java.awt.*;

public class Const {

    // Extension of GraTeX graph files
    public final static String GRAPH_FILES_EXTENSION = ".gph";

    // Color used to denote selection
    public final static Color SELECTION_COLOR = new Color(72, 118, 255, 128);

    // Maximum number tht a vertex can have. Alphabetical "ZZ" corresponds to digital 703.
    public final static int MAX_VERTEX_NUMBER = 703;

    // Default font for drawing graph elements
    public final static Font DEFAULT_FONT = new Font("Cambria", Font.PLAIN, 16);

    // Basic arrow head length
    public final static int ARROW_LENGTH_BASIC = 10;
    // Ratio: length of arrow head to lineWidth
    public final static int ARROW_LENGTH_FACTOR = 2;
}
