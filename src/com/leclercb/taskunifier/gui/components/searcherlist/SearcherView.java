package com.leclercb.taskunifier.gui.components.searcherlist;

import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionChangeSupported;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public interface SearcherView extends TaskSearcherSelectionChangeSupported {
	
	public abstract void selectDefaultTaskSearcher();
	
	public abstract TaskSearcher getSelectedTaskSearcher();
	
	public abstract void refreshTaskSearcher();
	
}
