package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.model.PropertyModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public abstract class AbstractPropertyPanel extends JPanel {

    protected List<JComponent> labels = new ArrayList<>();
    protected List<JComponent> components = new ArrayList<>();
    protected PanelPropertyEditor propertyEditorParent;

    public AbstractPropertyPanel() {
        initialize();
        setBounds();
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

    public void disableUnnecessaryFields() {

    }



   /* public void focusFirstElement() {
        components.get(0).requestFocus();
    }*/

    protected JLabel createJLabel(String text) {
        JLabel result = new JLabel(text);
        result.setHorizontalAlignment(SwingConstants.LEFT);
        add(result);
        labels.add(result);
        return result;
    }

    protected abstract void initialize();

    protected void setBounds() {
        int spacing = 35;

        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setBounds(6, 5 + i * spacing, 84, 30);
        }

        for (int i = 0; i < components.size(); i++) {
            if (components.get(i) instanceof JTextField) {
                components.get(i).setBounds(90, 7 + i * spacing, 80, 26);
            } else {
                components.get(i).setBounds(90, 7 + i * spacing, 80, 25);
            }
        }
    }

    public int getHeight() {
        return 35*components.size()+5;
    }

    public void setParent(PanelPropertyEditor parent) {
        this.propertyEditorParent = parent;
    }
}
