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
package com.leclercb.taskunifier.gui.components.taskedit;

import java.awt.BorderLayout;
import java.util.Calendar;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelNote;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.commons.converters.CalendarConverter;
import com.leclercb.taskunifier.gui.commons.converters.TaskLengthConverter;
import com.leclercb.taskunifier.gui.commons.converters.TaskTagsConverter;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.models.TaskModel;
import com.leclercb.taskunifier.gui.commons.models.TaskPriorityModel;
import com.leclercb.taskunifier.gui.commons.models.TaskReminderModel;
import com.leclercb.taskunifier.gui.commons.models.TaskRepeatFromModel;
import com.leclercb.taskunifier.gui.commons.models.TaskStatusModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskReminder;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ComponentUtils;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

@Reviewed
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
	private JSpinner taskProgress;
	private JCheckBox taskCompleted;
	private JDateChooser taskStartDate;
	private JDateChooser taskDueDate;
	private JComboBox taskReminder;
	private JComboBox taskRepeat;
	private JComboBox taskRepeatFrom;
	private JComboBox taskStatus;
	private JSpinner taskLength;
	private JComboBox taskPriority;
	private JCheckBox taskStar;
	private JTextArea taskNote;
	
	private TaskModel taskParentModel;
	
	public TaskEditPanel() {
		this.task = null;
		this.adapter = new BeanAdapter<Task>(this.task, true);
		
		this.initialize();
		this.initializeAdapter();
	}
	
	public Task getTask() {
		return this.task;
	}
	
	public void setTask(Task task) {
		this.task = task;
		this.adapter.setBean(this.task);
		
		this.taskParentModel.setHiddenTask(this.task);
		
		if (this.task == null || this.task.getChildren().length != 0)
			this.taskParent.setEnabled(false);
		else
			this.taskParent.setEnabled(true);
		
		ComponentUtils.focusAndSelectTextInTextField(this.taskTitle);
	}
	
	private void initialize() {
		String dateFormat = Main.SETTINGS.getStringProperty("date.date_format");
		String timeFormat = Main.SETTINGS.getStringProperty("date.time_format");
		
		String dueDateFormat = null;
		String dueDateMask = null;
		
		String startDateFormat = null;
		String startDateMask = null;
		
		if (Main.SETTINGS.getBooleanProperty("date.use_due_time")) {
			dueDateFormat = dateFormat + " " + timeFormat;
			dueDateMask = DateTimeFormatUtils.getMask(dateFormat)
					+ " "
					+ DateTimeFormatUtils.getMask(timeFormat);
		} else {
			dueDateFormat = dateFormat;
			dueDateMask = DateTimeFormatUtils.getMask(dateFormat);
		}
		
		if (Main.SETTINGS.getBooleanProperty("date.use_start_time")) {
			startDateFormat = dateFormat + " " + timeFormat;
			startDateMask = DateTimeFormatUtils.getMask(dateFormat)
					+ " "
					+ DateTimeFormatUtils.getMask(timeFormat);
		} else {
			startDateFormat = dateFormat;
			startDateMask = DateTimeFormatUtils.getMask(dateFormat);
		}
		
		final String finalDueDateMask = dueDateMask;
		final String finalStartDateMask = startDateMask;
		
		this.setLayout(new BorderLayout());
		
		this.taskTitle = new JTextField();
		this.taskTags = new JTextField();
		this.taskFolder = ComponentFactory.createModelComboBox(null);
		this.taskContext = ComponentFactory.createModelComboBox(null);
		this.taskGoal = ComponentFactory.createModelComboBox(null);
		this.taskLocation = ComponentFactory.createModelComboBox(null);
		this.taskParent = ComponentFactory.createModelComboBox(null);
		this.taskProgress = new JSpinner();
		this.taskCompleted = new JCheckBox();
		this.taskStartDate = new JDateChooser(new JTextFieldDateEditor(
				startDateFormat,
				null,
				'_') {
			
			@Override
			public String createMaskFromDatePattern(String datePattern) {
				return finalStartDateMask;
			}
			
		});
		this.taskDueDate = new JDateChooser(new JTextFieldDateEditor(
				dueDateFormat,
				null,
				'_') {
			
			@Override
			public String createMaskFromDatePattern(String datePattern) {
				return finalDueDateMask;
			}
			
		});
		this.taskReminder = new JComboBox();
		this.taskRepeat = new JComboBox();
		this.taskRepeatFrom = new JComboBox();
		this.taskStatus = new JComboBox();
		this.taskLength = new JSpinner();
		this.taskPriority = new JComboBox();
		this.taskStar = new JCheckBox();
		this.taskNote = new JTextArea(5, 0);
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, 120dlu:grow, 10dlu, right:pref, 4dlu, 120dlu:grow");
		
		// Task Title
		builder.appendI15d("general.task.title", true, this.taskTitle);
		
		// Task Tags
		builder.appendI15d("general.task.tags", true, this.taskTags);
		
		// Task Star
		builder.appendI15d("general.task.star", true, this.taskStar);
		
		this.taskStar.setIcon(Images.getResourceImage(
				"checkbox_star.png",
				18,
				18));
		this.taskStar.setSelectedIcon(Images.getResourceImage(
				"checkbox_star_selected.png",
				18,
				18));
		
		// Task Completed
		builder.appendI15d("general.task.completed", true, this.taskCompleted);
		
		// Task Progress
		builder.appendI15d("general.task.progress", true, this.taskProgress);
		
		// Empty
		builder.append("", new JLabel());
		
		// Task Context
		builder.appendI15d("general.task.context", true, this.taskContext);
		
		// Task Folder
		builder.appendI15d("general.task.folder", true, this.taskFolder);
		
		// Task Goal
		builder.appendI15d("general.task.goal", true, this.taskGoal);
		
		// Task Location
		builder.appendI15d("general.task.location", true, this.taskLocation);
		
		// Task Parent
		builder.appendI15d("general.task.parent", true, this.taskParent);
		
		// Empty
		builder.append("", new JLabel());
		
		// Task Start Date
		builder.appendI15d("general.task.start_date", true, this.taskStartDate);
		
		// Task Due Date
		builder.appendI15d("general.task.due_date", true, this.taskDueDate);
		
		// Task Reminder
		builder.appendI15d("general.task.reminder", true, this.taskReminder);
		
		this.taskReminder.setRenderer(new DefaultListRenderer(
				new StringValueTaskReminder()));
		this.taskReminder.setEditable(true);
		
		// Task Length
		builder.appendI15d("general.task.length", true, this.taskLength);
		
		// Task Repeat
		builder.appendI15d("general.task.repeat", true, this.taskRepeat);
		
		ComponentFactory.createRepeatComboBox(this.taskRepeat);
		
		// Task Repeat From
		builder.appendI15d(
				"general.task.repeat_from",
				true,
				this.taskRepeatFrom);
		
		this.taskRepeatFrom.setRenderer(new DefaultListRenderer(
				new StringValueTaskRepeatFrom()));
		
		// Task Status
		builder.appendI15d("general.task.status", true, this.taskStatus);
		
		this.taskStatus.setRenderer(new DefaultListRenderer(
				new StringValueTaskStatus()));
		
		// Task Priority
		builder.appendI15d("general.task.priority", true, this.taskPriority);
		
		this.taskPriority.setRenderer(new DefaultListRenderer(
				new StringValueTaskPriority(),
				new IconValueTaskPriority()));
		
		// Task Note
		builder.appendI15d("general.task.note", true);
		builder.getBuilder().add(
				new JScrollPane(this.taskNote),
				new CellConstraints(3, builder.getBuilder().getRowCount(), 5, 1));
		
		// Lay out the panel
		this.add(builder.getPanel(), BorderLayout.CENTER);
	}
	
	private void initializeAdapter() {
		ValueModel taskTitleModel = this.adapter.getValueModel(Model.PROP_TITLE);
		Bindings.bind(this.taskTitle, taskTitleModel);
		
		ValueModel taskTagsModel = new TaskTagsConverter(
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
		this.taskParentModel = new TaskModel(true);
		this.taskParent.setModel(new ComboBoxAdapter<Task>(
				this.taskParentModel,
				taskParentModel));
		
		ValueModel taskProgressModel = this.adapter.getValueModel(Task.PROP_PROGRESS);
		SpinnerNumberModel taskProgressSpinnerModel = SpinnerAdapterFactory.createNumberAdapter(
				taskProgressModel,
				new Double(0.00),
				new Double(0.00),
				new Double(1.00),
				new Double(0.01));
		
		this.taskProgress.setModel(taskProgressSpinnerModel);
		this.taskProgress.setEditor(new JSpinner.NumberEditor(
				this.taskProgress,
				"##0.00%"));
		
		ValueModel taskCompletedModel = this.adapter.getValueModel(Task.PROP_COMPLETED);
		Bindings.bind(this.taskCompleted, taskCompletedModel);
		
		ValueModel taskStartDateModel = this.adapter.getValueModel(Task.PROP_START_DATE);
		Bindings.bind(this.taskStartDate, "date", new CalendarConverter(
				taskStartDateModel));
		
		ValueModel taskDueDateModel = this.adapter.getValueModel(Task.PROP_DUE_DATE);
		Bindings.bind(this.taskDueDate, "date", new CalendarConverter(
				taskDueDateModel));
		
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
		
		TaskLengthConverter taskLengthModel = new TaskLengthConverter(
				this.adapter.getValueModel(Task.PROP_LENGTH));
		SpinnerDateModel taskLengthSpinnerModel = SpinnerAdapterFactory.createDateAdapter(
				taskLengthModel,
				Calendar.getInstance().getTime());
		
		this.taskLength.setModel(taskLengthSpinnerModel);
		this.taskLength.setEditor(new JSpinner.DateEditor(
				this.taskLength,
				Main.SETTINGS.getStringProperty("date.time_format")));
		
		ValueModel taskPriorityModel = this.adapter.getValueModel(Task.PROP_PRIORITY);
		this.taskPriority.setModel(new ComboBoxAdapter<TaskPriority>(
				new TaskPriorityModel(false),
				taskPriorityModel));
		
		ValueModel taskStarModel = this.adapter.getValueModel(Task.PROP_STAR);
		Bindings.bind(this.taskStar, taskStarModel);
		
		ValueModel taskNoteModel = this.adapter.getValueModel(ModelNote.PROP_NOTE);
		Bindings.bind(this.taskNote, taskNoteModel);
	}
	
}
