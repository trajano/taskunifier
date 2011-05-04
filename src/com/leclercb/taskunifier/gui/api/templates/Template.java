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
package com.leclercb.taskunifier.gui.api.templates;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsBuilder;
import com.leclercb.commons.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;

public class Template implements Serializable, Cloneable, PropertyChangeSupported {
	
	public static final String PROP_TITLE = "title";
	
	public static final String PROP_TASK_TITLE = "taskTitle";
	public static final String PROP_TASK_TAGS = "taskTags";
	public static final String PROP_TASK_FOLDER = "taskFolder";
	public static final String PROP_TASK_CONTEXT = "taskContext";
	public static final String PROP_TASK_GOAL = "taskGoal";
	public static final String PROP_TASK_LOCATION = "taskLocation";
	public static final String PROP_TASK_COMPLETED = "taskCompleted";
	public static final String PROP_TASK_DUE_DATE = "taskDueDate";
	public static final String PROP_TASK_DUE_TIME = "taskDueTime";
	public static final String PROP_TASK_START_DATE = "taskStartDate";
	public static final String PROP_TASK_START_TIME = "taskStartTime";
	public static final String PROP_TASK_REMINDER = "taskReminder";
	public static final String PROP_TASK_REPEAT = "taskRepeat";
	public static final String PROP_TASK_REPEAT_FROM = "taskRepeatFrom";
	public static final String PROP_TASK_STATUS = "taskStatus";
	public static final String PROP_TASK_LENGTH = "taskLength";
	public static final String PROP_TASK_PRIORITY = "taskPriority";
	public static final String PROP_TASK_STAR = "taskStar";
	public static final String PROP_TASK_NOTE = "taskNote";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private String id;
	private String title;
	
	private String taskTitle;
	private String taskTags;
	private ModelId taskFolder;
	private ModelId taskContext;
	private ModelId taskGoal;
	private ModelId taskLocation;
	private Boolean taskCompleted;
	private Integer taskDueDate;
	private Integer taskDueTime;
	private Integer taskStartDate;
	private Integer taskStartTime;
	private Integer taskReminder;
	private String taskRepeat;
	private TaskRepeatFrom taskRepeatFrom;
	private TaskStatus taskStatus;
	private Integer taskLength;
	private TaskPriority taskPriority;
	private Boolean taskStar;
	private String taskNote;
	
	public Template(String title) {
		this(UUID.randomUUID().toString(), title);
	}
	
	public Template(String id, String title) {
		CheckUtils.isNotNull(id, "Id cannot be null");
		
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setId(id);
		this.setTitle(title);
		
		this.setTaskTitle(null);
		this.setTaskTags(null);
		this.setTaskFolder(null);
		this.setTaskContext(null);
		this.setTaskGoal(null);
		this.setTaskLocation(null);
		this.setTaskCompleted(null);
		this.setTaskDueDate(null);
		this.setTaskDueTime(null);
		this.setTaskStartDate(null);
		this.setTaskStartTime(null);
		this.setTaskReminder(null);
		this.setTaskRepeat(null);
		this.setTaskRepeatFrom(null);
		this.setTaskStatus(null);
		this.setTaskLength(null);
		this.setTaskPriority(null);
		this.setTaskStar(null);
		this.setTaskNote(null);
	}
	
	public void applyToTask(Task task) {
		if (this.taskTitle != null && this.taskTitle.length() != 0)
			task.setTitle(this.taskTitle);
		
		if (this.taskTags != null && this.taskTags.length() != 0)
			task.setTags(this.taskTags.split(","));
		
		if (this.taskFolder != null)
			task.setFolder(FolderFactory.getInstance().get(this.taskFolder));
		
		if (this.taskContext != null)
			task.setContext(ContextFactory.getInstance().get(this.taskContext));
		
		if (this.taskGoal != null)
			task.setGoal(GoalFactory.getInstance().get(this.taskGoal));
		
		if (this.taskLocation != null)
			task.setLocation(LocationFactory.getInstance().get(
					this.taskLocation));
		
		if (this.taskCompleted != null)
			task.setCompleted(this.taskCompleted);
		
		if (this.taskDueDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, this.taskDueDate);
			
			if (this.taskDueTime != null) {
				int hour = this.taskDueTime / 60;
				int minute = this.taskDueTime % 60;
				
				calendar.set(Calendar.HOUR_OF_DAY, hour);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, 0);
			}
			
