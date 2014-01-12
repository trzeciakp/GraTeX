package pl.edu.agh.gratex.model.properties;


import pl.edu.agh.gratex.model.PropertyModel;

public enum JointLabelPosition implements Emptible{
    EMPTY {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, HIDDEN, ABOVE, RIGHT, BELOW, LEFT, INSIDE;

    public String toString() {
        return name().toLowerCase();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
