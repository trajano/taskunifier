package com.leclercb.taskunifier.gui.components.tasks.treetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.TreeTableNode;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskTreeTableRootNode implements TreeTableNode {
	
	public TaskTreeTableRootNode() {

	}
	
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	
	@Override
	public int getChildCount() {
		return this.getRootTasks().length;
	}
	
	@Override
	public int getIndex(TreeNode node) {
		if (!(node instanceof TaskTreeTableNode))
			return -1;
		
		Task[] tasks = this.getRootTasks();
		Task nodeTask = ((TaskTreeTableNode) node).getTask();
		
		int i = 0;
		for (Task task : tasks) {
			if (nodeTask.equals(task))
				return i;
			
			i++;
		}
		
		return -1;
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public Enumeration<TaskTreeTableNode> children() {
		Task[] tasks = this.getRootTasks();
		List<TaskTreeTableNode> children = new ArrayList<TaskTreeTableNode>();
		
		for (Task task : tasks) {
			children.add(new TaskTreeTableNode(this, task));
		}
		
		return Collections.enumeration(children);
	}
	
	@Override
	public TreeTableNode getChildAt(int childIndex) {
		Task[] tasks = this.getRootTasks();
		
		if (tasks.length <= childIndex)
			return null;
		
		return new TaskTreeTableNode(this, tasks[childIndex]);
	}
	
	@Override
	public int getColumnCount() {
		return TaskColumn.values().length;
	}
	
	@Override
	public TreeTableNode getParent() {
		return null;
	}
	
	@Override
	public Object getUserObject() {
		return null;
	}
	
	@Override
	public Object getValueAt(int column) {
		return null;
	}
	
	@Override
	public boolean isEditable(int column) {
		return false;
	}
	
	@Override
	public void setUserObject(Object userObject) {

	}
	
	@Override
	public void setValueAt(Object value, int column) {

	}
	
	private Task[] getRootTasks() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		List<Task> children = new ArrayList<Task>();
		
		for (Task task : tasks) {
			if (task.getParent() == null)
				children.add(task);
		}
		
		return children.toArray(new Task[0]);
	}
	
}
