package com.leclercb.taskunifier.gui.components.searcherlist;

import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public interface SearcherView {
	
	public abstract void selectDefaultTaskSearcher();
	
	public abstract TaskSearcher getSelectedTaskSearcher();
	
}
