/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.TaskFilterElement;

public class TaskFilterTreeModel extends DefaultTreeModel implements ListChangeListener, PropertyChangeListener {
	
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
			this.nodesWereInserted(
					new TaskFilterTreeNode(parent),
					new int[] { index });
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.nodesWereRemoved(
					new TaskFilterTreeNode(parent),
					new int[] { index },
					new Object[] { child });
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
