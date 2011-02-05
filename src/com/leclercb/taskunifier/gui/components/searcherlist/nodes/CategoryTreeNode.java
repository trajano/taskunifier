package com.leclercb.taskunifier.gui.components.searcherlist.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.components.searcherlist.TaskSearcherElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class CategoryTreeNode extends DefaultMutableTreeNode implements TaskSearcherElement {
	
	private ModelType modelType;
	private String expandedPropetyName;
	
	public CategoryTreeNode(
			ModelType modelType,
			String expandedPropetyName,
			String title) {
		super(title);
		
		CheckUtils.isNotNull(title, "Title cannot be null");
		
		this.modelType = modelType;
		this.expandedPropetyName = expandedPropetyName;
	}
	
	public ModelType getModelType() {
		return this.modelType;
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
	
	@Override
	public String toString() {
		return this.getUserObject().toString();
	}
	
}
