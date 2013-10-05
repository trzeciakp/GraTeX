package pl.edu.agh.gratex.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class AboutDialog extends JDialog
{
	private static final long	serialVersionUID	= 2858349132119113260L;
	private final static String DIALOG_TEXT = "GraTeX version 1.0\r\n\r\nAuthors: Łukasz Opioła, Piotr Trzeciak\r\n\r\nUniversity of Science and Technology\r\nKraków, Poland, 2012";
	private final static String DIALOG_TITLE = "About GraTeX";
	
	protected JRootPane createRootPane()
	{
		ActionListener actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				setVisible(false);
				dispose();
			}
		};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	public AboutDialog(MainWindow parent)
	{
		super(parent, DIALOG_TITLE, true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 137);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JTextArea txtrGratexVersion = new JTextArea()
		{
			private static final long	serialVersionUID	= 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				try
				{
					URL url = this.getClass().getClassLoader().getResource("images/icon.png");
					g.drawImage(ImageIO.read(url), 250, 42, null);
				}
				catch (Exception e)
				{
				}
			}
		};
		txtrGratexVersion
				.setText(DIALOG_TEXT );
		txtrGratexVersion.setFocusable(false);
		txtrGratexVersion.setBounds(0, 0, 294, 108);
		getContentPane().add(txtrGratexVersion);
		setVisible(true);
	}
}
