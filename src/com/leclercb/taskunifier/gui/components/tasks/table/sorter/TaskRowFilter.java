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
package com.leclercb.taskunifier.gui.components.tasks.table.sorter;

import java.util.List;

import javax.swing.RowFilter;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTableModel;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;

public class TaskRowFilter extends RowFilter<TaskTableModel, Integer> {
	
	private TaskFilter filter;
	
	public TaskRowFilter(TaskFilter filter) {
		this.setFilter(filter);
	}
	
	public TaskFilter getFilter() {
		return this.filter;
	}
	
	public void setFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		this.filter = filter;
	}
	
	@Override
	public boolean include(
			Entry<? extends TaskTableModel, ? extends Integer> entry) {
		TaskTableModel taskTableModel = entry.getModel();
		Task task = taskTableModel.getTask(entry.getIdentifier());
		
		if (!task.getModelStatus().equals(ModelStatus.LOADED)
				&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			return false;
		}
		
		// If a filtered parent task has non filtered children, it must be
		// displayed
		if (task.getParent() == null) {
			List<Task> tasks = TaskFactory.getInstance().getList();
			for (Task t : tasks) {
				if (task.equals(t.getParent()))
					if (this.filter.include(t))
						return true;
			}
		}
		
		return this.filter.include(task);
	}
	
}
