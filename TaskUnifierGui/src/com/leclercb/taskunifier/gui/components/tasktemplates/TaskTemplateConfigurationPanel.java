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
package com.leclercb.taskunifier.gui.components.tasktemplates;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventComboBoxModel;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.FormatterUtils;
import com.leclercb.taskunifier.api.models.BasicModel;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.actions.ActionManageModels;
import com.leclercb.taskunifier.gui.commons.converters.TemplateShorcutKeyConverter;
import com.leclercb.taskunifier.gui.commons.converters.TemplateTimeConverter;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.TaskPriorityModel;
import com.leclercb.taskunifier.gui.commons.models.TaskReminderModel;
import com.leclercb.taskunifier.gui.commons.models.TaskRepeatFromModel;
import com.leclercb.taskunifier.gui.commons.models.TaskRepeatModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskReminder;
import com.leclercb.taskunifier.gui.components.modelnote.HTMLEditorInterface;
import com.leclercb.taskunifier.gui.components.modelnote.editors.WysiwygHTMLEditorPane;
import com.leclercb.taskunifier.gui.components.models.ModelConfigurationDialog.ModelConfigurationTab;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TUModelListField;
import com.leclercb.taskunifier.gui.swing.TUShortcutField;
import com.leclercb.taskunifier.gui.swing.TUSpinnerTimeEditor;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.TaskStatusList;

public class TaskTemplateConfigurationPanel extends JSplitPane {
	
	private JTextField templateTitle;
	private TUShortcutField templateShortcut;
	
	private JTextField taskTitle;
	private JTextField taskTags;
	private JComboBox taskFolder;
	private TUModelListField<Context> taskContexts;
	private TUModelListField<Goal> taskGoals;
	private TUModelListField<Location> taskLocations;
	private JSpinner taskProgress;
	private JCheckBox taskCompleted;
	private JFormattedTextField taskStartDate;
	private JSpinner taskStartTime;
	private JFormattedTextField taskDueDate;
	private JSpinner taskDueTime;
	private JComboBox taskStartDateReminder;
	private JComboBox taskDueDateReminder;
	private JComboBox taskRepeat;
	private JComboBox taskRepeatFrom;
	private JComboBox taskStatus;
	private JSpinner taskLength;
	private JComboBox taskPriority;
	private JCheckBox taskStar;
	private WysiwygHTMLEditorPane taskNote;
	
	public TaskTemplateConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.templateTitle = new JTextField();
		this.templateShortcut = new TUShortcutField();
		
		this.taskTitle = new JTextField();
		this.taskTags = new JTextField();
		this.taskFolder = ComponentFactory.createModelComboBox(null, true);
		this.taskContexts = new TUModelListField<Context>(ModelType.CONTEXT);
		this.taskGoals = new TUModelListField<Goal>(ModelType.GOAL);
		this.taskLocations = new TUModelListField<Location>(ModelType.LOCATION);
		this.taskProgress = new JSpinner();
		this.taskCompleted = new JCheckBox();
		this.taskStartDate = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		this.taskStartTime = new JSpinner();
		this.taskDueDate = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		this.taskDueTime = new JSpinner();
		this.taskStartDateReminder = new JComboBox();
		this.taskDueDateReminder = new JComboBox();
		this.taskRepeat = new JComboBox();
		this.taskRepeatFrom = ComponentFactory.createTaskRepeatFromComboBox(
				null,
				true);
		this.taskStatus = ComponentFactory.createTaskStatusComboBox(null, true);
		this.taskLength = new JSpinner();
		this.taskPriority = ComponentFactory.createTaskPriorityComboBox(
				null,
				true);
		this.taskStar = new JCheckBox();
		this.taskNote = new WysiwygHTMLEditorPane("", false, null);
		
		// Initialize Model List
		final TaskTemplateList modelList = new TaskTemplateList(
				this.templateTitle);
		
		this.setLeftComponent(modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(ComponentFactory.createJScrollPane(
				rightPanel,
				false));
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow, "
						+ "10dlu, "
						+ "right:pref, 4dlu, fill:default:grow");
		
