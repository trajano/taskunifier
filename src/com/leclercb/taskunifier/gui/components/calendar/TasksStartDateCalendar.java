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

public class TasksStartDateCalendar extends TasksCalendar {
	
	private List<Event> events;
	
	public TasksStartDateCalendar() {
		super("Tasks: Start date", "Tasks: Start date", Color.GREEN);
		this.events = new ArrayList<Event>();
		
		this.setId("TasksStartDateCalendar");
		
		this.updateEvents();
	}
	
	@Override
	public void updateEvents() {
		this.events.clear();
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		for (Task task : tasks) {
			if (!task.getModelStatus().isEndUserStatus())
				continue;
			
			if (task.getStartDate() == null)
				continue;
			
			int length = task.getLength();
			
			Calendar end = task.getStartDate();
			end.add(Calendar.MINUTE, length);
			
			Event event = new Event();
			event.setId(task.getModelId());
			event.setEditable(true);
			event.setSelectable(true);
			event.setDescription(task.getTitle());
			event.setToolTip(task.getTitle());
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
		
		task.setDueDate(startDate);
	}
	
	@Override
	public void resized(Event event, Date orgEndDate, Date newEndDate)
			throws Exception {
		Task task = this.getTask(event);
		
		long diff = orgEndDate.getTime() - newEndDate.getTime();
		diff = diff / (60 * 1000);
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTime(newEndDate);
		
		task.setLength(task.getLength() + (int) diff);
	}
	
	public Task getTask(Event event) {
		return TaskFactory.getInstance().get((ModelId) event.getId());
	}
	
}
