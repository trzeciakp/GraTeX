package pl.edu.agh.gratex.model.properties;

import java.io.Serializable;

public enum LabelPosition implements Serializable, Emptible {
    EMPTY(-1) {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, N(0), NE(1), E(2), SE(3), S(4), SW(5), W(6), NW(7);
    private int value;

    public int getValue() {
        return value;
    }

    LabelPosition(int value) {
        this.value = value;
    }

    public String toString() {
        return name();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}