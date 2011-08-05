package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lu.tudor.santec.bizcal.NamedCalendar;
import bizcal.common.Event;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TasksCalendar extends NamedCalendar {
	
	private List<Event> events;
	
	public TasksCalendar() {
		super("All Tasks", "All Tasks", Color.RED);
		this.events = new ArrayList<Event>();
		this.setId(this.hashCode());
		
		this.updateEvents();
	}
	
	public void updateEvents() {
		this.events.clear();
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		for (Task task : tasks) {
			if (!task.getModelStatus().isEndUserStatus())
				continue;
			
			if (task.getDueDate() == null)
				continue;
			
			Calendar start = task.getDueDate();
			start.add(Calendar.MINUTE, -task.getLength());
			
			Event event = new Event();
			event.setEditable(false);
			event.setSelectable(true);
			event.setDescription(task.getTitle());
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
	public List<Event> addEvent(String arg0, Event arg1) {
		return null;
	}
	
	@Override
	public void deleteEvent(String arg0, Event arg1) {

	}
	
	@Override
	public List<Event> saveEvent(String arg0, Event arg1, boolean arg2) {
		return null;
	}
	
}
