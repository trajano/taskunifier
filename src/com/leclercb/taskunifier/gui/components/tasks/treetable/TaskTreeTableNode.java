package com.leclercb.taskunifier.gui.components.tasks.treetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.TaskUndoableEdit;
import com.leclercb.taskunifier.gui.constants.Constants;

public class TaskTreeTableNode implements MutableTreeTableNode {
	
	private MutableTreeTableNode root;
	private Task task;
	
	public TaskTreeTableNode(MutableTreeTableNode root, Task task) {
		this.root = root;
		this.setTask(task);
	}
	
	public Task getTask() {
		return this.task;
	}
	
	public void setTask(Task task) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		this.task = task;
	}
	
	@Override
	public TreeTableNode getChildAt(int childIndex) {
		Task[] children = this.task.getChildren();
		
		if (children.length <= childIndex)
			return null;
		
		return new TaskTreeTableNode(this.root, children[childIndex]);
	}
	
	@Override
	public int getColumnCount() {
		return TaskColumn.values().length;
	}
	
	@Override
	public TreeTableNode getParent() {
		if (this.task.getParent() == null)
			return this.root;
		
		return new TaskTreeTableNode(this.root, this.task.getParent());
	}
	
	@Override
	public Object getUserObject() {
		return this.task;
	}
	
	@Override
	public Object getValueAt(int column) {
		return TaskColumn.values()[column].getValue(this.task);
	}
	
	@Override
	public boolean isEditable(int column) {
		return TaskColumn.values()[column].isEditable();
	}
	
	@Override
	public void setUserObject(Object userObject) {
		if (!(userObject instanceof Task))
			throw new IllegalArgumentException();
		
		this.setTask((Task) userObject);
	}
	
	@Override
	public void setValueAt(Object value, int column) {
		TaskColumn col = TaskColumn.values()[column];
		
		Object oldValue = col.getValue(this.task);
		
		if (!EqualsUtils.equals(oldValue, value)) {
			col.setValue(this.task, value);
			Constants.UNDO_EDIT_SUPPORT.postEdit(new TaskUndoableEdit(
					this.task,
					col,
					value,
					oldValue));
		}
	}
	
	@Override
	public boolean getAllowsChildren() {
		return this.task.getParent() == null;
	}
	
	@Override
	public int getChildCount() {
		return this.task.getChildren().length;
	}
	
	@Override
	public int getIndex(TreeNode node) {
		Task[] children = this.task.getChildren();
		
		if (!(node instanceof TaskTreeTableNode))
			return -1;
		
		Task subtask = ((TaskTreeTableNode) node).getTask();
		
		int i = 0;
		for (Task child : children) {
			if (subtask.equals(child))
				return i;
			
			i++;
		}
		
		return -1;
	}
	
	@Override
	public boolean isLeaf() {
		return this.task.getParent() != null;
	}
	
	@Override
	public Enumeration<TaskTreeTableNode> children() {
		Task[] children = this.task.getChildren();
		List<TaskTreeTableNode> enumeration = new ArrayList<TaskTreeTableNode>();
		
		for (Task child : children) {
			enumeration.add(new TaskTreeTableNode(this.root, child));
		}
		
		return Collections.enumeration(enumeration);
	}
	
	@Override
	public void insert(MutableTreeTableNode arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void remove(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void remove(MutableTreeTableNode arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setParent(MutableTreeTableNode arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