		// Disable
		this.templateTitle.setEnabled(false);
		this.templateShortcut.setEnabled(false);
		this.taskTitle.setEnabled(false);
		this.taskTags.setEnabled(false);
		this.taskFolder.setEnabled(false);
		this.taskContexts.setEnabled(false);
		this.taskGoals.setEnabled(false);
		this.taskLocations.setEnabled(false);
		this.taskProgress.setEnabled(false);
		this.taskCompleted.setEnabled(false);
		this.taskDueDate.setEnabled(false);
		this.taskDueTime.setEnabled(false);
		this.taskStartDate.setEnabled(false);
		this.taskStartTime.setEnabled(false);
		this.taskDueDateReminder.setEnabled(false);
		this.taskStartDateReminder.setEnabled(false);
		this.taskRepeat.setEnabled(false);
		this.taskRepeatFrom.setEnabled(false);
		this.taskStatus.setEnabled(false);
		this.taskLength.setEnabled(false);
		this.taskPriority.setEnabled(false);
		this.taskStar.setEnabled(false);
		this.taskNote.setEnabled(false);
		
		// Template title
		builder.appendI15d("general.template.title", true);
		builder.getBuilder().append(this.templateTitle, 5);
		
		// Template shortcut
		builder.appendI15d(
				"general.template.shortcut",
				true,
				this.templateShortcut);
		
		// Empty
		builder.getBuilder().append("", new JLabel());
		
		// Separator
		builder.getBuilder().appendSeparator();
		
		// Task Title
		builder.appendI15d("general.task.title", true);
		builder.getBuilder().append(this.taskTitle, 5);
		
		// Task Star
		this.taskStar.setIcon(ImageUtils.getResourceImage(
				"checkbox_star.png",
				16,
				16));
		this.taskStar.setSelectedIcon(ImageUtils.getResourceImage(
				"checkbox_star_selected.png",
				16,
				16));
		
		builder.appendI15d("general.task.star", true, this.taskStar);
		
		// Task Completed
		builder.appendI15d("general.task.completed", true, this.taskCompleted);
		
		// Task Priority
		builder.appendI15d("general.task.priority", true, this.taskPriority);
		
		// Task Tags
		builder.appendI15d("general.task.tags", true, this.taskTags);
		
		// Task Status
		builder.appendI15d("general.task.status", true, this.taskStatus);
		
		// Task Progress
		builder.appendI15d("general.task.progress", true, this.taskProgress);
		
		// Separator
		builder.getBuilder().appendSeparator();
		
		// Task Folder
		builder.appendI15d("general.task.folder", true, this.createPanel(
				this.taskFolder,
				new JButton(new ActionManageModels(
						16,
						16,
						ModelConfigurationTab.FOLDERS))));
		
		// Task Goal
		builder.appendI15d("general.task.goal", true, this.createPanel(
				this.taskGoals,
				new JButton(new ActionManageModels(
						16,
						16,
						ModelConfigurationTab.GOALS))));
		
		// Task Context
		builder.appendI15d("general.task.context", true, this.createPanel(
				this.taskContexts,
				new JButton(new ActionManageModels(
						16,
						16,
						ModelConfigurationTab.CONTEXTS))));
		
		// Task Location
		builder.appendI15d("general.task.location", true, this.createPanel(
				this.taskLocations,
				new JButton(new ActionManageModels(
						16,
						16,
						ModelConfigurationTab.LOCATIONS))));
		
		// Separator
		builder.getBuilder().appendSeparator();
		
		// Task Start Date
		JPanel taskStartDatePanel = new JPanel(new BorderLayout(10, 0));
		
		builder.appendI15d("general.task.start_date", true, taskStartDatePanel);
		
		taskStartDatePanel.add(this.taskStartDate, BorderLayout.CENTER);
		taskStartDatePanel.add(this.taskStartTime, BorderLayout.EAST);
		
		// Task Due Date
		JPanel taskDueDatePanel = new JPanel(new BorderLayout(10, 0));
		
		builder.appendI15d("general.task.due_date", true, taskDueDatePanel);
		
		taskDueDatePanel.add(this.taskDueDate, BorderLayout.CENTER);
		taskDueDatePanel.add(this.taskDueTime, BorderLayout.EAST);
		
		// Task Start Date Reminder
		this.taskStartDateReminder.setRenderer(new DefaultListRenderer(
				StringValueTaskReminder.INSTANCE));
		
		this.taskStartDateReminder.setEditable(true);
		
		builder.appendI15d(
				"general.task.start_date_reminder",
				true,
				this.taskStartDateReminder);
		
		// Task Due Date Reminder
		this.taskDueDateReminder.setRenderer(new DefaultListRenderer(
				StringValueTaskReminder.INSTANCE));
		
		this.taskDueDateReminder.setEditable(true);
		
		builder.appendI15d(
				"general.task.due_date_reminder",
				true,
				this.taskDueDateReminder);
		
		// Task Length
		builder.appendI15d("general.task.length", true, this.taskLength);
		
		// Task Repeat
		ComponentFactory.createRepeatComboBox(this.taskRepeat);
		
