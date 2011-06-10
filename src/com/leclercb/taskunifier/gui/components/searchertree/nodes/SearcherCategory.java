package com.leclercb.taskunifier.gui.components.searchertree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;

public class SearcherCategory extends DefaultMutableTreeNode {
	
	private String expandedPropetyName;
	
	public SearcherCategory(String title, String expandedPropetyName) {
		super(title);
		
		CheckUtils.isNotNull(title, "Title cannot be null");
		
		this.expandedPropetyName = expandedPropetyName;
	}
	
	public String getExpandedPropetyName() {
		return this.expandedPropetyName;
	}
	
}
