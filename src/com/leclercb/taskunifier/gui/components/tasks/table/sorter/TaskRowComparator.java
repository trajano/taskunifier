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
package com.leclercb.taskunifier.gui.components.tasks.table.sorter;

import java.util.Calendar;
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
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskRowComparator implements Comparator<Task> {
	
	private static TaskRowComparator INSTANCE;
	
	public static TaskRowComparator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskRowComparator();
		
		return INSTANCE;
	}
	
	private TaskSearcher searcher;
	
	private TaskRowComparator() {
		this.searcher = null;
	}
	
	public TaskSearcher getSearcher() {
		return this.searcher;
	}
	
	public void setSearcher(TaskSearcher searcher) {
		this.searcher = searcher;
	}
	
	@Override
	public int compare(Task task1, Task task2) {
		if (this.searcher == null)
			return 0;
		
		List<TaskSorterElement> sortElements = this.searcher.getSorter().getElements();
		
		for (TaskSorterElement element : sortElements) {
			int result = this.compare(
					element.getColumn(),
					element.getSortOrder(),
					task1,
					task2);
			
			if (result != 0)
				return result;
		}
		
		return this.compare(TaskColumn.MODEL, SortOrder.ASCENDING, task1, task2);
	}
	
	private int compare(
			TaskColumn taskColumn,
			SortOrder sortOrder,
			Task task1,
			Task task2) {
		Object o1 = taskColumn.getValue(task1);
		Object o2 = taskColumn.getValue(task2);
		
		int result = 0;
		
		if (task1.getParent() == null && task2.getParent() == null) {
			// If both tasks are parents, compare them
			result = (sortOrder.equals(SortOrder.ASCENDING) ? 1 : -1)
					* this.compare(taskColumn, o1, o2);
		} else if (task1.getParent() != null
				&& task2.getParent() != null
				&& task1.getParent().equals(task2.getParent())) {
			// If both tasks have the same parent, compare them
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
		
		return result;
	}
	
	private int compare(TaskColumn column, Object o1, Object o2) {
		int result = 0;
		
		switch (column) {
			case MODEL:
				result = CompareUtils.compare(
						((Task) o1).getModelId(),
						((Task) o2).getModelId());
				break;
			case TITLE:
				result = CompareUtils.compare((String) o1, (String) o2);
				break;
			case TAGS:
				result = CompareUtils.compare((String) o1, (String) o2);
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
				result = CompareUtils.compare((Boolean) o1, (Boolean) o2);
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
				result = CompareUtils.compare((Integer) o1, (Integer) o2);
				break;
			case REPEAT:
				result = CompareUtils.compare((String) o1, (String) o2);
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
			case PRIORITY:
				result = CompareUtils.compare(
						(TaskPriority) o1,
						(TaskPriority) o2);
				break;
			case STAR:
				result = CompareUtils.compare((Boolean) o1, (Boolean) o2);
				break;
			case NOTE:
				result = CompareUtils.compare((String) o1, (String) o2);
				break;
			case IMPORTANCE:
				result = CompareUtils.compare((Integer) o1, (Integer) o2);
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
