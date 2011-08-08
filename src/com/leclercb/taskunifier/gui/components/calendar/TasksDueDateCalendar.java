package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bizcal.common.Event;
import bizcal.util.DateInterval;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TasksDueDateCalendar extends TasksCalendar {
	
	private List<Event> events;
	
	public TasksDueDateCalendar() {
		super("Tasks: Due date", "Tasks: Due date", Color.RED);
		this.events = new ArrayList<Event>();
		
		this.setId("TasksDueDateCalendar");
		
		this.updateEvents();
	}
	
	@Override
	public void updateEvents() {
		this.events.clear();
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		for (Task task : tasks) {
			if (!task.getModelStatus().isEndUserStatus())
				continue;
			
			if (task.getDueDate() == null)
				continue;
			
			int length = task.getLength();
			
			Calendar start = task.getDueDate();
			start.add(Calendar.MINUTE, -length);
			
			Event event = new Event();
			event.setId(task.getModelId());
			event.setEditable(true);
			event.setSelectable(true);
			event.setDescription(task.getTitle());
			event.setToolTip(task.getTitle());
			event.setStart(start.getTime());
			event.setEnd(task.getDueDate().getTime());
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
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTime(interval.getEndDate());
		
		task.setLength((int) diff);
		task.setDueDate(dueDate);
	}
	
	@Override
	public void moved(Event event, Date orgDate, Date newDate) throws Exception {
		Task task = this.getTask(event);
		
		int length = task.getLength();
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTime(newDate);
		dueDate.add(Calendar.MINUTE, length);
		
		task.setDueDate(dueDate);
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
		task.setDueDate(dueDate);
	}
	
	public Task getTask(Event event) {
		return TaskFactory.getInstance().get((ModelId) event.getId());
	}
	
}
