package com.leclercb.taskunifier.gui.template;

import java.io.Serializable;
import java.util.UUID;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsBuilder;
import com.leclercb.commons.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;

public class Template implements Serializable, Cloneable {
	
	public static final String PROP_TITLE = "title";
	public static final String PROP_ICON = "icon";
	public static final String PROP_FILTER = "filter";
	public static final String PROP_SORTER = "sorter";
	
	private String id;
	private String title;
	
	private String taskTitle;
	private String[] taskTags;
	private ModelId taskFolder;
	private ModelId taskContext;
	private ModelId taskGoal;
	private ModelId taskLocation;
	private ModelId taskParent;
	private Boolean taskCompleted;
	private Integer taskCompletedOn;
	private Integer taskDueDate;
	private Integer taskStartDate;
	private Integer taskReminder;
	private String taskRepeat;
	private TaskRepeatFrom taskRepeatFrom;
	private TaskStatus taskStatus;
	private Integer taskLength;
	private TaskPriority taskPriority;
	private Boolean taskStar;
	private String taskNote;
	
	public Template(String title) {
		this.setId(UUID.randomUUID().toString());
		
		this.setTitle(title);
	}
	
	@Override
	public Template clone() {
		Template template = new Template(this.title);
		
		template.setTaskTitle(taskTitle);
		template.setTaskTags(taskTags);
		template.setTaskFolder(taskFolder);
		template.setTaskContext(taskContext);
		template.setTaskGoal(taskGoal);
		template.setTaskLocation(taskLocation);
		template.setTaskParent(taskParent);
		template.setTaskCompleted(taskCompleted);
		template.setTaskCompletedOn(taskCompletedOn);
		template.setTaskDueDate(taskDueDate);
		template.setTaskStartDate(taskStartDate);
		template.setTaskReminder(taskReminder);
		template.setTaskRepeat(taskRepeat);
		template.setTaskRepeatFrom(taskRepeatFrom);
		template.setTaskStatus(taskStatus);
		template.setTaskLength(taskLength);
		template.setTaskPriority(taskPriority);
		template.setTaskStar(taskStar);
		template.setTaskNote(taskNote);
		
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
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTaskTitle() {
		return taskTitle;
	}
	
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	
	public String[] getTaskTags() {
		return taskTags;
	}
	
	public void setTaskTags(String[] taskTags) {
		this.taskTags = taskTags;
	}
	
	public ModelId getTaskFolder() {
		return taskFolder;
	}
	
	public void setTaskFolder(ModelId taskFolder) {
		this.taskFolder = taskFolder;
	}
	
	public ModelId getTaskContext() {
		return taskContext;
	}
	
	public void setTaskContext(ModelId taskContext) {
		this.taskContext = taskContext;
	}
	
	public ModelId getTaskGoal() {
		return taskGoal;
	}
	
	public void setTaskGoal(ModelId taskGoal) {
		this.taskGoal = taskGoal;
	}
	
	public ModelId getTaskLocation() {
		return taskLocation;
	}
	
	public void setTaskLocation(ModelId taskLocation) {
		this.taskLocation = taskLocation;
	}
	
	public ModelId getTaskParent() {
		return taskParent;
	}
	
	public void setTaskParent(ModelId taskParent) {
		this.taskParent = taskParent;
	}
	
	public Boolean getTaskCompleted() {
		return taskCompleted;
	}
	
	public void setTaskCompleted(Boolean taskCompleted) {
		this.taskCompleted = taskCompleted;
	}
	
	public Integer getTaskCompletedOn() {
		return taskCompletedOn;
	}
	
	public void setTaskCompletedOn(Integer taskCompletedOn) {
		this.taskCompletedOn = taskCompletedOn;
	}
	
	public Integer getTaskDueDate() {
		return taskDueDate;
	}
	
	public void setTaskDueDate(Integer taskDueDate) {
		this.taskDueDate = taskDueDate;
	}
	
	public Integer getTaskStartDate() {
		return taskStartDate;
	}
	
	public void setTaskStartDate(Integer taskStartDate) {
		this.taskStartDate = taskStartDate;
	}
	
	public Integer getTaskReminder() {
		return taskReminder;
	}
	
	public void setTaskReminder(Integer taskReminder) {
		this.taskReminder = taskReminder;
	}
	
	public String getTaskRepeat() {
		return taskRepeat;
	}
	
	public void setTaskRepeat(String taskRepeat) {
		this.taskRepeat = taskRepeat;
	}
	
	public TaskRepeatFrom getTaskRepeatFrom() {
		return taskRepeatFrom;
	}
	
	public void setTaskRepeatFrom(TaskRepeatFrom taskRepeatFrom) {
		this.taskRepeatFrom = taskRepeatFrom;
	}
	
	public TaskStatus getTaskStatus() {
		return taskStatus;
	}
	
	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	public Integer getTaskLength() {
		return taskLength;
	}
	
	public void setTaskLength(Integer taskLength) {
		this.taskLength = taskLength;
	}
	
	public TaskPriority getTaskPriority() {
		return taskPriority;
	}
	
	public void setTaskPriority(TaskPriority taskPriority) {
		this.taskPriority = taskPriority;
	}
	
	public Boolean getTaskStar() {
		return taskStar;
	}
	
	public void setTaskStar(Boolean taskStar) {
		this.taskStar = taskStar;
	}
	
	public String getTaskNote() {
		return taskNote;
	}
	
	public void setTaskNote(String taskNote) {
		this.taskNote = taskNote;
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
	
}
