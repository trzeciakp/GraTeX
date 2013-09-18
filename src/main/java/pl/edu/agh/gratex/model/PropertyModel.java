package pl.edu.agh.gratex.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public abstract class PropertyModel implements Serializable
{
	private static final long				serialVersionUID	= 4277135806397473605L;

	public final static int					EMPTY				= -1;
	public final static int					NONE				= 0;

	public final static int					SOLID				= 1;
	public final static int					DASHED				= 2;
	public final static int					DOTTED				= 3;
	public final static int					DOUBLE				= 4;

	public final static int					CIRCLE				= 1;
	public final static int					TRIANGLE			= 2;
	public final static int					SQUARE				= 3;
	public final static int					PENTAGON			= 4;
	public final static int					HEXAGON				= 5;

	public final static int					NUMBERS				= 1;
	public final static int					ALPHA				= 2;

	public final static int					NO					= 0;
	public final static int					YES					= 1;

	public final static int					N					= 0;
	public final static int					NE					= 1;
	public final static int					E					= 2;
	public final static int					SE					= 3;
	public final static int					S					= 4;
	public final static int					SW					= 5;
	public final static int					W					= 6;
	public final static int					NW					= 7;

	public final static int					BELOW				= 0;
	public final static int					ABOVE				= 1;

	public final static int					TANGENT				= 0;
	public final static int					LEVEL				= 1;
	
	public final static int					BASIC				= 0;
	public final static int					FILLED				= 1;

	public static HashMap<Color, String>	COLORS				= new HashMap<Color, String>();

	static
	{
		COLORS.put(new Color(0, 0, 0), "black");
		COLORS.put(new Color(new Float(1), new Float(0), new Float(0)), "red");
		COLORS.put(new Color(new Float(0), new Float(1), new Float(0)), "green");
		COLORS.put(new Color(new Float(0), new Float(0), new Float(1)), "blue");
		COLORS.put(new Color(new Float(0.75), new Float(0.5), new Float(0.25)), "brown");
		COLORS.put(new Color(new Float(0.75), new Float(1), new Float(0)), "lime");
		COLORS.put(new Color(new Float(1), new Float(0.5), new Float(0)), "orange");
		COLORS.put(new Color(new Float(1), new Float(0.75), new Float(0.75)), "pink");
		COLORS.put(new Color(new Float(0.75), new Float(0), new Float(0.25)), "purple");
		COLORS.put(new Color(new Float(0), new Float(0.5), new Float(0.5)), "teal");
		COLORS.put(new Color(new Float(0.5), new Float(0), new Float(0.5)), "violet");
		COLORS.put(new Color(new Float(0), new Float(1), new Float(1)), "cyan");
		COLORS.put(new Color(new Float(1), new Float(0), new Float(1)), "magenta");
		COLORS.put(new Color(new Float(1), new Float(1), new Float(0)), "yellow");
		COLORS.put(new Color(new Float(0.5), new Float(0.5), new Float(0)), "olive");
		COLORS.put(null, " ");
		COLORS.put(new Color(new Float(0.25), new Float(0.25), new Float(0.25)), "darkgray");
		COLORS.put(new Color(new Float(0.5), new Float(0.5), new Float(0.5)), "gray");
		COLORS.put(new Color(new Float(0.75), new Float(0.75), new Float(0.75)), "lightgray");
		COLORS.put(new Color(new Float(1), new Float(1), new Float(1)), "white");
	}

	public static Vector<Color>				ColorList			= new Vector<Color>();
	static
	{
		ColorList.add(null);
		ColorList.add(new Color(new Float(0.75), new Float(0), new Float(0.25))); // purple
		ColorList.add(new Color(new Float(1), new Float(0), new Float(0))); // red
		ColorList.add(new Color(new Float(1), new Float(0.5), new Float(0))); // orange
		ColorList.add(new Color(new Float(0.75), new Float(0.5), new Float(0.25))); // brown
		ColorList.add(new Color(new Float(1), new Float(1), new Float(0))); // yellow
		ColorList.add(new Color(new Float(0.75), new Float(1), new Float(0))); // lime
		ColorList.add(new Color(new Float(0), new Float(1), new Float(0))); // green
		ColorList.add(new Color(new Float(0.5), new Float(0.5), new Float(0))); // olive
		ColorList.add(new Color(new Float(0), new Float(0.5), new Float(0.5))); // teal
		ColorList.add(new Color(new Float(0), new Float(1), new Float(1))); // cyan
		ColorList.add(new Color(new Float(0), new Float(0), new Float(1))); // blue
		ColorList.add(new Color(new Float(0.5), new Float(0), new Float(0.5))); // violet
		ColorList.add(new Color(new Float(1), new Float(0), new Float(1))); // magenta
		ColorList.add(new Color(new Float(1), new Float(0.75), new Float(0.75))); // pink  
		ColorList.add(new Color(new Float(1), new Float(1), new Float(1))); // white
		ColorList.add(new Color(new Float(0.75), new Float(0.75), new Float(0.75))); // lightgray
		ColorList.add(new Color(new Float(0.5), new Float(0.5), new Float(0.5))); // gray
		ColorList.add(new Color(new Float(0.25), new Float(0.25), new Float(0.25))); // darkgray
		ColorList.add(new Color(0, 0, 0)); // black
	}

	public abstract void andOperator(PropertyModel pm);
}
