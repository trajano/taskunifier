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
package com.leclercb.taskunifier.gui.components.tasksearcheredit.filter;

import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;

public class TaskFilterElementTreeNode implements TreeNode {
	
	private TaskFilterElement element;
	
	public TaskFilterElementTreeNode(TaskFilterElement element) {
		CheckUtils.isNotNull(element);
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
			
			return new EqualsBuilder().append(this.element, node.element).isEquals();
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
