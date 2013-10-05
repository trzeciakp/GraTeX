package pl.edu.agh.gratex.property;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.ListEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;

import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.VertexPropertyModel;


public class VertexPropertyPanel extends AbstractPropertyPanel
{
	private static final long		serialVersionUID	= 722367652545807127L;

	private VertexPropertyModel		model;
	private JLabel					lblVertexType;
	private JComboBox<Option>		comboBoxVertexType;
	private JLabel					lblColor;
	private JComboBox<Color>		comboBoxVertexColor;
	private JLabel					lblLineType;
	private JComboBox<Option>		comboBoxLineType;
	private JLabel					lblLineSize;
	private JSpinner				spinnerLineSize;
	private JLabel					lblVertexSize;
	private JSpinner				spinnerVertexSize;
	private JLabel					lblLineColor;
	private JComboBox<Color>		comboBoxLineColor;
	private JLabel					lblLabelInside;
	private JComboBox<Option>		comboBoxLabelInside;
	private JLabel					lblNumber;
	private JSpinner				spinnerNumber;
	private JLabel					lblFontColor;
	private JComboBox<Color>		comboBoxFontColor;
	private AbstractSpinnerModel[]	listModels;

	private static int				MIN_SIZE;
	private static int				MAX_SIZE;

	private void changed()
	{
		if (changedByUser)
			((PanelPropertyEditor) getParent()).valueChanged(model);
	}

