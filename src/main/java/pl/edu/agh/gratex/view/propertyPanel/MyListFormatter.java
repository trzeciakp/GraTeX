package pl.edu.agh.gratex.view.propertyPanel;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.text.ParseException;

public class MyListFormatter extends JFormattedTextField.AbstractFormatter {
    private static final long serialVersionUID = 5913713259661444353L;

    private DocumentFilter filter;

    public String valueToString(Object value) throws ParseException {
        if (value == null)
            return "";

        return value.toString();
    }

    public Object stringToValue(String string) throws ParseException {
        return string;
    }

    protected DocumentFilter getDocumentFilter() {
        if (filter == null)
            filter = new Filter();

        return filter;
    }

    private class Filter extends DocumentFilter {
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            super.replace(fb, offset, length, string, attrs);
        }

        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }
    }
}