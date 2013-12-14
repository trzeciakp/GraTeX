package pl.edu.agh.gratex.model.properties;

public enum LineType implements Emptyable{
    EMPTY {
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, NONE, SOLID, DASHED, DOTTED, DOUBLE;

    public String toString() {
        return name().toLowerCase();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
