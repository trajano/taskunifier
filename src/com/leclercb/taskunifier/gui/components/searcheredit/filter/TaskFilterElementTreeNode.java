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

import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsBuilder;
import com.leclercb.commons.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.TaskFilterElement;

public class TaskFilterElementTreeNode implements TreeNode {
	
	private TaskFilterElement element;
	
	public TaskFilterElementTreeNode(TaskFilterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		this.element = element;
	}
	
	public TaskFilterElement getElement() {
		return this.element;
	}
	
	@Override
	public String toString() {
		return this.element.toString();
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}
	
	@Override
	public int getChildCount() {
		return 0;
	}
	
	@Override
	public TreeNode getParent() {
		return new TaskFilterTreeNode(this.element.getParent());
	}
	
	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}
	
	@Override
	public boolean getAllowsChildren() {
		return false;
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public Enumeration<?> children() {
		return Collections.enumeration(Collections.emptyList());
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof TaskFilterElementTreeNode) {
			TaskFilterElementTreeNode node = (TaskFilterElementTreeNode) o;
			
			return new EqualsBuilder().append(this.element, node.element).isEqual();
		}
		
		return false;
	}
	
	@Override
	public final int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.element);
		
		return hashCode.toHashCode();
	}
	
}
