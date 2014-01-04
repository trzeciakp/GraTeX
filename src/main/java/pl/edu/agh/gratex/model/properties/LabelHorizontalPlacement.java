package pl.edu.agh.gratex.model.properties;

import pl.edu.agh.gratex.model.PropertyModel;

public enum LabelHorizontalPlacement implements Emptible {
    EMPTY(PropertyModel.EMPTY) {
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