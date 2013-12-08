package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.constants.MenuBarItem;
import pl.edu.agh.gratex.constants.MenuBarSubmenu;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.graph.GraphElementType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

public class MenuBar extends JMenuBar implements ModeListener, ToolListener {
    private static final long serialVersionUID = 5037237692507363783L;

    private GeneralController generalController;
    private ModeController modeController;
    private ToolController toolController;
    private EnumMap<MenuBarItem, JMenuItem> menuItems;

    public MenuBar(GeneralController generalController, ModeController modeController, ToolController toolController) {
        super();
        this.generalController = generalController;
        this.modeController = modeController;
        this.toolController = toolController;
        EnumMap<MenuBarSubmenu, JMenu> menus = new EnumMap<MenuBarSubmenu, JMenu>(MenuBarSubmenu.class);
        EnumMap<MenuBarSubmenu, ButtonGroup> groups = new EnumMap<MenuBarSubmenu, ButtonGroup>(MenuBarSubmenu.class);
        menuItems = new EnumMap<MenuBarItem, JMenuItem>(MenuBarItem.class);

        for (MenuBarSubmenu menuBarSubmenu : MenuBarSubmenu.values()) {
            JMenu jMenu = new JMenu(menuBarSubmenu.getMenuName());
            jMenu.setMnemonic(menuBarSubmenu.getMnemonic());
            menus.put(menuBarSubmenu, jMenu);
            if (menuBarSubmenu.isRadioGroup()) {
                groups.put(menuBarSubmenu, new ButtonGroup());
            }
            add(jMenu);
        }

        for (MenuBarItem menuBarItem : MenuBarItem.values()) {
            String menuItemName = menuBarItem.getItemNam();
            if (menuItemName.length() > 0) {
                JMenuItem jMenuItem;
                if (menuBarItem.getMenuBarSubmenu().isRadioGroup()) {
                    jMenuItem = new JRadioButtonMenuItem(menuItemName);
                    groups.get(menuBarItem.getMenuBarSubmenu()).add(jMenuItem);
                    jMenuItem.setSelected(menuBarItem.isSelected());
                } else {
                    jMenuItem = new JMenuItem(menuItemName);
                }
                jMenuItem.setMnemonic(menuBarItem.getMnemonic());
                jMenuItem.addActionListener(getPerformedAction(menuBarItem));
                if (menuBarItem.getKeyStroke() != null) {
                    jMenuItem.setAccelerator(menuBarItem.getKeyStroke());
                }
                if (!menuBarItem.isEnabled()) {
                    jMenuItem.setEnabled(false);
                }
                menus.get(menuBarItem.getMenuBarSubmenu()).add(jMenuItem);
                menuItems.put(menuBarItem, jMenuItem);
            } else {
                menus.get(menuBarItem.getMenuBarSubmenu()).addSeparator();
            }
        }
    }

    public void updateFunctions() {
        menuItems.get(MenuBarItem.COPY).setEnabled(false);
        menuItems.get(MenuBarItem.PASTE).setEnabled(false);
        if (ControlManager.mode == ControlManager.VERTEX_MODE && ControlManager.selection.size() > 0) {
            menuItems.get(MenuBarItem.COPY).setEnabled(true);
        }
        if (ControlManager.currentCopyPasteOperation != null) {
            menuItems.get(MenuBarItem.PASTE).setEnabled(true);
        }
    }

    @Override
    public void fireModeChanged(GraphElementType previousMode, GraphElementType currentMode) {
        switch (currentMode) {
            case VERTEX:
                menuItems.get(MenuBarItem.VERTEX_MODE).setSelected(true);
                break;
            case EDGE:
                menuItems.get(MenuBarItem.EDGE_MODE).setSelected(true);
                break;
            case LABEL_VERTEX:
                menuItems.get(MenuBarItem.LABELV_MODE).setSelected(true);
                break;
            case LABEL_EDGE:
                menuItems.get(MenuBarItem.LABELE_MODE).setSelected(true);
                break;
        }
    }

    @Override
    public void fireToolChanged(ToolType previousToolType, ToolType currentToolType) {
        switch (currentToolType) {
            case ADD:
                menuItems.get(MenuBarItem.ADD_TOOL).setSelected(true);
                break;
            case REMOVE:
                menuItems.get(MenuBarItem.REMOVE_TOOL).setSelected(true);
                break;
            case SELECT:
                menuItems.get(MenuBarItem.SELECT_TOOL).setSelected(true);
                break;
        }
    }

    private ActionListener getPerformedAction(MenuBarItem menuBarItem) {
        switch (menuBarItem) {
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
                        modeController.setMode(GraphElementType.VERTEX);
                    }
                };
            case EDGE_MODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        modeController.setMode(GraphElementType.EDGE);
                    }
                };
            case LABELV_MODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        modeController.setMode(GraphElementType.LABEL_VERTEX);
                    }
                };
            case LABELE_MODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        modeController.setMode(GraphElementType.LABEL_EDGE);
                    }
                };
            case ADD_TOOL:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        toolController.setTool(ToolType.ADD);
                    }
                };
            case REMOVE_TOOL:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        toolController.setTool(ToolType.REMOVE);
                    }
                };
            case SELECT_TOOL:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        toolController.setTool(ToolType.SELECT);
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
}
