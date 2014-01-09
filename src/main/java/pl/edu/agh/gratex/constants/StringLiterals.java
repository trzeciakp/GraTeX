package pl.edu.agh.gratex.constants;

import pl.edu.agh.gratex.model.graph.GraphNumeration;

public class StringLiterals {
    //==================================================
    // Miscellaneous

    // "alpha" letter used to mark angles
    public final static String ALPHA = Character.toString('\u03B1');

    //==================================================
    // Window titles

    // General
    public final static String TITLE_CONFIRM_DIALOG = "Confirm decision";
    public final static String TITLE_ERROR_DIALOG = "Error";

    // MainWindow
    public final static String TITLE_MAIN_WINDOW = "GraTeX - graph code generator for LaTeX (TikZ)";

    // GraphsTemplateEditor
    public final static String TITLE_GRAPH_TEMPLATE_EDITOR = "Graph template editor";

    // GridDialog
    public final static String TITLE_GRID_DIALOG = "Grid properties";

    // LatexCodeDialog
    public final static String TITLE_LATEX_CODE_DIALOG = "LaTeX code for graph";

    // AboutDialog
    public final static String TITLE_ABOUT_DIALOG = "About GraTeX";

    // NumerationDialog
    public final static String TITLE_NUMERATION_DIALOG = "Numeration preferences";

    // OpenFileDialog
    public final static String TITLE_OPEN_FILE_DIALOG = "Open graph file...";

    // SaveFileDialog
    public final static String TITLE_SAVE_FILE_DIALOG = "Save as...";


    // ==================================================
    // Popup dialogs, error messages etc.

    public final static String MESSAGE_ERROR_CRITICAL = "The application has encountered a critical error and will now terminate:\n";

    // SaveFileDialog
    public final static String MESSAGE_ERROR_SAVE_GRAPH = "Saving graph failed.\nMake sure you have write permissions in the target location.";

    // OpenFileDialog
    public final static String MESSAGE_ERROR_OPEN_GRAPH = "Loading graph failed.\nThe chosen file has either improper format or is damaged.";

    public final static String MESSAGE_ERROR_GET_RESOURCE = "Unable to load resources. The .jar file might be corrupted.";

    // GraphsTemplateEditor
    public final static String MESSAGE_CONFIRM_GLOBAL_APPLY = "Current settings will be applied to ALL existing elements of the graph.\nDo you wish to continue?";

    // SaveFileDialog
    public final static String MESSAGE_CONFIRM_OVERWRITE_FILE = "The file exists, overwrite?";

    // AboutDialog
    public final static String MESSAGE_ABOUT_DIALOG = "GraTeX version 1.1\n\nAuthors: Łukasz Opioła, Piotr Trzeciak\n\nUniversity of Science and Technology\nKraków, Poland, 2012";


    //==================================================
    // Labels, buttons etc.

    // General
    public final static String BUTTON_GENERAL_OK = "OK";
    public final static String BUTTON_GENERAL_CANCEL = "Cancel";

    // GraphsTemplateEditor
    public final static String LABEL_TEMPLATE_EDITOR_HEADER = "Edit graph properties, according to which new elements will be created.";
    public final static String BUTTON_TEMPLATE_EDITOR_DISCARD = "Discard";
    public final static String TOOLTIP_TEMPLATE_EDITOR_DISCARD = "Discard all changes";

    public final static String BUTTON_TEMPLATE_EDITOR_SAVE = "Save";
    public final static String TOOLTIP_TEMPLATE_EDITOR_SAVE = "Save changes";

    public final static String BUTTON_TEMPLATE_EDITOR_RESTORE_DEFAULT = "Restore default settings";
    public final static String TOOLTIP_TEMPLATE_EDITOR_RESTORE_DEFAULT = "Reset values to default";

    public final static String CHECKBOX_TEMPLATE_EDITOR_GLOBAL_APPLY = "Apply to existing items";
    public final static String TOOLTIP_TEMPLATE_EDITOR_GLOBAL_APPLY = "Above settings will be applied to all existing elements of the graph";

    // GridDialog
    public final static String LABEL_GRID_DIALOG_HORIZONTAL_SPACING = "Horizontal spacing (px):";
    public final static String LABEL_GRID_DIALOG_VERTICAL_SPACING = "Vertical spacing (px):";

