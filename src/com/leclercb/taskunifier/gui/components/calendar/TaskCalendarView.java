package com.leclercb.taskunifier.gui.components.calendar;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupported;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;

public interface TaskCalendarView extends ModelSelectionChangeSupported {
	
	public abstract TaskSearcherView getTaskSearcherView();
	
	public abstract Task[] getSelectedTasks();
	
	public abstract void refreshTasks();
	
}