		builder.appendI15d("general.task.repeat", true, this.taskRepeat);
		
		// Task Repeat From
		builder.appendI15d(
				"general.task.repeat_from",
				true,
				this.taskRepeatFrom);
		
		// Empty
		builder.getBuilder().append("", new JLabel());
		
		// Separator
		builder.getBuilder().appendSeparator();
		
		// Task Note
		this.taskNote.getComponent().setBorder(
				BorderFactory.createLineBorder(Color.GRAY));
		
		JPanel notePanel = new JPanel(new BorderLayout());
		notePanel.add(new JLabel(Translations.getString("general.task.note")
				+ ":"), BorderLayout.NORTH);
		notePanel.add(this.taskNote.getComponent(), BorderLayout.CENTER);
		
		// Lay out the panel
		rightPanel.add(builder.getPanel(), BorderLayout.NORTH);
		rightPanel.add(notePanel, BorderLayout.CENTER);
		
		this.setDividerLocation(250);
	}
	
	private JPanel createPanel(JComponent component, JButton button) {
		button.setText("");
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.CENTER);
		panel.add(button, BorderLayout.EAST);
		
		return panel;
	}
	
	private class TaskTemplateList extends com.leclercb.taskunifier.gui.components.tasktemplates.TaskTemplateList {
		
		private BeanAdapter<TaskTemplate> adapter;
		
		public TaskTemplateList(JTextField templateTitle) {
			super(templateTitle);
			
			this.adapter = new BeanAdapter<TaskTemplate>(
					(TaskTemplate) null,
					true);
			
			ValueModel titleModel = this.adapter.getValueModel(BasicModel.PROP_TITLE);
			Bindings.bind(templateTitle, titleModel);
			
			TemplateShorcutKeyConverter shortcutModel = new TemplateShorcutKeyConverter(
					this.adapter.getValueModel("properties"));
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.templateShortcut,
					TUShortcutField.PROP_SHORTCUT_KEY,
					shortcutModel);
			
			ValueModel taskTitleModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_TITLE);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskTitle,
					taskTitleModel);
			
			ValueModel taskTagsModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_TAGS);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskTags,
					taskTagsModel);
			
			ValueModel taskFolderModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_FOLDER);
			TaskTemplateConfigurationPanel.this.taskFolder.setModel(new ComboBoxAdapter<Folder>(
					new FolderModel(true, false),
					taskFolderModel));
			
			ValueModel taskContextsModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_CONTEXTS);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskContexts,
					TUModelListField.PROP_MODELLIST,
					taskContextsModel);
			
			ValueModel taskGoalsModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_GOALS);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskGoals,
					TUModelListField.PROP_MODELLIST,
					taskGoalsModel);
			
			ValueModel taskLocationsModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_LOCATIONS);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskLocations,
					TUModelListField.PROP_MODELLIST,
					taskLocationsModel);
			
			ValueModel taskProgressModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_PROGRESS);
			SpinnerNumberModel taskProgressSpinnerModel = SpinnerAdapterFactory.createNumberAdapter(
					taskProgressModel,
					new Double(0.00),
					new Double(0.00),
					new Double(1.00),
					new Double(0.10));
			
			TaskTemplateConfigurationPanel.this.taskProgress.setModel(taskProgressSpinnerModel);
			TaskTemplateConfigurationPanel.this.taskProgress.setEditor(new JSpinner.NumberEditor(
					TaskTemplateConfigurationPanel.this.taskProgress,
					"##0%"));
			
			ValueModel taskCompletedModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_COMPLETED);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskCompleted,
					taskCompletedModel);
			
			ValueModel taskDueDateModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_DUE_DATE);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskDueDate,
					taskDueDateModel);
			
			TemplateTimeConverter taskDueTimeModel = new TemplateTimeConverter(
					this.adapter.getValueModel(TaskTemplate.PROP_TASK_DUE_TIME));
			SpinnerDateModel taskDueTimeSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
					taskDueTimeModel,
					(Date) taskDueTimeModel.convertFromSubject(0));
			
			TaskTemplateConfigurationPanel.this.taskDueTime.setModel(taskDueTimeSpinnerModel);
			TaskTemplateConfigurationPanel.this.taskDueTime.setEditor(new JSpinner.DateEditor(
					TaskTemplateConfigurationPanel.this.taskDueTime,
					Main.getSettings().getStringProperty("date.time_format")));
			
			ValueModel taskStartDateModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_START_DATE);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskStartDate,
					taskStartDateModel);
			
			TemplateTimeConverter taskStartTimeModel = new TemplateTimeConverter(
					this.adapter.getValueModel(TaskTemplate.PROP_TASK_START_TIME));
			SpinnerDateModel taskStartTimeSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
					taskStartTimeModel,
					(Date) taskStartTimeModel.convertFromSubject(0));
			
			TaskTemplateConfigurationPanel.this.taskStartTime.setModel(taskStartTimeSpinnerModel);
			TaskTemplateConfigurationPanel.this.taskStartTime.setEditor(new JSpinner.DateEditor(
					TaskTemplateConfigurationPanel.this.taskStartTime,
					Main.getSettings().getStringProperty("date.time_format")));
			
			ValueModel taskDueDateReminderModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_DUE_DATE_REMINDER);
			StringToIntegerConverter taskDueDateReminderConverter = new StringToIntegerConverter(
					taskDueDateReminderModel);
			TaskTemplateConfigurationPanel.this.taskDueDateReminder.setModel(new ComboBoxAdapter<Integer>(
					new TaskReminderModel(),
					taskDueDateReminderConverter));
			
			ValueModel taskStartDateReminderModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_START_DATE_REMINDER);
			StringToIntegerConverter taskStartDateReminderConverter = new StringToIntegerConverter(
					taskStartDateReminderModel);
			TaskTemplateConfigurationPanel.this.taskStartDateReminder.setModel(new ComboBoxAdapter<Integer>(
					new TaskReminderModel(),
					taskStartDateReminderConverter));
			
			ValueModel taskRepeatModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_REPEAT);
			TaskTemplateConfigurationPanel.this.taskRepeat.setModel(new ComboBoxAdapter<String>(
					new TaskRepeatModel(false),
					taskRepeatModel));
			
			ValueModel taskRepeatFromModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_REPEAT_FROM);
			TaskTemplateConfigurationPanel.this.taskRepeatFrom.setModel(new ComboBoxAdapter<TaskRepeatFrom>(
					new TaskRepeatFromModel(true),
					taskRepeatFromModel));
			
			ValueModel taskStatusModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_STATUS);
			TaskTemplateConfigurationPanel.this.taskStatus.setModel(new ComboBoxAdapter<String>(
					new EventComboBoxModel<String>(new SortedList<String>(
							TaskStatusList.getInstance().getEventList())),
					taskStatusModel));
			
			ValueModel taskLengthModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_LENGTH);
			SpinnerNumberModel taskLengthSpinnerModel = SpinnerAdapterFactory.createNumberAdapter(
					taskLengthModel,
					0,
					0,
					5760,
					1);
			
			TaskTemplateConfigurationPanel.this.taskLength.setModel(taskLengthSpinnerModel);
			TaskTemplateConfigurationPanel.this.taskLength.setEditor(new TUSpinnerTimeEditor(
					TaskTemplateConfigurationPanel.this.taskLength));
			
			ValueModel taskPriorityModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_PRIORITY);
			TaskTemplateConfigurationPanel.this.taskPriority.setModel(new ComboBoxAdapter<TaskPriority>(
					new TaskPriorityModel(true),
					taskPriorityModel));
			
			ValueModel taskStarModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_STAR);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskStar,
					taskStarModel);
			
			ValueModel taskNoteModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_NOTE);
			Bindings.bind(
					TaskTemplateConfigurationPanel.this.taskNote,
					HTMLEditorInterface.PROP_TEXT,
					taskNoteModel);
		}
		
		@Override
		public void templateSelected(TaskTemplate template) {
			this.adapter.setBean(template != null ? template : null);
			
			TaskTemplateConfigurationPanel.this.templateTitle.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.templateShortcut.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskTitle.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskTags.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskFolder.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskContexts.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskGoals.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskLocations.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskProgress.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskCompleted.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskDueDate.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskDueTime.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskStartDate.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskStartTime.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskDueDateReminder.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskStartDateReminder.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskRepeat.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskRepeatFrom.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskStatus.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskLength.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskPriority.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskStar.setEnabled(template != null);
			TaskTemplateConfigurationPanel.this.taskNote.setEnabled(template != null);
		}
		
	}
	
	private static class StringToIntegerConverter extends AbstractConverter {
		
		public StringToIntegerConverter(ValueModel subject) {
			super(subject);
		}
		
		@Override
		public void setValue(Object value) {
			if (value == null)
				this.subject.setValue(null);
			
			try {
				Integer i = Integer.parseInt(value.toString());
				this.subject.setValue(i);
			} catch (NumberFormatException exc) {
				this.subject.setValue(null);
			}
		}
		
		@Override
		public Object convertFromSubject(Object value) {
			return this.subject.getValue();
		}
		
	}
	
}
