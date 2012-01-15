package com.leclercb.taskunifier.gui.components.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bizcal.common.Event;
import bizcal.util.DateInterval;

import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionEditTasks;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TasksStartDateCalendar extends TasksCalendar {
	
	private List<Event> events;
	
	public TasksStartDateCalendar() {
		super(
				Translations.getString("calendar.tasks_by_start_date"),
				Translations.getString("calendar.tasks_by_start_date"),
				null);
		this.events = new ArrayList<Event>();
		
		this.setId("tasksstartdatecalendar");
	}
	
	@Override
	public void updateEvents(boolean showCompletedTasks, TaskSearcher searcher) {
		this.events.clear();
		
		List<TaskColumn> columns = new ArrayList<TaskColumn>(
				Arrays.asList(TaskColumn.getVisibleTaskColumns()));
		columns.remove(TaskColumn.MODEL_EDIT);
		columns.remove(TaskColumn.NOTE);
		columns.remove(TaskColumn.SHOW_CHILDREN);
		columns.remove(TaskColumn.ORDER);
		TaskColumn[] c = columns.toArray(new TaskColumn[0]);
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		for (Task task : tasks) {
			if (!task.getModelStatus().isEndUserStatus())
				continue;
			
			if (!showCompletedTasks && task.isCompleted())
				continue;
			
			if (task.getStartDate() == null)
				continue;
			
			if (searcher != null
					&& !TaskUtils.showUnindentTask(task, searcher.getFilter()))
				continue;
			
			Calendar startDate = task.getStartDate();
			
			if (!Main.getSettings().getBooleanProperty("date.use_start_time")) {
				startDate.set(
						Calendar.HOUR_OF_DAY,
						Main.getSettings().getIntegerProperty(
								"date.day_start_hour"));
				startDate.set(Calendar.MINUTE, 0);
				startDate.set(Calendar.SECOND, 0);
				startDate.set(Calendar.MILLISECOND, 0);
			}
			
			int length = task.getLength();
			
			if (length < 30)
				length = 30;
			
			Calendar dueDate = DateUtils.cloneCalendar(startDate);
			dueDate.add(Calendar.MINUTE, length);
			
			Event event = new Event();
			event.setId(task.getModelId());
			event.set(CALENDAR_ID, this.getId());
			event.setEditable(true);
			event.setSelectable(true);
			event.setDescription(task.getTitle());
			event.setToolTip("<html><i>"
					+ Translations.getString("calendar.task_by_start_date")
					+ "</i><br />"
					+ TaskUtils.toText(new Task[] { task }, c, true)
					+ "</html>");
			event.setStart(startDate.getTime());
			event.setEnd(dueDate.getTime());
			event.setColor(Main.getSettings().getColorProperty(
					"theme.color.importance." + TaskUtils.getImportance(task)));
			
			if (task.isCompleted())
				event.setIcon(ImageUtils.getResourceImage(
						"checkbox_selected.png",
						16,
						16));
			else
				event.setIcon(ImageUtils.getResourceImage(
						"warning_blue.png",
						16,
						16));
			
			this.events.add(event);
		}
	}
	
	@Override
	public List<Event> getEvents(Date from, Date to) {
		return this.events;
	}
	
	@Override
	public List<Event> addEvent(String clientId, Event event) {
		return null;
	}
	
	@Override
	public void deleteEvent(String clientId, Event event) {
		
	}
	
	@Override
	public List<Event> saveEvent(
			String clientId,
			Event event,
			boolean userInteraction) {
		return null;
	}
	
	@Override
	public void newEvent(DateInterval interval) throws Exception {
		Task task = ActionAddTask.addTask((String) null, false);
		
		long diff = interval.getDuration();
		diff = diff / (60 * 1000);
		
		int length = (int) diff;
		
		if (length < 30)
			length = 30;
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(interval.getStartDate());
		
		task.setLength(length);
		task.setStartDate(startDate);
		
		if (!ActionEditTasks.editTasks(new Task[] { task }))
			TaskFactory.getInstance().markDeleted(task);
	}
	
	@Override
	public void moved(Event event, Date orgDate, Date newDate) throws Exception {
		Task task = TasksCalendar.getTask(event);
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(newDate);
		
		task.setStartDate(startDate);
	}
	
	@Override
	public void resized(Event event, Date orgEndDate, Date newEndDate)
			throws Exception {
		Task task = TasksCalendar.getTask(event);
		
		long diff = orgEndDate.getTime() - newEndDate.getTime();
		diff = diff / (60 * 1000);
		
		int length = task.getLength() - (int) diff;
		
		if (length < 30)
			length = 30;
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTime(newEndDate);
		
		task.setLength(length);
	}
	
}
