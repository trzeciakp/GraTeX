package pl.edu.agh.gratex.property;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.edu.agh.gratex.model.EdgePropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;


public class EdgePropertyPanel extends AbstractPropertyPanel
{
	private static final long	serialVersionUID	= 5389175138358360647L;

	private EdgePropertyModel	model;
	private JLabel				lblLineType;
	private JComboBox<LineType> comboBoxLineType;
	private JLabel				lblLineSize;
	private JSpinner			spinnerLineSize;
	private JLabel				lblLineColor;
	private JComboBox<Color>	comboBoxLineColor;
	private JLabel				lblDirect;
	private JComboBox<Option>	comboBoxDirect;
	private JLabel				lblArrowType;
	private JComboBox<Option>	comboBoxArrowType;
	private JLabel				lblAngle;
	private JSpinner			spinnerAngle;
	private JComboBox<Option>	comboBoxAngle;
	private static int			RANGE;
	private static int			STEP;
	private static int			MIN_SIZE;
	private static int			MAX_SIZE;

	private void changed()
	{
		if (changedByUser)
			((PanelPropertyEditor) getParent()).valueChanged(model);
	}

	public EdgePropertyModel getModel()
	{
		return model;
	}

	public void setModel(PropertyModel pm)
	{
		changedByUser = false;
		model = new EdgePropertyModel((EdgePropertyModel) pm);
		if (model.directed == PropertyModel.YES)
		{
			lblArrowType.setEnabled(true);
			comboBoxArrowType.setEnabled(true);
		}
		else if (model.directed == PropertyModel.NO)
		{
			lblArrowType.setEnabled(false);
			comboBoxArrowType.setEnabled(false);
		}
		comboBoxLineType.setSelectedItem(model.lineType);
		if (model.lineWidth == -1)
			spinnerLineSize.setValue(" ");
		else
			spinnerLineSize.setValue(model.lineWidth + " px");
		comboBoxLineColor.setSelectedItem(model.lineColor);
		comboBoxDirect.setSelectedIndex(model.directed + 1);
		comboBoxArrowType.setSelectedIndex(model.arrowType + 1);
		spinnerAngle.setVisible(true);
		comboBoxAngle.setVisible(false);
		if (model.isLoop == PropertyModel.YES)
		{
			spinnerAngle.setVisible(false);
			comboBoxAngle.setVisible(true);
			if (model.relativeEdgeAngle >= 0)
				comboBoxAngle.setSelectedIndex(model.relativeEdgeAngle / 90 + 1);
			else
				comboBoxAngle.setSelectedIndex(0);
		}
		else if (model.relativeEdgeAngle == PropertyModel.EMPTY)
		{
			spinnerAngle.setValue(" ");
		}
		else if (model.relativeEdgeAngle > 180)
			spinnerAngle.setValue(model.relativeEdgeAngle - 360 + " deg");
		else
			spinnerAngle.setValue(model.relativeEdgeAngle + " deg");
		if (model.isLoop == PropertyModel.EMPTY)
		{
			spinnerAngle.setEnabled(false);
			lblAngle.setEnabled(false);
		}

		changedByUser = true;
	}

	public EdgePropertyPanel()
	{
		model = new EdgePropertyModel();
		initialize();
		setModel(model);
	}

