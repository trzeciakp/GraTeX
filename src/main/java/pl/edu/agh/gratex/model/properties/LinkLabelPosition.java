package pl.edu.agh.gratex.model.properties;


public enum LinkLabelPosition implements Emptible{
    EMPTY {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, HIDDEN, ABOVE, THROUGH, BELOW;

    public String toString() {
        return name().toLowerCase();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
