package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.model.PropertyModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractPropertyPanel extends JPanel {
    private static final long serialVersionUID = 235712066745109734L;

    protected List<JComponent> labels;
    protected List<JComponent> components;

    public AbstractPropertyPanel() {
    }

    protected boolean changedByUser;

    public abstract PropertyModel getModel();

    public abstract void setModel(PropertyModel pm);

    public void setEnabled(boolean flag) {
        for (JComponent label : labels) {
            label.setEnabled(flag);
        }
        List<Component> editorComps = new ArrayList<>();
        for (JComponent tmp : components) {
            if (tmp instanceof JSpinner) {
                editorComps.addAll(Arrays.asList(((JSpinner) tmp).getEditor().getComponents()));
            }
            tmp.setEnabled(flag);
            tmp.setFocusable(flag);
        }
        for (Component editorComp : editorComps)  {
            editorComp.setFocusable(flag);
        }
    }

}
