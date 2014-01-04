package pl.edu.agh.gratex.model.properties;

public enum ArrowType implements Emptible {
    EMPTY {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, BASIC, FILLED;

    public String toString() {
        return name().toLowerCase();
    }

    public boolean isEmpty() {
        return false;
    }

}