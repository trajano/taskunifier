package com.leclercb.taskunifier.gui.components.tasks.list.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.list.TaskCell;

public class TaskRenderer implements TableCellRenderer {
	
	private TaskCell taskCell;
	
	public TaskRenderer() {
		this.taskCell = new TaskCell();
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		this.taskCell.setTask((Task) value);
		
		if (isSelected)
			this.taskCell.setBackground(UIManager.getColor("Table.selectionBackground"));
		
		return this.taskCell;
	}
	
}
