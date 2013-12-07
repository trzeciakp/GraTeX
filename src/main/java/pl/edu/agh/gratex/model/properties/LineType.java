package pl.edu.agh.gratex.model.properties;

public enum LineType {
    EMPTY {
        public String toString() {
            return " ";
        }
    },NONE,SOLID,DASHED,DOTTED,DOUBLE;

    public String toString() {
        return name().toLowerCase();
    }
}
