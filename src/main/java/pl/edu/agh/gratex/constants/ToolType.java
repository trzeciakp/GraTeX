package pl.edu.agh.gratex.constants;

public enum ToolType {
    ADD("ADD"), REMOVE("REMOVE"), SELECT("SELECT");

    private String name;

    ToolType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
