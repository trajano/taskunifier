/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.tasksearcheredit.filter;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.Condition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.DaysCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.EnumCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.NumberCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.models.TaskModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueBoolean;
import com.leclercb.taskunifier.gui.commons.values.StringValueFilterCondition;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.utils.FormBuilder;

public class TaskFilterElementPanel extends JPanel {
	
	private TaskFilterElement element;
	
	private JXComboBox elementColumn;
	private JXComboBox elementCondition;
	private JXComboBox elementValueCb;
	private JTextField elementValueTf;
	
	public TaskFilterElementPanel() {
		this.initialize();
		this.setElement(null);
	}
	
	public TaskFilterElement getElement() {
		return this.element;
	}
	
	public void saveElement() {
		if (this.element != null) {
			Object elementValue = null;
			
			if (this.elementValueCb.isVisible())
				elementValue = this.elementValueCb.getSelectedItem();
			else
				elementValue = this.elementValueTf.getText();
			
			Object value = null;
			
			switch ((TaskColumn) this.elementColumn.getSelectedItem()) {
				case MODEL_EDIT:
				case TIMER:
				case SHOW_CHILDREN:
				case ORDER:
					value = null;
					break;
				case TITLE:
				case CONTACTS:
				case TASKS:
				case FILES:
				case TAGS:
				case NOTE:
				case REPEAT:
					value = elementValue.toString();
					break;
				case MODEL_CREATION_DATE:
				case MODEL_UPDATE_DATE:
				case COMPLETED_ON:
				case DUE_DATE:
				case START_DATE:
					try {
						if (elementValue.toString().length() == 0)
							value = null;
						else
							value = Integer.parseInt(elementValue.toString());
					} catch (NumberFormatException e) {
						value = 0;
					}
					break;
				case DUE_DATE_REMINDER:
				case START_DATE_REMINDER:
				case LENGTH:
				case IMPORTANCE:
					try {
						value = Integer.parseInt(elementValue.toString());
					} catch (NumberFormatException e) {
						value = 0;
					}
					break;
				case COMPLETED:
				case STAR:
					value = elementValue.toString();
					break;
				case PROGRESS:
					try {
						value = Double.parseDouble(elementValue.toString());
					} catch (NumberFormatException e) {
						value = 0.0;
					}
					break;
				default:
					value = elementValue;
					break;
			}
			
			this.element.checkAndSet(
					(TaskColumn) this.elementColumn.getSelectedItem(),
					(Condition<?, ?>) this.elementCondition.getSelectedItem(),
					value);
		}
	}
	
	public void setElement(TaskFilterElement element) {
		if (element == null)
			this.resetFields(null, null, null);
		else
			this.resetFields(
					element.getProperty(),
					element.getCondition(),
					element.getValue());
		
		this.element = element;
	}
	
	private void resetFields(
			TaskColumn column,
			Condition<?, ?> condition,
			Object value) {
		TaskFilterElement currentElement = this.element;
		this.element = null;
		
		this.elementValueCb.setVisible(false);
		this.elementValueTf.setVisible(false);
		
		this.elementColumn.setEnabled(column != null);
		this.elementCondition.setEnabled(column != null);
		this.elementValueCb.setEnabled(column != null);
		this.elementValueTf.setEnabled(column != null);
		
		this.elementColumn.setModel(new DefaultComboBoxModel());
		this.elementCondition.setModel(new DefaultComboBoxModel());
		this.elementValueCb.setModel(new DefaultComboBoxModel());
		this.elementValueTf.setText("");
		
		if (column == null) {
			this.elementValueTf.setVisible(true);
			return;
		}
		
		DefaultComboBoxModel taskColumnsModel = new DefaultComboBoxModel(
				TaskColumn.values());
		taskColumnsModel.removeElement(TaskColumn.MODEL_EDIT);
		taskColumnsModel.removeElement(TaskColumn.TIMER);
		taskColumnsModel.removeElement(TaskColumn.SHOW_CHILDREN);
		taskColumnsModel.removeElement(TaskColumn.ORDER);
		
		this.elementColumn.setModel(taskColumnsModel);
		this.elementColumn.setSelectedItem(column);
		
		this.elementValueCb.setRenderer(new DefaultListCellRenderer());
		
		List<Condition<?, ?>> modelConditionList = new ArrayList<Condition<?, ?>>();
		modelConditionList.addAll(Arrays.asList(ModelCondition.values()));
		modelConditionList.addAll(Arrays.asList(StringCondition.values()));
		modelConditionList.remove(StringCondition.EQUALS);
		modelConditionList.remove(StringCondition.NOT_EQUALS);
		
		Object[] modelConditions = modelConditionList.toArray();
		
		switch (column) {
			case CONTEXT:
			case FOLDER:
			case GOAL:
			case LOCATION:
			case PARENT:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						modelConditions));
				
