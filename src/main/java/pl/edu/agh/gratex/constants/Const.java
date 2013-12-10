package pl.edu.agh.gratex.constants;

import java.awt.*;

public class Const {

    // Extension of GraTeX graph files
    public final static String GRAPH_FILES_EXTENSION = ".gph";

    // Color used to denote selection on graph elements
    public final static Color SELECTION_COLOR = new Color(72, 118, 255, 128);


    // Color of selection rectangle inside
    public final static Color SELECTION_RECT_INSIDE_COLOR = new Color(72, 118, 255, 128);
    // Color of selection rectangle border
    public final static Color SELECTION_RECT_BORDER_COLOR = new Color(72, 118, 255, 128);


    // Color used to draw grid
    public final static Color GRID_COLOR = new Color(200, 200, 200);


    public static final int MAX_REMEMBERED_OPERATIONS = 1024;

    // Maximum number tht a vertex can have. Alphabetical "ZZ" corresponds to digital 703.
    public final static int MAX_VERTEX_NUMBER = 703;

    // Default font for drawing graph elements
    public final static Font DEFAULT_FONT = new Font("Cambria", Font.PLAIN, 16);

    // Basic arrow head length
    public final static int ARROW_LENGTH_BASIC = 10;
    // Ratio: length of arrow head to lineWidth
    public final static int ARROW_LENGTH_FACTOR = 2;
}
