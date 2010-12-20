package com.leclercb.taskunifier.gui.components.searcherlist.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherTreeNode extends DefaultMutableTreeNode implements TaskSearcherTreeNode {

	public SearcherTreeNode(TaskSearcher searcher) {
		super(searcher);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public TaskSearcher getTaskSearcher() {
		return (TaskSearcher) this.getUserObject();
	}

}
