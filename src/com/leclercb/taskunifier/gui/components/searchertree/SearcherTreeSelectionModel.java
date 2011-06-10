package com.leclercb.taskunifier.gui.components.searchertree;

import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherCategory;

public class SearcherTreeSelectionModel extends DefaultTreeSelectionModel {
	
	public SearcherTreeSelectionModel() {
		this.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	private boolean canSelect(TreePath path) {
		return !(path.getLastPathComponent() instanceof SearcherCategory);
	}
	
	@Override
	public void setSelectionPath(TreePath path) {
		if (this.canSelect(path))
			super.setSelectionPath(path);
	}
	
	@Override
	public void setSelectionPaths(TreePath[] paths) {
		if (this.canSelect(paths[0]))
			super.setSelectionPaths(paths);
	}
	
}
