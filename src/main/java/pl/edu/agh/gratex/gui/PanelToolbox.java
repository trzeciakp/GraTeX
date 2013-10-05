package pl.edu.agh.gratex.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class PanelToolbox extends JPanel
{
	private static final long	serialVersionUID	= 1477920757019877516L;

	private ToolButton			button_addTool;
	private ToolButton			button_removeTool;
	private ToolButton			button_selectTool;
	private JComboBox<String>	comboBox_mode;

	public PanelToolbox()
	{
		super();
		setLayout(null);

		comboBox_mode = new JComboBox<String>();
		comboBox_mode.setModel(new DefaultComboBoxModel<String>(new String[] { "VERTEX", "EDGE", "LABEL (V)", "LABEL (E)" }));
		comboBox_mode.setSelectedIndex(0);
		comboBox_mode.setBounds(0, 11, 90, 30);
		comboBox_mode.setToolTipText("Edition mode");
		comboBox_mode.setFocusable(false);

		comboBox_mode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				ControlManager.changeMode(comboBox_mode.getSelectedIndex() + 1);
				button_addTool.requestFocus();
			}
		});

		add(comboBox_mode);

		button_addTool = new ToolButton("addtool.png", "addtoolpassive.png", 1);
		button_addTool.setBounds(20, 65, 50, 50);
		button_addTool.setToolTipText("Add tool");
		button_addTool.setFocusable(false);
		add(button_addTool);

		button_removeTool = new ToolButton("removetool.png", "removetoolpassive.png", 2);
		button_removeTool.setBounds(20, 125, 50, 50);
		button_removeTool.setToolTipText("Remove tool");
		button_removeTool.setFocusable(false);
		add(button_removeTool);

		button_selectTool = new ToolButton("selecttool.png", "selecttoolpassive.png", 3);
		button_selectTool.setBounds(20, 185, 50, 50);
		button_selectTool.setToolTipText("Select tool");
		button_selectTool.setFocusable(false);
		add(button_selectTool);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (comboBox_mode.getSelectedIndex() != ControlManager.mode - 1)
		{
			comboBox_mode.setSelectedIndex(ControlManager.mode - 1);
		}
		paintChildren(g);
	}
}
