package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.model.PropertyModel;

import javax.swing.*;
import java.awt.*;


@SuppressWarnings("serial")
public class ColorComboBox extends JComboBox<Color> {

    public ColorComboBox() {
        super(PropertyModel.ColorList);
        setMaximumRowCount(PropertyModel.ColorList.size());
        setRenderer(new CellColorRenderer());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getSelectedItem() != null) {
            g.setColor((Color) getSelectedItem());
            g.fillRect(6, 6, 51, 12);
            g.setColor(Color.black);
            if ((getSelectedItem()).equals(Color.black)) {
                g.setColor(Color.gray);
            }
            g.drawRect(6, 6, 51, 12);
        }
    }
}
