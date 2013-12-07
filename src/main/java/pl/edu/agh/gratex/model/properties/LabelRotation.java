package pl.edu.agh.gratex.model.properties;

import java.io.Serializable;

public enum LabelRotation implements Serializable {
    EMPTY(-1) {
        public String toString() {
            return " ";
        }
    }, TANGENT(0), LEVEL(1);
    private int value;

    public int getValue() {
        return value;
    }

    LabelRotation(int value) {
        this.value = value;
    }

    public String toString() {
        return name().toLowerCase();
    }
}