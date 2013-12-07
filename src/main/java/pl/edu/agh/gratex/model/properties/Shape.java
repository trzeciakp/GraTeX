package pl.edu.agh.gratex.model.properties;

import java.io.Serializable;

public enum Shape implements Serializable {
    CIRCLE(1), TRIANGLE(2), SQUARE(3), PENTAGON(4), HEXAGON(5);
    private int value;

    public int getValue() {
        return value;
    }

    Shape(int value) {
        this.value = value;
    }
}