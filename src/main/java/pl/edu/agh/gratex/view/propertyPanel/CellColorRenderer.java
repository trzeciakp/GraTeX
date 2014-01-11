package pl.edu.agh.gratex.view.propertyPanel;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

@SuppressWarnings("serial")
class CellColorRenderer extends JLabel implements ListCellRenderer<Color> {

    public CellColorRenderer() {
        setOpaque(true);
    }

    public void setBackground(Color col) {
    }

    public void setMyBackground(Color col) {
        super.setBackground(col);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus) {
        if (index < 1) {
            setBorder(null);
        } else {
            setBorder(new MatteBorder(1, 0, 0, 0, new Color(0, 0, 0)));
        }
        setText(" ");
        if (value != null) {
            setMyBackground((Color) value);
        } else {
            setMyBackground(list.getBackground());
        }

        return this;
    }

}
