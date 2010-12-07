package com.leclercb.taskunifier.gui.components.searcheredit;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.searcher.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterPanel;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherEditPanel extends JPanel {

	public SearcherEditPanel(TaskSearcher searcher) {
		this.initialize(searcher);
	}

	private void initialize(TaskSearcher searcher) {
		this.setLayout(new BorderLayout());

		JPanel panel = null;

		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		TaskSearcherPanel searcherPanel = new TaskSearcherPanel(searcher);
		panel.add(searcherPanel, BorderLayout.CENTER);
		this.add(panel, BorderLayout.NORTH);

		JSplitPane splitPane = new JSplitPane();
		this.add(splitPane, BorderLayout.CENTER);

		TaskSorterPanel sorterPanel = new TaskSorterPanel(searcher.getSorter());
		splitPane.setLeftComponent(sorterPanel);

		TaskFilterPanel filterPanel = new TaskFilterPanel(searcher.getFilter());
		splitPane.setRightComponent(filterPanel);

		splitPane.setDividerLocation(300);
	}

}
