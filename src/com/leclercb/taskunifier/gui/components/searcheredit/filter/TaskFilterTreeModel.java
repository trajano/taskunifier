/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
