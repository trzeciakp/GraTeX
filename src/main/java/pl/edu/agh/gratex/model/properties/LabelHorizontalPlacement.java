package pl.edu.agh.gratex.model.properties;

import java.io.Serializable;

public enum LabelHorizontalPlacement implements Serializable, Emptible {
    EMPTY(-1) {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, TANGENT(0), LEVEL(1);
    private int value;

    public int getValue() {
        return value;
    }

    LabelHorizontalPlacement(int value) {
        this.value = value;
    }

    public String toString() {
        return name().toLowerCase();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}