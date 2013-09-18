package pl.edu.agh.gratex.gui;

import javax.swing.JPanel;

public class PanelButtonContainer extends JPanel
{
	private static final long	serialVersionUID	= 4702408962784933450L;

	private ActionButton[]		buttons;

	public PanelButtonContainer()
	{
		super();
		setLayout(null);
		setFocusTraversalKeysEnabled(false);

		buttons = new ActionButton[11];

		buttons[0] = new ActionButton("new.png", 0);
		buttons[0].setBounds(10, 5, 40, 40);
		buttons[0].setFocusable(false);
		add(buttons[0]);

		buttons[1] = new ActionButton("open.png", 1);
		buttons[1].setBounds(55, 5, 40, 40);
		buttons[1].setFocusable(false);
		add(buttons[1]);

		buttons[2] = new ActionButton("save.png", 2);
		buttons[2].setBounds(100, 5, 40, 40);
		buttons[2].setFocusable(false);
		add(buttons[2]);

		buttons[3] = new ActionButton("defaults.png", 3);
		buttons[3].setBounds(155, 5, 40, 40);
		buttons[3].setFocusable(false);
		add(buttons[3]);

		buttons[4] = new ActionButton("copy.png", 4);
		buttons[4].setBounds(210, 5, 40, 40);
		buttons[4].setFocusable(false);
		add(buttons[4]);

		buttons[5] = new ActionButton("paste.png", 5);
		buttons[5].setBounds(255, 5, 40, 40);
		buttons[5].setFocusable(false);
		add(buttons[5]);

		buttons[6] = new ActionButton("undo.png", 6);
		buttons[6].setBounds(310, 5, 40, 40);
		buttons[6].setFocusable(false);
		add(buttons[6]);

		buttons[7] = new ActionButton("redo.png", 7);
		buttons[7].setBounds(355, 5, 40, 40);
		buttons[7].setFocusable(false);
		add(buttons[7]);

		buttons[8] = new ActionButton("grid.png", 8);
		buttons[8].setBounds(410, 5, 40, 40);
		buttons[8].setFocusable(false);
		add(buttons[8]);

		buttons[9] = new ActionButton("numeration.png", 9);
		buttons[9].setBounds(455, 5, 40, 40);
		buttons[9].setFocusable(false);
		add(buttons[9]);

		buttons[10] = new ActionButton("tex.png", 10);
		buttons[10].setBounds(510, 5, 40, 40);
		buttons[10].setFocusable(false);
		add(buttons[10]);

		buttons[4].setEnabled(false);
		buttons[5].setEnabled(false);
	}

	public void updateFunctions()
	{
		buttons[4].setEnabled(false);
		buttons[5].setEnabled(false);
		if (ControlManager.mode == ControlManager.VERTEX_MODE && ControlManager.selection.size() > 0)
		{
			buttons[4].setEnabled(true);
		}
		if (ControlManager.currentCopyPasteOperation != null)
		{
			buttons[5].setEnabled(true);
		}
	}
}
