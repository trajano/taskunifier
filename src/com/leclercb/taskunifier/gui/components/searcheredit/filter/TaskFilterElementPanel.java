package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.models.ContextComboBoxModel;
import com.leclercb.taskunifier.gui.models.FolderComboBoxModel;
import com.leclercb.taskunifier.gui.models.GoalComboBoxModel;
import com.leclercb.taskunifier.gui.models.LocationComboBoxModel;
import com.leclercb.taskunifier.gui.models.TaskComboBoxModel;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SpringUtils;

public class TaskFilterElementPanel extends JPanel {

	private TaskFilterElement element;

	private JComboBox elementColumn;
	private JComboBox elementCondition;
	private JComboBox elementValue;

	public TaskFilterElementPanel() {
		this.initialize();
		this.setElement(null);
	}

	public TaskFilterElement getElement() {
		return element;
	}

	public void setElement(TaskFilterElement element) {
		// Set the values before changing the element
		if (this.element != null) {
			Object value = null;

			switch ((TaskColumn) elementColumn.getSelectedItem()) {
			case TITLE: 
			case TAGS:
			case NOTE:
			case REPEAT:
				value = elementValue.getSelectedItem().toString();
				break;
			case COMPLETED_ON:
			case DUE_DATE:
			case START_DATE:
			case REMINDER:
			case LENGTH:
				value = Integer.parseInt(elementValue.getSelectedItem().toString());
				break;
			}

			this.element.checkAndSet(
					(TaskColumn) elementColumn.getSelectedItem(), 
					(TaskFilter.Condition<?, ?>) elementCondition.getSelectedItem(), 
					value);
		}

		this.element = null;

		elementColumn.setEnabled(element != null);
		elementCondition.setEnabled(element != null);
		elementValue.setEnabled(element != null);

		elementColumn.removeAllItems();
		elementCondition.removeAllItems();
		elementValue.removeAllItems();

		if (element == null) {
			return;
		}

		elementColumn.setModel(new DefaultComboBoxModel(TaskColumn.values()));
		elementColumn.setSelectedItem(element.getColumn());

		switch (element.getColumn()) {
		case TITLE: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.StringCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case TAGS: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.StringCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case FOLDER: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.ModelCondition.values()));
			elementValue.setModel(new FolderComboBoxModel());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(false);
			break;
		case CONTEXT: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.ModelCondition.values()));
			elementValue.setModel(new ContextComboBoxModel());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(false);
			break;
		case GOAL: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.ModelCondition.values()));
			elementValue.setModel(new GoalComboBoxModel());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(false);
			break;
		case LOCATION:
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.ModelCondition.values()));
			elementValue.setModel(new LocationComboBoxModel());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(false);
			break;
		case PARENT:
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.ModelCondition.values()));
			elementValue.setModel(new TaskComboBoxModel());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(false);
			break;
		case COMPLETED: 
			elementCondition.setModel(new DefaultComboBoxModel(new Object[] { TaskFilter.StringCondition.EQUALS }));
			elementValue.setModel(new DefaultComboBoxModel(new Object[] {true, false}));
			elementValue.setSelectedItem(element.getValue());
			elementValue.setEditable(false);
			break;
		case COMPLETED_ON: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.DaysCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case DUE_DATE: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.DaysCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case START_DATE: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.DaysCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case REMINDER:
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.NumberCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case REPEAT: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.StringCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case REPEAT_FROM:
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.EnumCondition.values()));
			elementValue.setModel(new DefaultComboBoxModel(TaskRepeatFrom.values()));
			elementValue.setSelectedItem(element.getValue());
			elementValue.setEditable(false);
			break;
		case STATUS: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.EnumCondition.values()));
			elementValue.setModel(new DefaultComboBoxModel(TaskStatus.values()));
			elementValue.setSelectedItem(element.getValue());
			elementValue.setEditable(false);
			break;
		case LENGTH:
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.NumberCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		case PRIORITY: 
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.EnumCondition.values()));
			elementValue.setModel(new DefaultComboBoxModel(TaskPriority.values()));
			elementValue.setSelectedItem(element.getValue());
			elementValue.setEditable(false);
			break;
		case STAR: 
			elementCondition.setModel(new DefaultComboBoxModel(new Object[] { TaskFilter.StringCondition.EQUALS }));
			elementValue.setModel(new DefaultComboBoxModel(new Object[] {true, false}));
			elementValue.setSelectedItem(element.getValue());
			elementValue.setEditable(false);
			break;
		case NOTE:
			elementCondition.setModel(new DefaultComboBoxModel(TaskFilter.StringCondition.values()));
			elementValue.addItem(element.getValue());
			elementValue.setSelectedIndex(0);
			elementValue.setEditable(true);
			break;
		}

		this.element = element;
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panel.add(new JLabel(Translations.getString("searcheredit.element.column")));
		panel.add(new JLabel(Translations.getString("searcheredit.element.condition")));
		panel.add(new JLabel(Translations.getString("searcheredit.element.value")));

		// Column
		elementColumn = new JComboBox();
		elementColumn.setEnabled(false);
		elementColumn.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent evt) {
				if (element == null)
					return;

				setElement(element);
			}

		});

		panel.add(elementColumn);

		// Condition
		elementCondition = new JComboBox();
		elementCondition.setEnabled(false);

		panel.add(elementCondition);

		// Value
		elementValue = new JComboBox(TaskColumn.values());
		elementValue.setEnabled(false);

		panel.add(elementValue);

		// Lay out the panel
		SpringUtils.makeCompactGrid(panel,
				2, 3, //rows, cols
				6, 6, //initX, initY
				6, 6); //xPad, yPad

		this.add(panel, BorderLayout.NORTH);
	}

}
