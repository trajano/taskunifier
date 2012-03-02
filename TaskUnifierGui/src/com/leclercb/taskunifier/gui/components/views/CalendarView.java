package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.gui.components.calendar.TaskCalendarView;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;

public interface CalendarView extends View {
	
	public abstract TaskSearcherView getTaskSearcherView();
	
	public abstract TaskCalendarView getTaskCalendarView();
	
}
