package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.api.utils.EqualsBuilder;
import com.leclercb.taskunifier.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;

public class TaskFilterTreeNode implements TreeNode {

	private TaskFilter filter;

	public TaskFilterTreeNode(TaskFilter filter) {
		this.filter = filter;
	}

	public TaskFilter getFilter() {
		return this.filter;
	}

	@Override
	public String toString() {
		return this.filter.getLink() + "";
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		if (childIndex < this.filter.getElementCount())
			return new TaskFilterElementTreeNode(
					this.filter.getElement(childIndex));

		return new TaskFilterTreeNode(
				this.filter.getFilter(childIndex - this.filter.getElementCount()));
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

		for (TaskFilterElement e : filter.getElements())
			list.add(new TaskFilterElementTreeNode(e));

		for (TaskFilter f : filter.getFilters())
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

			return new EqualsBuilder()
			.append(this.filter, node.filter)
			.isEqual();
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
