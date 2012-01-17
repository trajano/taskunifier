package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.gui.components.modelnote.ModelNoteView;
import com.leclercb.taskunifier.gui.components.tasks.TaskTableView;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;

public interface TaskView extends View {
	
	public static enum InfoTab {
		
		NOTE,
		CONTACTS;
		
	}
	
	public abstract TaskSearcherView getTaskSearcherView();
	
	public abstract TaskTableView getTaskTableView();
	
	public abstract ModelNoteView getModelNoteView();
	
	public abstract void setSelectedInfoTab(InfoTab tab);
	
}