    // LatexCodeDialog
    public final static String BUTTON_LATEX_DIALOG_COPY_TO_CLIPBOARD = "Copy to clipboard";
    public final static String MENU_ITEM_LATEX_DIALOG_COPY_TO_CLIPBOARD = "Copy to clipboard";
    public final static char MNEMONIC_LATEX_DIALOG_COPY_TO_CLIPBOARD = 'C';

    // NumerationDialog
    public final static String RADIO_BUTTON_NUMERATION_DIALOG_DIGITAL = "digital numeration";
    public final static String RADIO_BUTTON_NUMERATION_DIALOG_ALPHABETICAL = "alphabetical numeration";
    public final static String LABEL_NUMERATION_DIALOG_STARTING_NUMBER = "Starting number:";

    // OpenFileDialog
    public final static String BUTTON_OPEN_FILE_DIALOG_APPROVE = "Open";
    public final static String TOOLTIP_OPEN_FILE_DIALOG_APPROVE = "Open selected graph";
    public final static String TOOLTIP_OPEN_FILE_DIALOG_DESCRIPTION = "Graph file (*" + Const.GRAPH_FILES_EXTENSION + ")";

    // SaveFileDialog
    public final static String BUTTON_SAVE_FILE_DIALOG_APPROVE = "Save";
    public final static String TOOLTIP_SAVE_FILE_DIALOG_APPROVE = "Save graph under selected filename";
    public final static String TOOLTIP_SAVE_FILE_DIALOG_DESCRIPTION = "Graph file (*" + Const.GRAPH_FILES_EXTENSION + ")";

    // PanelPreview
    public final static String LABEL_PANEL_PREVIEW_TITLE = "Preview";

    // PanelToolbox
    public final static String COMBOBOX_PANEL_TOOLBOX_MODE = "Edition mode";

    // PropertyPanels
    public static final String LABEL_VERTEX_POSITION = "Position:";
    public static final String LABEL_VERTEX_DISTANCE = "Distance:";
    public static final String EMPTY_VALUE = " ";
    public static final String PX_SUFFIX = " px";
    public static final String PERCENT_SUFFIX = " %";
    public static final String DEG_SUFFIX = " deg";
    public static final String LABEL_VERTEX_TEXT = "Text:";
    public static final String LABEL_VERTEX_TEXT_COLOR = "Text color:";
    public static final String LABEL_EDGE_ROTATION = "Rotation";
    public static final String LABEL_EDGE_PLACEMENT = "Placement:";
    public static final String LABEL_EDGE_TEXT = "Text:";
    public static final String LABEL_EDGE_POSITION = "Position:";
    public static final String LABEL_EDGE_DISTANCE = "Distance:";
    public static final String LABEL_EDGE_TEXT_COLOR = "Text color:";
    public static final String EDGE_LINE_TYPE = "Line type:";
    public static final String EDGE_LINE_WIDTH = "Line width:";
    public static final String EDGE_LINE_COLOR = "Line color:";
    public static final String EDGE_DIRECTED = "Directed:";
    public static final String EDGE_ARROW_TYPE = "Arrow type:";
    public static final String EDGE_ANGLE = "Angle:";
    public static final String VERTEX_SHAPE_TYPE = "Vertex type:";
    public static final String VERTEX_SIZE = "Vertex size:";
    public static final String VERTEX_COLOR = "Vertex color:";
    public static final String VERTEX_LINE_TYPE = "Line type:";
    public static final String VERTEX_LINE_COLOR = "Line color:";
    public static final String VERTEX_LABEL_INSIDE = "Label inside:";
    public static final String VERTEX_NUMBER = "Number:";
    public static final String VERTEX_LINE_WIDTH = "Line width:";
    public static final String VERTEX_FONT_COLOR = "Font color:";
    public static final String BOUNDARY_FILL_COLOR = "Fill color:";
    public static final String BOUNDARY_LINE_TYPE = "Line type:";
    public static final String HYPEREDGE_JOINT_SHAPE_TYPE = "Joint type:";
    public static final String HYPEREDGE_JOINT_SIZE = "Joint size:";
    public static final String HYPEREDGE_JOINT_COLOR = "Joint color:";
    public static final String HYPEREDGE_LINE_TYPE = "Line type:";
    public static final String HYPEREDGE_LINE_WIDTH = "Line width:";
    public static final String HYPEREDGE_LINE_COLOR = "Line color:";
    public static final String HYPEREDGE_JOINT_DISPLAY = "Joint display:";


