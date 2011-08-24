package com.leclercb.taskunifier.gui.components.calendar;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupported;

public interface TaskCalendarView extends ModelSelectionChangeSupported {
	
	public abstract Task[] getSelectedTasks();
	
	public abstract void refreshTasks();
	
}