	public void updateNumeration()
	{
		if (ControlManager.graph.digitalNumeration)
		{
			spinnerNumber.setModel(listModels[1]);
			((JSpinner.DefaultEditor) spinnerNumber.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
		}
		else
			spinnerNumber.setModel(listModels[0]);
	}

	public VertexPropertyPanel()
	{
		model = new VertexPropertyModel();
		initialize();
		setModel(model);
	}

	public VertexPropertyModel getModel()
	{
		return model;
	}

	public void setModel(PropertyModel pm)
	{

		model = new VertexPropertyModel((VertexPropertyModel) pm);
		changedByUser = false;
		lblNumber.setEnabled(true);
		spinnerNumber.setEnabled(true);
		if (model.labelInside == PropertyModel.NO)
		{
			lblNumber.setEnabled(false);
			spinnerNumber.setEnabled(false);
			lblFontColor.setEnabled(false);
			comboBoxFontColor.setEnabled(false);
		}
		else if (model.labelInside == PropertyModel.YES)
		{
			lblFontColor.setEnabled(true);
			comboBoxFontColor.setEnabled(true);
		}
		updateNumeration();
		if (model.number == -1)
		{
			lblNumber.setEnabled(false);
			spinnerNumber.setEnabled(false);
			spinnerNumber.setValue(" ");
		}
		else if (ControlManager.graph.digitalNumeration)
		{
			spinnerNumber.setValue(model.number);
		}
		else
			spinnerNumber.setValue(pl.edu.agh.gratex.graph.Utilities.getABC(model.number));
		if (model.type == -1)
			comboBoxVertexType.setSelectedIndex(0);
		else
			comboBoxVertexType.setSelectedIndex(model.type);
		if (model.radius == -1)
			spinnerVertexSize.setValue(" ");
		else
			spinnerVertexSize.setValue(model.radius + " px");
		comboBoxVertexColor.setSelectedItem(model.vertexColor);
		comboBoxFontColor.setSelectedItem(model.fontColor);
		comboBoxLineType.setSelectedIndex(model.lineType + 1);
		if (model.lineWidth == -1)
			spinnerLineSize.setValue(" ");
		else
			spinnerLineSize.setValue(model.lineWidth + " px");
		comboBoxLineColor.setSelectedItem(model.lineColor);
		comboBoxLabelInside.setSelectedIndex(model.labelInside + 1);

		changedByUser = true;
	}

	private void initialize()
	{
		setLayout(null);

		/**************************** VERTEX TYPE COMBOBOX **************************/
		lblVertexType = new JLabel("Vertex type:");
		lblVertexType.setHorizontalAlignment(SwingConstants.LEFT);
		lblVertexType.setBounds(6, 92, 64, 14);
		add(lblVertexType);

		Option[] vertexTypes = new Option[] { new Option(PropertyModel.EMPTY, " "), new Option(PropertyModel.CIRCLE, "circle"),
				new Option(PropertyModel.TRIANGLE, "triangle"), new Option(PropertyModel.SQUARE, "square"),
				new Option(PropertyModel.PENTAGON, "pentagon"), new Option(PropertyModel.HEXAGON, "hexagon") };
		comboBoxVertexType = new JComboBox<Option>(vertexTypes);
		comboBoxVertexType.setBounds(101, 89, 80, 20);
		comboBoxVertexType.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int newValue = ((Option) comboBoxVertexType.getSelectedItem()).getValue();
				if ((newValue != -1) && (model.type != newValue) || (!changedByUser))
				{
					model.type = newValue;
					changed();
				}
				else
				{
					if (model.type != -1)
						comboBoxVertexType.setSelectedIndex(model.type);
					else
						comboBoxVertexType.setSelectedIndex(0);
				}
			}
		});
		add(comboBoxVertexType);
		/**************************************************************************/

		/**************************** VERTEX SIZE SPINNER *************************/
		lblVertexSize = new JLabel("Vertex size:");
		lblVertexSize.setHorizontalAlignment(SwingConstants.LEFT);
		lblVertexSize.setBounds(6, 42, 64, 14);
		add(lblVertexSize);

		MIN_SIZE = 10;
		MAX_SIZE = 99;
		String[] vertexSizes = new String[MAX_SIZE - MIN_SIZE + 2];
		vertexSizes[0] = new String(" ");
		for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++)
			vertexSizes[i - MIN_SIZE + 1] = new String(i + " px");

		spinnerVertexSize = new JSpinner();
		spinnerVertexSize.setModel(new SpinnerListModel(vertexSizes));
		spinnerVertexSize.setBounds(101, 38, 50, 22);
		spinnerVertexSize.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				String value = (String) spinnerVertexSize.getValue();
				value = value.substring(0, value.indexOf(" "));
				try
				{
					int newValue = Integer.parseInt(value);
					if (model.radius != newValue)
					{
						model.radius = newValue;
						changed();
					}
				}
				catch (NumberFormatException e)
				{
					if (changedByUser)
					{
						if (model.radius != -1)
							spinnerVertexSize.setValue(model.radius + " px");
					}
					else
					{
						spinnerVertexSize.setValue(" ");
						model.radius = -1;
					}
				}
			}
		});
		add(spinnerVertexSize);
		/**************************************************************************/

		/**************************** VERTEX COLOR COMBOBOX ***********************/
		lblColor = new JLabel("Vertex color:");
		lblColor.setHorizontalAlignment(SwingConstants.LEFT);
		lblColor.setBounds(6, 67, 84, 14);
		add(lblColor);

		comboBoxVertexColor = new ColorComboBox();
		comboBoxVertexColor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (changedByUser)
				{
					Color newValue = (Color) comboBoxVertexColor.getSelectedItem();
					if (newValue != null)
					{
						if (!newValue.equals(model.vertexColor))
						{
							model.vertexColor = newValue;
							changed();
						}
					}
					else
					{
						comboBoxVertexColor.setSelectedItem(model.vertexColor);
					}
				}
				repaint();
			}
		});
		add(comboBoxVertexColor);
		/**************************************************************************/

		/**************************** LINE TYPE COMBOBOX **************************/
		lblLineType = new JLabel("Line type:");
		lblLineType.setHorizontalAlignment(SwingConstants.LEFT);
		lblLineType.setBounds(6, 92, 64, 14);
		add(lblLineType);

		Option[] lineTypes = new Option[] { new Option(PropertyModel.EMPTY, " "), new Option(PropertyModel.NONE, "none"),
				new Option(PropertyModel.SOLID, "solid"), new Option(PropertyModel.DASHED, "dashed"), new Option(PropertyModel.DOTTED, "dotted"),
				new Option(PropertyModel.DOUBLE, "double") };
		comboBoxLineType = new JComboBox<Option>(lineTypes);
		comboBoxLineType.setBounds(101, 89, 80, 20);
		comboBoxLineType.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int newValue = ((Option) comboBoxLineType.getSelectedItem()).getValue();
				if ((newValue != -1) && (model.lineType != newValue) || (!changedByUser))
				{
					model.lineType = newValue;
					changed();
				}
				else
				{
					comboBoxLineType.setSelectedIndex(model.lineType + 1);
				}
			}
		});
		add(comboBoxLineType);
		/**************************************************************************/

		/**************************** LINE WIDTH SPINNER **************************/
		lblLineSize = new JLabel("Line width:");
		lblLineSize.setHorizontalAlignment(SwingConstants.LEFT);
		lblLineSize.setBounds(6, 117, 64, 14);
		add(lblLineSize);

		MIN_SIZE = 1;
		MAX_SIZE = 10;
		String[] lineSizes = new String[MAX_SIZE - MIN_SIZE + 2];
		lineSizes[0] = new String(" ");
		for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++)
			lineSizes[i - MIN_SIZE + 1] = new String(i + " px");

		spinnerLineSize = new JSpinner();
		spinnerLineSize.setModel(new SpinnerListModel(lineSizes));
		spinnerLineSize.setBounds(101, 38, 50, 22);
		spinnerLineSize.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				String value = (String) spinnerLineSize.getValue();
				value = value.substring(0, value.indexOf(" "));
				try
				{
					int newValue = Integer.parseInt(value);
					if (model.lineWidth != newValue)
					{
						model.lineWidth = newValue;
						changed();
					}
				}
				catch (NumberFormatException e)
				{
					if (changedByUser)
					{
						if (model.lineWidth != -1)
							spinnerLineSize.setValue(model.lineWidth + " px");
					}
					else
					{
						spinnerLineSize.setValue(" ");
						model.lineWidth = -1;
					}
				}
			}
		});

		add(spinnerLineSize);
		/**************************************************************************/

		/**************************** LINE COLOR COMBOBOX *************************/
		lblLineColor = new JLabel("Line color:");
		lblLineColor.setHorizontalAlignment(SwingConstants.LEFT);
		lblLineColor.setBounds(6, 142, 84, 14);
		add(lblLineColor);

		comboBoxLineColor = new ColorComboBox();
		comboBoxLineColor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (changedByUser)
				{
					Color newValue = (Color) comboBoxLineColor.getSelectedItem();
					if (newValue != null)
					{
						if (!newValue.equals(model.lineColor))
						{
							model.lineColor = newValue;
							changed();
						}
					}
					else
					{
						comboBoxLineColor.setSelectedItem(model.lineColor);
					}
				}
				repaint();
			}
		});
		add(comboBoxLineColor);
		/**************************************************************************/

		/**************************** LABEL INSIDE COMBOBOX ***********************/
		lblLabelInside = new JLabel("Label inside:");
		lblLabelInside.setHorizontalAlignment(SwingConstants.LEFT);
		lblLabelInside.setBounds(6, 167, 75, 14);
		add(lblLabelInside);

		Option[] labelTypes = new Option[] { new Option(PropertyModel.EMPTY, " "), new Option(PropertyModel.NO, "no"),
				new Option(PropertyModel.YES, "yes") };
		comboBoxLabelInside = new JComboBox<Option>(labelTypes);
		comboBoxLabelInside.setBounds(101, 164, 81, 20);
		comboBoxLabelInside.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int newValue = ((Option) comboBoxLabelInside.getSelectedItem()).getValue();
				if ((newValue != -1) && (model.labelInside != newValue) || (!changedByUser))
				{
					model.labelInside = newValue;
					changed();
				}
				else
				{
					comboBoxLabelInside.setSelectedIndex(model.labelInside + 1);
				}
			}
		});
		add(comboBoxLabelInside);
		/**************************************************************************/

		/**************************** COLOR COMBOBOX ******************************/
		lblFontColor = new JLabel("Font color:");
		lblFontColor.setHorizontalAlignment(SwingConstants.LEFT);
		lblFontColor.setBounds(6, 67, 84, 14);
		add(lblFontColor);

		comboBoxFontColor = new ColorComboBox();
		comboBoxFontColor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (changedByUser)
				{
					Color newValue = (Color) comboBoxFontColor.getSelectedItem();
					if (newValue != null)
					{
						if (!newValue.equals(model.fontColor))
						{
							model.fontColor = newValue;
							changed();
						}
					}
					else
					{
						comboBoxFontColor.setSelectedItem(model.fontColor);
					}
				}
				repaint();
			}
		});
		add(comboBoxFontColor);
		/**************************************************************************/

		/**************************** NUMBER SPINNER ******************************/
		lblNumber = new JLabel("Number:");
		lblNumber.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblNumber);

		String[] alpha = new String[ControlManager.graph.maxNumber];
		for (int i = 0; i < ControlManager.graph.maxNumber; i++)
		{
			alpha[i] = pl.edu.agh.gratex.graph.Utilities.getABC(i);
		}
		listModels = new AbstractSpinnerModel[2];
		listModels[0] = new SpinnerListModel(alpha);
		listModels[1] = new SpinnerNumberModel(1, 1, ControlManager.graph.maxNumber - 1, 1);

		spinnerNumber = new JSpinner()
		{
			private static final long	serialVersionUID	= -3030254701594657020L;

			public void setEnabled(boolean flag)
			{
				super.setEnabled(flag);
				if (!flag)
				{
					setModel(new SpinnerListModel(new String[] { " " }));
				}
			}
		};

		if (ControlManager.graph.digitalNumeration)
		{
			spinnerNumber.setModel(listModels[1]);
			((JSpinner.DefaultEditor) spinnerNumber.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
		}
		else
		{
			spinnerNumber.setModel(listModels[0]);
			((ListEditor) spinnerNumber.getEditor()).getTextField().setFormatterFactory(new DefaultFormatterFactory(new MyListFormatter()));
		}
		spinnerNumber.addChangeListener(new ChangeListener()
		{
			private int	previous	= 0;

			private int getValue()
			{
				int value;
				if (ControlManager.graph.digitalNumeration)
					value = (Integer) spinnerNumber.getValue();
				else
				{
					String s = (String) spinnerNumber.getValue();
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

			private void setValue(int arg)
			{
				if (ControlManager.graph.digitalNumeration)
					spinnerNumber.setValue(arg);
				else
					spinnerNumber.setValue(pl.edu.agh.gratex.graph.Utilities.getABC(arg));
			}

			public void stateChanged(ChangeEvent arg0)
			{

				if (changedByUser)
				{
					int value = getValue();
					if (value == previous)
					{
					}
					else
					{
						while ((value > 0) && (value < ControlManager.graph.maxNumber))
						{
							if (!ControlManager.graph.usedNumber[value])
							{

								model.number = value;
								previous = value;
								setValue(value);
								changed();
								break;
							}
							value += Integer.signum((value - previous));
						}
						if ((value == 0) || (value == ControlManager.graph.maxNumber))
						{
							setValue(previous);
						}
					}

				}
				else
				{
					previous = getValue();
				}

			}
		});
		add(spinnerNumber);
		/**************************************************************************/

		/************************ USTAWIANIE BOUNDS *******************************/

		int spacing = 35;
		labels = new Vector<JComponent>();
		labels.add(lblVertexType);
		labels.add(lblVertexSize);
		labels.add(lblColor);
		labels.add(lblLineType);
		labels.add(lblLineSize);
		labels.add(lblLineColor);
		labels.add(lblLabelInside);
		labels.add(lblFontColor);
		labels.add(lblNumber);

		for (int i = 0; i < labels.size(); i++)
			labels.get(i).setBounds(6, 22 + i * spacing, 84, 30);
		components = new Vector<JComponent>();
		components.add(comboBoxVertexType);
		components.add(spinnerVertexSize);
		components.add(comboBoxVertexColor);
		components.add(comboBoxLineType);
		components.add(spinnerLineSize);
		components.add(comboBoxLineColor);
		components.add(comboBoxLabelInside);
		components.add(comboBoxFontColor);
		components.add(spinnerNumber);

		for (int i = 0; i < components.size(); i++)
		{
			components.get(i).setBounds(90, 24 + i * spacing, 80, 25);
		}

	}
}
