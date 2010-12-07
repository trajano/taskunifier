package com.leclercb.taskunifier.gui.components.searcheredit;

import javax.swing.JSplitPane;

import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterTable;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherEditPanel extends JSplitPane {

	TaskSorterTable sorterTable;

	public SearcherEditPanel(TaskSearcher searcher) {
		this.initialize(searcher);
	}

	private void initialize(TaskSearcher searcher) {
		this.sorterTable = new TaskSorterTable(searcher.getSorter());
		this.setLeftComponent(this.sorterTable);
		
		
		
		this.setDividerLocation(200);
	}

}
