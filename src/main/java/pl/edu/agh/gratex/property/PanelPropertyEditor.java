package pl.edu.agh.gratex.property;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import pl.edu.agh.gratex.model.PropertyModel;


public class PanelPropertyEditor extends JPanel
{
	private static final long		serialVersionUID	= -4423821822436250916L;

	private int						mode				= 1;
	private JLabel					label_title;
	private AbstractPropertyPanel[]	panels				= new AbstractPropertyPanel[5];

	public PanelPropertyEditor()
	{
		initialize();
		setEnabled(false);
	}

	public void valueChanged(PropertyModel model)
	{
		pl.edu.agh.gratex.gui.ControlManager.updateSelectedItemsModel(model);
	}

	private void initialize()
	{
		setLayout(null);
		label_title = new JLabel("Property editor");
		label_title.setHorizontalAlignment(SwingConstants.CENTER);
		label_title.setBounds(10, 11, 180, 14);
		add(label_title);

		panels[1] = new VertexPropertyPanel();
		panels[2] = new EdgePropertyPanel();
		panels[3] = new LabelVertexPropertyPanel();
		panels[4] = new LabelEdgePropertyPanel();

		for (int i = 1; i < 5; i++)
		{
			add(panels[i]);
			if (mode != i)
				panels[i].setVisible(false);
			else
				panels[i].setVisible(true);
			panels[i].setBounds(10, 30, 180, 340);
		}

	}

	public void setModel(PropertyModel pm)
	{
		panels[mode].setModel(pm);
	}

	public void setEnabled(boolean flag)
	{
		panels[mode].setEnabled(flag);
		if (flag)
		{
			panels[mode].components.get(0).requestFocus();
		}
	}

	public void setMode(int m)
	{
		panels[mode].setVisible(false);
		mode = m;
		panels[mode].setVisible(true);
	}

	public PropertyModel getModel()
	{
		return panels[mode].getModel();
	}

	public void giveFocusToTextField()
	{
		panels[mode].components.get(0).requestFocus();
		((JTextField) panels[mode].components.get(0)).selectAll();
	}

	public void disableLabelEdition()
	{
		panels[3].components.get(0).setEnabled(false);
		panels[3].components.get(0).setFocusable(false);
		panels[4].components.get(0).setEnabled(false);
		panels[4].components.get(0).setFocusable(false);
	}
}
