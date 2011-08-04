package com.leclercb.taskunifier.gui.components.views.statistics;

import com.leclercb.taskunifier.gui.components.tasks.TaskTableView;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.components.views.View;

public interface TaskView extends View {
	
	public abstract TaskSearcherView getTaskSearcherView();
	
	public abstract TaskTableView getTaskTableView();
	
}
