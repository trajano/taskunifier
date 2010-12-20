package com.leclercb.taskunifier.gui.components.searcherlist.nodes;

import javax.swing.tree.MutableTreeNode;

import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public interface TaskSearcherTreeNode extends MutableTreeNode {

	public abstract TaskSearcher getTaskSearcher();

}
