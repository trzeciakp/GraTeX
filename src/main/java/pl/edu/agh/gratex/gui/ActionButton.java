package pl.edu.agh.gratex.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ActionButton extends JButton implements MouseListener
{
	private static final long		serialVersionUID	= 3129459381915495010L;

	private static final String[]	tooltip				= new String[] { "New graph", "Open graph file", "Save", "Edit graph's template",
			"Copy selected subgraph", "Paste copied subgraph", "Undo", "Redo", "Toggle grid on/off", "Numeration preferences",
			"Show graph's LaTeX code"					};

	private int						actionID;
	private Image					image;

	public ActionButton(String imageName, int _actionID)
	{
		actionID = _actionID;
		try
		{
			URL url = this.getClass().getClassLoader().getResource("images/" + imageName);
			image = ImageIO.read(url);
		}
		catch (Exception e)
		{
		}
		addMouseListener(this);
		setToolTipText(tooltip[actionID]);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 8, 8, null);
		if (!isEnabled())
		{
			g.setColor(new Color(220, 220, 220, 200));
			g.fillRect(5, 5, 30, 30);
		}
	}

	public void mouseClicked(MouseEvent e)
	{
		if (isEnabled())
		{
			ControlManager.processActionButtonClicking(actionID);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}
}
