/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.SortOrder;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.Timer;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;

public class TaskComparator implements Comparator<Task> {
	
	private TaskSorter sorter;
	
	public TaskComparator() {
		this.sorter = null;
	}
	
	public TaskSorter getTaskSorter() {
		return this.sorter;
	}
	
	public void setTaskSorter(TaskSorter sorter) {
		this.sorter = sorter;
	}
	
	@Override
	public int compare(Task task1, Task task2) {
		if (this.sorter == null)
			return 0;
		
		List<TaskSorterElement> sortElements = this.sorter.getElements();
		
		for (TaskSorterElement element : sortElements) {
			int result = this.compare(
					element.getProperty(),
					element.getSortOrder(),
					task1,
					task2);
			
			if (result != 0)
				return result;
		}
		
		int result = this.compare(
				TaskColumn.MODEL_CREATION_DATE,
				SortOrder.ASCENDING,
				task1,
				task2);
		
		if (result != 0)
			return result;
		
		return this.compare(TaskColumn.MODEL, SortOrder.ASCENDING, task1, task2);
	}
	
	private int compare(
			TaskColumn taskColumn,
			SortOrder sortOrder,
			Task task1,
			Task task2) {
		boolean indentSubtasks = Main.getSettings().getBooleanProperty(
				"task.indent_subtasks");
		
		if (indentSubtasks)
			return this.compareIndented(taskColumn, sortOrder, task1, task2);
		else
			return this.compareUnindented(taskColumn, sortOrder, task1, task2);
	}
	
	private int compareUnindented(
			TaskColumn taskColumn,
			SortOrder sortOrder,
			Task task1,
			Task task2) {
		Object o1 = taskColumn.getProperty(task1);
		Object o2 = taskColumn.getProperty(task2);
		
		return this.compare(taskColumn, o1, o2, sortOrder);
	}
	
	private int compareIndented(
			TaskColumn taskColumn,
			SortOrder sortOrder,
			Task task1,
			Task task2) {
		Object o1 = taskColumn.getProperty(task1);
		Object o2 = taskColumn.getProperty(task2);
		
		int result = 0;
		
		List<Task> parents1 = new ArrayList<Task>(
				Arrays.asList(task1.getAllParents()));
		List<Task> parents2 = new ArrayList<Task>(
				Arrays.asList(task2.getAllParents()));
		
		if (task1.getParent() == null && task2.getParent() == null) {
			// If both tasks are parents, compare them
			result = this.compare(taskColumn, o1, o2, sortOrder);
		} else if (task1.getParent() != null
				&& task2.getParent() != null
				&& task1.getParent().equals(task2.getParent())) {
			// If both tasks have the same parent, compare them
			result = this.compare(taskColumn, o1, o2, sortOrder);
		} else if (parents1.contains(task2)) {
			// If a task is the child of the other task
			result = 1;
		} else if (parents2.contains(task1)) {
			// If a task is the child of the other task
			result = -1;
		} else {
			// Else, compare tasks with parent
			parents1.add(0, task1);
			parents2.add(0, task2);
			
			Collections.reverse(parents1);
			Collections.reverse(parents2);
			
			int max = Math.max(parents1.size(), parents2.size());
			for (int i = 0; i < max; i++) {
				if (i < parents1.size())
					task1 = parents1.get(i);
				
				if (i < parents2.size())
					task2 = parents2.get(i);
				
				if (task1.equals(task2))
					continue;
				
				Object newO1 = taskColumn.getProperty(task1);
				Object newO2 = taskColumn.getProperty(task2);
				
				result = this.compare(taskColumn, newO1, newO2, sortOrder);
				
				break;
			}
		}
		
		return result;
	}
	
