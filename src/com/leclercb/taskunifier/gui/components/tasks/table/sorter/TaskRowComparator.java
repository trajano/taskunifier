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

import java.util.Calendar;

import javax.swing.SortOrder;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTableModel;
import com.leclercb.taskunifier.gui.swing.rowsorter.RowComparator;

public class TaskRowComparator implements RowComparator<Object> {
	
	private TaskTableModel model;
	
	public TaskRowComparator(TaskTableModel model) {
		this.model = model;
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		return 0;
	}
	
	@Override
	public int compare(int row1, int row2, int column, SortOrder sortOrder) {
		TaskTableModel tableModel = this.model;
		TaskColumn taskColumn = tableModel.getTaskColumn(column);
		
		Task task1 = tableModel.getTask(row1);
		Task task2 = tableModel.getTask(row2);
		
		Object o1 = tableModel.getValueAt(row1, column);
		Object o2 = tableModel.getValueAt(row2, column);
		
		int result = 0;
		
		if (task1.getParent() == null && task2.getParent() == null) {
			// If both task are parents, compare them
			result = (sortOrder.equals(SortOrder.ASCENDING) ? 1 : -1)
					* this.compare(taskColumn, o1, o2);
		} else if (task1.getParent() != null
				&& task2.getParent() != null
				&& task1.getParent().equals(task2.getParent())) {
			// If both task have the same parent, compare them
			result = (sortOrder.equals(SortOrder.ASCENDING) ? 1 : -1)
					* this.compare(taskColumn, o1, o2);
		} else if (task1.getParent() == null
				&& task2.getParent() != null
				&& task1.equals(task2.getParent())) {
			// If a task is the child of the other task
			result = -1;
		} else if (task1.getParent() != null
				&& task2.getParent() == null
				&& task1.getParent().equals(task2)) {
			// If a task is the child of the other task
			result = 1;
		} else {
			// Else, compare tasks with parent
			if (task1.getParent() != null)
				task1 = task1.getParent();
			
			if (task2.getParent() != null)
				task2 = task2.getParent();
			
			Object newO1 = taskColumn.getValue(task1);
			Object newO2 = taskColumn.getValue(task2);
			result = (sortOrder.equals(SortOrder.ASCENDING) ? 1 : -1)
					* this.compare(taskColumn, newO1, newO2);
		}
		
		if (result == 0) {
			result = (sortOrder.equals(SortOrder.ASCENDING) ? 1 : -1)
					* task1.getModelId().getId().compareTo(
							task2.getModelId().getId());
		}
		
		return result;
	}
	
	private int compare(TaskColumn column, Object o1, Object o2) {
		int result = 0;
		
		switch (column) {
			case TITLE:
				result = ((String) o1).compareTo((String) o2);
				break;
			case TAGS:
				result = ((String) o1).compareTo((String) o2);
				break;
			case FOLDER:
				result = this.compareModels(((Folder) o1), ((Folder) o2));
				break;
			case CONTEXT:
				result = this.compareModels(((Context) o1), ((Context) o2));
				break;
			case GOAL:
				result = this.compareModels(((Goal) o1), ((Goal) o2));
				break;
			case LOCATION:
				result = this.compareModels(((Location) o1), ((Location) o2));
				break;
			case PARENT:
				result = this.compareModels(((Task) o1), ((Task) o2));
				break;
			case COMPLETED:
				result = ((Boolean) o1).compareTo((Boolean) o2);
				break;
			case COMPLETED_ON:
				result = this.compareCalendars((Calendar) o1, (Calendar) o2);
				break;
			case DUE_DATE:
				result = this.compareCalendars((Calendar) o1, (Calendar) o2);
				break;
			case START_DATE:
				result = this.compareCalendars((Calendar) o1, (Calendar) o2);
				break;
			case REMINDER:
				result = ((Integer) o1).compareTo((Integer) o2);
				break;
			case REPEAT:
				result = ((String) o1).compareTo((String) o2);
				break;
			case REPEAT_FROM:
				result = ((TaskRepeatFrom) o1).compareTo((TaskRepeatFrom) o2);
				break;
			case STATUS:
				result = ((TaskStatus) o1).compareTo((TaskStatus) o2);
				break;
			case LENGTH:
				result = ((Integer) o1).compareTo((Integer) o2);
				break;
			case PRIORITY:
				result = ((TaskPriority) o1).compareTo((TaskPriority) o2);
				break;
			case STAR:
				result = ((Boolean) o1).compareTo((Boolean) o2);
				break;
			case NOTE:
				result = ((String) o1).compareTo((String) o2);
				break;
			default:
				result = 0;
				break;
		}
		
		return result;
	}
	
	private int compareModels(Model model1, Model model2) {
		if (model1 == null && model2 == null)
			return 0;
		
		if (model1 == null)
			return 1;
		
		if (model2 == null)
			return -1;
		
		return model1.getTitle().compareTo(model2.getTitle());
	}
	
	private int compareCalendars(Calendar calendar1, Calendar calendar2) {
		if (calendar1 == null && calendar2 == null)
			return 0;
		
		if (calendar1 == null)
			return 1;
		
		if (calendar2 == null)
			return -1;
		
		return calendar1.compareTo(calendar2);
	}
	
}
