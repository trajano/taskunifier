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
package com.leclercb.taskunifier.gui.components.searcheredit.sorter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.api.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskSorterTableModel extends DefaultTableModel implements ListChangeListener, PropertyChangeListener {
	
	private TaskSorter sorter;
	
	public TaskSorterTableModel(TaskSorter sorter) {
		this.sorter = sorter;
		this.sorter.addListChangeListener(this);
		this.sorter.addPropertyChangeListener(this);
	}
	
	public TaskSorterElement getTaskSorterElement(int row) {
		return this.sorter.getElement(row);
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public int getRowCount() {
		if (this.sorter == null)
			return 0;
		
		return this.sorter.getElementCount();
	}
	
	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return Translations.getString("task_sorter.order");
			case 1:
				return Translations.getString("task_sorter.column");
			case 2:
				return Translations.getString("task_sorter.sort_order");
			default:
				return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0:
				return String.class;
			case 1:
				return TaskColumn.class;
			case 2:
				return SortOrder.class;
			default:
				return null;
		}
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return this.sorter.getElement(row).getOrder() + "";
			case 1:
				return this.sorter.getElement(row).getColumn();
			case 2:
				return this.sorter.getElement(row).getSortOrder();
			default:
				return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
			case 0:
				this.sorter.getElement(row).setOrder(
						Integer.parseInt((String) value));
				break;
			case 1:
				this.sorter.getElement(row).setColumn((TaskColumn) value);
				break;
			case 2:
				this.sorter.getElement(row).setSortOrder((SortOrder) value);
				break;
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
