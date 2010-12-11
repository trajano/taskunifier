package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.utils.EqualsBuilder;
import com.leclercb.taskunifier.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;

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
		return this.element.getColumn() + " " + 
		this.element.getCondition() + " \"" +
		this.element.getValue() + "\"";
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

			return new EqualsBuilder()
			.append(this.element, node.element)
			.isEqual();
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
