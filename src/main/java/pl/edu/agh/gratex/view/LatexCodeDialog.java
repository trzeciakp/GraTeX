package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class LatexCodeDialog extends JDialog implements ClipboardOwner {

    private JTextArea textArea_code;
    private JScrollPane scrollPane;
    private JButton button_copyToClipboard;
    private JPopupMenu popupMenu;
    private JMenuItem menuItem;
    private final List<String> codeWithPicture;

    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }

    private String listToString(List<String> code, boolean removeComments) {
        StringBuilder result = new StringBuilder();
        for (String line : code) {
            result.append((removeComments ? removeComment(line) : line)).append("\n");
        }
        return result.toString();
    }

    private String removeComment(String line) {
        //TODO what if % in latex code?
        int commentStart = line.indexOf("%");
        if (commentStart > -1) {
            return line.substring(0,commentStart);
        }
        return line;
    }

    private void addDocumentHeader(List<String> documentHeader) {
        documentHeader.add(0, "\\documentclass[]{article}");
        documentHeader.add(1, "\\usepackage{tikz}");
        documentHeader.add(2, "\\usetikzlibrary{backgrounds,shapes.geometric}");
        documentHeader.add(3, "\\begin{document}\n");

        documentHeader.add("\n\\end{document}");
    }

    private void removeDocumentHeader(List<String> documentHeader) {
        documentHeader.remove(0);
        documentHeader.remove(0);
        documentHeader.remove(0);
        documentHeader.remove(0);
        documentHeader.remove(documentHeader.size()-1);
    }


    public LatexCodeDialog(MainWindow parent, List<String> code) {
        super(parent, StringLiterals.TITLE_LATEX_CODE_DIALOG, true);
        codeWithPicture = new LinkedList<>();
        codeWithPicture.add("\\begin{tikzpicture}");
        codeWithPicture.add("[every node/.style={inner sep=0pt}]");
        codeWithPicture.addAll(code);
        codeWithPicture.add("\\end{tikzpicture}");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(768, 768);
        setMinimumSize(new Dimension(200, 200));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel checkboxesPanel = new JPanel();
        final JCheckBox showCommentsCheckBox = new JCheckBox("Show comments");
        showCommentsCheckBox.setToolTipText("Show comments that allow to parse LaTeX code back to GraTeX");
        showCommentsCheckBox.setSelected(false);
        showCommentsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea_code.setText(listToString(codeWithPicture, !showCommentsCheckBox.isSelected()));
            }
        });
        //getContentPane().add(showCommentsCheckBox, BorderLayout.NORTH);
        checkboxesPanel.add(showCommentsCheckBox);
        final JCheckBox showDocumentHeaderCheckBox = new JCheckBox("Show document header");
        showCommentsCheckBox.setToolTipText("Show commands that import packages required to use TikZ in LaTeX documents");
        showDocumentHeaderCheckBox.setSelected(false);
        showDocumentHeaderCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showDocumentHeaderCheckBox.isSelected()) {
                    addDocumentHeader(codeWithPicture);
                } else {
                    removeDocumentHeader(codeWithPicture);
                }
                textArea_code.setText(listToString(codeWithPicture, showCommentsCheckBox.isSelected()));
            }
        });
        checkboxesPanel.add(showDocumentHeaderCheckBox);
        //getContentPane().add(showDocumentHeaderCheckBox, BorderLayout.NORTH);
        getContentPane().add(checkboxesPanel, BorderLayout.NORTH);

        textArea_code = new JTextArea();
        textArea_code.setWrapStyleWord(true);
        textArea_code.setLineWrap(true);
        textArea_code.setText(listToString(codeWithPicture, true));
        textArea_code.setCaretPosition(0);

        popupMenu = new JPopupMenu();
        addPopup(textArea_code, popupMenu);
        menuItem = new JMenuItem(StringLiterals.MENU_ITEM_LATEX_DIALOG_COPY_TO_CLIPBOARD);
        menuItem.setMnemonic(StringLiterals.MNEMONIC_LATEX_DIALOG_COPY_TO_CLIPBOARD);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(textArea_code.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, LatexCodeDialog.this);
            }
        });
        popupMenu.add(menuItem);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea_code);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        button_copyToClipboard = new JButton(StringLiterals.BUTTON_LATEX_DIALOG_COPY_TO_CLIPBOARD);
        button_copyToClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                StringSelection stringSelection = new StringSelection(textArea_code.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, LatexCodeDialog.this);
            }
        });
        getContentPane().add(button_copyToClipboard, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
    }

    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
}
