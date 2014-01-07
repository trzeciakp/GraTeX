package pl.edu.agh.gratex.model.properties;


import pl.edu.agh.gratex.model.PropertyModel;

public enum JointLabelPosition implements Emptible{
    EMPTY(PropertyModel.EMPTY) {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, ABOVE(0), RIGHT(1), BELOW(2), LEFT(3), INSIDE(4), ;
    private int value;

    public int getValue() {
        return value;
    }

    JointLabelPosition(int value) {
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
