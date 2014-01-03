package pl.edu.agh.gratex.model.properties;

public enum LabelTopPlacement implements Emptible {
    EMPTY(-1) {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, BELOW(0), ABOVE(1);
    private int value;

    public int getValue() {
        return value;
    }

    LabelTopPlacement(int value) {
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