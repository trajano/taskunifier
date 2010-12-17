package com.leclercb.taskunifier.gui.components.tasks.list.editors;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.list.TaskCell;

public class TaskEditor extends AbstractCellEditor implements TableCellEditor {

	private TaskCell taskCell;

	public TaskEditor() {
		this.taskCell = new TaskCell();
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.taskCell.setTask((Task) value);
		return this.taskCell;
	}

}
