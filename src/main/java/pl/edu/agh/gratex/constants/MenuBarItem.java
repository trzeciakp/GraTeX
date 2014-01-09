package pl.edu.agh.gratex.constants;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public enum MenuBarItem {
    NEW("New", 'N', KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK), MenuBarSubmenu.FILE, false, true),
    OPEN("Open", 'O', KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK), MenuBarSubmenu.FILE, false, true),
    SEPARATOR1("", '0', null, MenuBarSubmenu.FILE, false, false),
    SAVE("Save", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK), MenuBarSubmenu.FILE, false, true),
    SAVE_AS("Save as...", 'a', KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK), MenuBarSubmenu.FILE, false, true),
    SEPARATOR2("", '0', null, MenuBarSubmenu.FILE, false, false),
    TEMPLATE("Edit graph's template", 'T', KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK), MenuBarSubmenu.FILE, false, true),
    LATEX("Show graph's LaTeX code", 'L', KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK), MenuBarSubmenu.FILE, false, true),
    SEPARATOR3("", '0', null, MenuBarSubmenu.FILE, false, false),
    EXIT("Exit", 'E', null, MenuBarSubmenu.FILE, false, true),

    UNDO("Undo", 'U', KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), MenuBarSubmenu.EDIT, false, true),
    REDO("Redo", 'R', KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK), MenuBarSubmenu.EDIT, false, true),
    SEPARATOR4("", '0', null, MenuBarSubmenu.EDIT, false, false),
    SELECT_ALL("Select everything", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK), MenuBarSubmenu.EDIT, false, true),
    DUPLICATE("Duplicate selected subgraph", 'C', KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK), MenuBarSubmenu.EDIT, false, false),
    DELETE("Delete selected", 'D', KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), MenuBarSubmenu.EDIT, false, true),
    SEPARATOR5("", '0', null, MenuBarSubmenu.EDIT, false, false),
    GRID("Toggle grid on/off", 'G', KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK), MenuBarSubmenu.EDIT, false, true),
    NUMERATION("Numeration preferences", 'N', KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK), MenuBarSubmenu.EDIT, false, true),

    VERTEX_MODE("Edit vertices", 'V', KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), MenuBarSubmenu.MODE, true, true),
    EDGE_MODE("Edit edges", 'E', KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), MenuBarSubmenu.MODE, false, true),
    LABELV_MODE("Edit labels of vertices", 'L', KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), MenuBarSubmenu.MODE, false, true),
    LABELE_MODE("Edit labels of edges", 'A', KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), MenuBarSubmenu.MODE, false, true),
    HYPEREDGE_MODE("Edit hyperedges", 'H', KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), MenuBarSubmenu.MODE, false, true),
    BOUNDARY_MODE("Edit boundaries", 'B', KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), MenuBarSubmenu.MODE, false, true),
    LINK_BOUNDARY_MODE("Edit links of boundaries", 'B', KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), MenuBarSubmenu.MODE, false, true),

    ADD_TOOL("Add tool", 'A', KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK), MenuBarSubmenu.TOOLS, true, true),
    REMOVE_TOOL("Remove tool", 'R', KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK), MenuBarSubmenu.TOOLS, false, true),
    SELECT_TOOL("Select tool", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK), MenuBarSubmenu.TOOLS, false, true),

    ABOUT("GraTeX...", 'G', null, MenuBarSubmenu.ABOUT, false, true);

    private final boolean isEnabled;
    private final String itemNam;
    private final char mnemonic;
    private final KeyStroke keyStroke;
    private final MenuBarSubmenu menuBarSubmenu;
    private final boolean isSelected;

    MenuBarItem(String itemName, char mnemonic, KeyStroke keyStroke, MenuBarSubmenu menuBarSubmenu, boolean isSelected, boolean isEnabled) {
        this.itemNam = itemName;
        this.mnemonic = mnemonic;
        this.keyStroke = keyStroke;
        this.menuBarSubmenu = menuBarSubmenu;
        this.isSelected = isSelected;
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getItemNam() {
        return itemNam;
    }

    public char getMnemonic() {
        return mnemonic;
    }

    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    public MenuBarSubmenu getMenuBarSubmenu() {
        return menuBarSubmenu;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
