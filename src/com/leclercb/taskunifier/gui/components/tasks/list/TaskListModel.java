package com.leclercb.taskunifier.gui.components.tasks.list;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskListModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {

	public TaskListModel() {
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
	}

	public Task getTask(int row) {
		return TaskFactory.getInstance().get(row);
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return TaskFactory.getInstance().size();
	}

	@Override
	public String getColumnName(int col) {
		return Translations.getString("general.tasks");
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return Task.class;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return this.getTask(row);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {

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
		if (event.getPropertyName().equals(Task.PROP_MODEL_STATUS) || event.getPropertyName().equals(Task.PROP_PARENT)) {
			this.fireTableDataChanged();
		} else {
			int index = TaskFactory.getInstance().getIndexOf((Task) event.getSource());
			this.fireTableRowsUpdated(index, index);
		}
	}

}
