package com.leclercb.taskunifier.gui.components.tasks.list;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.list.editors.TaskEditor;
import com.leclercb.taskunifier.gui.components.tasks.list.renderers.TaskRenderer;

public class TaskList extends JTable {
	
	public TaskList() {
		this.initialize();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.setRowHeight(150);
		
		this.setModel(new TaskListModel());
		
		this.setDefaultRenderer(Task.class, new TaskRenderer());
		this.setDefaultEditor(Task.class, new TaskEditor());
		
		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
}
