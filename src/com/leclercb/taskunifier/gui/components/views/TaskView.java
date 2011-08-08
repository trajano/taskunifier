package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.gui.components.tasks.TaskTableView;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;

public interface TaskView extends View {
	
	public abstract TaskSearcherView getTaskSearcherView();
	
	public abstract TaskTableView getTaskTableView();
	
}
