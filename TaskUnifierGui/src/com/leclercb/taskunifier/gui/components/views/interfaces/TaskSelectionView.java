package com.leclercb.taskunifier.gui.components.views.interfaces;

import com.leclercb.taskunifier.api.models.Task;

public interface TaskSelectionView {
	
	public abstract Task[] getSelectedTasks();
	
	public abstract void setSelectedTasks(Task[] tasks);
	
}
