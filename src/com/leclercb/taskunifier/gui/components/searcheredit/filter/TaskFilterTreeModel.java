package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;

public class TaskFilterTreeModel extends DefaultTreeModel implements
		ListChangeListener, PropertyChangeListener {

	public TaskFilterTreeModel(TaskFilter filter) {
		super(new TaskFilterTreeNode(filter));

		filter.addListChangeListener(this);
		filter.addPropertyChangeListener(this);
	}

	@Override
	public void listChange(ListChangeEvent event) {
		int index = 0;
		TaskFilter parent = null;
		TreeNode child = null;

		if (event.getValue() instanceof TaskFilterElement) {
			child = new TaskFilterElementTreeNode(
					(TaskFilterElement) event.getValue());
			parent = (TaskFilter) event.getSource();
			index = event.getIndex();
		} else {
			child = new TaskFilterTreeNode((TaskFilter) event.getValue());
			parent = (TaskFilter) event.getSource();
			index = parent.getElementCount() + event.getIndex();
		}

		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.nodesWereInserted(new TaskFilterTreeNode(parent),
					new int[] { index });
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.nodesWereRemoved(new TaskFilterTreeNode(parent),
					new int[] { index }, new Object[] { child });
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		TreeNode node = null;

		if (event.getSource() instanceof TaskFilterElement) {
			node = new TaskFilterElementTreeNode(
					(TaskFilterElement) event.getSource());
		} else {
			node = new TaskFilterTreeNode((TaskFilter) event.getSource());
		}

		this.nodeChanged(node);
	}

}
