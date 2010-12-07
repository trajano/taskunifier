package com.leclercb.taskunifier.gui.components.searcheredit;

import javax.swing.JSplitPane;

import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterPanel;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherEditPanel extends JSplitPane {

	public SearcherEditPanel(TaskSearcher searcher) {
		this.initialize(searcher);
	}

	private void initialize(TaskSearcher searcher) {
		TaskSorterPanel sorterPanel = new TaskSorterPanel(searcher.getSorter());
		this.setLeftComponent(sorterPanel);

		this.setDividerLocation(200);
	}

}
