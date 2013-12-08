package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.graph.GraphElementType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EnumMap;

public class MenuBar extends JMenuBar {
    private static final long serialVersionUID = 5037237692507363783L;

    private GeneralController generalController;
    private EnumMap<MenuItem,JMenuItem> menuItems;

    public MenuBar(GeneralController generalController) {
        super();
        this.generalController = generalController;
        EnumMap<MenuList, JMenu> menus = new EnumMap<MenuList, JMenu>(MenuList.class);
        EnumMap<MenuList, ButtonGroup> groups = new EnumMap<MenuList, ButtonGroup>(MenuList.class);
        menuItems = new EnumMap<MenuItem, JMenuItem>(MenuItem.class);

        for(MenuList menuList : MenuList.values()) {
            JMenu jMenu = new JMenu(menuList.getMenuName());
            jMenu.setMnemonic(menuList.getMnemonic());
            menus.put(menuList, jMenu);
            if(menuList.isRadioGroup()) {
                groups.put(menuList, new ButtonGroup());
            }
            add(jMenu);
        }

        for(MenuItem menuItem : MenuItem.values()) {
            String menuItemName = menuItem.getItemNam();
            if(menuItemName.length() > 0) {
                JMenuItem jMenuItem;
                if(menuItem.getMenuList().isRadioGroup()) {
                    jMenuItem = new JRadioButtonMenuItem(menuItemName);
                    groups.get(menuItem.getMenuList()).add(jMenuItem);
                    jMenuItem.setSelected(menuItem.isSelected());
                } else {
                    jMenuItem = new JMenuItem(menuItemName);
                }
                jMenuItem.setMnemonic(menuItem.getMnemonic());
                jMenuItem.addActionListener(getPerformedAction(menuItem));
                if(menuItem.getKeyStroke() != null) {
                    jMenuItem.setAccelerator(menuItem.getKeyStroke());
                }
                if(!menuItem.isEnabled()) {
                    jMenuItem.setEnabled(false);
                }
                menus.get(menuItem.getMenuList()).add(jMenuItem);
                menuItems.put(menuItem, jMenuItem);
            } else {
                menus.get(menuItem.getMenuList()).addSeparator();
            }
        }
    }

    public void updateFunctions() {
        menuItems.get(MenuItem.COPY).setEnabled(false);
        menuItems.get(MenuItem.PASTE).setEnabled(false);
        if (ControlManager.mode == ControlManager.VERTEX_MODE && ControlManager.selection.size() > 0) {
            menuItems.get(MenuItem.COPY).setEnabled(true);
        }
        if (ControlManager.currentCopyPasteOperation != null) {
            menuItems.get(MenuItem.PASTE).setEnabled(true);
        }
    }

    private enum MenuList {
        FILE("File", 'F', false),
        EDIT("Edit", 'E', false),
        MODE("Mode", 'M', true),
        TOOLS("Tools", 'T', true),
        ABOUT("About", 'A', false);

        private final boolean isRadioGroup;
        private final String menuName;
        private final char mnemonic;

        MenuList(String name, char mnemonic, boolean isRadioGroup) {
            this.menuName = name;
            this.mnemonic = mnemonic;
            this.isRadioGroup = isRadioGroup;
        }

        private char getMnemonic() {
            return mnemonic;
        }

        private String getMenuName() {
            return menuName;
        }

        private boolean isRadioGroup() {
            return isRadioGroup;
        }
    }

