package com.leclercb.taskunifier.gui.components.calendar;

import com.leclercb.taskunifier.api.models.Task;

public interface TaskCalendarView {
	
	public abstract Task[] getSelectedTasks();
	
	public abstract void refreshTasks();
	
}