			task.setDueDate(calendar);
		}
		
		if (this.taskStartDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, this.taskStartDate);
			
			if (this.taskStartTime != null) {
				int hour = this.taskStartTime / 60;
				int minute = this.taskStartTime % 60;
				
				calendar.set(Calendar.HOUR_OF_DAY, hour);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, 0);
			}
			
			task.setStartDate(calendar);
		}
		
		if (this.taskReminder != null)
			task.setReminder(this.taskReminder);
		
		if (this.taskRepeat != null && this.taskRepeat.length() != 0)
			task.setRepeat(this.taskRepeat);
		
		if (this.taskRepeatFrom != null)
			task.setRepeatFrom(this.taskRepeatFrom);
		
		if (this.taskStatus != null)
			task.setStatus(this.taskStatus);
		
		if (this.taskLength != null)
			task.setLength(this.taskLength);
		
		if (this.taskPriority != null)
			task.setPriority(this.taskPriority);
		
		if (this.taskStar != null)
			task.setStar(this.taskStar);
		
		if (this.taskNote != null && this.taskNote.length() != 0)
			task.setNote(this.taskNote);
	}
	
	@Override
	public Template clone() {
		Template template = new Template(this.title);
		
		template.setTaskTitle(this.taskTitle);
		template.setTaskTags(this.taskTags);
		template.setTaskFolder(this.taskFolder);
		template.setTaskContext(this.taskContext);
		template.setTaskGoal(this.taskGoal);
		template.setTaskLocation(this.taskLocation);
		template.setTaskCompleted(this.taskCompleted);
		template.setTaskDueDate(this.taskDueDate);
		template.setTaskDueTime(this.taskDueTime);
		template.setTaskStartDate(this.taskStartDate);
		template.setTaskStartTime(this.taskStartTime);
		template.setTaskReminder(this.taskReminder);
		template.setTaskRepeat(this.taskRepeat);
		template.setTaskRepeatFrom(this.taskRepeatFrom);
		template.setTaskStatus(this.taskStatus);
		template.setTaskLength(this.taskLength);
		template.setTaskPriority(this.taskPriority);
		template.setTaskStar(this.taskStar);
		template.setTaskNote(this.taskNote);
		
		return template;
	}
	
	public String getId() {
		return this.id;
	}
	
	private void setId(String id) {
		CheckUtils.isNotNull(id, "ID cannot be null");
		this.id = id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		CheckUtils.isNotNull(title, "Title cannot be null");
		String oldTitle = this.title;
		this.title = title;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TITLE,
				oldTitle,
				title);
	}
	
	public String getTaskTitle() {
		return this.taskTitle;
	}
	
	public void setTaskTitle(String taskTitle) {
		String oldTaskTitle = this.taskTitle;
		this.taskTitle = taskTitle;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_TITLE,
				oldTaskTitle,
				taskTitle);
	}
	
	public String getTaskTags() {
		return this.taskTags;
	}
	
	public void setTaskTags(String taskTags) {
		String oldTaskTags = this.taskTags;
		this.taskTags = taskTags;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_TAGS,
				oldTaskTags,
				taskTags);
	}
	
	public ModelId getTaskFolder() {
		return this.taskFolder;
	}
	
	public void setTaskFolder(ModelId taskFolder) {
		ModelId oldTaskFolder = this.taskFolder;
		this.taskFolder = taskFolder;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_FOLDER,
				oldTaskFolder,
				taskFolder);
	}
	
	public ModelId getTaskContext() {
		return this.taskContext;
	}
	
	public void setTaskContext(ModelId taskContext) {
		ModelId oldTaskContext = this.taskContext;
		this.taskContext = taskContext;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_CONTEXT,
				oldTaskContext,
				taskContext);
	}
	
	public ModelId getTaskGoal() {
		return this.taskGoal;
	}
	
	public void setTaskGoal(ModelId taskGoal) {
		ModelId oldTaskGoal = this.taskGoal;
		this.taskGoal = taskGoal;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_GOAL,
				oldTaskGoal,
				taskGoal);
	}
	
	public ModelId getTaskLocation() {
		return this.taskLocation;
	}
	
	public void setTaskLocation(ModelId taskLocation) {
		ModelId oldTaskLocation = this.taskLocation;
		this.taskLocation = taskLocation;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_LOCATION,
				oldTaskLocation,
				taskLocation);
	}
	
	public Boolean getTaskCompleted() {
		return this.taskCompleted;
	}
	
	public void setTaskCompleted(Boolean taskCompleted) {
		Boolean oldTaskCompleted = this.taskCompleted;
		this.taskCompleted = taskCompleted;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_COMPLETED,
				oldTaskCompleted,
				taskCompleted);
	}
	
	public Integer getTaskDueDate() {
		return this.taskDueDate;
	}
	
	public void setTaskDueDate(Integer taskDueDate) {
		Integer oldTaskDueDate = this.taskDueDate;
		this.taskDueDate = taskDueDate;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_DUE_DATE,
				oldTaskDueDate,
				taskDueDate);
	}
	
	public Integer getTaskDueTime() {
		return this.taskDueTime;
	}
	
	public void setTaskDueTime(Integer taskDueTime) {
		Integer oldTaskDueTime = this.taskDueTime;
		this.taskDueTime = taskDueTime;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_DUE_TIME,
				oldTaskDueTime,
				taskDueTime);
	}
	
	public Integer getTaskStartDate() {
		return this.taskStartDate;
	}
	
	public void setTaskStartDate(Integer taskStartDate) {
		Integer oldTaskStartDate = this.taskStartDate;
		this.taskStartDate = taskStartDate;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_START_DATE,
				oldTaskStartDate,
				taskStartDate);
	}
	
	public Integer getTaskStartTime() {
		return this.taskStartTime;
	}
	
	public void setTaskStartTime(Integer taskStartTime) {
		Integer oldTaskStartTime = this.taskStartTime;
		this.taskStartTime = taskStartTime;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_START_TIME,
				oldTaskStartTime,
				taskStartTime);
	}
	
	public Integer getTaskReminder() {
		return this.taskReminder;
	}
	
	public void setTaskReminder(Integer taskReminder) {
		Integer oldTaskReminder = this.taskReminder;
		this.taskReminder = taskReminder;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_REMINDER,
				oldTaskReminder,
				taskReminder);
	}
	
	public String getTaskRepeat() {
		return this.taskRepeat;
	}
	
	public void setTaskRepeat(String taskRepeat) {
		String oldTaskRepeat = this.taskRepeat;
		this.taskRepeat = taskRepeat;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_REPEAT,
				oldTaskRepeat,
				taskRepeat);
	}
	
	public TaskRepeatFrom getTaskRepeatFrom() {
		return this.taskRepeatFrom;
	}
	
	public void setTaskRepeatFrom(TaskRepeatFrom taskRepeatFrom) {
		TaskRepeatFrom oldTaskRepeatFrom = this.taskRepeatFrom;
		this.taskRepeatFrom = taskRepeatFrom;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_REPEAT_FROM,
				oldTaskRepeatFrom,
				taskRepeatFrom);
	}
	
	public TaskStatus getTaskStatus() {
		return this.taskStatus;
	}
	
	public void setTaskStatus(TaskStatus taskStatus) {
		TaskStatus oldTaskStatus = this.taskStatus;
		this.taskStatus = taskStatus;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_STATUS,
				oldTaskStatus,
				taskStatus);
	}
	
	public Integer getTaskLength() {
		return this.taskLength;
	}
	
	public void setTaskLength(Integer taskLength) {
		Integer oldTaskLength = this.taskLength;
		this.taskLength = taskLength;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_LENGTH,
				oldTaskLength,
				taskLength);
	}
	
	public TaskPriority getTaskPriority() {
		return this.taskPriority;
	}
	
	public void setTaskPriority(TaskPriority taskPriority) {
		TaskPriority oldTaskPriority = this.taskPriority;
		this.taskPriority = taskPriority;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_PRIORITY,
				oldTaskPriority,
				taskPriority);
	}
	
	public Boolean getTaskStar() {
		return this.taskStar;
	}
	
	public void setTaskStar(Boolean taskStar) {
		Boolean oldTaskStar = this.taskStar;
		this.taskStar = taskStar;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_STAR,
				oldTaskStar,
				taskStar);
	}
	
	public String getTaskNote() {
		return this.taskNote;
	}
	
	public void setTaskNote(String taskNote) {
		String oldTaskNote = this.taskNote;
		this.taskNote = taskNote;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TASK_NOTE,
				oldTaskNote,
				taskNote);
	}
	
	@Override
	public String toString() {
		return this.title;
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof Template) {
			Template template = (Template) o;
			
			return new EqualsBuilder().append(this.id, template.id).isEqual();
		}
		
		return false;
	}
	
	@Override
	public final int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.id);
		
		return hashCode.toHashCode();
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
