package com.leclercb.taskunifier.gui.components.searcheredit;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterElementPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterElementTreeNode;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.searcher.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterPanel;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherEditPanel extends JPanel implements TreeSelectionListener {

	private TaskSearcherPanel searcherPanel;
	private TaskFilterElementPanel elementPanel;
	private TaskSorterPanel sorterPanel;
	private TaskFilterPanel filterPanel;

	public SearcherEditPanel(TaskSearcher searcher) {
		this.initialize(searcher);
	}

	private void initialize(TaskSearcher searcher) {
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.CENTER);

		searcherPanel = new TaskSearcherPanel(searcher);
		panel.add(searcherPanel, BorderLayout.NORTH);

		elementPanel = new TaskFilterElementPanel();
		panel.add(elementPanel, BorderLayout.SOUTH);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		panel.add(splitPane, BorderLayout.CENTER);

		sorterPanel = new TaskSorterPanel(searcher.getSorter());
		splitPane.setLeftComponent(sorterPanel);

		filterPanel = new TaskFilterPanel(searcher.getFilter());
		filterPanel.getTree().addTreeSelectionListener(this);
		splitPane.setRightComponent(filterPanel);

		splitPane.setDividerLocation(300);
	}

	@Override
	public void valueChanged(TreeSelectionEvent evt) {
		if (filterPanel.getTree().getSelectionCount() != 0) {
			TreeNode node = (TreeNode) filterPanel.getTree().getLastSelectedPathComponent();

			if (node instanceof TaskFilterElementTreeNode) {
				elementPanel.setElement(((TaskFilterElementTreeNode) node).getElement());
				return;
			}
		}

		elementPanel.setElement(null);
	}

}
