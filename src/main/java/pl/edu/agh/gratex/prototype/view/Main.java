package pl.edu.agh.gratex.prototype.view;

import pl.edu.agh.gratex.prototype.controller.CustomShapeParser;
import pl.edu.agh.gratex.prototype.controller.LineControllerImpl;
import pl.edu.agh.gratex.prototype.model.ElementFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private final JFrame frame;

    public Main() {
        frame = new JFrame();
        int width = 800;
        int height = 600;
        frame.setBounds(100, 100, width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final ElementFactory elementFactory = new ElementFactory();
        DrawingLineListener listener = new DrawingLineListener(new LineControllerImpl(elementFactory));

        EditorPanel panel = new EditorPanel(listener, width - 100, height);
        final CustomShapeParser parser = new CustomShapeParser(elementFactory);
        elementFactory.setEditorPanel(panel);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        frame.setLayout(null);
        JButton button = new JButton("to LaTeX");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LatexCodeDialog(frame, parser.parse());
            }
        });
        button.setBounds(width - 95, height / 2, 90, 30);
        frame.getContentPane().add(button);
        JButton resetButton = new JButton("reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elementFactory.clear();
            }
        });
        resetButton.setBounds(width - 90, height / 2+40, 80, 30);
        frame.getContentPane().add(resetButton);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
