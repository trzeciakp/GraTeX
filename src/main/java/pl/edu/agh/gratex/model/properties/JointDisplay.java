package pl.edu.agh.gratex.model.properties;

import pl.edu.agh.gratex.model.properties.Emptible;

/**
 *
 */
public enum JointDisplay implements Emptible{
    EMPTY {
        @Override
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, HIDDEN, VISIBLE;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
