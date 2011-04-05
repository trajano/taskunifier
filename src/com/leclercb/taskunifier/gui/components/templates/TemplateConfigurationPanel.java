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
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.FormatterUtils;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.commons.converters.LengthConverter;
import com.leclercb.taskunifier.gui.commons.converters.ModelConverter;
import com.leclercb.taskunifier.gui.commons.converters.TimeConverter;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskReminderListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class TemplateConfigurationPanel extends JSplitPane {
	
	public TemplateConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setBorder(null);
		
		// Initialize Fields
		final JTextField templateTitle = new JTextField(20);
		final JTextField templateTaskTitle = new JTextField();
		final JTextField templateTaskTags = new JTextField();
		final JComboBox templateTaskFolder = ComponentFactory.createModelComboBox(null);
		final JComboBox templateTaskContext = ComponentFactory.createModelComboBox(null);
		final JComboBox templateTaskGoal = ComponentFactory.createModelComboBox(null);
		final JComboBox templateTaskLocation = ComponentFactory.createModelComboBox(null);
		final JCheckBox templateTaskCompleted = new JCheckBox();
		final JFormattedTextField templateTaskDueDate = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		final JSpinner templateTaskDueTime = new JSpinner();
		final JFormattedTextField templateTaskStartDate = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		final JSpinner templateTaskStartTime = new JSpinner();
		final JComboBox templateTaskReminder = new JComboBox();
		final JTextField templateTaskRepeat = new JTextField();
		final JComboBox templateTaskRepeatFrom = new JComboBox();
		final JComboBox templateTaskStatus = new JComboBox();
		final JSpinner templateTaskLength = new JSpinner();
		final JComboBox templateTaskPriority = new JComboBox();
		final JCheckBox templateTaskStar = new JCheckBox();
		final JTextArea templateTaskNote = new JTextArea(3, 5);
		
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
				
				ValueModel taskFolderModel = new ModelConverter(
						ModelType.FOLDER,
						this.adapter.getValueModel(Template.PROP_TASK_FOLDER));
				templateTaskFolder.setModel(new ComboBoxAdapter<Folder>(
						new FolderModel(true),
						taskFolderModel));
				
				ValueModel taskContextModel = new ModelConverter(
						ModelType.CONTEXT,
						this.adapter.getValueModel(Template.PROP_TASK_CONTEXT));
				templateTaskContext.setModel(new ComboBoxAdapter<Context>(
						new ContextModel(true),
						taskContextModel));
				
				ValueModel taskGoalModel = new ModelConverter(
						ModelType.GOAL,
						this.adapter.getValueModel(Template.PROP_TASK_GOAL));
				templateTaskGoal.setModel(new ComboBoxAdapter<Goal>(
						new GoalModel(true),
						taskGoalModel));
				
				ValueModel taskLocationModel = new ModelConverter(
						ModelType.LOCATION,
						this.adapter.getValueModel(Template.PROP_TASK_LOCATION));
				templateTaskLocation.setModel(new ComboBoxAdapter<Location>(
						new LocationModel(true),
						taskLocationModel));
				
				ValueModel taskCompletedModel = this.adapter.getValueModel(Template.PROP_TASK_COMPLETED);
				Bindings.bind(templateTaskCompleted, taskCompletedModel);
				
				ValueModel taskDueDateModel = this.adapter.getValueModel(Template.PROP_TASK_DUE_DATE);
				Bindings.bind(templateTaskDueDate, taskDueDateModel);
				
				TimeConverter taskDueTimeModel = new TimeConverter(
						this.adapter.getValueModel(Template.PROP_TASK_DUE_TIME));
				SpinnerDateModel taskDueTimeSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
						taskDueTimeModel,
						(Date) taskDueTimeModel.convertFromSubject(0));
				
				templateTaskDueTime.setModel(taskDueTimeSpinnerModel);
				templateTaskDueTime.setEditor(new JSpinner.DateEditor(
						templateTaskDueTime,
						Main.SETTINGS.getStringProperty("date.time_format")));
				
				ValueModel taskStartDateModel = this.adapter.getValueModel(Template.PROP_TASK_START_DATE);
				Bindings.bind(templateTaskStartDate, taskStartDateModel);
				
				TimeConverter taskStartTimeModel = new TimeConverter(
						this.adapter.getValueModel(Template.PROP_TASK_START_TIME));
				SpinnerDateModel taskStartTimeSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
						taskStartTimeModel,
						(Date) taskStartTimeModel.convertFromSubject(0));
				
				templateTaskStartTime.setModel(taskStartTimeSpinnerModel);
				templateTaskStartTime.setEditor(new JSpinner.DateEditor(
						templateTaskStartTime,
						Main.SETTINGS.getStringProperty("date.time_format")));
				
				ValueModel taskReminderModel = this.adapter.getValueModel(Template.PROP_TASK_REMINDER);
				templateTaskReminder.setModel(new ComboBoxAdapter<Integer>(
						new Integer[] { 0, 5, 15, 30, 60, 60 * 24, 60 * 24 * 7 },
						taskReminderModel));
				
				ValueModel taskRepeatModel = this.adapter.getValueModel(Template.PROP_TASK_REPEAT);
				Bindings.bind(templateTaskRepeat, taskRepeatModel);
				
				ValueModel taskRepeatFromModel = this.adapter.getValueModel(Template.PROP_TASK_REPEAT_FROM);
				templateTaskRepeatFrom.setModel(new ComboBoxAdapter<TaskRepeatFrom>(
						TaskRepeatFrom.values(),
						taskRepeatFromModel));
				
				ValueModel taskStatusModel = this.adapter.getValueModel(Template.PROP_TASK_STATUS);
				templateTaskStatus.setModel(new ComboBoxAdapter<TaskStatus>(
						TaskStatus.values(),
						taskStatusModel));
				
				LengthConverter taskLengthModel = new LengthConverter(
						this.adapter.getValueModel(Template.PROP_TASK_LENGTH));
				SpinnerDateModel taskLengthSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
						taskLengthModel,
						(Date) taskLengthModel.convertFromSubject(0));
				
				templateTaskLength.setModel(taskLengthSpinnerModel);
				templateTaskLength.setEditor(new JSpinner.DateEditor(
						templateTaskLength,
						Main.SETTINGS.getStringProperty("date.time_format")));
				
				ValueModel taskPriorityModel = this.adapter.getValueModel(Template.PROP_TASK_PRIORITY);
				templateTaskPriority.setModel(new ComboBoxAdapter<TaskPriority>(
						TaskPriority.values(),
						taskPriorityModel));
				
				ValueModel taskStarModel = this.adapter.getValueModel(Template.PROP_TASK_STAR);
				Bindings.bind(templateTaskStar, taskStarModel);
				
				ValueModel taskNoteModel = this.adapter.getValueModel(Template.PROP_TASK_NOTE);
				Bindings.bind(templateTaskNote, taskNoteModel);
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
				templateTaskFolder.setEnabled(template != null);
				templateTaskContext.setEnabled(template != null);
				templateTaskGoal.setEnabled(template != null);
				templateTaskLocation.setEnabled(template != null);
				templateTaskCompleted.setEnabled(template != null);
				templateTaskDueDate.setEnabled(template != null);
				templateTaskDueTime.setEnabled(template != null);
				templateTaskStartDate.setEnabled(template != null);
				templateTaskStartTime.setEnabled(template != null);
				templateTaskReminder.setEnabled(template != null);
				templateTaskRepeat.setEnabled(template != null);
				templateTaskRepeatFrom.setEnabled(template != null);
				templateTaskStatus.setEnabled(template != null);
				templateTaskLength.setEnabled(template != null);
				templateTaskPriority.setEnabled(template != null);
				templateTaskStar.setEnabled(template != null);
				templateTaskNote.setEnabled(template != null);
			}
			
		};
		
		this.setLeftComponent(modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(ComponentFactory.createJScrollPane(
				rightPanel,
				false));
		
		JPanel info = new JPanel();
		info.setLayout(new SpringLayout());
		rightPanel.add(info, BorderLayout.NORTH);
		
		JLabel label = null;
		
		// Help
		info.add(new JLabel());
		info.add(Help.getHelpButton("manage_templates.html"));
		
		// Template Title
		label = new JLabel(Translations.getString("general.template.title")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		templateTitle.setEnabled(false);
		info.add(templateTitle);
		
		// Template Separator
		
		label = new JLabel();
		info.add(label);
		
		info.add(new JSeparator(SwingConstants.HORIZONTAL));
		
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
		
		// Template Task Context
		label = new JLabel(
				Translations.getString("general.task.context") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskContext.setEnabled(false);
		info.add(templateTaskContext);
		
		// Template Task Folder
		label = new JLabel(
				Translations.getString("general.task.folder") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskFolder.setEnabled(false);
		info.add(templateTaskFolder);
		
		// Template Task Goal
		label = new JLabel(
				Translations.getString("general.task.goal") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskGoal.setEnabled(false);
		info.add(templateTaskGoal);
		
		// Template Task Location
		label = new JLabel(Translations.getString("general.task.location")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskLocation.setEnabled(false);
		info.add(templateTaskLocation);
		
		// Template Task Completed
		label = new JLabel(Translations.getString("general.task.completed")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskCompleted.setEnabled(false);
		info.add(templateTaskCompleted);
		
		// Template Task Due Date
		label = new JLabel(Translations.getString("general.task.due_date")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		JPanel taskDueDatePanel = new JPanel(new BorderLayout(10, 0));
		info.add(taskDueDatePanel);
		
		templateTaskDueDate.setEnabled(false);
		taskDueDatePanel.add(templateTaskDueDate, BorderLayout.CENTER);
		
		templateTaskDueTime.setEnabled(false);
		taskDueDatePanel.add(templateTaskDueTime, BorderLayout.EAST);
		
		// Template Task Start Date
		label = new JLabel(Translations.getString("general.task.start_date")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		JPanel taskStartDatePanel = new JPanel(new BorderLayout(10, 0));
		info.add(taskStartDatePanel);
		
		templateTaskStartDate.setEnabled(false);
		taskStartDatePanel.add(templateTaskStartDate, BorderLayout.CENTER);
		
		templateTaskStartTime.setEnabled(false);
		taskStartDatePanel.add(templateTaskStartTime, BorderLayout.EAST);
		
		// Template Task Reminder
		label = new JLabel(Translations.getString("general.task.reminder")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskReminder.setRenderer(new TaskReminderListCellRenderer());
		templateTaskReminder.setEditable(true);
		
		templateTaskReminder.setEnabled(false);
		info.add(templateTaskReminder);
		
		// Template Task Repeat
		label = new JLabel(
				Translations.getString("general.task.repeat") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskRepeat.setEnabled(false);
		info.add(templateTaskRepeat);
		
		// Template Task Repeat From
		label = new JLabel(Translations.getString("general.task.repeat_from")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskRepeatFrom.setEnabled(false);
		templateTaskRepeatFrom.setRenderer(new TaskRepeatFromListCellRenderer());
		info.add(templateTaskRepeatFrom);
		
		// Template Task Status
		label = new JLabel(
				Translations.getString("general.task.status") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskStatus.setEnabled(false);
		templateTaskStatus.setRenderer(new TaskStatusListCellRenderer());
		info.add(templateTaskStatus);
		
		// Template Task Length
		label = new JLabel(
				Translations.getString("general.task.length") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskLength.setEnabled(false);
		info.add(templateTaskLength);
		
		// Template Task Priority
		label = new JLabel(Translations.getString("general.task.priority")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskPriority.setEnabled(false);
		templateTaskPriority.setRenderer(new TaskPriorityListCellRenderer());
		info.add(templateTaskPriority);
		
		// Template Task Star
		label = new JLabel(
				Translations.getString("general.task.star") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskStar.setIcon(Images.getResourceImage(
				"checkbox_star.png",
				18,
				18));
		templateTaskStar.setSelectedIcon(Images.getResourceImage(
				"checkbox_star_selected.png",
				18,
				18));
		
		templateTaskStar.setEnabled(false);
		info.add(templateTaskStar);
		
		// Template Task Note
		label = new JLabel(
				Translations.getString("general.task.note") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		templateTaskNote.setEnabled(false);
		info.add(new JScrollPane(templateTaskNote));
		
		// Template Separator
		
		label = new JLabel();
		info.add(label);
		
		info.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		// Set Default Info
		label = new JLabel();
		info.add(label);
		
		label = new JLabel(Translations.getString("templates.set_default"));
		info.add(label);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 22, 2, 6, 6, 6, 6);
		
		this.setDividerLocation(200);
	}
	
	private void focusAndSelectTextInTextField(JTextField field) {
		int length = field.getText().length();
		
		field.setSelectionStart(0);
		field.setSelectionEnd(length);
		
		field.requestFocus();
	}
	
}
