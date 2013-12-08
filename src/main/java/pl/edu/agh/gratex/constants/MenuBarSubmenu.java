package pl.edu.agh.gratex.constants;

public enum MenuBarSubmenu {
    FILE("File", 'F', false),
    EDIT("Edit", 'E', false),
    MODE("Mode", 'M', true),
    TOOLS("Tools", 'T', true),
    ABOUT("About", 'A', false);

    private final boolean isRadioGroup;
    private final String menuName;
    private final char mnemonic;

    MenuBarSubmenu(String name, char mnemonic, boolean isRadioGroup) {
        this.menuName = name;
        this.mnemonic = mnemonic;
        this.isRadioGroup = isRadioGroup;
    }

    public char getMnemonic() {
        return mnemonic;
    }

    public String getMenuName() {
        return menuName;
    }

    public boolean isRadioGroup() {
        return isRadioGroup;
    }
}
