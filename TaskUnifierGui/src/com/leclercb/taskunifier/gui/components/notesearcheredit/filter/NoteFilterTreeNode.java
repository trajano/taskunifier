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
package com.leclercb.taskunifier.gui.components.notesearcheredit.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilterElement;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class NoteFilterTreeNode implements TreeNode {
	
	private NoteFilter filter;
	
	public NoteFilterTreeNode(NoteFilter filter) {
		CheckUtils.isNotNull(filter);
		this.filter = filter;
	}
	
	public NoteFilter getFilter() {
		return this.filter;
	}
	
	@Override
	public String toString() {
		return TranslationsUtils.translateFilterLink(this.filter.getLink());
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		if (childIndex < this.filter.getElementCount())
			return new NoteFilterElementTreeNode(
					this.filter.getElement(childIndex));
		
		return new NoteFilterTreeNode(this.filter.getFilter(childIndex
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
		
		return new NoteFilterTreeNode(this.filter.getParent());
	}
	
	@Override
	public int getIndex(TreeNode node) {
		if (node instanceof NoteFilterElementTreeNode)
			return this.filter.getIndexOf(((NoteFilterElementTreeNode) node).getElement());
		
		return this.filter.getIndexOf(((NoteFilterTreeNode) node).getFilter());
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
		
		for (NoteFilterElement e : this.filter.getElements())
			list.add(new NoteFilterElementTreeNode(e));
		
		for (NoteFilter f : this.filter.getFilters())
			list.add(new NoteFilterTreeNode(f));
		
		return Collections.enumeration(list);
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof NoteFilterTreeNode) {
			NoteFilterTreeNode node = (NoteFilterTreeNode) o;
			
			return new EqualsBuilder().append(this.filter, node.filter).isEquals();
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
