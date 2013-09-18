package pl.edu.agh.gratex.graph;

import java.awt.Graphics2D;
import java.io.Serializable;

import pl.edu.agh.gratex.model.PropertyModel;


public abstract class GraphElement implements Serializable
{
	private static final long	serialVersionUID	= 633609989731960865L;

	public abstract void draw(Graphics2D g, boolean dummy);

	public abstract void setModel(PropertyModel pm);

	public abstract PropertyModel getModel();
}
