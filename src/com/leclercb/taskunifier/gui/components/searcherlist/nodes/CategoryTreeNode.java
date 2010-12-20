package com.leclercb.taskunifier.gui.components.searcherlist.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class CategoryTreeNode extends DefaultMutableTreeNode implements TaskSearcherTreeNode {
	
	private String expandedPropetyName;
	
	public CategoryTreeNode(String expandedPropetyName, String title) {
		super(title);
		
		this.expandedPropetyName = expandedPropetyName;
	}
	
	public String getExpandedPropetyName() {
		return this.expandedPropetyName;
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		return null;
	}
	
}
