package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

@SuppressWarnings("serial")
public class LatexCodeDialog extends JDialog implements ClipboardOwner {

    private JTextArea textArea_code;
    private JScrollPane scrollPane;
    private JButton button_copyToClipboard;
    private JPopupMenu popupMenu;
    private JMenuItem menuItem;

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

    public LatexCodeDialog(MainWindow parent, String code) {
        super(parent, StringLiterals.TITLE_LATEX_CODE_DIALOG, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(768, 768);
        setMinimumSize(new Dimension(200, 200));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        textArea_code = new JTextArea();
        textArea_code.setWrapStyleWord(true);
        textArea_code.setLineWrap(true);
        textArea_code.setText(code);
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
