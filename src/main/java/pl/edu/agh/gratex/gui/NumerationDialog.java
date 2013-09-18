package pl.edu.agh.gratex.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.ListEditor;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;

import pl.edu.agh.gratex.graph.Utilities;
import pl.edu.agh.gratex.property.MyListFormatter;

public class NumerationDialog extends JDialog
{
	private static final long	serialVersionUID	= 8082961708684770814L;

	private MainWindow			mainWindow;
	private boolean				digital;
	private int					startNumber;
	private int					maxNumber;

	private JRadioButton		radioButton_digital;
	private JRadioButton		radioButton_alphabetical;
	private JSpinner			spinner_startingNumber;
	private JLabel				label_startingNumber;
	private JButton				button_ok;
	private JButton				button_cancel;

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

	public NumerationDialog(MainWindow _mainWindow, boolean _digital, int initialStartNumber, int _maxNumber)
	{
		super(_mainWindow, "Numeration preferences", true);
		digital = _digital;
		startNumber = initialStartNumber;
		maxNumber = _maxNumber;
		mainWindow = _mainWindow;
		initComponents();
		rootPane.setDefaultButton(button_ok);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
	}

	private void updateSpinner()
	{
		if (radioButton_digital.isSelected())
		{
			spinner_startingNumber.setModel(new SpinnerNumberModel(startNumber, 1, maxNumber - 1, 1));
			((JSpinner.DefaultEditor) spinner_startingNumber.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
		}
		else
		{
			String[] alpha = new String[ControlManager.graph.maxNumber];
			for (int i = 0; i < ControlManager.graph.maxNumber - 1; i++)
			{
				alpha[i] = Utilities.getABC(i + 1);
			}
			spinner_startingNumber.setModel(new SpinnerListModel(alpha));
			spinner_startingNumber.setValue(Utilities.getABC(startNumber));
			((ListEditor) spinner_startingNumber.getEditor()).getTextField().setFormatterFactory(new DefaultFormatterFactory(new MyListFormatter()));
		}
	}

	private void initComponents()
	{
		setLocation(mainWindow.getLocation().x + 345, mainWindow.getLocation().y + 25);
		setSize(218, 175);
		getContentPane().setLayout(null);

		radioButton_digital = new JRadioButton("digital numeration");
		radioButton_digital.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updateSpinner();
			}
		});
		radioButton_digital.setBounds(8, 7, 186, 23);
		radioButton_digital.setFocusable(false);
		radioButton_digital.setSelected(digital);
		getContentPane().add(radioButton_digital);

		radioButton_alphabetical = new JRadioButton("alphabetical numeration");
		radioButton_alphabetical.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updateSpinner();
			}
		});
		radioButton_alphabetical.setBounds(8, 33, 186, 23);
		radioButton_alphabetical.setFocusable(false);
		radioButton_alphabetical.setSelected(!digital);
		getContentPane().add(radioButton_alphabetical);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(radioButton_alphabetical);
		buttonGroup.add(radioButton_digital);

		spinner_startingNumber = new JSpinner();
		spinner_startingNumber.addChangeListener(new ChangeListener()
		{
			private int getValue()
			{
				int value;
				if (radioButton_digital.isSelected())
				{
					value = (Integer) spinner_startingNumber.getValue();
				}
				else
				{
					String s = (String) spinner_startingNumber.getValue();
					int podst = 1;
					value = 0;
					for (int i = s.length(); i > 0; i--)
					{
						value += (s.charAt(i - 1) - 'A' + 1) * podst;
						podst *= 26;
					}
				}
				return value;
			}

			public void stateChanged(ChangeEvent arg0)
			{
				startNumber = getValue();
			}
		});
		spinner_startingNumber.setBounds(117, 62, 77, 25);
		updateSpinner();
		getContentPane().add(spinner_startingNumber);

		label_startingNumber = new JLabel("Starting number:");
		label_startingNumber.setBounds(8, 62, 104, 25);
		getContentPane().add(label_startingNumber);

		button_ok = new JButton("OK");
		button_ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int dig = 0;
				if (radioButton_alphabetical.isSelected())
				{
					dig = 1;
				}
				result = new int[] { dig, startNumber };
				setVisible(false);
				dispose();
			}
		});
		button_ok.setBounds(8, 100, 89, 30);
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
		button_cancel.setBounds(105, 100, 89, 30);
		getContentPane().add(button_cancel);
	}

	public int[] showDialog()
	{
		setVisible(true);
		return result;
	}
}
