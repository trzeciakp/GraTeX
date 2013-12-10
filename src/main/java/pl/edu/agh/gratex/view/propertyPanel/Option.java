package pl.edu.agh.gratex.view.propertyPanel;

public class Option {
    private int value;
    private String name;

    public String toString() {
        return name;
    }

    public Option(int _id, String _name) {
        value = _id;
        name = _name;
    }

    public int getValue() {
        return value;
    }
}