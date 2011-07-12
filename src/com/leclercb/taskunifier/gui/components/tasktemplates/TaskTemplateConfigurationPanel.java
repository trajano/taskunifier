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
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.FormatterUtils;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.commons.converters.ModelConverter;
import com.leclercb.taskunifier.gui.commons.converters.TaskLengthConverter;
import com.leclercb.taskunifier.gui.commons.converters.TemplateTimeConverter;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.models.TaskPriorityModel;
import com.leclercb.taskunifier.gui.commons.models.TaskReminderModel;
import com.leclercb.taskunifier.gui.commons.models.TaskRepeatFromModel;
import com.leclercb.taskunifier.gui.commons.models.TaskStatusModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskReminder;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskTemplateConfigurationPanel extends JSplitPane {
	
	public TaskTemplateConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setBorder(null);
		
		// Initialize Fields
		final JTextField templateTitle = new JTextField();
		final JTextField templateTaskTitle = new JTextField();
		final JTextField templateTaskTags = new JTextField();
		final JComboBox templateTaskFolder = ComponentFactory.createModelComboBox(null);
		final JComboBox templateTaskContext = ComponentFactory.createModelComboBox(null);
		final JComboBox templateTaskGoal = ComponentFactory.createModelComboBox(null);
		final JComboBox templateTaskLocation = ComponentFactory.createModelComboBox(null);
		final JSpinner templateTaskProgress = new JSpinner();
		final JCheckBox templateTaskCompleted = new JCheckBox();
		final JFormattedTextField templateTaskDueDate = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		final JSpinner templateTaskDueTime = new JSpinner();
		final JFormattedTextField templateTaskStartDate = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		final JSpinner templateTaskStartTime = new JSpinner();
		final JComboBox templateTaskReminder = new JComboBox();
		final JComboBox templateTaskRepeat = new JComboBox();
		final JComboBox templateTaskRepeatFrom = new JComboBox();
		final JComboBox templateTaskStatus = new JComboBox();
		final JSpinner templateTaskLength = new JSpinner();
		final JComboBox templateTaskPriority = new JComboBox();
		final JCheckBox templateTaskStar = new JCheckBox();
		final JTextArea templateTaskNote = new JTextArea(5, 5);
		
		// Set Disabled
		templateTitle.setEnabled(false);
		templateTaskTitle.setEnabled(false);
		templateTaskTags.setEnabled(false);
		templateTaskContext.setEnabled(false);
		templateTaskFolder.setEnabled(false);
		templateTaskGoal.setEnabled(false);
		templateTaskLocation.setEnabled(false);
		templateTaskProgress.setEnabled(false);
		templateTaskCompleted.setEnabled(false);
		templateTaskDueDate.setEnabled(false);
		templateTaskDueTime.setEnabled(false);
		templateTaskStartDate.setEnabled(false);
		templateTaskStartTime.setEnabled(false);
		templateTaskReminder.setEnabled(false);
		templateTaskRepeat.setEnabled(false);
		templateTaskRepeatFrom.setEnabled(false);
		templateTaskStatus.setEnabled(false);
		templateTaskLength.setEnabled(false);
		templateTaskPriority.setEnabled(false);
		templateTaskStar.setEnabled(false);
		templateTaskNote.setEnabled(false);
		
		// Initialize Model List
		final TaskTemplateList modelList = new TaskTemplateList(templateTitle) {
			
			private BeanAdapter<TaskTemplate> adapter;
			
			{
				this.adapter = new BeanAdapter<TaskTemplate>(
						(TaskTemplate) null,
						true);
				
				ValueModel titleModel = this.adapter.getValueModel(TaskTemplate.PROP_TITLE);
				Bindings.bind(templateTitle, titleModel);
				
				ValueModel taskTitleModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_TITLE);
				Bindings.bind(templateTaskTitle, taskTitleModel);
				
				ValueModel taskTagsModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_TAGS);
				Bindings.bind(templateTaskTags, taskTagsModel);
				
				ValueModel taskFolderModel = new ModelConverter(
						ModelType.FOLDER,
						this.adapter.getValueModel(TaskTemplate.PROP_TASK_FOLDER));
				templateTaskFolder.setModel(new ComboBoxAdapter<Folder>(
						new FolderModel(true),
						taskFolderModel));
				
				ValueModel taskContextModel = new ModelConverter(
						ModelType.CONTEXT,
						this.adapter.getValueModel(TaskTemplate.PROP_TASK_CONTEXT));
				templateTaskContext.setModel(new ComboBoxAdapter<Context>(
						new ContextModel(true),
						taskContextModel));
				
				ValueModel taskGoalModel = new ModelConverter(
						ModelType.GOAL,
						this.adapter.getValueModel(TaskTemplate.PROP_TASK_GOAL));
				templateTaskGoal.setModel(new ComboBoxAdapter<Goal>(
						new GoalModel(true),
						taskGoalModel));
				
				ValueModel taskLocationModel = new ModelConverter(
						ModelType.LOCATION,
						this.adapter.getValueModel(TaskTemplate.PROP_TASK_LOCATION));
				templateTaskLocation.setModel(new ComboBoxAdapter<Location>(
						new LocationModel(true),
						taskLocationModel));
				
				ValueModel taskProgressModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_PROGRESS);
				SpinnerNumberModel taskProgressSpinnerModel = SpinnerAdapterFactory.createNumberAdapter(
						taskProgressModel,
						new Double(0.00),
						new Double(0.00),
						new Double(1.00),
						new Double(0.01));
				
				templateTaskProgress.setModel(taskProgressSpinnerModel);
				templateTaskProgress.setEditor(new JSpinner.NumberEditor(
						templateTaskProgress,
						"##0.00%"));
				
				ValueModel taskCompletedModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_COMPLETED);
				Bindings.bind(templateTaskCompleted, taskCompletedModel);
				
				ValueModel taskDueDateModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_DUE_DATE);
				Bindings.bind(templateTaskDueDate, taskDueDateModel);
				
				TemplateTimeConverter taskDueTimeModel = new TemplateTimeConverter(
						this.adapter.getValueModel(TaskTemplate.PROP_TASK_DUE_TIME));
				SpinnerDateModel taskDueTimeSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
						taskDueTimeModel,
						(Date) taskDueTimeModel.convertFromSubject(0));
				
				templateTaskDueTime.setModel(taskDueTimeSpinnerModel);
				templateTaskDueTime.setEditor(new JSpinner.DateEditor(
						templateTaskDueTime,
						Main.SETTINGS.getStringProperty("date.time_format")));
				
				ValueModel taskStartDateModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_START_DATE);
				Bindings.bind(templateTaskStartDate, taskStartDateModel);
				
				TemplateTimeConverter taskStartTimeModel = new TemplateTimeConverter(
						this.adapter.getValueModel(TaskTemplate.PROP_TASK_START_TIME));
				SpinnerDateModel taskStartTimeSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
						taskStartTimeModel,
						(Date) taskStartTimeModel.convertFromSubject(0));
				
				templateTaskStartTime.setModel(taskStartTimeSpinnerModel);
				templateTaskStartTime.setEditor(new JSpinner.DateEditor(
						templateTaskStartTime,
						Main.SETTINGS.getStringProperty("date.time_format")));
				
				ValueModel taskReminderModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_REMINDER);
				templateTaskReminder.setModel(new ComboBoxAdapter<Integer>(
						new TaskReminderModel(),
						taskReminderModel));
				
				ValueModel taskRepeatModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_REPEAT);
				templateTaskRepeat.setModel(new ComboBoxAdapter<String>(
						SynchronizerUtils.getPlugin().getSynchronizerApi().getDefaultRepeatValues(),
						taskRepeatModel));
				
				ValueModel taskRepeatFromModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_REPEAT_FROM);
				templateTaskRepeatFrom.setModel(new ComboBoxAdapter<TaskRepeatFrom>(
						new TaskRepeatFromModel(true),
						taskRepeatFromModel));
				
				ValueModel taskStatusModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_STATUS);
				templateTaskStatus.setModel(new ComboBoxAdapter<TaskStatus>(
						new TaskStatusModel(true),
						taskStatusModel));
				
				TaskLengthConverter taskLengthModel = new TaskLengthConverter(
						this.adapter.getValueModel(TaskTemplate.PROP_TASK_LENGTH));
				SpinnerDateModel taskLengthSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
						taskLengthModel,
						(Date) taskLengthModel.convertFromSubject(0));
				
				templateTaskLength.setModel(taskLengthSpinnerModel);
				templateTaskLength.setEditor(new JSpinner.DateEditor(
						templateTaskLength,
						Main.SETTINGS.getStringProperty("date.time_format")));
				
				ValueModel taskPriorityModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_PRIORITY);
				templateTaskPriority.setModel(new ComboBoxAdapter<TaskPriority>(
						new TaskPriorityModel(true),
						taskPriorityModel));
				
				ValueModel taskStarModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_STAR);
				Bindings.bind(templateTaskStar, taskStarModel);
				
				ValueModel taskNoteModel = this.adapter.getValueModel(TaskTemplate.PROP_TASK_NOTE);
				Bindings.bind(templateTaskNote, taskNoteModel);
			}
			
			@Override
			public void templateSelected(TaskTemplate template) {
				this.adapter.setBean(template != null ? template : null);
				templateTitle.setEnabled(template != null);
				templateTaskTitle.setEnabled(template != null);
				templateTaskTags.setEnabled(template != null);
				templateTaskFolder.setEnabled(template != null);
				templateTaskContext.setEnabled(template != null);
				templateTaskGoal.setEnabled(template != null);
				templateTaskLocation.setEnabled(template != null);
				templateTaskProgress.setEnabled(template != null);
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
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow");
		
		// Help
		rightPanel.add(
				Help.getHelpButton(Help.getHelpFile("manage_templates.html")),
				BorderLayout.NORTH);
		
		// Template Title
		builder.appendI15d("general.template.title", true, templateTitle);
		
		// Template Separator
		builder.appendSeparator();
		
		// Template Task Title
		builder.appendI15d("general.task.title", true, templateTaskTitle);
		
		// Template Task Tags
		builder.appendI15d("general.task.tags", true, templateTaskTags);
		
		// Template Task Context
		builder.appendI15d("general.task.context", true, templateTaskContext);
		
		// Template Task Folder
		builder.appendI15d("general.task.folder", true, templateTaskFolder);
		
		// Template Task Goal
		builder.appendI15d("general.task.goal", true, templateTaskGoal);
		
		// Template Task Location
		builder.appendI15d("general.task.location", true, templateTaskLocation);
		
		// Template Task Completed
		builder.appendI15d(
				"general.task.completed",
				true,
				templateTaskCompleted);
		
		// Template Task Progress
		builder.appendI15d("general.task.progress", true, templateTaskProgress);
		
		// Template Task Due Date
		JPanel taskDueDatePanel = new JPanel(new BorderLayout(10, 0));
		
		builder.appendI15d("general.task.due_date", true, taskDueDatePanel);
		
		taskDueDatePanel.add(templateTaskDueDate, BorderLayout.CENTER);
		taskDueDatePanel.add(templateTaskDueTime, BorderLayout.EAST);
		
		// Template Task Start Date
		JPanel taskStartDatePanel = new JPanel(new BorderLayout(10, 0));
		
		builder.appendI15d("general.task.start_date", true, taskStartDatePanel);
		
		taskStartDatePanel.add(templateTaskStartDate, BorderLayout.CENTER);
		taskStartDatePanel.add(templateTaskStartTime, BorderLayout.EAST);
		
		// Template Task Reminder
		builder.appendI15d("general.task.reminder", true, templateTaskReminder);
		
		templateTaskReminder.setRenderer(new DefaultListRenderer(
				StringValueTaskReminder.INSTANCE));
		templateTaskReminder.setEditable(true);
		
		// Template Task Repeat
		builder.appendI15d("general.task.repeat", true, templateTaskRepeat);
		
		ComponentFactory.createRepeatComboBox(templateTaskRepeat);
		
		// Template Task Repeat From
		builder.appendI15d(
				"general.task.repeat_from",
				true,
				templateTaskRepeatFrom);
		
		templateTaskRepeatFrom.setRenderer(new DefaultListRenderer(
				StringValueTaskRepeatFrom.INSTANCE));
		
		// Template Task Status
		builder.appendI15d("general.task.status", true, templateTaskStatus);
		
		templateTaskStatus.setRenderer(new DefaultListRenderer(
				StringValueTaskStatus.INSTANCE));
		
		// Template Task Length
		builder.appendI15d("general.task.length", true, templateTaskLength);
		
		// Template Task Priority
		builder.appendI15d("general.task.priority", true, templateTaskPriority);
		
		templateTaskPriority.setRenderer(new DefaultListRenderer(
				StringValueTaskPriority.INSTANCE,
				IconValueTaskPriority.INSTANCE));
		
		// Template Task Star
		builder.appendI15d("general.task.star", true, templateTaskStar);
		
		templateTaskStar.setIcon(Images.getResourceImage(
				"checkbox_star.png",
				18,
				18));
		templateTaskStar.setSelectedIcon(Images.getResourceImage(
				"checkbox_star_selected.png",
				18,
				18));
		
		// Template Task Note
		builder.appendI15d("general.task.note", true, new JScrollPane(
				templateTaskNote));
		
		// Lay out the panel
		rightPanel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.setDividerLocation(200);
	}
	
}