	private int compare(
			TaskColumn column,
			Object o1,
			Object o2,
			SortOrder sortOrder) {
		if (o1 == null && o2 == null)
			return 0;
		
		if (o1 == null)
			return 1;
		
		if (o2 == null)
			return -1;
		
		int result = 0;
		
		switch (column) {
			case MODEL:
				result = CompareUtils.compare(
						((Task) o1).getModelId(),
						((Task) o2).getModelId());
				break;
			case MODEL_EDIT:
				result = 0;
				break;
			case MODEL_CREATION_DATE:
				result = this.compareCalendars(
						(Calendar) o1,
						(Calendar) o2,
						true);
				break;
			case MODEL_UPDATE_DATE:
				result = this.compareCalendars(
						(Calendar) o1,
						(Calendar) o2,
						true);
				break;
			case SHOW_CHILDREN:
				result = CompareUtils.compare((Boolean) o1, (Boolean) o2);
				break;
			case TITLE:
				result = CompareUtils.compareStringIgnoreCase(
						(String) o1,
						(String) o2);
				break;
			case ORDER:
				result = CompareUtils.compare((Integer) o1, (Integer) o2);
				break;
			case CONTACTS:
				result = CompareUtils.compareStringIgnoreCase(
						(String) o1,
						(String) o2);
				break;
			case TASKS:
				result = CompareUtils.compareStringIgnoreCase(
						(String) o1,
						(String) o2);
				break;
			case FILES:
				result = CompareUtils.compareStringIgnoreCase(
						(String) o1,
						(String) o2);
				break;
			case TAGS:
				result = CompareUtils.compareStringIgnoreCase(
						(String) o1,
						(String) o2);
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
			case PROGRESS:
				result = CompareUtils.compare((Double) o1, (Double) o2);
				break;
			case COMPLETED:
				result = CompareUtils.compare((Boolean) o1, (Boolean) o2);
				break;
			case COMPLETED_ON:
				result = this.compareCalendars(
						(Calendar) o1,
						(Calendar) o2,
						false);
				break;
			case DUE_DATE:
				result = this.compareCalendars(
						(Calendar) o1,
						(Calendar) o2,
						false);
				break;
			case START_DATE:
				result = this.compareCalendars(
						(Calendar) o1,
						(Calendar) o2,
						false);
				break;
			case DUE_DATE_REMINDER:
				result = CompareUtils.compare((Integer) o1, (Integer) o2);
				break;
			case START_DATE_REMINDER:
				result = CompareUtils.compare((Integer) o1, (Integer) o2);
				break;
			case REPEAT:
				result = CompareUtils.compareStringIgnoreCase(
						(String) o1,
						(String) o2);
				break;
			case REPEAT_FROM:
				result = CompareUtils.compare(
						(TaskRepeatFrom) o1,
						(TaskRepeatFrom) o2);
				break;
			case STATUS:
				result = CompareUtils.compare((TaskStatus) o1, (TaskStatus) o2);
				break;
			case LENGTH:
				result = CompareUtils.compare((Integer) o1, (Integer) o2);
				break;
			case TIMER:
				result = CompareUtils.compare(((Timer) o1), ((Timer) o2));
				break;
			case PRIORITY:
				result = CompareUtils.compare(
						(TaskPriority) o1,
						(TaskPriority) o2);
				break;
			case STAR:
				result = CompareUtils.compare((Boolean) o1, (Boolean) o2);
				break;
			case NOTE:
				result = CompareUtils.compareStringIgnoreCase(
						(String) o1,
						(String) o2);
				break;
			case IMPORTANCE:
				result = CompareUtils.compare((Integer) o1, (Integer) o2);
				break;
			default:
				result = 0;
				break;
		}
		
		return (sortOrder.equals(SortOrder.ASCENDING) ? 1 : -1) * result;
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
	
	private int compareCalendars(
			Calendar calendar1,
			Calendar calendar2,
			boolean raw) {
		if (calendar1 == null && calendar2 == null)
			return 0;
		
		if (calendar1 == null)
			return 1;
		
		if (calendar2 == null)
			return -1;
		
		if (!raw) {
			calendar1.set(Calendar.SECOND, 0);
			calendar1.set(Calendar.MILLISECOND, 0);
			
			calendar2.set(Calendar.SECOND, 0);
			calendar2.set(Calendar.MILLISECOND, 0);
		}
		
		return calendar1.compareTo(calendar2);
	}
	
}
