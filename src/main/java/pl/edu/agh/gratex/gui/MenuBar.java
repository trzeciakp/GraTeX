package pl.edu.agh.gratex.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar
{
	private static final long		serialVersionUID	= 5037237692507363783L;

	private JMenu					menu_file;
	private JMenuItem				menuItem_new;
	private JMenuItem				menuItem_open;
	private JMenuItem				menuItem_save;
	private JMenuItem				menuItem_saveAs;
	private JMenuItem				menuItem_template;
	private JMenuItem				menuItem_latex;
	private JMenuItem				menuItem_exit;

	private JMenu					menu_edit;
	private JMenuItem				menuItem_undo;
	private JMenuItem				menuItem_redo;
	private JMenuItem				menuItem_selectAll;
	private JMenuItem				menuItem_copy;
	private JMenuItem				menuItem_paste;
	private JMenuItem				menuItem_delete;
	private JMenuItem				menuItem_grid;
	private JMenuItem				menuItem_numeration;

	private JMenu					menu_mode;
	private JRadioButtonMenuItem	menuItem_vertexMode;
	private JRadioButtonMenuItem	menuItem_edgeMode;
	private JRadioButtonMenuItem	menuItem_labelVMode;
	private JRadioButtonMenuItem	menuItem_labelEMode;

	private JMenu					menu_tools;
	private JRadioButtonMenuItem	menuItem_addTool;
	private JRadioButtonMenuItem	menuItem_removeTool;
	private JRadioButtonMenuItem	menuItem_selectTool;

	private JMenu					menu_about;
	private JMenuItem				menuItem_about;

	public MenuBar()
	{
		super();
		initFileMenu();
		initEditMenu();
		initModeMenu();
		initToolMenu();
		initAboutMenu();
	}

	private void initFileMenu()
	{
		menu_file = new JMenu("File");
		menu_file.setMnemonic('F');
		add(menu_file);

		menuItem_new = new JMenuItem("New");
		menuItem_new.setMnemonic('N');
		menuItem_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem_new.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.newGraphFile();
			}
		});
		menu_file.add(menuItem_new);

		menuItem_open = new JMenuItem("Open");
		menuItem_open.setMnemonic('O');
		menuItem_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem_open.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.openGraphFile();
			}
		});
		menu_file.add(menuItem_open);

		menu_file.addSeparator();

		menuItem_save = new JMenuItem("Save");
		menuItem_save.setMnemonic('S');
		menuItem_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem_save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.saveGraphFile(false);
			}
		});
		menu_file.add(menuItem_save);

		menuItem_saveAs = new JMenuItem("Save as...");
		menuItem_saveAs.setMnemonic('a');
		menuItem_saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK));
		menuItem_saveAs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.saveGraphFile(true);
			}
		});
		menu_file.add(menuItem_saveAs);

		menu_file.addSeparator();

		menuItem_template = new JMenuItem("Edit graph's template");
		menuItem_template.setMnemonic('T');
		menuItem_template.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		menuItem_template.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.editGraphTemplate();
			}
		});
		menu_file.add(menuItem_template);

		menuItem_latex = new JMenuItem("Show graph's LaTeX code");
		menuItem_latex.setMnemonic('L');
		menuItem_latex.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuItem_latex.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.parseToTeX();
			}
		});
		menu_file.add(menuItem_latex);

		menu_file.addSeparator();

		menuItem_exit = new JMenuItem("Exit");
		menuItem_exit.setMnemonic('E');
		menuItem_exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.exitApplication();
			}
		});
		menu_file.add(menuItem_exit);
	}

	private void initEditMenu()
	{
		menu_edit = new JMenu("Edit");
		menu_edit.setMnemonic('E');
		add(menu_edit);

		menuItem_undo = new JMenuItem("Undo");
		menuItem_undo.setMnemonic('U');
		menuItem_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menuItem_undo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.undo();
			}
		});
		menu_edit.add(menuItem_undo);

		menuItem_redo = new JMenuItem("Redo");
		menuItem_redo.setMnemonic('R');
		menuItem_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		menuItem_redo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.redo();
			}
		});
		menu_edit.add(menuItem_redo);

		menu_edit.addSeparator();

		menuItem_selectAll = new JMenuItem("Select everything");
		menuItem_selectAll.setMnemonic('S');
		menuItem_selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menuItem_selectAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.selectAll();
			}
		});
		menu_edit.add(menuItem_selectAll);

		menuItem_copy = new JMenuItem("Copy selected subgraph");
		menuItem_copy.setMnemonic('C');
		menuItem_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuItem_copy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.copyToClipboard();
			}
		});
		menuItem_copy.setEnabled(false);
		menu_edit.add(menuItem_copy);

		menuItem_paste = new JMenuItem("Paste copied subgraph");
		menuItem_paste.setMnemonic('P');
		menuItem_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		menuItem_paste.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (menuItem_paste.isEnabled())
				{
					ControlManager.pasteFromClipboard();
				}
			}
		});
		menuItem_paste.setEnabled(false);
		menu_edit.add(menuItem_paste);

		menuItem_delete = new JMenuItem("Delete selected");
		menuItem_delete.setMnemonic('D');
		menuItem_delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		menuItem_delete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.deleteSelection();
			}
		});
		menu_edit.add(menuItem_delete);

		menu_edit.addSeparator();

		menuItem_grid = new JMenuItem("Toggle grid on/off");
		menuItem_grid.setMnemonic('G');
		menuItem_grid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		menuItem_grid.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.toggleGrid();
			}
		});
		menu_edit.add(menuItem_grid);

		menuItem_numeration = new JMenuItem("Numeration preferences");
		menuItem_numeration.setMnemonic('N');
		menuItem_numeration.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		menuItem_numeration.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.setNumeration();
			}
		});
		menu_edit.add(menuItem_numeration);
	}

	private void initModeMenu()
	{
		menu_mode = new JMenu("Mode");
		menu_mode.setMnemonic('M');
		add(menu_mode);

		ButtonGroup group = new ButtonGroup();
		menuItem_vertexMode = new JRadioButtonMenuItem("Edit vertices");
		menuItem_vertexMode.setSelected(true);
		menuItem_vertexMode.setMnemonic(KeyEvent.VK_V);
		menuItem_vertexMode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		menuItem_vertexMode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.changeMode(1);
			}
		});
		group.add(menuItem_vertexMode);
		menu_mode.add(menuItem_vertexMode);

		menuItem_edgeMode = new JRadioButtonMenuItem("Edit edges");
		menuItem_edgeMode.setSelected(false);
		menuItem_edgeMode.setMnemonic(KeyEvent.VK_E);
		menuItem_edgeMode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		menuItem_edgeMode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.changeMode(2);
			}
		});
		group.add(menuItem_edgeMode);
		menu_mode.add(menuItem_edgeMode);

		menuItem_labelVMode = new JRadioButtonMenuItem("Edit labels of vertices");
		menuItem_labelVMode.setSelected(false);
		menuItem_labelVMode.setMnemonic(KeyEvent.VK_L);
		menuItem_labelVMode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		menuItem_labelVMode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.changeMode(3);
			}
		});
		group.add(menuItem_labelVMode);
		menu_mode.add(menuItem_labelVMode);

		menuItem_labelEMode = new JRadioButtonMenuItem("Edit labels of edges");
		menuItem_labelEMode.setSelected(false);
		menuItem_labelEMode.setMnemonic(KeyEvent.VK_A);
		menuItem_labelEMode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		menuItem_labelEMode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.changeMode(4);
			}
		});
		group.add(menuItem_labelEMode);
		menu_mode.add(menuItem_labelEMode);
	}

	private void initToolMenu()
	{
		menu_tools = new JMenu("Tools");
		menu_tools.setMnemonic('T');
		add(menu_tools);

		ButtonGroup group = new ButtonGroup();
		menuItem_addTool = new JRadioButtonMenuItem("Add tool");
		menuItem_addTool.setSelected(true);
		menuItem_addTool.setMnemonic(KeyEvent.VK_A);
		menuItem_addTool.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem_addTool.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.changeTool(1);
			}
		});
		group.add(menuItem_addTool);
		menu_tools.add(menuItem_addTool);

		menuItem_removeTool = new JRadioButtonMenuItem("Remove tool");
		menuItem_removeTool.setSelected(false);
		menuItem_removeTool.setMnemonic(KeyEvent.VK_R);
		menuItem_removeTool.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem_removeTool.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.changeTool(2);
			}
		});
		group.add(menuItem_removeTool);
		menu_tools.add(menuItem_removeTool);

		menuItem_selectTool = new JRadioButtonMenuItem("Select tool");
		menuItem_selectTool.setSelected(false);
		menuItem_selectTool.setMnemonic(KeyEvent.VK_S);
		menuItem_selectTool.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		menuItem_selectTool.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.changeTool(3);
			}
		});
		group.add(menuItem_selectTool);
		menu_tools.add(menuItem_selectTool);
	}
	
	private void initAboutMenu()
	{
		menu_about = new JMenu("About");
		menu_about.setMnemonic('A');
		add(menu_about);

		menuItem_about = new JMenuItem("GraTeX...");
		menuItem_about.setMnemonic(KeyEvent.VK_G);
		menuItem_about.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ControlManager.showAboutDialog();
			}
		});
		menu_about.add(menuItem_about);
	}

	public void updateFunctions()
	{
		menuItem_copy.setEnabled(false);
		menuItem_paste.setEnabled(false);
		if (ControlManager.mode == ControlManager.VERTEX_MODE && ControlManager.selection.size() > 0)
		{
			menuItem_copy.setEnabled(true);
		}
		if (ControlManager.currentCopyPasteOperation != null)
		{
			menuItem_paste.setEnabled(true);
		}
	}
}
