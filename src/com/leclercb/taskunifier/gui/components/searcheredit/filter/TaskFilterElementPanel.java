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
package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

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
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueBoolean;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskFilterCondition;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.utils.FormBuilder;

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
				case MODEL:
					value = null;
					break;
				case TITLE:
				case TAGS:
				case NOTE:
				case REPEAT:
					value = this.elementValue.getSelectedItem().toString();
					break;
				case COMPLETED_ON:
				case DUE_DATE:
				case START_DATE:
					try {
						if (this.elementValue.getSelectedItem().toString().length() == 0)
							value = null;
						else
							value = Integer.parseInt(this.elementValue.getSelectedItem().toString());
					} catch (NumberFormatException e) {
						value = 0;
					}
					break;
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
		
		DefaultComboBoxModel taskColumnsModel = new DefaultComboBoxModel(
				TaskColumn.values());
		taskColumnsModel.removeElement(TaskColumn.MODEL);
		
		this.elementColumn.setModel(taskColumnsModel);
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
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueModel(),
						new IconValueModel()));
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case CONTEXT:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new ContextModel(true));
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueModel(),
						new IconValueModel()));
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case GOAL:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new GoalModel(true));
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueModel(),
						new IconValueModel()));
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case LOCATION:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new LocationModel(true));
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueModel(),
						new IconValueModel()));
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case PARENT:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.ModelCondition.values()));
				this.elementValue.setModel(new TaskModel(true));
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueModel(),
						new IconValueModel()));
				this.elementValue.setSelectedItem(value);
				this.elementValue.setEditable(false);
				break;
			case COMPLETED:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						new Object[] { TaskFilter.StringCondition.EQUALS }));
				this.elementValue.setModel(new DefaultComboBoxModel(
						new Object[] { true, false }));
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueBoolean()));
				this.elementValue.setSelectedIndex(value != null
						&& Boolean.parseBoolean(value.toString()) ? 0 : 1);
				this.elementValue.setEditable(false);
				break;
			case COMPLETED_ON:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.DaysCondition.values()));
				this.elementValue.addItem(value == null ? "" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case DUE_DATE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.DaysCondition.values()));
				this.elementValue.addItem(value == null ? "" : value);
				this.elementValue.setSelectedIndex(0);
				this.elementValue.setEditable(true);
				break;
			case START_DATE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.DaysCondition.values()));
				this.elementValue.addItem(value == null ? "" : value);
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
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueTaskRepeatFrom()));
				this.elementValue.setSelectedItem(value == null ? TaskRepeatFrom.DUE_DATE : value);
				this.elementValue.setEditable(false);
				break;
			case STATUS:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						TaskFilter.EnumCondition.values()));
				this.elementValue.setModel(new DefaultComboBoxModel(
						TaskStatus.values()));
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueTaskStatus()));
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
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueTaskPriority(),
						new IconValueTaskPriority()));
				this.elementValue.setSelectedItem(value == null ? TaskPriority.LOW : value);
				this.elementValue.setEditable(false);
				break;
			case STAR:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						new Object[] { TaskFilter.StringCondition.EQUALS }));
				this.elementValue.setModel(new DefaultComboBoxModel(
						new Object[] { true, false }));
				this.elementValue.setRenderer(new DefaultListRenderer(
						new StringValueBoolean()));
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
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		FormBuilder builder = new FormBuilder(
				"fill:default:grow, 10dlu, fill:default:grow, 10dlu, fill:default:grow");
		
		builder.appendI15d("searcheredit.element.column", true);
		builder.appendI15d("searcheredit.element.condition", true);
		builder.appendI15d("searcheredit.element.value", true);
		
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
		
		builder.append(this.elementColumn);
		
		// Condition
		this.elementCondition = new JComboBox();
		this.elementCondition.setRenderer(new DefaultListRenderer(
				new StringValueTaskFilterCondition()));
		this.elementCondition.setEnabled(false);
		
		builder.append(this.elementCondition);
		
		// Value
		this.elementValue = new JComboBox(TaskColumn.values());
		this.elementValue.setEnabled(false);
		
		builder.append(this.elementValue);
		
		// Lay out the panel
		panel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.NORTH);
	}
	
}
