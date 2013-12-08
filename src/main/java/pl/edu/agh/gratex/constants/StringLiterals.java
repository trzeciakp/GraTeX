package pl.edu.agh.gratex.constants;

public class StringLiterals {
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


    //==================================================
    // Popup dialogs, error messages etc.

    public final static String MESSAGE_ERROR_GENERAL = "The application has encountered an error:\n";

    // SaveFileDialog
    public final static String MESSAGE_ERROR_SAVE_GRAPH = "Saving graph failed. Make sure you have write permissions in the target location.";

    // OpenFileDialog
    public final static String MESSAGE_ERROR_OPEN_GRAPH = "Loading graph failed.\nThe chosen file has either improper format or is damaged.";

    // GraphsTemplateEditor
    public final static String MESSAGE_CONFIRM_GLOBAL_APPLY = "Current settings will be applied to ALL existing elements of the graph.\nDo you wish to continue?";

    // SaveFileDialog
    public final static String MESSAGE_CONFIRM_OVERWRITE_FILE = "The file exists, overwrite?";

    // AboutDialog
    public final static String MESSAGE_ABOUT_DIALOG = "GraTeX version 1.0\n\nAuthors: Łukasz Opioła, Piotr Trzeciak\n\nUniversity of Science and Technology\nKraków, Poland, 2012";



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
    public final static String TOOLTIP_OPEN_FILE_DIALOG_DESCRIPTION = "Graph file (*" + Constants.GRAPH_FILES_EXTENSION + ")";

    // SaveFileDialog
    public final static String BUTTON_SAVE_FILE_DIALOG_APPROVE = "Save";
    public final static String TOOLTIP_SAVE_FILE_DIALOG_APPROVE = "Save graph under selected filename";
    public final static String TOOLTIP_SAVE_FILE_DIALOG_DESCRIPTION = "Graph file (*" + Constants.GRAPH_FILES_EXTENSION + ")";

    // PanelPreview
    public final static String LABEL_PANEL_PREVIEW_TITLE = "Preview";

    // PanelToolbox
    public final static String COMBOBOX_PANEL_TOOLBOX_MODE = "Edition mode";


    //==================================================
    // Messages that go to label_info in MainWindow

    // This should go before message when undoing changes
    private final static String UNDO_PREFIX = "[UNDONE] ";

    public static String INFO_UNDO(String literal) {
        return UNDO_PREFIX + literal;
    }

    // AddOperation
    public final static String INFO_VERTEX_ADD = "Vertex added";
    public final static String INFO_EDGE_ADD = "Edge added";
    public final static String INFO_LABEL_V_ADD = "Label added to a vertex";
    public final static String INFO_LABEL_E_ADD = "Label added to an edge";

    // CopyPasteOperation
    public final static String INFO_SUBGRAPH_PASTE = "Subgraph pasted";

    // DragOperation
    public final static String INFO_VERTEX_MOVE = "Vertex moved";
    public final static String INFO_EDGE_MOVE = "Edge moved";
    public final static String INFO_LABEL_V_MOVE = "Label (vertex) moved";
    public final static String INFO_LABEL_E_MOVE = "Label (edge) moved";

    // OperationList
    public final static String INFO_NOTHING_TO_UNDO = "Nothing to undo";
    public final static String INFO_NOTHING_TO_REDO = "Nothing to redo";

    // PropertyChangeOperation
    public final static String INFO_PROPERTY_CHANGE = "Property changed";

    // RemoveOperation
    public static String INFO_REMOVE_ELEMENT(GraphElementType type, int number) {
        String elementName = number > 1 ? type.getPluralName() : type.getSingularName();
        String amount = number > 1 ? number + " " : "";
        return amount + elementName + " removed";
    }

    // TemplateChangeOperation
    public final static String INFO_TEMPLATE_CHANGE_AND_GLOBAL_APPLY = "Template changed and applied globally";
    public final static String INFO_TEMPLATE_CHANGE = "Template changed";

    // SaveFileDialog
    public final static String INFO_GRAPH_SAVE_OK = "Graph saved successfully";
    public final static String INFO_GRAPH_SAVE_FAIL = "Saving graph failed. Make sure you have write permissions in the target location.";

    // OpenFileDialog
    public final static String INFO_GRAPH_OPEN_OK = "Graph loaded successfully";
    public final static String INFO_GRAPH_OPEN_FAIL = "Loading graph failed!";

    // MainWindow
    public static String INFO_MODE_AND_TOOL(GraphElementType mode, ToolType tool) {
        String tipPart1 = mode.toString() + " mode - ";
        String tipPart2 = null;
        String tipPart3 = null;
        String tipPart4a = null;
        String tipPart4b = null;

        switch (mode) {
            case VERTEX: {
                tipPart4a = "a vertex. ";
                tipPart4b = "a vertex. ";
                break;
            }
            case EDGE: {
                tipPart4a = "an edge. Hold down SHIFT for directed edge.";
                tipPart4b = "an edge. ";
                break;
            }
            case LABEL_VERTEX: {
                tipPart4a = "a label to a vertex. ";
                tipPart4b = "a label of a vertex. ";
                break;
            }
            case LABEL_EDGE: {
                tipPart4a = "a label to an edge. Hold down SHIFT for horizontal label.";
                tipPart4b = "a label of an edge. ";
                break;
            }
        }


        tipPart2 = tool.toString() + " tool. ";
        switch (tool) {
            case ADD: {
                tipPart3 = "Left-click to add " + tipPart4a;
                break;
            }
            case REMOVE: {
                tipPart3 = "Left-click to remove " + tipPart4b + "Click and drag to remove all items in the area.";
                break;
            }
            case SELECT: {
                tipPart3 = "Left-click to select " + tipPart4b
                        + "Click and drag to select all items in the area. CTRL + click/drag to extend selection.";
                break;
            }
        }

        return tipPart1 + tipPart2 + tipPart3;
    }

}
