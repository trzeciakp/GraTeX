package pl.edu.agh.gratex.model.properties;

import pl.edu.agh.gratex.model.PropertyModel;

public enum LabelTopPlacement implements Emptible {
    EMPTY {
        @Override
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, BELOW, ABOVE;

    public String toString() {
        return name().toLowerCase();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}