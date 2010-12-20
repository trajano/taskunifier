package com.leclercb.taskunifier.gui.components.searcherlist.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.utils.StringUtils;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherTreeNode extends DefaultMutableTreeNode implements TaskSearcherTreeNode {
	
	public SearcherTreeNode(TaskSearcher searcher) {
		super(searcher);
		
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		return (TaskSearcher) this.getUserObject();
	}
	
	@Override
	public String toString() {
		return StringUtils.fillWith(this.getUserObject().toString(), 40);
	}
	
}
