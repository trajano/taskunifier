package com.leclercb.taskunifier.gui.components.searcheredit.sorter;

import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.searcher.TaskSorter;

public class TaskSorterTableModel extends DefaultTableModel {

	private TaskSorter sorter;

	public TaskSorterTableModel(TaskSorter sorter) {
		this.sorter = sorter;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return sorter.getElementCount();
	}

	@Override
	public String getColumnName(int col) {
		switch(col) {
		case 0: return "Column";
		case 1: return "Sort order";
		default: return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch(col) {
		case 0: return TaskColumn.class;
		case 1: return SortOrder.class;
		default: return null;
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: return sorter.getElement(row).getColumn();
		case 1: return sorter.getElement(row).getSortOrder();
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
		case 0: sorter.getElement(row).setColumn((TaskColumn) value); break;
		case 1: sorter.getElement(row).setSortOrder((SortOrder) value); break;
		}
	}

}
