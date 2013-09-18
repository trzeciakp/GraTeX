package pl.edu.agh.gratex.property;

import java.awt.Component;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import pl.edu.agh.gratex.model.PropertyModel;


public abstract class AbstractPropertyPanel extends JPanel
{
	private static final long		serialVersionUID	= 235712066745109734L;

	protected Vector<JComponent>	labels;
	protected Vector<JComponent>	components;

	public AbstractPropertyPanel()
	{
	}

	public boolean	changedByUser;

	public abstract PropertyModel getModel();

	public abstract void setModel(PropertyModel pm);

	public void setEnabled(boolean flag)
	{
		for (int i = 0; i < labels.size(); i++)
		{
			labels.get(i).setEnabled(flag);
		}
		Vector<Component> editorComps = new Vector<Component>();
		for (int i = 0; i < components.size(); i++)
		{
			JComponent tmp = components.get(i);
			if (tmp instanceof JSpinner)
				editorComps.addAll(Arrays.asList(((JSpinner) tmp).getEditor().getComponents()));
			tmp.setEnabled(flag);
			tmp.setFocusable(flag);
		}
		for (int i = 0; i < editorComps.size(); i++)
			editorComps.get(i).setFocusable(flag);
	}

}
