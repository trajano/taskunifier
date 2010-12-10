package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;

public class TaskFilterTree extends JTree {

	public TaskFilterTree(TaskFilter filter) {
		super(new TaskFilterTreeModel(filter));
		this.initialize();
	}

	private void initialize() {
		this.setRootVisible(true);

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(Images.getResourceImage("tree_leaf.png"));
		renderer.setOpenIcon(Images.getResourceImage("tree_open.png"));
		renderer.setClosedIcon(Images.getResourceImage("tree_closed.png"));
		this.setCellRenderer(renderer);
	}

}