    private ActionListener getPerformedAction(MenuItem menuItem) {
        switch (menuItem) {
            case NEW:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.newGraphFile();
                    }
                };
            case OPEN:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.openGraphFile();
                    }
                };
            case SAVE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.saveGraphFile(false);
                    }
                };
            case SAVE_AS:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.saveGraphFile(true);
                    }
                };
            case TEMPLATE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.editGraphTemplate();
                    }
                };
            case LATEX:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.parseToTeX();
                    }
                };
            case EXIT:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.exitApplication();
                    }
                };
            case UNDO:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.undo();
                    }
                };
            case REDO:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.redo();
                    }
                };
            case SELECT_ALL:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.selectAll();
                    }
                };
            case COPY:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.copyToClipboard();
                    }
                };
            case PASTE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.pasteFromClipboard();
                    }
                };
            case DELETE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.deleteSelection();
                    }
                };
            case GRID:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.toggleGrid();
                    }
                };
            case NUMERATION:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.setNumeration();
                    }
                };
            case VERTEX_MODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.changeMode(GraphElementType.VERTEX);
                    }
                };
            case EDGE_MODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.changeMode(GraphElementType.EDGE);
                    }
                };
            case LABELV_MODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.changeMode(GraphElementType.LABEL_VERTEX);
                    }
                };
            case LABELE_MODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.changeMode(GraphElementType.LABEL_EDGE);
                    }
                };
            case ADD_TOOL:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.changeTool(ToolType.ADD);
                    }
                };
            case REMOVE_TOOL:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.changeTool(ToolType.REMOVE);
                    }
                };
            case SELECT_TOOL:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.changeTool(ToolType.SELECT);
                    }
                };
            case ABOUT:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        generalController.showAboutDialog();
                    }
                };
            default:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                    }
                };
        }
    }
    private enum MenuItem {
        NEW("New", 'N', KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK), MenuList.FILE, false, true),
        OPEN("Open", 'O', KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK), MenuList.FILE, false, true),
        SEPARATOR1("",'0',null,MenuList.FILE, false, false),
        SAVE("Save", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK), MenuList.FILE, false, true),
        SAVE_AS("Save as...", 'a', KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK), MenuList.FILE, false, true),
        SEPARATOR2("",'0',null,MenuList.FILE, false, false),
        TEMPLATE("Edit graph's template", 'T', KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK), MenuList.FILE, false, true),
        LATEX("Show graph's LaTeX code", 'L', KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK), MenuList.FILE, false, true),
        SEPARATOR3("",'0',null,MenuList.FILE, false, false),
        EXIT("Exit", 'E', null, MenuList.FILE, false, true),

        UNDO("Undo", 'U', KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), MenuList.EDIT, false, true),
        REDO("Redo", 'R', KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK), MenuList.EDIT, false, true),
        SEPARATOR4("",'0',null,MenuList.EDIT, false, false),
        SELECT_ALL("Select everything", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK), MenuList.EDIT, false, true),
        COPY("Copy selected subgraph", 'C', KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK), MenuList.EDIT, false, false),
        PASTE("Paste copied subgraph", 'P', KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK), MenuList.EDIT, false, false),
        DELETE("Delete selected", 'D', KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), MenuList.EDIT, false, true),
        SEPARATOR5("",'0',null,MenuList.EDIT, false, false),
        GRID("Toggle grid on/off", 'G', KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK), MenuList.EDIT, false, true),
        NUMERATION("Numeration preferences", 'N', KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK), MenuList.EDIT, false, true),

        VERTEX_MODE("Edit vertices", 'V', KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), MenuList.MODE, true, true),
        EDGE_MODE("Edit edges", 'E', KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), MenuList.MODE, false, true),
        LABELV_MODE("Edit labels of vertices", 'L', KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), MenuList.MODE, false, true),
        LABELE_MODE("Edit labels of edges", 'A', KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), MenuList.MODE, false, true),

        ADD_TOOL("Add tool", 'A', KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK), MenuList.TOOLS, true, true),
        REMOVE_TOOL("Remove tool", 'R', KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK), MenuList.TOOLS, false, true),
        SELECT_TOOL("Select tool", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK), MenuList.TOOLS, false, true),

        ABOUT("GraTeX...", 'G', null, MenuList.ABOUT, false, true);

        private final boolean isEnabled;
        private final String itemNam;
        private final char mnemonic;
        private final KeyStroke keyStroke;
        private final MenuList menuList;
        private final boolean isSelected;

        MenuItem(String itemName, char mnemonic, KeyStroke keyStroke, MenuList menuList, boolean isSelected, boolean isEnabled) {
            this.itemNam = itemName;
            this.mnemonic = mnemonic;
            this.keyStroke = keyStroke;
            this.menuList = menuList;
            this.isSelected = isSelected;
            this.isEnabled = isEnabled;
        }

        private boolean isEnabled() {
            return isEnabled;
        }

        private String getItemNam() {
            return itemNam;
        }

        private char getMnemonic() {
            return mnemonic;
        }

        private KeyStroke getKeyStroke() {
            return keyStroke;
        }

        private MenuList getMenuList() {
            return menuList;
        }

        private boolean isSelected() {
            return isSelected;
        }
    }
}
