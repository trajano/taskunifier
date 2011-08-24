package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.Color;
import java.util.Date;

import lu.tudor.santec.bizcal.NamedCalendar;
import bizcal.common.Event;
import bizcal.util.DateInterval;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;

public abstract class TasksCalendar extends NamedCalendar {
	
	public TasksCalendar(String name, String description, Color color) {
		super(name, description, color);
	}
	
	public TasksCalendar(String name, String description) {
		super(name, description);
	}
	
	public TasksCalendar(String name) {
		super(name);
	}
	
	public abstract void updateEvents(boolean showCompletedTasks);
	
	public abstract void newEvent(DateInterval interval) throws Exception;
	
	public abstract void moved(Event event, Date orgDate, Date newDate)
			throws Exception;
	
	public abstract void resized(Event event, Date orgEndDate, Date newEndDate)
			throws Exception;
	
	public static Task getTask(Event event) {
		return TaskFactory.getInstance().get((ModelId) event.getId());
	}
	
}
