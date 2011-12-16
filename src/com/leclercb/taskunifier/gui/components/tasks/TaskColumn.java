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
package com.leclercb.taskunifier.gui.components.tasks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.Timer;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.models.GuiTask;
import com.leclercb.taskunifier.gui.api.models.properties.ModelProperties;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public enum TaskColumn implements ModelProperties<Task> {
	
	MODEL(Task.class, Translations.getString("general.task.id"), false),
	MODEL_EDIT(Void.class, Translations.getString("general.edit"), false),
	MODEL_CREATION_DATE(Calendar.class, Translations.getString("general.creation_date"), false),
	MODEL_UPDATE_DATE(Calendar.class, Translations.getString("general.update_date"), false),
	SHOW_CHILDREN(Boolean.class, Translations.getString("general.task.show_children"), true),
	TITLE(String.class, Translations.getString("general.task.title"), true),
	ORDER(Integer.class, Translations.getString("general.task.order"), false),
	TAGS(String.class, Translations.getString("general.task.tags"), true),
	CONTACTS(String.class, Translations.getString("general.task.contacts"), false),
	FOLDER(Folder.class, Translations.getString("general.task.folder"), true),
	CONTEXT(Context.class, Translations.getString("general.task.context"), true),
	GOAL(Goal.class, Translations.getString("general.task.goal"), true),
	LOCATION(Location.class, Translations.getString("general.task.location"), true),
	PARENT(Task.class, Translations.getString("general.task.parent"), false),
	PROGRESS(Double.class, Translations.getString("general.task.progress"), true),
	COMPLETED(Boolean.class, Translations.getString("general.task.completed"), true),
	COMPLETED_ON(Calendar.class, Translations.getString("general.task.completed_on"), false),
	DUE_DATE(Calendar.class, Translations.getString("general.task.due_date"), true),
	START_DATE(Calendar.class, Translations.getString("general.task.start_date"), true),
	DUE_DATE_REMINDER(Integer.class, Translations.getString("general.task.due_date_reminder"), true),
	START_DATE_REMINDER(Integer.class, Translations.getString("general.task.start_date_reminder"), true),
	REPEAT(String.class, Translations.getString("general.task.repeat"), true),
	REPEAT_FROM(TaskRepeatFrom.class, Translations.getString("general.task.repeat_from"), true),
	STATUS(TaskStatus.class, Translations.getString("general.task.status"), true),
	LENGTH(Integer.class, Translations.getString("general.task.length"), true),
	TIMER(Timer.class, Translations.getString("general.task.timer"), true),
	PRIORITY(TaskPriority.class, Translations.getString("general.task.priority"), true),
	STAR(Boolean.class, Translations.getString("general.task.star"), true),
	IMPORTANCE(Integer.class, Translations.getString("general.task.importance"), false),
	NOTE(String.class, Translations.getString("general.task.note"), false);
	
	public static final String PROP_ORDER = "order";
	public static final String PROP_WIDTH = "width";
	public static final String PROP_VISIBLE = "visible";
	
	public static TaskColumn[] getVisibleTaskColumns() {
		List<TaskColumn> columns = new ArrayList<TaskColumn>(
				Arrays.asList(TaskColumn.values()));
		
		for (TaskColumn column : TaskColumn.values()) {
			if (!column.isVisible()) {
				columns.remove(column);
			}
		}
		
		return columns.toArray(new TaskColumn[0]);
	}
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private Class<?> type;
	private String label;
	private boolean editable;
	
	private int order;
	private int width;
	private boolean visible;
	
	private TaskColumn(Class<?> type, String label, boolean editable) {
		this.propertyChangeSupport = new PropertyChangeSupport(TaskColumn.class);
		
		this.setType(type);
		this.setLabel(label);
		this.setEditable(editable);
		
		this.setOrder(Main.getSettings().getIntegerProperty(
				"taskcolumn." + TaskColumn.this.name().toLowerCase() + ".order",
				0));
		
		this.setWidth(Main.getSettings().getIntegerProperty(
				"taskcolumn." + TaskColumn.this.name().toLowerCase() + ".width",
				100));
		
		this.setVisible(Main.getSettings().getBooleanProperty(
				"taskcolumn."
						+ TaskColumn.this.name().toLowerCase()
						+ ".visible",
				true));
		
		Main.getSettings().addPropertyChangeListener(
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().startsWith(
								"taskcolumn."
										+ TaskColumn.this.name().toLowerCase())) {
							if (evt.getNewValue() == null)
								return;
							
							if (evt.getPropertyName().equals(
									"taskcolumn."
											+ TaskColumn.this.name().toLowerCase()
											+ ".order"))
								TaskColumn.this.setOrder(Integer.parseInt(evt.getNewValue().toString()));
							
							if (evt.getPropertyName().equals(
									"taskcolumn."
											+ TaskColumn.this.name().toLowerCase()
											+ ".width"))
								TaskColumn.this.setWidth(Integer.parseInt(evt.getNewValue().toString()));
							
							if (evt.getPropertyName().equals(
									"taskcolumn."
											+ TaskColumn.this.name().toLowerCase()
											+ ".visible"))
								TaskColumn.this.setVisible(Boolean.parseBoolean(evt.getNewValue().toString()));
						}
					}
					
				});
	}
	
	@Override
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
		if (order == this.getOrder())
			return;
		
		int oldOrder = this.getOrder();
		this.order = order;
		Main.getSettings().setIntegerProperty(
				"taskcolumn." + this.name().toLowerCase() + ".order",
				order);
		this.propertyChangeSupport.firePropertyChange(
				PROP_ORDER,
				oldOrder,
				order);
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
		if (width == this.getWidth())
			return;
		
		int oldWidth = this.getWidth();
		this.width = width;
		Main.getSettings().setIntegerProperty(
				"taskcolumn." + this.name().toLowerCase() + ".width",
				width);
		this.propertyChangeSupport.firePropertyChange(
				PROP_WIDTH,
				oldWidth,
				width);
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
		if (visible == this.isVisible())
			return;
		
		boolean oldVisible = this.isVisible();
		this.visible = visible;
		Main.getSettings().setBooleanProperty(
				"taskcolumn." + this.name().toLowerCase() + ".visible",
				visible);
		this.propertyChangeSupport.firePropertyChange(
				PROP_VISIBLE,
				oldVisible,
				visible);
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	@Override
	public Object getProperty(Task task) {
		if (task == null)
			return null;
		
		switch (this) {
			case MODEL:
				return task;
			case MODEL_EDIT:
				return null;
			case MODEL_CREATION_DATE:
				return task.getModelCreationDate();
			case MODEL_UPDATE_DATE:
				return task.getModelUpdateDate();
			case SHOW_CHILDREN:
				return ((GuiTask) task).isShowChildren();
			case TITLE:
				return task.getTitle();
			case ORDER:
				return task.getOrder();
			case TAGS:
				return task.getTags().toString();
			case CONTACTS:
				return task.getContacts().toString();
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
			case PROGRESS:
				return task.getProgress();
			case COMPLETED:
				return task.isCompleted();
			case COMPLETED_ON:
				return task.getCompletedOn();
			case DUE_DATE:
				return task.getDueDate();
			case START_DATE:
				return task.getStartDate();
			case DUE_DATE_REMINDER:
				return task.getDueDateReminder();
			case START_DATE_REMINDER:
				return task.getStartDateReminder();
			case REPEAT:
				return task.getRepeat();
			case REPEAT_FROM:
				return task.getRepeatFrom();
			case STATUS:
				return task.getStatus();
			case LENGTH:
				return task.getLength();
			case TIMER:
				return task.getTimer();
			case PRIORITY:
				return task.getPriority();
			case STAR:
				return task.isStar();
			case NOTE:
				return task.getNote();
			case IMPORTANCE:
				return TaskUtils.getImportance(task);
			default:
				return null;
		}
	}
	
	@Override
	public void setProperty(Task task, Object value) {
		if (task == null)
			return;
		
		switch (this) {
			case MODEL:
				break;
			case MODEL_EDIT:
				break;
			case MODEL_CREATION_DATE:
				break;
			case MODEL_UPDATE_DATE:
				break;
			case SHOW_CHILDREN:
				((GuiTask) task).setShowChildren((Boolean) value);
				break;
			case TITLE:
				task.setTitle((String) value);
				break;
			case ORDER:
				task.setOrder((Integer) value);
				break;
			case TAGS:
				task.setTags(TagList.fromString((String) value));
				break;
			case CONTACTS:
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
			case PROGRESS:
				task.setProgress((Double) value);
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
			case DUE_DATE_REMINDER:
				if (value == null || !(value instanceof Integer))
					task.setDueDateReminder(0);
				else
					task.setDueDateReminder((Integer) value);
				break;
			case START_DATE_REMINDER:
				if (value == null || !(value instanceof Integer))
					task.setStartDateReminder(0);
				else
					task.setStartDateReminder((Integer) value);
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
				if (value == null || !(value instanceof Integer))
					task.setLength(0);
				else
					task.setLength((Integer) value);
				break;
			case TIMER:
				task.setTimer((Timer) value);
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
			case IMPORTANCE:
				break;
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
