package pl.edu.agh.gratex.model.properties;

public enum ArrowType implements Emptible {
    EMPTY(-1) {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, BASIC(0), FILLED(1);
    private int value;

    public int getValue() {
        return value;
    }

    ArrowType(int value) {
        this.value = value;
    }

    public String toString() {
        return name().toLowerCase();
    }

    public boolean isEmpty() {
        return false;
    }

}