	private void initialize()
	{
		setLayout(null);

		/**************************** LINE TYPE COMBOBOX **************************/
		lblLineType = new JLabel("Line type:");
		lblLineType.setHorizontalAlignment(SwingConstants.LEFT);
		lblLineType.setBounds(26, 42, 64, 14);
		add(lblLineType);

		//Option[] lineTypes = new Option[] { new Option(PropertyModel.EMPTY, " "), new Option(PropertyModel.NONE, "none"),
		//		new Option(PropertyModel.SOLID, "solid"), new Option(PropertyModel.DASHED, "dashed"), new Option(PropertyModel.DOTTED, "dotted"),
		//		new Option(PropertyModel.DOUBLE, "double") };
        List<LineType> lineTypes = new ArrayList<>();
        for(LineType lineType : LineType.values()) {
            if(lineType != LineType.NONE) {
                lineTypes.add(lineType);
            }
        }
        comboBoxLineType = new JComboBox<LineType>(lineTypes.toArray(new LineType[0]));
		comboBoxLineType.setBounds(101, 39, 80, 20);
		comboBoxLineType.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				//int newValue = ((Option) comboBoxLineType.getSelectedItem()).getValue();
                LineType newValue = (LineType) comboBoxLineType.getSelectedItem();
				//if ((newValue != -1) && (model.lineType != newValue) || (!changedByUser))
                if(newValue != LineType.EMPTY && model.lineType != newValue || (!changedByUser))
				{
					model.lineType = newValue;
					changed();
				}
				else
				{
					comboBoxLineType.setSelectedItem(model.lineType);
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
		lblLineColor.setBounds(6, 92, 84, 14);
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

		/**************************** DIRECTED COMBOBOX **********************/
		lblDirect = new JLabel("Directed:");
		lblDirect.setHorizontalAlignment(SwingConstants.LEFT);
		lblDirect.setBounds(6, 117, 84, 14);
		add(lblDirect);

		Option[] labelsOutside = new Option[] { new Option(PropertyModel.EMPTY, " "), new Option(PropertyModel.NO, "no"),
				new Option(PropertyModel.YES, "yes") };
		comboBoxDirect = new JComboBox<Option>(labelsOutside);
		comboBoxDirect.setBounds(101, 114, 80, 20);
		comboBoxDirect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int newValue = ((Option) comboBoxDirect.getSelectedItem()).getValue();
				if ((newValue != -1) && (model.directed != newValue) || (!changedByUser))
				{
					model.directed = newValue;
					changed();
				}
				else
				{
					comboBoxDirect.setSelectedIndex(model.directed + 1);
				}
			}
		});
		add(comboBoxDirect);
		/**************************************************************************/

		/**************************** LABEL INSIDE COMBOBOX ***********************/
		lblArrowType = new JLabel("Arrow type:");
		lblArrowType.setHorizontalAlignment(SwingConstants.LEFT);
		lblArrowType.setBounds(6, 167, 75, 14);
		add(lblArrowType);

		Option[] arrowTypes = new Option[] { new Option(PropertyModel.EMPTY, " "), new Option(PropertyModel.BASIC, "basic"),
				new Option(PropertyModel.FILLED, "filled") };
		comboBoxArrowType = new JComboBox<Option>(arrowTypes);
		comboBoxArrowType.setBounds(101, 164, 81, 20);
		comboBoxArrowType.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int newValue = ((Option) comboBoxArrowType.getSelectedItem()).getValue();
				if ((newValue != -1) && (model.arrowType != newValue) || (!changedByUser))
				{
					model.arrowType = newValue;
					changed();
				}
				else
				{
					comboBoxArrowType.setSelectedIndex(model.arrowType + 1);
				}
			}
		});
		add(comboBoxArrowType);
		/**************************************************************************/

		/**************************** ANGLE SPINNER *******************************/
		lblAngle = new JLabel("Angle:");
		lblAngle.setHorizontalAlignment(SwingConstants.LEFT);
		lblAngle.setBounds(26, 142, 64, 14);
		add(lblAngle);

		RANGE = 60;
		STEP = 5;
		String[] angles = new String[RANGE / STEP * 2 + 2];
		angles[0] = new String(" ");
		for (int i = 0; i < RANGE / STEP * 2 + 1; i++)
			angles[i + 1] = new String((i * STEP - RANGE) + " deg");

		spinnerAngle = new JSpinner();
		spinnerAngle.setModel(new SpinnerListModel(angles));
		spinnerAngle.setBounds(101, 138, 80, 26);
		spinnerAngle.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				String value = (String) spinnerAngle.getValue();
				value = value.substring(0, value.indexOf(" "));
				try
				{
					int tmp = Integer.parseInt(value);
					if (tmp < 0)
						tmp += 360;
					if (tmp != model.relativeEdgeAngle)
					{
						model.relativeEdgeAngle = tmp;
						changed();
					}
				}
				catch (NumberFormatException e)
				{
					if (changedByUser)
					{
						if (model.relativeEdgeAngle != -1)
							spinnerAngle.setValue((model.relativeEdgeAngle >= 0 ? model.relativeEdgeAngle : model.relativeEdgeAngle + 360) + " deg");
					}
					else
					{
						spinnerAngle.setValue(" ");
						model.relativeEdgeAngle = -1;
					}
				}

			}
		});
		add(spinnerAngle);

		Option[] loopAngles = new Option[] { new Option(-1, " "), new Option(0, "0 deg"), new Option(90, "90 deg"), new Option(180, "180 deg"),
				new Option(270, "270 deg") };
		comboBoxAngle = new JComboBox<Option>(loopAngles);
		comboBoxAngle.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int newValue = ((Option) comboBoxAngle.getSelectedItem()).getValue();
				if ((newValue != -1) && (model.relativeEdgeAngle != newValue) || (!changedByUser))
				{
					model.relativeEdgeAngle = newValue;
					changed();
				}
				else
				{
					if (model.relativeEdgeAngle >= 0)
						comboBoxAngle.setSelectedIndex(model.relativeEdgeAngle / 90 + 1);
					else
						comboBoxAngle.setSelectedIndex(0);
				}
			}
		});
		add(comboBoxAngle);
		/**************************************************************************/

		/************************ USTAWIANIE BOUNDS *******************************/

		int spacing = 35;
		labels = new Vector<JComponent>();
		labels.add(lblLineType);
		labels.add(lblLineSize);
		labels.add(lblLineColor);
		labels.add(lblDirect);
		labels.add(lblArrowType);
		labels.add(lblAngle);

		for (int i = 0; i < labels.size(); i++)
			labels.get(i).setBounds(6, 22 + i * spacing, 84, 30);

		components = new Vector<JComponent>();
		components.add(comboBoxLineType);
		components.add(spinnerLineSize);
		components.add(comboBoxLineColor);
		components.add(comboBoxDirect);
		components.add(comboBoxArrowType);
		components.add(spinnerAngle);

		for (int i = 0; i < components.size(); i++)
			components.get(i).setBounds(90, 24 + i * spacing, 80, 25);
		comboBoxAngle.setBounds(spinnerAngle.getBounds());
		comboBoxAngle.setVisible(false);
	}

}
