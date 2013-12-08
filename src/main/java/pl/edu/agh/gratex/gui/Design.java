package pl.edu.agh.gratex.gui;

import java.awt.*;

public class Design {
    public static final Color mainColor = new Color(100, 100, 100);
    public static final Color vertexModeColor = new Color(10, 190, 230);
    public static final Color edgeModeColor = new Color(245, 186, 14);
    public static final Color labelModeColor = new Color(70, 50, 255);

    public static Color currentModeColor;

    //TODO method is not used
    public static void changeMode(int mode) {
        switch (mode) {
            case 1:
                currentModeColor = vertexModeColor;
                break;
            case 2:
                currentModeColor = edgeModeColor;
                break;
            case 3:
                currentModeColor = labelModeColor;
                break;
        }
    }
}
