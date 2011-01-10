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
package com.leclercb.taskunifier.gui.components.tasks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import com.leclercb.taskunifier.api.event.ListenerList;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.api.utils.ListUtils;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum TaskColumn {
	
	TITLE(String.class, 1, Translations.getString("general.task.title"), 200, true, true),
	TAGS(String.class, 2, Translations.getString("general.task.tags"), 100, true, true),
	FOLDER(Folder.class, 3, Translations.getString("general.task.folder"), 100, true, true),
	CONTEXT(Context.class, 4, Translations.getString("general.task.context"), 100, true, true),
	GOAL(Goal.class, 5, Translations.getString("general.task.goal"), 100, true, true),
	LOCATION(Location.class, 6, Translations.getString("general.task.location"), 100, true, true),
	PARENT(Task.class, 7, Translations.getString("general.task.parent"), 100, false, false),
	COMPLETED(Boolean.class, 8, Translations.getString("general.task.completed"), 100, true, true),
	COMPLETED_ON(Calendar.class, 9, Translations.getString("general.task.completed_on"), 100, false, true),
	DUE_DATE(Calendar.class, 10, Translations.getString("general.task.due_date"), 150, true, true),
	START_DATE(Calendar.class, 11, Translations.getString("general.task.start_date"), 150, true, true),
	REMINDER(Integer.class, 12, Translations.getString("general.task.reminder"), 100, true, true),
	REPEAT(String.class, 13, Translations.getString("general.task.repeat"), 100, false, true),
	REPEAT_FROM(TaskRepeatFrom.class, 14, Translations.getString("general.task.repeat_from"), 100, true, true),
	STATUS(TaskStatus.class, 15, Translations.getString("general.task.status"), 100, true, true),
	LENGTH(Integer.class, 16, Translations.getString("general.task.length"), 50, true, true),
	PRIORITY(TaskPriority.class, 17, Translations.getString("general.task.priority"), 100, true, true),
	STAR(Boolean.class, 18, Translations.getString("general.task.star"), 40, true, true),
	NOTE(String.class, 19, Translations.getString("general.task.note"), 100, false, false);
	
	private Class<?> type;
	private int order;
	private String label;
	private int width;
	private boolean editable;
	private boolean visible;
	
	private TaskColumn(
			Class<?> type,
			int order,
			String label,
			int width,
			boolean editable,
			boolean visible) {
		this.setType(type);
		this.setLabel(label);
		this.setWidth(width);
		this.setEditable(editable);
		this.setVisible(visible);
	}
	
	public Class<?> getType() {
		return this.type;
	}
	
	private void setType(Class<?> type) {
		this.type = type;
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public void setOrder(int order) {
		int oldOrder = this.order;
		this.order = order;
		TaskColumn.firePropertyChange(this, PROP_ORDER, oldOrder, order);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		int oldWidth = this.width;
		this.width = width;
		TaskColumn.firePropertyChange(this, PROP_WIDTH, oldWidth, width);
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		boolean oldVisible = this.visible;
		this.visible = visible;
		TaskColumn.firePropertyChange(this, PROP_VISIBLE, oldVisible, visible);
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	public static TaskColumn[] getValues(boolean onlyVisible) {
		TaskColumn[] columns = TaskColumn.values();
		Arrays.sort(columns, new Comparator<TaskColumn>() {
			
			@Override
			public int compare(TaskColumn o1, TaskColumn o2) {
				return new Integer(o1.getOrder()).compareTo(o2.getOrder());
			}
			
		});
		
		if (!onlyVisible)
			return columns;
		
		int count = 0;
		for (int i = 0; i < columns.length; i++)
			if (columns[i].isVisible())
				count++;
		
		TaskColumn[] visibleColumns = new TaskColumn[count];
		for (int i = 0, j = 0; i < columns.length; i++)
			if (columns[i].isVisible())
				visibleColumns[j++] = columns[i];
		
		return visibleColumns;
	}
	
	public Object getValue(Task task) {
		switch (this) {
			case TITLE:
				return task.getTitle();
			case TAGS:
				return ListUtils.listToString(task.getTags(), ", ");
			case FOLDER:
				return task.getFolder();
			case CONTEXT:
				return task.getContext();
			case GOAL:
				return task.getGoal();
			case LOCATION:
				return task.getLocation();
			case PARENT:
				return task.getParent();
			case COMPLETED:
				return task.isCompleted();
			case COMPLETED_ON:
				return task.getCompletedOn();
			case DUE_DATE:
				return task.getDueDate();
			case START_DATE:
				return task.getStartDate();
			case REMINDER:
				return task.getReminder();
			case REPEAT:
				return task.getRepeat();
			case REPEAT_FROM:
				return task.getRepeatFrom();
			case STATUS:
				return task.getStatus();
			case LENGTH:
				return task.getLength();
			case PRIORITY:
				return task.getPriority();
			case STAR:
				return task.isStar();
			case NOTE:
				return task.getNote();
			default:
				return null;
		}
	}
	
	public void setValue(Task task, Object value) {
		switch (this) {
			case TITLE:
				task.setTitle((String) value);
				break;
			case TAGS:
				task.clearTags();
				String[] tags = ((String) value).split(",");
				for (int i = 0; i < tags.length; i++)
					if (tags[i].trim().length() != 0)
						task.addTag(tags[i].trim());
				break;
			case FOLDER:
				task.setFolder((Folder) value);
				break;
			case CONTEXT:
				task.setContext((Context) value);
				break;
			case GOAL:
				task.setGoal((Goal) value);
				break;
			case LOCATION:
				task.setLocation((Location) value);
				break;
			case PARENT:
				task.setParent((Task) value);
				break;
			case COMPLETED:
				task.setCompleted((Boolean) value);
				break;
			case COMPLETED_ON:
				task.setCompletedOn((Calendar) value);
				break;
			case DUE_DATE:
				task.setDueDate((Calendar) value);
				break;
			case START_DATE:
				task.setStartDate((Calendar) value);
				break;
			case REMINDER:
				task.setReminder((Integer) value);
				break;
			case REPEAT:
				task.setRepeat((String) value);
				break;
			case REPEAT_FROM:
				task.setRepeatFrom((TaskRepeatFrom) value);
				break;
			case STATUS:
				task.setStatus((TaskStatus) value);
				break;
			case LENGTH:
				task.setLength((Integer) value);
				break;
			case PRIORITY:
				task.setPriority((TaskPriority) value);
				break;
			case STAR:
				task.setStar((Boolean) value);
				break;
			case NOTE:
				task.setNote((String) value);
				break;
		}
	}
	
	public static final String PROP_ORDER = "TASK_COLUMN_ORDER";
	public static final String PROP_WIDTH = "TASK_COLUMN_WIDTH";
	public static final String PROP_VISIBLE = "TASK_COLUMN_VISIBLE";
	
	private static ListenerList<PropertyChangeListener> propertyChangeListenerList;
	
	static {
		propertyChangeListenerList = new ListenerList<PropertyChangeListener>();
	}
	
	public static void addPropertyChangeListener(PropertyChangeListener listener) {
		TaskColumn.propertyChangeListenerList.addListener(listener);
	}
	
	public static void removePropertyChangeListener(
			PropertyChangeListener listener) {
		TaskColumn.propertyChangeListenerList.removeListener(listener);
	}
	
	protected static void firePropertyChange(PropertyChangeEvent event) {
		if (propertyChangeListenerList != null)
			for (PropertyChangeListener listener : TaskColumn.propertyChangeListenerList)
				listener.propertyChange(event);
	}
	
	protected static void firePropertyChange(
			Object source,
			String property,
			Object oldValue,
			Object newValue) {
		firePropertyChange(new PropertyChangeEvent(
				source,
				property,
				oldValue,
				newValue));
	}
	
}