				if (condition == null)
					condition = (Condition<?, ?>) modelConditions[0];
				
				if (condition instanceof StringCondition) {
					this.elementValueTf.setText(value == null ? "" : value.toString());
					this.elementValueTf.setVisible(true);
				}
				break;
		}
		
		switch (column) {
			case TITLE:
			case NOTE:
			case CONTACTS:
			case TAGS:
			case REPEAT:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						StringCondition.values()));
				this.elementValueTf.setText(value == null ? "" : value.toString());
				this.elementValueTf.setVisible(true);
				break;
			case COMPLETED_ON:
			case DUE_DATE:
			case START_DATE:
			case MODEL_CREATION_DATE:
			case MODEL_UPDATE_DATE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						DaysCondition.values()));
				this.elementValueTf.setText(value == null ? "" : value.toString());
				this.elementValueTf.setVisible(true);
				break;
			case DUE_DATE_REMINDER:
			case START_DATE_REMINDER:
			case LENGTH:
			case IMPORTANCE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						NumberCondition.values()));
				this.elementValueTf.setText(value == null ? "0" : value.toString());
				this.elementValueTf.setVisible(true);
				break;
			case PROGRESS:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						NumberCondition.values()));
				this.elementValueTf.setText(value == null ? "0.0" : value.toString());
				this.elementValueTf.setVisible(true);
				break;
			case MODEL:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						ModelCondition.values()));
				this.elementValueCb.setModel(new TaskModel(true));
				this.elementValueCb.setRenderer(new DefaultListRenderer(
						StringValueModel.INSTANCE,
						IconValueModel.INSTANCE));
				this.elementValueCb.setSelectedItem(value);
				this.elementValueCb.setVisible(true);
				break;
			case CONTEXT:
				if (condition instanceof ModelCondition) {
					this.elementValueCb.setModel(new ContextModel(true));
					this.elementValueCb.setRenderer(new DefaultListRenderer(
							StringValueModel.INSTANCE,
							IconValueModel.INSTANCE));
					this.elementValueCb.setSelectedItem(value);
					this.elementValueCb.setVisible(true);
				}
				break;
			case FOLDER:
				if (condition instanceof ModelCondition) {
					this.elementValueCb.setModel(new FolderModel(true, true));
					this.elementValueCb.setRenderer(new DefaultListRenderer(
							StringValueModel.INSTANCE,
							IconValueModel.INSTANCE));
					this.elementValueCb.setSelectedItem(value);
					this.elementValueCb.setVisible(true);
				}
				break;
			case GOAL:
				if (condition instanceof ModelCondition) {
					this.elementValueCb.setModel(new GoalModel(true));
					this.elementValueCb.setRenderer(new DefaultListRenderer(
							StringValueModel.INSTANCE,
							IconValueModel.INSTANCE));
					this.elementValueCb.setSelectedItem(value);
					this.elementValueCb.setVisible(true);
				}
				break;
			case LOCATION:
				if (condition instanceof ModelCondition) {
					this.elementValueCb.setModel(new LocationModel(true));
					this.elementValueCb.setRenderer(new DefaultListRenderer(
							StringValueModel.INSTANCE,
							IconValueModel.INSTANCE));
					this.elementValueCb.setSelectedItem(value);
					this.elementValueCb.setVisible(true);
				}
				break;
			case PARENT:
				if (condition instanceof ModelCondition) {
					this.elementValueCb.setModel(new TaskModel(true));
					this.elementValueCb.setRenderer(new DefaultListRenderer(
							StringValueModel.INSTANCE,
							IconValueModel.INSTANCE));
					this.elementValueCb.setSelectedItem(value);
					this.elementValueCb.setVisible(true);
				}
				break;
			case COMPLETED:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						new Object[] { StringCondition.EQUALS }));
				this.elementValueCb.setModel(new DefaultComboBoxModel(
						new Object[] { true, false }));
				this.elementValueCb.setRenderer(new DefaultListRenderer(
						StringValueBoolean.INSTANCE));
				this.elementValueCb.setSelectedIndex(value != null
						&& Boolean.parseBoolean(value.toString()) ? 0 : 1);
				this.elementValueCb.setVisible(true);
				break;
			case REPEAT_FROM:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						EnumCondition.values()));
				this.elementValueCb.setModel(new DefaultComboBoxModel(
						TaskRepeatFrom.values()));
				this.elementValueCb.setRenderer(new DefaultListRenderer(
						StringValueTaskRepeatFrom.INSTANCE));
				this.elementValueCb.setSelectedItem(value == null ? TaskRepeatFrom.DUE_DATE : value);
				this.elementValueCb.setVisible(true);
				break;
			case STATUS:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						EnumCondition.values()));
				this.elementValueCb.setModel(new DefaultComboBoxModel(
						TaskStatus.values()));
				this.elementValueCb.setRenderer(new DefaultListRenderer(
						StringValueTaskStatus.INSTANCE));
				this.elementValueCb.setSelectedItem(value == null ? TaskStatus.NONE : value);
				this.elementValueCb.setVisible(true);
				break;
			case PRIORITY:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						EnumCondition.values()));
				this.elementValueCb.setModel(new DefaultComboBoxModel(
						TaskPriority.values()));
				this.elementValueCb.setRenderer(new DefaultListRenderer(
						StringValueTaskPriority.INSTANCE,
						IconValueTaskPriority.INSTANCE));
				this.elementValueCb.setSelectedItem(value == null ? TaskPriority.LOW : value);
				this.elementValueCb.setVisible(true);
				break;
			case STAR:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						new Object[] { StringCondition.EQUALS }));
				this.elementValueCb.setModel(new DefaultComboBoxModel(
						new Object[] { true, false }));
				this.elementValueCb.setRenderer(new DefaultListRenderer(
						StringValueBoolean.INSTANCE));
				this.elementValueCb.setSelectedIndex(value != null
						&& Boolean.parseBoolean(value.toString()) ? 0 : 1);
				this.elementValueCb.setVisible(true);
				break;
		}
		
		if (condition == null)
			this.elementCondition.setSelectedIndex(0);
		else
			this.elementCondition.setSelectedItem(condition);
		
		this.element = currentElement;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		FormBuilder builder = new FormBuilder(
				"fill:default:grow, 10dlu, fill:default:grow, 10dlu, fill:default:grow");
		
		builder.appendI15d("searcheredit.element.column", true);
		builder.appendI15d("searcheredit.element.condition", true);
		builder.appendI15d("searcheredit.element.value", true);
		
		// Column
		this.elementColumn = new JXComboBox();
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
		
		builder.append(this.elementColumn);
		
		// Condition
		this.elementCondition = new JXComboBox();
		this.elementCondition.setRenderer(new DefaultListRenderer(
				StringValueFilterCondition.INSTANCE));
		this.elementCondition.setEnabled(false);
		this.elementCondition.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if (TaskFilterElementPanel.this.element == null)
					return;
				
				TaskColumn column = (TaskColumn) TaskFilterElementPanel.this.elementColumn.getSelectedItem();
				
				switch (column) {
					case CONTEXT:
					case FOLDER:
					case GOAL:
					case LOCATION:
					case PARENT:
						break;
					default:
						return;
				}
				
				TaskFilterElementPanel.this.resetFields(
						(TaskColumn) TaskFilterElementPanel.this.elementColumn.getSelectedItem(),
						(Condition<?, ?>) TaskFilterElementPanel.this.elementCondition.getSelectedItem(),
						null);
			}
			
		});
		
		builder.append(this.elementCondition);
		
		// Value
		this.elementValueCb = new JXComboBox();
		this.elementValueCb.setEnabled(false);
		
		this.elementValueTf = new JTextField();
		this.elementValueTf.setEnabled(false);
		
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
		valuePanel.add(this.elementValueCb);
		valuePanel.add(this.elementValueTf);
		
		builder.append(valuePanel);
		
		// Lay out the panel
		panel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
}
