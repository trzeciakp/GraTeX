package pl.edu.agh.gratex.property;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComboBox;

import pl.edu.agh.gratex.model.PropertyModel;


public class ColorComboBox extends JComboBox
{
	private static final long	serialVersionUID	= 2626900901227763359L;

	public ColorComboBox()
	{
		super(PropertyModel.ColorList);
		setMaximumRowCount(PropertyModel.ColorList.size());
		setRenderer(new CellColorRenderer());
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if ((Color) getSelectedItem() != null)
		{
			g.setColor((Color) getSelectedItem());
			g.fillRect(6, 6, 51, 12);
			g.setColor(Color.black);
			if (((Color) getSelectedItem()).equals(Color.black))
			{
				g.setColor(Color.gray);
			}
			g.drawRect(6, 6, 51, 12);
		}
	}
}
