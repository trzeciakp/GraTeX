package pl.edu.agh.gratex.property;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.MatteBorder;

class CellColorRenderer extends JLabel implements ListCellRenderer<Color>
{
	private static final long	serialVersionUID	= 6524312801000597079L;

	public CellColorRenderer()
	{
		setOpaque(true);
	}

	public void setBackground(Color col)
	{
	}

	public void setMyBackground(Color col)
	{
		super.setBackground(col);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus)
	{
		if (index < 1)
		{
			setBorder(null);
		}
		else
		{
			setBorder(new MatteBorder(1, 0, 0, 0, new Color(0, 0, 0)));
		}
		setText(" ");
		if (value != null)
		{
			setMyBackground((Color) value);
		}
		else
		{
			setMyBackground(list.getBackground());
		}

		return this;
	}

}
