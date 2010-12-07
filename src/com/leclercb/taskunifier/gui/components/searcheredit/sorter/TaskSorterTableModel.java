package com.leclercb.taskunifier.gui.components.searcheredit.sorter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;

public class TaskSorterTableModel extends DefaultTableModel implements ListChangeListener, PropertyChangeListener {

	private TaskSorter sorter;

	public TaskSorterTableModel(TaskSorter sorter) {
		this.sorter = sorter;
		this.sorter.addListChangeListener(this);
		this.sorter.addPropertyChangeListener(this);
	}

	public TaskSorterElement getTaskSorterElement(int row) {
		return sorter.getElement(row);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		if (sorter == null)
			return 0;

		return sorter.getElementCount();
	}

	@Override
	public String getColumnName(int col) {
		switch(col) {
		case 0: return "Order";
		case 1: return "Column";
		case 2: return "Sort order";
		default: return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch(col) {
		case 0: return String.class;
		case 1: return TaskColumn.class;
		case 2: return SortOrder.class;
		default: return null;
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: return sorter.getElement(row).getOrder() + "";
		case 1: return sorter.getElement(row).getColumn();
		case 2: return sorter.getElement(row).getSortOrder();
		default: return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		switch(col) {
		case 0: sorter.getElement(row).setOrder(Integer.parseInt((String) value)); break;
		case 1: sorter.getElement(row).setColumn((TaskColumn) value); break;
		case 2: sorter.getElement(row).setSortOrder((SortOrder) value); break;
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
		int index = this.sorter.getIndexOf((TaskSorterElement) event.getSource());
		this.fireTableRowsUpdated(index, index);
	}

}
