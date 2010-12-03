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

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskTableColumnModel extends DefaultTableColumnModel {

	public TaskTableColumnModel() {
		this.initialize();
	}

	private void initialize() {
		TaskColumn[] columns = TaskColumn.getValues(true);
		for (int i=0; i<columns.length; i++)
			this.addColumn(columns[i]);
	}

	public void addColumn(TaskColumn taskColumn) {
		TableColumn column = new TableColumn(taskColumn.ordinal());
		column.setIdentifier(taskColumn);
		column.setHeaderValue(taskColumn.getLabel());
		column.setPreferredWidth(taskColumn.getWidth());
		this.addColumn(column);
	}

	public TaskColumn getTaskColumn(int col) {
		return (TaskColumn) getColumn(col).getIdentifier();
	}

}
