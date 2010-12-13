/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.tasks.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.undo.TaskUndoableEdit;

public class TaskTableModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {

	public TaskTableModel() {
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
	}

	public Task getTask(int row) {
		return TaskFactory.getInstance().get(row);
	}

	public TaskColumn getTaskColumn(int col) {
		return TaskColumn.values()[col];
	}

	@Override
	public int getColumnCount() {
		return TaskColumn.values().length;
	}

	@Override
	public int getRowCount() {
		return TaskFactory.getInstance().size();
	}

	@Override
	public String getColumnName(int col) {
		return TaskColumn.values()[col].getLabel();
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return TaskColumn.values()[col].getType();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Task task = TaskFactory.getInstance().get(row);
		return TaskColumn.values()[col].getValue(task);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return TaskColumn.values()[col].isEditable();
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Task task = TaskFactory.getInstance().get(row);
		TaskColumn column = TaskColumn.values()[col];

		Object oldValue = column.getValue(task);

		if (!EqualsUtils.equals(oldValue, value)) {
			column.setValue(task, value);
			Constants.UNDO_EDIT_SUPPORT.postEdit(new TaskUndoableEdit(task, column, value, oldValue));
		}
	}

	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.fireTableRowsInserted(event.getIndex(), event.getIndex());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.fireTableRowsDeleted(event.getIndex(), event.getIndex());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(Task.PROP_MODEL_STATUS) || 
				event.getPropertyName().equals(Task.PROP_PARENT)) {
			this.fireTableDataChanged();
		} else {
			int index = TaskFactory.getInstance().getIndexOf((Task) event.getSource());
			this.fireTableRowsUpdated(index, index);
		}
	}

}
