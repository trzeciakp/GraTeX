package pl.edu.agh.gratex.constants;

import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.utils.DrawingTools;

import java.awt.*;

public class Const {
    // MainWindow size constraints
    public final static int MAIN_WINDOW_MIN_WIDTH = 800;
    public final static int MAIN_WINDOW_MIN_HEIGHT = 600;
    public final static int MAIN_WINDOW_DEFAULT_WIDTH = 1038;
    public final static int MAIN_WINDOW_DEFAULT_HEIGHT = 768;

    // Extension of GraTeX graph files
    public final static String GRAPH_FILES_EXTENSION = ".gph";

    // Page dimensions in pixels
    public final static int PAGE_WIDTH = 672;
    public final static int PAGE_HEIGHT = 880;

    // Color used to denote selection on graph elements
    public final static Color SELECTION_COLOR = new Color(72, 118, 255, 128);

    // Color of selection rectangle inside
    public final static Color SELECTION_RECT_INSIDE_COLOR = new Color(72, 118, 255, 128);
    // Color of selection rectangle border
    public final static Color SELECTION_RECT_BORDER_COLOR = new Color(72, 118, 255, 128);


    // Color of deletion rectangle inside
    public final static Color DELETION_RECT_INSIDE_COLOR = new Color(255, 80, 80, 128);
    // Color of deletion rectangle border
    public final static Color DELETION_RECT_BORDER_COLOR = new Color(255, 0, 0, 128);


    // Color used to draw grid
    public final static Color GRID_COLOR = new Color(200, 200, 200);

    // Limit of operations to keep in memory. When exceeded, oldest operations will be deleted
    public static final int MAX_REMEMBERED_OPERATIONS = 1024;

    // Maximum number tht a vertex can have. Alphabetical "ZZ" corresponds to digital 703
    public final static int MAX_VERTEX_NUMBER = 703;

    // Default font for drawing graph elements
    public final static Font DEFAULT_FONT = new Font("Cambria", Font.PLAIN, 16);

    // Basic arrow head length
    public final static int ARROW_LENGTH_BASIC = 10;
    // Ratio: length of arrow head to lineWidth
    public final static int ARROW_LENGTH_FACTOR = 2;

    // How far from edge will selection work
    public final static int EDGE_SELECTION_MARGIN = 10;

    // Stroke used to draw angle visualization
    public final static Stroke ANGLE_VISUALIZATION_STROKE = DrawingTools.getStroke(LineType.DASHED, 2, 0.0);
    // Color used to draw angle visualization
    public final static Color ANGLE_VISUALIZATION_COLOR = Color.gray;
}