    //==================================================
    // Messages that go to label_info in MainWindow

    // This should go before message when undoing changes
    private final static String UNDO_PREFIX = "[UNDONE] ";

    public static String INFO_UNDO(String literal) {
        return UNDO_PREFIX + literal;
    }

    // This should go before message when redoing changes
    private final static String REDO_PREFIX = "[REDONE] ";

    public static String INFO_REDO(String literal) {
        return REDO_PREFIX + literal;
    }

    // AddOperation
    public final static String INFO_VERTEX_ADD = "vertex added";
    public final static String INFO_EDGE_ADD = "edge added";
    public final static String INFO_LABEL_V_ADD = "label added to a vertex";
    public final static String INFO_LABEL_E_ADD = "label added to an edge";
    public final static String INFO_HYPEREDGE_ADD = "hyperedge added";
    public final static String INFO_BOUNDARY_ADD = "boundary added";
    public final static String INFO_LINK_BOUNDARY_ADD = "link (boundary) added";


    // DragOperation
    public final static String INFO_VERTEX_MOVE = "vertex moved";
    public final static String INFO_EDGE_MOVE = "edge moved";
    public final static String INFO_LABEL_V_MOVE = "label (vertex) moved";
    public final static String INFO_LABEL_E_MOVE = "label (edge) moved";
    public final static String INFO_HYPEREDGE_JOINT_MOVE = "hyperedge's joint moved";
    public static final String INFO_HYPEREDGE_EDIT = "hyperedge edited";
    public static final String INFO_HYPEREDGE_EXTEND = "vertex added to a hyperedge";
    public final static String INFO_BOUNDARY_MOVE = "boundary moved";
    public final static String INFOLINK_BOUNDARY_MOVE = "link (boundary) moved";

    // OldOperationList
    public final static String INFO_NOTHING_TO_UNDO = "nothing to undo";
    public final static String INFO_NOTHING_TO_REDO = "nothing to redo";

    // PropertyChangeOperation
    public final static String INFO_PROPERTY_CHANGE = "property changed";

    // RemoveOperation
    public static String INFO_REMOVE_ELEMENT(ModeType type, int number) {
        String elementName = number > 1 ? type.getRelatedElementType().getPluralName() : type.getRelatedElementType().getSingularName();
        String amount = number > 1 ? number + " " : "";
        return amount + elementName + " removed";
    }

    public final static String INFO_NOTHING_TO_REMOVE = "nothing to remove";

    // TemplateChangeOperation
    public final static String INFO_TEMPLATE_APPLIED_GLOBALLY = "template applied globally";
    public final static String INFO_TEMPLATE_CHANGE = "template changed";

    // ControlManager
    public final static String INFO_SUBGRAPH_DUPLICATE = "supgraph duplicated";
    public final static String INFO_SUBGRAPH_WHERE_TO_PASTE = "choose location for the copy of subgraph";
    public final static String INFO_SUBGRAPH_CANNOT_PASTE = "cannot insert this subgraph here - vertices collide with existing ones or are out of bounds";

    public final static String INFO_CANNOT_CREATE_VERTEX_BOUNDARY = "cannot createEmptyModel a vertex here - too close to page edge";
    public final static String INFO_CANNOT_CREATE_VERTEX_COLLISION = "cannot createEmptyModel a vertex here - too close to another vertex";

    public final static String INFO_CHOOSE_EDGE_START = "choose a starting vertex for the edge (click)";
    public final static String INFO_CHOOSE_EDGE_END = "now choose the target vertex (click)";
    public final static String INFO_EDGE_ADDING_CANCELLED = "adding edge cancelled";

    public final static String INFO_CHOOSE_VERTEX_FOR_LABEL = "choose a vertex to attach label to";
    public final static String INFO_CANNOT_CREATE_LABEL_V_EXISTS = "cannot create the label, as this vertex already has one";

