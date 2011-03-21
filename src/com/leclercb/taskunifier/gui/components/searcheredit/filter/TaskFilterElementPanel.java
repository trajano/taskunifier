/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.models.TaskModel;
import com.leclercb.taskunifier.gui.commons.renderers.BooleanListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.ModelListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskFilterConditionListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;

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
		return this.element;
	}
	
	public void saveElement() {
		if (this.element != null) {
			Object value = null;
			
			switch ((TaskColumn) this.elementColumn.getSelectedItem()) {
				case TITLE:
				case TAGS:
				case NOTE:
				case REPEAT:
					value = this.elementValue.getSelectedItem().toString();
					break;
				case COMPLETED_ON:
				case DUE_DATE:
				case START_DATE:
				case REMINDER:
				case LENGTH:
				case IMPORTANCE:
					try {
						value = Integer.parseInt(this.elementValue.getSelectedItem().toString());
					} catch (NumberFormatException e) {
						value = 0;
					}
					break;
				case COMPLETED:
				case STAR:
					value = this.elementValue.getSelectedItem().toString();
					break;
				default:
					value = this.elementValue.getSelectedItem();
					break;
			}
			
			this.element.checkAndSet(
					(TaskColumn) this.elementColumn.getSelectedItem(),
					(TaskFilter.Condition<?, ?>) this.elementCondition.getSelectedItem(),
					value);
		}
	}
	
	public void setElement(TaskFilterElement element) {
		if (element == null)
			this.resetFields(null, null, null);
		else
			this.resetFields(
					element.getColumn(),
					element.getCondition(),
					element.getValue());
		
		this.element = element;
	}
	
	private void resetFields(
			TaskColumn column,
			TaskFilter.Condition<?, ?> condition,
			Object value) {
		TaskFilterElement currentElement = this.element;
		this.element = null;
		
		this.elementColumn.setEnabled(column != null);
		this.elementCondition.setEnabled(column != null);
		this.elementValue.setEnabled(column != null);
		
		this.elementColumn.removeAllItems();
		this.elementCondition.removeAllItems();
		this.elementValue.removeAllItems();
		
		if (column == null) {
			return;
		}
		
		this.elementColumn.setModel(new DefaultComboBoxModel(
				TaskColumn.values()));
		this.elementColumn.setSelectedItem(column);
		
		this.elementValue.setRenderer(new DefaultListCellRenderer());
		
		switch (column) {
			case TITLE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.StringCondition.values()));
				this.elementValue.addItem(value == null ? "" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case TAGS:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.StringCondition.values()));
				this.elementValue.addItem(value == null ? "" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case FOLDER:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new FolderModel(true));
				this.elementValue.setRenderer(new ModelListCellRenderer());
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case CONTEXT:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new ContextModel(true));
				this.elementValue.setRenderer(new ModelListCellRenderer());
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case GOAL:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new GoalModel(true));
				this.elementValue.setRenderer(new ModelListCellRenderer());
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case LOCATION:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new LocationModel(true));
				this.elementValue.setRenderer(new ModelListCellRenderer());
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case PARENT:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new TaskModel(true));
				this.elementValue.setRenderer(new ModelListCellRenderer());
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case COMPLETED:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						new Object[] { TaskFilter.StringCondition.EQUALS }));
				this.elementValue.setModel(new DefaultComboBoxModel(
						new Object[] { true, false }));
				this.elementValue.setRenderer(new BooleanListCellRenderer());
				this.elementValue.setSelectedIndex(value != null
						&& Boolean.parseBoolean(value.toString()) ? 0 : 1);
				this.elementValue.setEditable(false);
				break;
			case COMPLETED_ON:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.DaysCondition.values()));
				this.elementValue.addItem(value == null ? "0" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case DUE_DATE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.DaysCondition.values()));
				this.elementValue.addItem(value == null ? "0" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case START_DATE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.DaysCondition.values()));
				this.elementValue.addItem(value == null ? "0" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case REMINDER:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.NumberCondition.values()));
				this.elementValue.addItem(value == null ? "0" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case REPEAT:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.StringCondition.values()));
				this.elementValue.addItem(value == null ? "" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case REPEAT_FROM:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.EnumCondition.values()));
				this.elementValue.setModel(new DefaultComboBoxModel(
						TaskRepeatFrom.values()));
				this.elementValue.setRenderer(new TaskRepeatFromListCellRenderer());
				this.elementValue.setSelectedItem(value == null ? TaskRepeatFrom.DUE_DATE : value);
				this.elementValue.setEditable(false);
				break;
			case STATUS:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.EnumCondition.values()));
				this.elementValue.setModel(new DefaultComboBoxModel(
						TaskStatus.values()));
				this.elementValue.setRenderer(new TaskStatusListCellRenderer());
				this.elementValue.setSelectedItem(value == null ? TaskStatus.NONE : value);
				this.elementValue.setEditable(false);
				break;
			case LENGTH:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.NumberCondition.values()));
				this.elementValue.addItem(value == null ? "0" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case PRIORITY:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.EnumCondition.values()));
				this.elementValue.setModel(new DefaultComboBoxModel(
						TaskPriority.values()));
				this.elementValue.setRenderer(new TaskPriorityListCellRenderer());
				this.elementValue.setSelectedItem(value == null ? TaskPriority.LOW : value);
				this.elementValue.setEditable(false);
				break;
			case STAR:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						new Object[] { TaskFilter.StringCondition.EQUALS }));
				this.elementValue.setModel(new DefaultComboBoxModel(
						new Object[] { true, false }));
				this.elementValue.setRenderer(new BooleanListCellRenderer());
				this.elementValue.setSelectedIndex(value != null
						&& Boolean.parseBoolean(value.toString()) ? 0 : 1);
				this.elementValue.setEditable(false);
				break;
			case NOTE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.StringCondition.values()));
				this.elementValue.addItem(value == null ? "" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case IMPORTANCE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.NumberCondition.values()));
				this.elementValue.addItem(value == null ? "0" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
		}
		
		if (condition == null)
			this.elementCondition.setSelectedIndex(0);
		else
			this.elementCondition.setSelectedItem(condition);
		
		this.element = currentElement;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		panel.add(new JLabel(
				Translations.getString("searcheredit.element.column") + ":"));
		panel.add(new JLabel(
				Translations.getString("searcheredit.element.condition") + ":"));
		panel.add(new JLabel(
				Translations.getString("searcheredit.element.value") + ":"));
		
		// Column
		this.elementColumn = new JComboBox();
		this.elementColumn.setEnabled(false);
		this.elementColumn.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if (TaskFilterElementPanel.this.element == null)
					return;
				
				TaskFilterElementPanel.this.resetFields(
						(TaskColumn) TaskFilterElementPanel.this.elementColumn.getSelectedItem(),
						null,
						null);
			}
			
		});
		
		panel.add(this.elementColumn);
		
		// Condition
		this.elementCondition = new JComboBox();
		this.elementCondition.setRenderer(new TaskFilterConditionListCellRenderer());
		this.elementCondition.setEnabled(false);
		
		panel.add(this.elementCondition);
		
		// Value
		this.elementValue = new JComboBox(TaskColumn.values());
		this.elementValue.setEnabled(false);
		
		panel.add(this.elementValue);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(panel, 2, 3, // rows, cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
		
		this.add(panel, BorderLayout.NORTH);
	}
	
}
