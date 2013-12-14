package pl.edu.agh.gratex.model.properties;

import java.io.Serializable;

public enum ShapeType implements Serializable, Emptyable {
    EMPTY(-1) {
        @Override
        public boolean isEmpty() {
            return true;
        }

        public String toString() {
            return " ";
        }
    },CIRCLE(1), TRIANGLE(2), SQUARE(3), PENTAGON(4), HEXAGON(5);
    private int value;

    public int getValue() {
        return value;
    }

    ShapeType(int value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static ShapeType getShapeBySidesNumber(int sides) {
        switch(sides) {
            case 3: return TRIANGLE;
            case 4: return SQUARE;
            case 5: return PENTAGON;
            case 6: return HEXAGON;
        }
        return null;
    }

    public int getSides() {
        return value+1;
    }
}