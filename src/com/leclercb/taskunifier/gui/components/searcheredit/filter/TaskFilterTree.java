package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import javax.swing.JTree;

import com.leclercb.taskunifier.gui.searchers.TaskFilter;

public class TaskFilterTree extends JTree {

	private TaskFilter filter;

	public TaskFilterTree(TaskFilter filter) {
		super(new TaskFilterTreeModel(filter));
		this.filter = filter;
	}

}
