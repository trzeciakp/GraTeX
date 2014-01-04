package pl.edu.agh.gratex.model.properties;

/**
 *
 */
public enum IsLabelInside implements Emptible{
    EMPTY {

        @Override
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }, YES, NO;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
