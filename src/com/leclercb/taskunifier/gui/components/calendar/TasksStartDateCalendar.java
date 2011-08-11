package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bizcal.common.Event;
import bizcal.util.DateInterval;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TasksStartDateCalendar extends TasksCalendar {
	
	private List<Event> events;
	
	public TasksStartDateCalendar() {
		super(
				Translations.getString("calendar.tasks_by_start_date"),
				Translations.getString("calendar.tasks_by_start_date"),
				Color.GREEN);
		this.events = new ArrayList<Event>();
		
		this.setId("TasksStartDateCalendar");
	}
	
	@Override
	public void updateEvents(boolean showCompletedTasks) {
		this.events.clear();
		
		List<TaskColumn> columns = new ArrayList<TaskColumn>(
				Arrays.asList(TaskColumn.values()));
		columns.remove(TaskColumn.MODEL);
		columns.remove(TaskColumn.NOTE);
		columns.remove(TaskColumn.SHOW_CHILDREN);
		TaskColumn[] c = columns.toArray(new TaskColumn[0]);
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		for (Task task : tasks) {
			if (!task.getModelStatus().isEndUserStatus())
				continue;
			
			if (!showCompletedTasks && task.isCompleted())
				continue;
			
			if (task.getStartDate() == null)
				continue;
			
			int length = task.getLength();
			
			if (length < 15)
				length = 15;
			
			Calendar end = task.getStartDate();
			end.add(Calendar.MINUTE, length);
			
			String title = task.getTitle();
			
			if (task.isCompleted())
				title = Translations.getString("general.task.completed")
						+ ": "
						+ title;
			
			Event event = new Event();
			event.setId(task.getModelId());
			event.set(CALENDAR_ID, this.getId());
			event.setEditable(true);
			event.setSelectable(true);
			event.setDescription(title);
			event.setToolTip("<html><i>"
					+ Translations.getString("calendar.task_by_start_date")
					+ "</i><br />"
					+ TaskUtils.toText(new Task[] { task }, c, true)
					+ "</html>");
			event.setStart(task.getStartDate().getTime());
			event.setEnd(end.getTime());
			event.setColor(Main.SETTINGS.getColorProperty("theme.color.importance."
					+ TaskUtils.getImportance(task)));
			
			if (task.isOverDue(false))
				event.setIcon(Images.getResourceImage("warning.gif"));
			
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
		Task task = ActionAddTask.addTask(null, false, false);
		
		long diff = interval.getDuration();
		diff = diff / (60 * 1000);
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(interval.getStartDate());
		
		task.setLength((int) diff);
		task.setStartDate(startDate);
	}
	
	@Override
	public void moved(Event event, Date orgDate, Date newDate) throws Exception {
		Task task = this.getTask(event);
		
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(newDate);
		
		task.setStartDate(startDate);
	}
	
	@Override
	public void resized(Event event, Date orgEndDate, Date newEndDate)
			throws Exception {
		Task task = this.getTask(event);
		
		long diff = orgEndDate.getTime() - newEndDate.getTime();
		diff = diff / (60 * 1000);
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTime(newEndDate);
		
		task.setLength(task.getLength() - (int) diff);
	}
	
	public Task getTask(Event event) {
		return TaskFactory.getInstance().get((ModelId) event.getId());
	}
	
}