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
package com.leclercb.taskunifier.gui.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.models.GuiTask;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.commons.values.StringValueBoolean;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModelId;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskLength;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskProgress;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskReminder;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeat;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class TaskUtils {
	
	private TaskUtils() {

	}
	
	public static String toHtml(Task[] tasks, TaskColumn[] columns) {
		String[][] data = toStringData(tasks, columns);
		StringBuffer buffer = new StringBuffer();
		
		if (data == null)
			return null;
		
		buffer.append("<table>");
		
		int i = 0;
		for (String[] row : data) {
			if (i == 0)
				buffer.append("<tr style=\"font-weight:bold;\">");
			else
				buffer.append("<tr>");
			
			for (String col : row)
				buffer.append("<td>"
						+ StringEscapeUtils.escapeHtml(col)
						+ "</td>");
			buffer.append("</tr>");
			
			i++;
		}
		
		buffer.append("</table>");
		
		return buffer.toString();
	}
	
	public static String[][] toStringData(Task[] tasks, TaskColumn[] columns) {
		CheckUtils.isNotNull(tasks, "Tasks cannot be null");
		CheckUtils.isNotNull(columns, "Columns cannot be null");
		
		boolean useDueTime = Main.SETTINGS.getBooleanProperty("date.use_due_time");
		boolean useStartTime = Main.SETTINGS.getBooleanProperty("date.use_start_time");
		
		int colCount = 0;
		List<String[]> data = new ArrayList<String[]>();
		
		for (TaskColumn column : columns) {
			if (column == null)
				continue;
			
			colCount++;
		}
		
		if (colCount == 0)
			return null;
		
		int i = 0;
		String[] row = new String[colCount];
		
		for (TaskColumn column : columns) {
			if (column == null)
				continue;
			
			row[i++] = column.getLabel();
		}
		
		data.add(row);
		
		for (Task task : tasks) {
			if (task == null)
				continue;
			
			i = 0;
			row = new String[colCount];
			
			for (TaskColumn column : columns) {
				if (column == null)
					continue;
				
				String content = null;
				Object value = column.getValue(task);
				
				switch (column) {
					case COMPLETED:
						content = StringValueBoolean.INSTANCE.getString(value);
						break;
					case COMPLETED_ON:
						content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
						break;
					case CONTEXT:
					case FOLDER:
					case GOAL:
					case LOCATION:
					case PARENT:
						content = StringValueModel.INSTANCE.getString(value);
						break;
					case DUE_DATE:
						if (useDueTime)
							content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
						else
							content = StringValueCalendar.INSTANCE_DATE.getString(value);
						break;
					case IMPORTANCE:
						content = (value == null ? null : value.toString());
						break;
					case LENGTH:
						content = StringValueTaskLength.INSTANCE.getString(value);
						break;
					case MODEL:
						content = StringValueModelId.INSTANCE.getString(value);
						break;
					case NOTE:
						content = (value == null ? null : value.toString());
						break;
					case PRIORITY:
						content = StringValueTaskPriority.INSTANCE.getString(value);
						break;
					case PROGRESS:
						content = StringValueTaskProgress.INSTANCE.getString(value);
						break;
					case REMINDER:
						content = StringValueTaskReminder.INSTANCE.getString(value);
						break;
					case REPEAT:
						content = StringValueTaskRepeat.INSTANCE.getString(value);
						break;
					case REPEAT_FROM:
						content = StringValueTaskRepeatFrom.INSTANCE.getString(value);
						break;
					case SHOW_CHILDREN:
						content = StringValueBoolean.INSTANCE.getString(value);
						break;
					case STAR:
						content = StringValueBoolean.INSTANCE.getString(value);
						break;
					case START_DATE:
						if (useStartTime)
							content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
						else
							content = StringValueCalendar.INSTANCE_DATE.getString(value);
						break;
					case STATUS:
						content = StringValueTaskStatus.INSTANCE.getString(value);
						break;
					case TAGS:
						content = (value == null ? null : value.toString());
						break;
					case TITLE:
						content = (value == null ? null : value.toString());
						break;
				}
				
				if (content == null)
					content = "";
				
				row[i++] = content;
			}
			
			data.add(row);
		}
		
		return data.toArray(new String[0][]);
	}
	
	public static int getImportance(Task task) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		
		int importance = 2;
		
		switch (task.getPriority()) {
			case NEGATIVE:
				importance += -1;
				break;
			case LOW:
				importance += 0;
				break;
			case MEDIUM:
				importance += 1;
				break;
			case HIGH:
				importance += 2;
				break;
			case TOP:
				importance += 3;
				break;
		}
		
		importance += (task.isStar() ? 1 : 0);
		
		if (task.getDueDate() != null) {
			long milliSeconds1 = task.getDueDate().getTimeInMillis();
			long milliSeconds2 = Calendar.getInstance().getTimeInMillis();
			long diff = milliSeconds1 - milliSeconds2;
			double diffDays = diff / (24 * 60 * 60 * 1000.0);
			
			if (diffDays > 14)
				importance += 0;
			else if (diffDays >= 7)
				importance += 1;
			else if (diffDays >= 2)
				importance += 2;
			else if (diffDays == 1)
				importance += 3;
			else if (diffDays == 0)
				importance += 5;
			else
				importance += 6;
		}
		
		return importance;
	}
	
	public static boolean showTask(Task task, TaskFilter filter) {
		return showTask(task, filter, containsCompletedTrue(filter), false);
	}
	
	private static boolean showTask(
			Task task,
			TaskFilter filter,
			boolean containsCompletedTrue,
			boolean skipParentCheck) {
		if (!task.getModelStatus().equals(ModelStatus.LOADED)
				&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			return false;
		}
		
		// If a filtered parent task has non filtered children,
		// it must be displayed
		if (!skipParentCheck) {
			if (task.getParent() == null) {
				Task[] children = task.getChildren();
				for (Task child : children)
					if (showTask(child, filter, containsCompletedTrue, false))
						return true;
			}
		}
		
		if (task.getParent() != null) {
			if (!((GuiTask) task.getParent()).isShowChildren())
				return false;
		}
		
		if (!Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks")) {
			if (task.isCompleted() && !containsCompletedTrue)
				return false;
		}
		
		if (task.getParent() != null) {
			if (showTask(task.getParent(), filter, containsCompletedTrue, true))
				return true;
		}
		
		if (filter == null)
			return true;
		
		return filter.include(task);
	}
	
	private static boolean containsCompletedTrue(TaskFilter filter) {
		if (filter == null)
			return false;
		
		List<TaskFilterElement> elements = filter.getElements();
		List<TaskFilter> filters = filter.getFilters();
		
		for (TaskFilterElement e : elements) {
			if (e.getColumn() == TaskColumn.COMPLETED) {
				if (e.getValue().toString().equals("true"))
					return true;
			}
		}
		
		for (TaskFilter f : filters) {
			if (containsCompletedTrue(f))
				return true;
		}
		
		return false;
	}
	
}
