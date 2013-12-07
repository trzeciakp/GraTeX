package pl.edu.agh.gratex.model.properties;

import java.io.Serializable;

public enum ArrowType implements Serializable {
    EMPTY(-1) {
        public String toString() {
            return " ";
        }
    },BASIC(0), FILLED(1);
    private int value;

    public int getValue() {
        return value;
    }

    ArrowType(int value) {
        this.value = value;
    }

    public String toString() {
        return name().toLowerCase();
    }
}