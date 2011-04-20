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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsBuilder;
import com.leclercb.commons.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class TaskFilterTreeNode implements TreeNode {
	
	private TaskFilter filter;
	
	public TaskFilterTreeNode(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		this.filter = filter;
	}
	
	public TaskFilter getFilter() {
		return this.filter;
	}
	
	@Override
	public String toString() {
		return TranslationsUtils.translateTaskFilterLink(this.filter.getLink());
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		if (childIndex < this.filter.getElementCount())
			return new TaskFilterElementTreeNode(
					this.filter.getElement(childIndex));
		
		return new TaskFilterTreeNode(this.filter.getFilter(childIndex
				- this.filter.getElementCount()));
	}
	
	@Override
	public int getChildCount() {
		return this.filter.getElementCount() + this.filter.getFilterCount();
	}
	
	@Override
	public TreeNode getParent() {
		if (this.filter.getParent() == null)
			return null;
		
		return new TaskFilterTreeNode(this.filter.getParent());
	}
	
	@Override
	public int getIndex(TreeNode node) {
		if (node instanceof TaskFilterElementTreeNode)
			return this.filter.getIndexOf(((TaskFilterElementTreeNode) node).getElement());
		
		return this.filter.getIndexOf(((TaskFilterTreeNode) node).getFilter());
	}
	
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public Enumeration<?> children() {
		List<Object> list = new ArrayList<Object>();
		
		for (TaskFilterElement e : this.filter.getElements())
			list.add(new TaskFilterElementTreeNode(e));
		
		for (TaskFilter f : this.filter.getFilters())
			list.add(new TaskFilterTreeNode(f));
		
		return Collections.enumeration(list);
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof TaskFilterTreeNode) {
			TaskFilterTreeNode node = (TaskFilterTreeNode) o;
			
			return new EqualsBuilder().append(this.filter, node.filter).isEqual();
		}
		
		return false;
	}
	
	@Override
	public final int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.filter);
		
		return hashCode.toHashCode();
	}
	
}
