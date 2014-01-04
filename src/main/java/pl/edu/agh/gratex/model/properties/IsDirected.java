package pl.edu.agh.gratex.model.properties;

/**
 *
 */
public enum IsDirected implements Emptible {
    EMPTY {

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return " ";
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
