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
package com.leclercb.taskunifier.gui.components.tasks.edit;

import java.awt.BorderLayout;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.commons.converters.CalendarConverter;
import com.leclercb.taskunifier.gui.commons.converters.LengthConverter;
import com.leclercb.taskunifier.gui.commons.converters.TagsConverter;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.models.TaskModel;
import com.leclercb.taskunifier.gui.commons.models.TaskPriorityModel;
import com.leclercb.taskunifier.gui.commons.models.TaskReminderModel;
import com.leclercb.taskunifier.gui.commons.models.TaskRepeatFromModel;
import com.leclercb.taskunifier.gui.commons.models.TaskStatusModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskReminderListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class TaskEditPanel extends JPanel {
	
	private Task task;
	private BeanAdapter<Task> adapter;
	
	private JTextField taskTitle;
	private JTextField taskTags;
	private JComboBox taskFolder;
	private JComboBox taskContext;
	private JComboBox taskGoal;
	private JComboBox taskLocation;
	private JComboBox taskParent;
	private JCheckBox taskCompleted;
	private JDateChooser taskDueDate;
	private JDateChooser taskStartDate;
	private JComboBox taskReminder;
	private JComboBox taskRepeat;
	private JComboBox taskRepeatFrom;
	private JComboBox taskStatus;
	private JSpinner taskLength;
	private JComboBox taskPriority;
	private JCheckBox taskStar;
	private JTextArea taskNote;
	
	public TaskEditPanel(Task task) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		
		this.task = task;
		this.adapter = new BeanAdapter<Task>(task, true);
		
		this.initialize();
		this.initializeAdapter();
	}
	
	private void initialize() {
		final String dateFormat = Main.SETTINGS.getStringProperty("date.date_format");
		final String timeFormat = Main.SETTINGS.getStringProperty("date.time_format");
		final String format = dateFormat + " " + timeFormat;
		final String mask = DateTimeFormatUtils.getMask(dateFormat)
				+ " "
				+ DateTimeFormatUtils.getMask(timeFormat);
		
		this.setLayout(new BorderLayout(0, 10));
		
		this.taskTitle = new JTextField();
		this.taskTags = new JTextField();
		this.taskFolder = ComponentFactory.createModelComboBox(null);
		this.taskContext = ComponentFactory.createModelComboBox(null);
		this.taskGoal = ComponentFactory.createModelComboBox(null);
		this.taskLocation = ComponentFactory.createModelComboBox(null);
		this.taskParent = ComponentFactory.createModelComboBox(null);
		this.taskCompleted = new JCheckBox();
		this.taskDueDate = new JDateChooser(new JTextFieldDateEditor(
				format,
				null,
				'_') {
			
			@Override
			public String createMaskFromDatePattern(String datePattern) {
				return mask;
			}
			
		});
		this.taskStartDate = new JDateChooser(new JTextFieldDateEditor(
				format,
				null,
				'_') {
			
			@Override
			public String createMaskFromDatePattern(String datePattern) {
				return mask;
			}
			
		});
		this.taskReminder = new JComboBox();
		this.taskRepeat = new JComboBox();
		this.taskRepeatFrom = new JComboBox();
		this.taskStatus = new JComboBox();
		this.taskLength = new JSpinner();
		this.taskPriority = new JComboBox();
		this.taskStar = new JCheckBox();
		this.taskNote = new JTextArea(3, 5);
		
		JPanel info = new JPanel();
		info.setLayout(new SpringLayout());
		this.add(info, BorderLayout.NORTH);
		
		JLabel label = null;
		
		// Task Title
		label = new JLabel(
				Translations.getString("general.task.title") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskTitle);
		
		// Task Tags
		label = new JLabel(
				Translations.getString("general.task.tags") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskTags);
		
		// Task Star
		label = new JLabel(
				Translations.getString("general.task.star") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		this.taskStar.setIcon(Images.getResourceImage(
				"checkbox_star.png",
				18,
				18));
		this.taskStar.setSelectedIcon(Images.getResourceImage(
				"checkbox_star_selected.png",
				18,
				18));
		
		info.add(this.taskStar);
		
		// Task Completed
		label = new JLabel(Translations.getString("general.task.completed")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskCompleted);
		
		// Task Context
		label = new JLabel(
				Translations.getString("general.task.context") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskContext);
		
		// Task Folder
		label = new JLabel(
				Translations.getString("general.task.folder") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskFolder);
		
		// Task Goal
		label = new JLabel(
				Translations.getString("general.task.goal") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskGoal);
		
		// Task Location
		label = new JLabel(Translations.getString("general.task.location")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskLocation);
		
		// Task Parent
		label = new JLabel(
				Translations.getString("general.task.parent") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		if (TaskFactory.getInstance().getChildren(this.task).size() != 0)
			this.taskParent.setEnabled(false);
		
		info.add(this.taskParent);
		
		// Empty
		info.add(new JLabel());
		info.add(new JLabel());
		
		// Task Due Date
		label = new JLabel(Translations.getString("general.task.due_date")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskDueDate);
		
		// Task Start Date
		label = new JLabel(Translations.getString("general.task.start_date")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskStartDate);
		
		// Task Reminder
		label = new JLabel(Translations.getString("general.task.reminder")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		this.taskReminder.setRenderer(new TaskReminderListCellRenderer());
		this.taskReminder.setEditable(true);
		
		info.add(this.taskReminder);
		
		// Task Length
		label = new JLabel(
				Translations.getString("general.task.length") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		// this.taskLength.setModel(new SpinnerDateModel());
		
		info.add(this.taskLength);
		
		// Task Repeat
		label = new JLabel(
				Translations.getString("general.task.repeat") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		ComponentFactory.createRepeatComboBox(this.taskRepeat);
		
		info.add(this.taskRepeat);
		
		// Task Repeat From
		label = new JLabel(Translations.getString("general.task.repeat_from")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		this.taskRepeatFrom.setRenderer(new TaskRepeatFromListCellRenderer());
		info.add(this.taskRepeatFrom);
		
		// Task Status
		label = new JLabel(
				Translations.getString("general.task.status") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		this.taskStatus.setRenderer(new TaskStatusListCellRenderer());
		info.add(this.taskStatus);
		
		// Task Priority
		label = new JLabel(Translations.getString("general.task.priority")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		this.taskPriority.setRenderer(new TaskPriorityListCellRenderer());
		info.add(this.taskPriority);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 9, 4, // rows, cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
		
		// Task Note
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BorderLayout());
		
		label = new JLabel(
				Translations.getString("general.task.note") + ":",
				SwingConstants.LEADING);
		notePanel.add(label, BorderLayout.NORTH);
		
		notePanel.add(new JScrollPane(this.taskNote), BorderLayout.CENTER);
		
		this.add(notePanel, BorderLayout.SOUTH);
	}
	
	private void initializeAdapter() {
		ValueModel taskTitleModel = this.adapter.getValueModel(Model.PROP_TITLE);
		Bindings.bind(this.taskTitle, taskTitleModel);
		
		ValueModel taskTagsModel = new TagsConverter(
				this.adapter.getValueModel(Task.PROP_TAGS));
		Bindings.bind(this.taskTags, taskTagsModel);
		
		ValueModel taskFolderModel = this.adapter.getValueModel(Task.PROP_FOLDER);
		this.taskFolder.setModel(new ComboBoxAdapter<Folder>(new FolderModel(
				true), taskFolderModel));
		
		ValueModel taskContextModel = this.adapter.getValueModel(Task.PROP_CONTEXT);
		this.taskContext.setModel(new ComboBoxAdapter<Context>(
				new ContextModel(true),
				taskContextModel));
		
		ValueModel taskGoalModel = this.adapter.getValueModel(Task.PROP_GOAL);
		this.taskGoal.setModel(new ComboBoxAdapter<Goal>(
				new GoalModel(true),
				taskGoalModel));
		
		ValueModel taskLocationModel = this.adapter.getValueModel(Task.PROP_LOCATION);
		this.taskLocation.setModel(new ComboBoxAdapter<Location>(
				new LocationModel(true),
				taskLocationModel));
		
		ValueModel taskParentModel = this.adapter.getValueModel(Task.PROP_PARENT);
		if (TaskFactory.getInstance().getChildren(this.task).size() == 0)
			this.taskParent.setModel(new ComboBoxAdapter<Task>(new TaskModel(
					true,
					this.task), taskParentModel));
		else
			this.taskParent.setModel(new ComboBoxAdapter<Task>(
					new Task[0],
					taskParentModel));
		
		ValueModel taskCompletedModel = this.adapter.getValueModel(Task.PROP_COMPLETED);
		Bindings.bind(this.taskCompleted, taskCompletedModel);
		
		ValueModel taskDueDateModel = this.adapter.getValueModel(Task.PROP_DUE_DATE);
		Bindings.bind(this.taskDueDate, "date", new CalendarConverter(
				taskDueDateModel));
		
		ValueModel taskStartDateModel = this.adapter.getValueModel(Task.PROP_START_DATE);
		Bindings.bind(this.taskStartDate, "date", new CalendarConverter(
				taskStartDateModel));
		
		ValueModel taskReminderModel = this.adapter.getValueModel(Task.PROP_REMINDER);
		this.taskReminder.setModel(new ComboBoxAdapter<Integer>(
				new TaskReminderModel(),
				taskReminderModel));
		
		ValueModel taskRepeatModel = this.adapter.getValueModel(Task.PROP_REPEAT);
		this.taskRepeat.setModel(new ComboBoxAdapter<String>(
				SynchronizerUtils.getPlugin().getSynchronizerApi().getDefaultRepeatValues(),
				taskRepeatModel));
		
		ValueModel taskRepeatFromModel = this.adapter.getValueModel(Task.PROP_REPEAT_FROM);
		this.taskRepeatFrom.setModel(new ComboBoxAdapter<TaskRepeatFrom>(
				new TaskRepeatFromModel(false),
				taskRepeatFromModel));
		
		ValueModel taskStatusModel = this.adapter.getValueModel(Task.PROP_STATUS);
		this.taskStatus.setModel(new ComboBoxAdapter<TaskStatus>(
				new TaskStatusModel(false),
				taskStatusModel));
		
		LengthConverter taskLengthModel = new LengthConverter(
				this.adapter.getValueModel(Task.PROP_LENGTH));
		SpinnerDateModel model = SpinnerAdapterFactory.createDateAdapter(
				taskLengthModel,
				(Date) taskLengthModel.convertFromSubject(this.task.getLength()));
		
		this.taskLength.setModel(model);
		this.taskLength.setEditor(new JSpinner.DateEditor(
				this.taskLength,
				Main.SETTINGS.getStringProperty("date.time_format")));
		
		ValueModel taskPriorityModel = this.adapter.getValueModel(Task.PROP_PRIORITY);
		this.taskPriority.setModel(new ComboBoxAdapter<TaskPriority>(
				new TaskPriorityModel(false),
				taskPriorityModel));
		
		ValueModel taskStarModel = this.adapter.getValueModel(Task.PROP_STAR);
		Bindings.bind(this.taskStar, taskStarModel);
		
		ValueModel taskNoteModel = this.adapter.getValueModel(Task.PROP_NOTE);
		Bindings.bind(this.taskNote, taskNoteModel);
	}
	
}
