package com.leclercb.taskunifier.gui.components.tasks.edit;

import java.awt.BorderLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.utils.FormatterUtils;
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
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.models.TaskModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;
import com.leclercb.taskunifier.gui.utils.Images;
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
	private JFormattedTextField taskReminder;
	private JTextField taskRepeat;
	private JComboBox taskRepeatFrom;
	private JComboBox taskStatus;
	private JFormattedTextField taskLength;
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
		
		this.setLayout(new BorderLayout());
		
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
		this.taskReminder = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		this.taskRepeat = new JTextField();
		this.taskRepeatFrom = new JComboBox();
		this.taskStatus = new JComboBox();
		this.taskLength = new JFormattedTextField(
				FormatterUtils.getIntegerFormatter());
		this.taskPriority = new JComboBox();
		this.taskStar = new JCheckBox();
		this.taskNote = new JTextArea(3, 5);
		
		JPanel info = new JPanel();
		info.setLayout(new SpringLayout());
		this.add(info, BorderLayout.CENTER);
		
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
		
		info.add(this.taskReminder);
		
		// Task Length
		label = new JLabel(
				Translations.getString("general.task.length") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		info.add(this.taskLength);
		
		// Task Repeat
		label = new JLabel(
				Translations.getString("general.task.repeat") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
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
		
		// Empty
		info.add(new JLabel());
		info.add(new JLabel());
		
		// Task Note
		label = new JLabel(
				Translations.getString("general.task.note") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		info.add(new JScrollPane(this.taskNote));
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 10, 4, // rows, cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
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
		Bindings.bind(this.taskReminder, taskReminderModel);
		
		ValueModel taskRepeatModel = this.adapter.getValueModel(Task.PROP_REPEAT);
		Bindings.bind(this.taskRepeat, taskRepeatModel);
		
		ValueModel taskRepeatFromModel = this.adapter.getValueModel(Task.PROP_REPEAT_FROM);
		this.taskRepeatFrom.setModel(new ComboBoxAdapter<TaskRepeatFrom>(
				TaskRepeatFrom.values(),
				taskRepeatFromModel));
		
		ValueModel taskStatusModel = this.adapter.getValueModel(Task.PROP_STATUS);
		this.taskStatus.setModel(new ComboBoxAdapter<TaskStatus>(
				TaskStatus.values(),
				taskStatusModel));
		
		ValueModel taskLengthModel = this.adapter.getValueModel(Task.PROP_LENGTH);
		Bindings.bind(this.taskLength, taskLengthModel);
		
		ValueModel taskPriorityModel = this.adapter.getValueModel(Task.PROP_PRIORITY);
		this.taskPriority.setModel(new ComboBoxAdapter<TaskPriority>(
				TaskPriority.values(),
				taskPriorityModel));
		
		ValueModel taskStarModel = this.adapter.getValueModel(Task.PROP_STAR);
		Bindings.bind(this.taskStar, taskStarModel);
		
		ValueModel taskNoteModel = this.adapter.getValueModel(Task.PROP_NOTE);
		Bindings.bind(this.taskNote, taskNoteModel);
	}
	
	public static class TagsConverter extends AbstractConverter {
		
		public TagsConverter(ValueModel subject) {
			super(subject);
		}
		
		@Override
		public void setValue(Object tags) {
			this.subject.setValue(((String) tags).split(","));
		}
		
		@Override
		public Object convertFromSubject(Object tags) {
			return ArrayUtils.arrayToString((String[]) tags, ", ");
		}
		
	}
	
	public static class CalendarConverter extends AbstractConverter {
		
		public CalendarConverter(ValueModel subject) {
			super(subject);
		}
		
		@Override
		public void setValue(Object date) {
			if (date == null) {
				this.subject.setValue(null);
				return;
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(((Date) date).getTime());
			this.subject.setValue(calendar);
		}
		
		@Override
		public Object convertFromSubject(Object calendar) {
			if (calendar == null)
				return null;
			
			return ((Calendar) calendar).getTime();
		}
		
	}
	
}
