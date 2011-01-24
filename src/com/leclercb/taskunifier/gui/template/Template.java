package com.leclercb.taskunifier.gui.template;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

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
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;

public class Template implements Serializable, Cloneable, PropertyChangeSupported {
	
	public static final String PROP_TITLE = "title";
	
	private PropertyChangeSupport propertyChangeSupport;
	
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
	
	Template(String title) {
		this(UUID.randomUUID().toString(), title);
	}
	
	Template(String id, String title) {
		CheckUtils.isNotNull(id, "Id cannot be null");
		
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setId(id);
		this.setTitle(title);
	}
	
	public void applyToTask(Task task) {
		if (this.taskTitle != null)
			task.setTitle(this.taskTitle);
		else
			task.setTitle("");
		
		if (this.taskTags != null)
			task.setTags(this.taskTags);
		else
			task.setTags(new String[0]);
		
		if (this.taskFolder != null)
			task.setFolder(FolderFactory.getInstance().get(this.taskFolder));
		else
			task.setFolder(null);
		
		if (this.taskContext != null)
			task.setContext(ContextFactory.getInstance().get(this.taskContext));
		else
			task.setContext(null);
		
		if (this.taskGoal != null)
			task.setGoal(GoalFactory.getInstance().get(this.taskGoal));
		else
			task.setGoal(null);
		
		if (this.taskLocation != null)
			task.setLocation(LocationFactory.getInstance().get(
					this.taskLocation));
		else
			task.setLocation(null);
		
		if (this.taskParent != null)
			task.setParent(TaskFactory.getInstance().get(this.taskParent));
		else
			task.setParent(null);
		
		if (this.taskCompleted != null)
			task.setCompleted(this.taskCompleted);
		else
			task.setCompleted(false);
		
		if (this.taskDueDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, this.taskDueDate);
			
			task.setDueDate(calendar);
		} else {
			task.setDueDate(null);
		}
		
		if (this.taskStartDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, this.taskStartDate);
			
			task.setStartDate(calendar);
		} else {
			task.setStartDate(null);
		}
		
		if (this.taskReminder != null)
			task.setReminder(this.taskReminder);
		else
			task.setReminder(0);
		
		if (this.taskRepeat != null)
			task.setRepeat(this.taskRepeat);
		else
			task.setRepeat(null);
		
		if (this.taskRepeatFrom != null)
			task.setRepeatFrom(this.taskRepeatFrom);
		
		if (this.taskStatus != null)
			task.setStatus(this.taskStatus);
		
		if (this.taskLength != null)
			task.setLength(this.taskLength);
		else
			task.setLength(0);
		
		if (this.taskPriority != null)
			task.setPriority(this.taskPriority);
		
		if (this.taskStar != null)
			task.setStar(this.taskStar);
		else
			task.setStar(false);
		
		if (this.taskNote != null)
			task.setNote(this.taskNote);
		else
			task.setNote(null);
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
		template.setTaskParent(this.taskParent);
		template.setTaskCompleted(this.taskCompleted);
		template.setTaskDueDate(this.taskDueDate);
		template.setTaskStartDate(this.taskStartDate);
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
		this.taskTitle = taskTitle;
	}
	
	public String[] getTaskTags() {
		return this.taskTags;
	}
	
	public void setTaskTags(String[] taskTags) {
		this.taskTags = taskTags;
	}
	
	public ModelId getTaskFolder() {
		return this.taskFolder;
	}
	
	public void setTaskFolder(ModelId taskFolder) {
		this.taskFolder = taskFolder;
	}
	
	public ModelId getTaskContext() {
		return this.taskContext;
	}
	
	public void setTaskContext(ModelId taskContext) {
		this.taskContext = taskContext;
	}
	
	public ModelId getTaskGoal() {
		return this.taskGoal;
	}
	
	public void setTaskGoal(ModelId taskGoal) {
		this.taskGoal = taskGoal;
	}
	
	public ModelId getTaskLocation() {
		return this.taskLocation;
	}
	
	public void setTaskLocation(ModelId taskLocation) {
		this.taskLocation = taskLocation;
	}
	
	public ModelId getTaskParent() {
		return this.taskParent;
	}
	
	public void setTaskParent(ModelId taskParent) {
		this.taskParent = taskParent;
	}
	
	public Boolean getTaskCompleted() {
		return this.taskCompleted;
	}
	
	public void setTaskCompleted(Boolean taskCompleted) {
		this.taskCompleted = taskCompleted;
	}
	
	public Integer getTaskDueDate() {
		return this.taskDueDate;
	}
	
	public void setTaskDueDate(Integer taskDueDate) {
		this.taskDueDate = taskDueDate;
	}
	
	public Integer getTaskStartDate() {
		return this.taskStartDate;
	}
	
	public void setTaskStartDate(Integer taskStartDate) {
		this.taskStartDate = taskStartDate;
	}
	
	public Integer getTaskReminder() {
		return this.taskReminder;
	}
	
	public void setTaskReminder(Integer taskReminder) {
		this.taskReminder = taskReminder;
	}
	
	public String getTaskRepeat() {
		return this.taskRepeat;
	}
	
	public void setTaskRepeat(String taskRepeat) {
		this.taskRepeat = taskRepeat;
	}
	
	public TaskRepeatFrom getTaskRepeatFrom() {
		return this.taskRepeatFrom;
	}
	
	public void setTaskRepeatFrom(TaskRepeatFrom taskRepeatFrom) {
		this.taskRepeatFrom = taskRepeatFrom;
	}
	
	public TaskStatus getTaskStatus() {
		return this.taskStatus;
	}
	
	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	public Integer getTaskLength() {
		return this.taskLength;
	}
	
	public void setTaskLength(Integer taskLength) {
		this.taskLength = taskLength;
	}
	
	public TaskPriority getTaskPriority() {
		return this.taskPriority;
	}
	
	public void setTaskPriority(TaskPriority taskPriority) {
		this.taskPriority = taskPriority;
	}
	
	public Boolean getTaskStar() {
		return this.taskStar;
	}
	
	public void setTaskStar(Boolean taskStar) {
		this.taskStar = taskStar;
	}
	
	public String getTaskNote() {
		return this.taskNote;
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
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
