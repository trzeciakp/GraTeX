package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public abstract class PropertyModel {

    public final static int EMPTY = -1;
    public final static int NONE = 0;

    public final static int NUMBERS = 1;
    public final static int ALPHA = 2;

    public final static int NO = 0;
    public final static int YES = 1;


    public static Vector<Color> ColorList = new Vector<Color>();

    static {
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

    public static HashMap<Color, String> COLORS = new HashMap<Color, String>();

    static {
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

    public static HashMap<String, Color> REVERSE_COLORS = new HashMap<String, Color>();

    static {
        for (Color color : ColorList) {
            REVERSE_COLORS.put(COLORS.get(color), color);
        }

    }

    protected GraphElement owner;

    public void setOwner(GraphElement owner) {
        this.owner = owner;
    }

    public GraphElement getOwner() {
        return owner;
    }


    public abstract void andOperator(PropertyModel pm);

    public abstract void updateWithModel(PropertyModel pm);

    public abstract PropertyModel getCopy();

    public static PropertyModel andOpertarorList(GraphElementType type, List<? extends GraphElement> elements) {
        //TODO
        PropertyModelFactory propertyModelFactory = new PropertyModelFactoryImpl();
        if(elements.size() == 0) {
            return propertyModelFactory.createEmptyModel(type);
        }
        PropertyModel result = elements.get(0).getModel();
        for (GraphElement element : elements) {
            result.andOperator(element.getModel());
        }
        return result;
    }
}
