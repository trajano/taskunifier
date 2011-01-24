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
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.commons.gui.swing.formatters.RegexFormatter;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.models.ContextComboBoxModel;
import com.leclercb.taskunifier.gui.models.FolderComboBoxModel;
import com.leclercb.taskunifier.gui.models.GoalComboBoxModel;
import com.leclercb.taskunifier.gui.models.LocationComboBoxModel;
import com.leclercb.taskunifier.gui.models.TemplateComboBoxModel;
import com.leclercb.taskunifier.gui.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.TemplateListCellRenderer;
import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.template.TemplateFactory;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TemplateConfigurationPanel extends ConfigurationPanel {
	
	private JComboBox templateComboBox;
	
	public TemplateConfigurationPanel() {
		super("configuration_template.html");
		this.initialize();
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {

	}
	
	private void initialize() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		panel.add(buttonsPanel, BorderLayout.EAST);
		
		this.templateComboBox = new JComboBox();
		this.templateComboBox.setModel(new TemplateComboBoxModel());
		this.templateComboBox.setRenderer(new TemplateListCellRenderer());
		panel.add(this.templateComboBox, BorderLayout.CENTER);
		
		this.templateComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Template template = (Template) TemplateConfigurationPanel.this.templateComboBox.getSelectedItem();
				
				if (template != null)
					TemplateConfigurationPanel.this.loadTemplate(template);
			}
			
		});
		
		final JButton addTemplateButton = new JButton(Images.getResourceImage(
				"add.png",
				16,
				16));
		addTemplateButton.setActionCommand("ADD");
		buttonsPanel.add(addTemplateButton);
		
		final JButton removeTemplateButton = new JButton(
				Images.getResourceImage("remove.png", 16, 16));
		removeTemplateButton.setActionCommand("REMOVE");
		buttonsPanel.add(removeTemplateButton);
		
		final JButton defaultTemplateButton = new JButton(
				Images.getResourceImage("properties.png", 16, 16));
		defaultTemplateButton.setActionCommand("DEFAULT");
		defaultTemplateButton.setToolTipText(Translations.getString("general.set_default"));
		buttonsPanel.add(defaultTemplateButton);
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ADD")) {
					Template template = TemplateFactory.getInstance().create(
							Translations.getString("general.template"));
					TemplateConfigurationPanel.this.templateComboBox.setSelectedItem(template);
				} else if (event.getActionCommand().equals("REMOVE")) {
					Template template = (Template) TemplateConfigurationPanel.this.templateComboBox.getSelectedItem();
					TemplateFactory.getInstance().unregister(template);
				} else if (event.getActionCommand().equals("DEFAULT")) {
					Template template = (Template) TemplateConfigurationPanel.this.templateComboBox.getSelectedItem();
					TemplateFactory.getInstance().setDefaultTemplate(template);
				}
			}
			
		};
		
		addTemplateButton.addActionListener(listener);
		removeTemplateButton.addActionListener(listener);
		defaultTemplateButton.addActionListener(listener);
		
		// Template fields
		
		this.addField(new ConfigurationField(
				"TEMPLATES",
				Translations.getString("general.template"),
				new ConfigurationFieldType.Panel(panel)));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"TITLE",
				Translations.getString("general.task.title"),
				new ConfigurationFieldType.TextField("")));
		
		this.addField(new ConfigurationField(
				"TASK_TITLE",
				Translations.getString("general.task.title"),
				new ConfigurationFieldType.TextField("")));
		
		this.addField(new ConfigurationField(
				"TASK_TAGS",
				Translations.getString("general.task.tags"),
				new ConfigurationFieldType.TextField("")));
		
		this.addField(new ConfigurationField(
				"TASK_FOLDER",
				Translations.getString("general.task.folder"),
				new ConfigurationFieldType.ComboBox(new FolderComboBoxModel(
						true), null)));
		
		this.addField(new ConfigurationField(
				"TASK_CONTEXT",
				Translations.getString("general.task.context"),
				new ConfigurationFieldType.ComboBox(new ContextComboBoxModel(
						true), null)));
		
		this.addField(new ConfigurationField(
				"TASK_GOAL",
				Translations.getString("general.task.goal"),
				new ConfigurationFieldType.ComboBox(
						new GoalComboBoxModel(true),
						null)));
		
		this.addField(new ConfigurationField(
				"TASK_LOCATION",
				Translations.getString("general.task.location"),
				new ConfigurationFieldType.ComboBox(new LocationComboBoxModel(
						true), null)));
		
		this.addField(new ConfigurationField(
				"TASK_COMPLETED",
				Translations.getString("general.task.completed"),
				new ConfigurationFieldType.CheckBox(false)));
		
		this.addField(new ConfigurationField(
				"TASK_DUE_DATE",
				Translations.getString("general.task.due_date"),
				new ConfigurationFieldType.FormattedTextField(
						new RegexFormatter("^[0-9]{0,3}$"),
						"")));
		
		this.addField(new ConfigurationField(
				"TASK_START_DATE",
				Translations.getString("general.task.start_date"),
				new ConfigurationFieldType.FormattedTextField(
						new RegexFormatter("^[0-9]{0,3}$"),
						"")));
		
		this.addField(new ConfigurationField(
				"TASK_REMINDER",
				Translations.getString("general.task.reminder"),
				new ConfigurationFieldType.FormattedTextField(
						new RegexFormatter("^[0-9]{0,3}$"),
						"")));
		
		this.addField(new ConfigurationField(
				"TASK_REPEAT",
				Translations.getString("general.task.repeat"),
				new ConfigurationFieldType.TextField("")));
		
		this.addField(new ConfigurationField(
				"TASK_REPEAT_FROM",
				Translations.getString("general.task.repeat_from"),
				new ConfigurationFieldType.ComboBox(
						TaskRepeatFrom.values(),
						TaskRepeatFrom.DUE_DATE)));
		((ConfigurationFieldType.ComboBox) this.getField("TASK_REPEAT_FROM").getType()).getFieldComponent().setRenderer(
				new TaskRepeatFromListCellRenderer());
		
		this.addField(new ConfigurationField(
				"TASK_STATUS",
				Translations.getString("general.task.status"),
				new ConfigurationFieldType.ComboBox(
						TaskStatus.values(),
						TaskStatus.NONE)));
		((ConfigurationFieldType.ComboBox) this.getField("TASK_STATUS").getType()).getFieldComponent().setRenderer(
				new TaskStatusListCellRenderer());
		
		this.addField(new ConfigurationField(
				"TASK_PRIORITY",
				Translations.getString("general.task.priority"),
				new ConfigurationFieldType.ComboBox(
						TaskPriority.values(),
						TaskPriority.LOW)));
		((ConfigurationFieldType.ComboBox) this.getField("TASK_PRIORITY").getType()).getFieldComponent().setRenderer(
				new TaskPriorityListCellRenderer());
		
		this.addField(new ConfigurationField(
				"TASK_STAR",
				Translations.getString("general.task.star"),
				new ConfigurationFieldType.StarCheckBox(false)));
		
		this.addField(new ConfigurationField(
				"TASK_NOTE",
				Translations.getString("general.task.note"),
				new ConfigurationFieldType.TextArea("")));
		
		this.addField(new ConfigurationField(
				"SAVE",
				null,
				new ConfigurationFieldType.Button(
						Translations.getString("general.save"),
						Images.getResourceImage("save.png", 24, 24),
						new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent event) {
								Template template = (Template) TemplateConfigurationPanel.this.templateComboBox.getSelectedItem();
								
								if (template != null)
									TemplateConfigurationPanel.this.saveTemplate(template);
							}
							
						})));
		
		// TODO finish config panel
	}
	
	private void saveTemplate(Template template) {
		template.setTitle((String) this.getValue("TITLE"));
	}
	
	private void loadTemplate(Template template) {
		String tags = "";
		if (template.getTaskTags() != null)
			tags = ArrayUtils.arrayToString(template.getTaskTags(), ", ");
		
		((ConfigurationFieldType.TextField) this.getField("TITLE").getType()).getFieldComponent().setText(
				template.getTitle());
		
		((ConfigurationFieldType.TextField) this.getField("TASK_TITLE").getType()).getFieldComponent().setText(
				template.getTaskTitle());
		((ConfigurationFieldType.TextField) this.getField("TASK_TAGS").getType()).getFieldComponent().setText(
				tags);
		((ConfigurationFieldType.ComboBox) this.getField("TASK_FOLDER").getType()).getFieldComponent().setSelectedItem(
				FolderFactory.getInstance().get(template.getTaskFolder()));
		((ConfigurationFieldType.ComboBox) this.getField("TASK_CONTEXT").getType()).getFieldComponent().setSelectedItem(
				ContextFactory.getInstance().get(template.getTaskContext()));
		((ConfigurationFieldType.ComboBox) this.getField("TASK_GOAL").getType()).getFieldComponent().setSelectedItem(
				GoalFactory.getInstance().get(template.getTaskGoal()));
		((ConfigurationFieldType.ComboBox) this.getField("TASK_LOCATION").getType()).getFieldComponent().setSelectedItem(
				LocationFactory.getInstance().get(template.getTaskLocation()));
	}
	
}
