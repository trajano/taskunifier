package com.leclercb.taskunifier.gui.components.searcherlist;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionChangeSupported;

public interface SearcherView extends TaskSearcherSelectionChangeSupported {
	
	public abstract void setTitleFilter(String title);
	
	public abstract void selectDefaultTaskSearcher();
	
	public abstract TaskSearcher getSelectedTaskSearcher();
	
	public abstract void refreshTaskSearcher();
	
}