    public final static String INFO_CHOOSE_EDGE_FOR_LABEL = "choose an edge to attach label to";
    public final static String INFO_CANNOT_CREATE_LABEL_E_EXISTS = "cannot create the label, as this edge already has one";

    public final static String INFO_CHOOSE_BOUNDARY_END = "choose opposite corner for the boundary";

    public static final String INFO_CHOOSE_HYPEREDGE_START = "choose a starting vertex for the hyperedge (click)";
    public static final String INFO_CHOOSE_HYPEREDGE_END = "now choose the second vertex (click)";
    public static final String INFO_HYPEREDGE_ADDING_CANCELLED = "adding hyperedge cancelled";
    public final static String INFO_HYPEREDGE_HOW_TO_EXPAND = "Drag joint to add a vertex. Drag an edge to edit. " +
            "CTRL + drag to move joint around - SHIFT for auto-center.";

    // SaveFileDialog
    public final static String INFO_GRAPH_SAVE_OK = "graph saved successfully";
    public final static String INFO_GRAPH_SAVE_FAIL = "saving graph failed. Make sure you have write permissions in the target location.";

    // OpenFileDialog
    public final static String INFO_GRAPH_OPEN_OK = "graph loaded successfully";
    public final static String INFO_GRAPH_OPEN_FAIL = "loading graph failed!";

    // MainWindow
    public static String INFO_MODE_AND_TOOL(ModeType mode, ToolType tool) {
        String tipPart1 = mode.toString() + " mode - ";
        String tipPart3 = "";
        String tipPart4a = "";
        String tipPart4b = "";

        switch (mode) {
            case VERTEX: {
                tipPart4a = "a vertex.";
                tipPart4b = "a vertex.";
                break;
            }
            case EDGE: {
                tipPart4a = "an edge. Hold down SHIFT for directed edge. Hold down CTRL for straight edge.";
                tipPart4b = "an edge.";
                break;
            }
            case LABEL_VERTEX: {
                tipPart4a = "a label to a vertex.";
                tipPart4b = "a label of a vertex.";
                break;
            }
            case LABEL_EDGE: {
                tipPart4a = "a label to an edge. Hold down SHIFT for horizontal label.";
                tipPart4b = "a label of an edge.";
                break;
            }
            case HYPEREDGE: {
                tipPart4a = "a hyperedge. " + INFO_HYPEREDGE_HOW_TO_EXPAND;
                tipPart4b = "a hyperedge.";
                break;
            }
            case BOUNDARY: {
                tipPart4a = "a boundary.";
                tipPart4b = "a boundary.";
                break;
            }
            case LINK_BOUNDARY: {
                tipPart4a = "a link to a boundary.";
                tipPart4b = "a link of aboundary.";
                break;
            }
        }


        String tipPart2 = tool.toString() + " tool.";
        switch (tool) {
            case ADD: {
                tipPart3 = " Click to add " + tipPart4a;
                break;
            }
            case REMOVE: {
                tipPart3 = " Click to remove " + tipPart4b + " Click and drag to remove all items in the area.";
                break;
            }
            case SELECT: {
                tipPart3 = " Click to select " + tipPart4b
                        + " Drag to select all items in the area. CTRL + click/drag to extend selection. Move selected items by dragging them.";
                break;
            }
        }

        return tipPart1 + tipPart2 + tipPart3;
    }

    public static String INFO_GENERIC_GRID(boolean gridOn, int gridResolutionX, int gridResolutionY) {
        if (gridOn) {
            return String.format("%dx%d grid enabled", gridResolutionX, gridResolutionY);
        } else {
            return "grid disabled";
        }
    }

    public static String INFO_GENERIC_NUMERATION(boolean digital, int startingNumber) {
        String numerationType = digital ? "digital" : "alphabetical";
        String startingNumString = digital ? Integer.toString(startingNumber) : GraphNumeration.digitalToAlphabetical(startingNumber);
        return String.format("%s numeration enabled (starting with '%s')", numerationType, startingNumString);
    }

    public static String INFO_GENERIC_SELECT_ALL(ModeType mode){
        return "all " + mode.getRelatedElementType().getPluralName() + " selected";
    }
}
