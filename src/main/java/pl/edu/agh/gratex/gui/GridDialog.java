package pl.edu.agh.gratex.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

public class GridDialog extends JDialog
{
	private static final long	serialVersionUID	= -1593271105437878228L;

	private MainWindow			mainWindow;
	private JLabel				label_horizontalSpacing;
	private JSpinner			spinner_horizontalSpacing;
	private JLabel				label_verticalSpacing;
	private JSpinner			spinner_verticalSpacing;
	private JButton				button_ok;
	private JButton				button_cancel;

	private int					initialX;
	private int					initialY;
	private int[]				result				= null;

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

	public GridDialog(MainWindow _mainWindow, int _initialX, int _initialY)
	{
		super(_mainWindow, "Grid properties", true);
		initialX = _initialX;
		initialY = _initialY;
		mainWindow = _mainWindow;
		initComponents();
		rootPane.setDefaultButton(button_ok);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
	}

	private void initComponents()
	{
		setLocation(mainWindow.getLocation().x + 300, mainWindow.getLocation().y + 25);
		setSize(210, 157);
		getContentPane().setLayout(null);

		label_horizontalSpacing = new JLabel("Horizontal spacing (px):");
		label_horizontalSpacing.setBounds(8, 11, 140, 25);
		getContentPane().add(label_horizontalSpacing);

		spinner_horizontalSpacing = new JSpinner();
		spinner_horizontalSpacing.setModel(new SpinnerNumberModel(initialX, 5, 100, 1));
		spinner_horizontalSpacing.setBounds(144, 11, 50, 25);
		getContentPane().add(spinner_horizontalSpacing);

		label_verticalSpacing = new JLabel("Vertical spacing (px):");
		label_verticalSpacing.setBounds(8, 47, 140, 25);
		getContentPane().add(label_verticalSpacing);

		spinner_verticalSpacing = new JSpinner();
		spinner_verticalSpacing.setModel(new SpinnerNumberModel(initialY, 5, 100, 1));
		spinner_verticalSpacing.setBounds(144, 47, 50, 25);
		getContentPane().add(spinner_verticalSpacing);

		button_ok = new JButton("OK");
		button_ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				result = new int[] { (Integer) spinner_horizontalSpacing.getValue(), (Integer) spinner_verticalSpacing.getValue() };
				setVisible(false);
				dispose();
			}
		});
		button_ok.setBounds(8, 83, 89, 30);
		getContentPane().add(button_ok);

		button_cancel = new JButton("Cancel");
		button_cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
				dispose();
			}
		});
		button_cancel.setBounds(105, 83, 89, 30);
		getContentPane().add(button_cancel);
	}

	public int[] showDialog()
	{
		setVisible(true);
		return result;
	}
}
