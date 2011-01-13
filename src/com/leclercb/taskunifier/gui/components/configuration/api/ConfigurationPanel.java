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
package com.leclercb.taskunifier.gui.components.configuration.api;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.gui.help.Help;

public abstract class ConfigurationPanel extends JPanel {
	
	private String helpFile;
	private List<ConfigurationField> fields;
	
	public ConfigurationPanel() {
		this(null);
	}
	
	public ConfigurationPanel(String helpFile) {
		this.helpFile = helpFile;
		this.fields = new ArrayList<ConfigurationField>();
	}
	
	public Object getValue(String id) {
		ConfigurationField field = this.getField(id);
		
		if (field == null)
			throw new IllegalArgumentException("Id not found");
		
		return field.getType().getFieldValue();
	}
	
	public void setEnabled(String id, boolean enabled) {
		ConfigurationField field = this.getField(id);
		
		if (field == null)
			throw new IllegalArgumentException("Id not found");
		
		((JComponent) field.getType().getFieldComponent()).setEnabled(enabled);
	}
	
	public ConfigurationField getField(String id) {
		CheckUtils.isNotNull(id, "Id cannot be null");
		
		for (ConfigurationField field : this.fields)
			if (EqualsUtils.equals(id, field.getId()))
				return field;
		
		return null;
	}
	
	public List<ConfigurationField> getFields() {
		return Collections.unmodifiableList(this.fields);
	}
	
	public void addField(ConfigurationField field) {
		CheckUtils.isNotNull(field, "Field cannot be null");
		
		if (this.getField(field.getId()) != null)
			throw new IllegalArgumentException(
					"A field with the same id already exists");
		
		this.fields.add(field);
	}
	
	public void removeField(ConfigurationField field) {
		this.fields.remove(field);
	}
	
	public void pack() {
		this.removeAll();
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());
		
		JLabel label = null;
		Component component = null;
		
		if (this.helpFile != null) {
			panel.add(new JLabel());
			panel.add(Help.getHelpButton(this.helpFile));
		}
		
		for (ConfigurationField field : this.fields) {
			if (field.getLabel() == null)
				label = new JLabel();
			else
				label = new JLabel(
						field.getLabel() + ":",
						SwingConstants.TRAILING);
			
			component = field.getType().getFieldComponent();
			
			panel.add(label);
			panel.add(component);
		}
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(panel, this.fields.size()
				+ (this.helpFile != null ? 1 : 0), 2, // rows,
														// cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
		
		this.add(panel, BorderLayout.NORTH);
	}
	
	public abstract void saveAndApplyConfig();
	
}
