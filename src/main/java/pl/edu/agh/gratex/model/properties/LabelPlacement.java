package pl.edu.agh.gratex.model.properties;

import java.io.Serializable;

public enum LabelPlacement implements Serializable {
    EMPTY(-1) {
        public String toString() {
            return " ";
        }
    }, BELOW(0), ABOVE(1);
    private int value;

    public int getValue() {
        return value;
    }

    LabelPlacement(int value) {
        this.value = value;
    }

    public String toString() {
        return name().toLowerCase();
    }
}