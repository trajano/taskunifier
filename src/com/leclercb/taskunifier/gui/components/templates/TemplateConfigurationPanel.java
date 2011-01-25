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
package com.leclercb.taskunifier.gui.components.templates;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TemplateConfigurationPanel extends JSplitPane {
	
	public TemplateConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		// Initialize Fields
		final JTextField templateTitle = new JTextField(30);
		final JSeparator templateSeparator = new JSeparator(
				JSeparator.HORIZONTAL);
		
		final JTextField templateTaskTitle = new JTextField(30);
		final JTextField templateTaskTags = new JTextField(30);
		
		// Initialize Model List
		final TemplateList modelList = new TemplateList() {
			
			private BeanAdapter<Template> adapter;
			
			{
				this.adapter = new BeanAdapter<Template>((Template) null, true);
				
				ValueModel titleModel = this.adapter.getValueModel(Template.PROP_TITLE);
				Bindings.bind(templateTitle, titleModel);
				
				ValueModel taskTitleModel = this.adapter.getValueModel(Template.PROP_TASK_TITLE);
				Bindings.bind(templateTaskTitle, taskTitleModel);
				
				ValueModel taskTagsModel = this.adapter.getValueModel(Template.PROP_TASK_TAGS);
				Bindings.bind(templateTaskTags, taskTagsModel);
			}
			
			@Override
			public void addTemplate() {
				super.addTemplate();
				TemplateConfigurationPanel.this.focusAndSelectTextInTextField(templateTitle);
			}
			
			@Override
			public void templateSelected(Template template) {
				this.adapter.setBean(template != null ? template : null);
				templateTitle.setEnabled(template != null);
				templateTaskTitle.setEnabled(template != null);
				templateTaskTags.setEnabled(template != null);
			}
			
		};
		
		this.setLeftComponent(modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(rightPanel);
		
		JPanel info = new JPanel();
		info.setBorder(new LineBorder(Color.BLACK));
		info.setLayout(new SpringLayout());
		rightPanel.add(info, BorderLayout.NORTH);
		
		JLabel label = null;
		
		// Template Title
		label = new JLabel(Translations.getString("general.template.title")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		templateTitle.setEnabled(false);
		info.add(templateTitle);
		
		// Template Separator
		
		label = new JLabel();
		info.add(label);
		
		info.add(templateSeparator);
		
		// Template Task Title
		label = new JLabel(
				Translations.getString("general.task.title") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskTitle.setEnabled(false);
		info.add(templateTaskTitle);
		
		// Template Task Tags
		label = new JLabel(
				Translations.getString("general.task.tags") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskTags.setEnabled(false);
		info.add(templateTaskTags);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 4, 2, 6, 6, 6, 6);
		
		this.setDividerLocation(200);
	}
	
	private void focusAndSelectTextInTextField(JTextField field) {
		int length = field.getText().length();
		
		field.setSelectionStart(0);
		field.setSelectionEnd(length);
		
		field.requestFocus();
	}
	
}
