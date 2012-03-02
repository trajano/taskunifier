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
package com.leclercb.taskunifier.api.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.ContactGroup.ContactItem;
import com.leclercb.taskunifier.api.models.FileGroup.FileItem;
import com.leclercb.taskunifier.api.models.TaskGroup.TaskItem;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;

public class Task extends AbstractModelParent<Task> implements ModelNote, PropertyChangeListener, ListChangeListener {
	
	public static final String PROP_TAGS = "tags";
	public static final String PROP_FOLDER = "folder";
	public static final String PROP_CONTEXT = "context";
	public static final String PROP_GOAL = "goal";
	public static final String PROP_LOCATION = "location";
	public static final String PROP_PROGRESS = "progress";
	public static final String PROP_COMPLETED = "completed";
	public static final String PROP_COMPLETED_ON = "completedOn";
	public static final String PROP_START_DATE = "startDate";
	public static final String PROP_START_DATE_REMINDER = "startDateReminder";
	public static final String PROP_DUE_DATE = "dueDate";
	public static final String PROP_DUE_DATE_REMINDER = "dueDateReminder";
	public static final String PROP_REPEAT = "repeat";
	public static final String PROP_REPEAT_FROM = "repeatFrom";
	public static final String PROP_STATUS = "status";
	public static final String PROP_LENGTH = "length";
	public static final String PROP_TIMER = "timer";
	public static final String PROP_PRIORITY = "priority";
	public static final String PROP_STAR = "star";
	public static final String PROP_CONTACTS = "contacts";
	public static final String PROP_TASKS = "tasks";
	public static final String PROP_FILES = "files";
	
	private TagList tags;
	private Folder folder;
	private Context context;
	private Goal goal;
	private Location location;
	private double progress;
	private boolean completed;
	private Calendar completedOn;
	private Calendar startDate;
	private int startDateReminder;
	private Calendar dueDate;
	private int dueDateReminder;
	private String repeat;
	private TaskRepeatFrom repeatFrom;
	private TaskStatus status;
	private int length;
	private Timer timer;
	private TaskPriority priority;
	private boolean star;
	private String note;
	private ContactGroup contacts;
	private TaskGroup tasks;
	private FileGroup files;
	
	protected Task(TaskBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}
	
	protected Task(String title) {
		this(new ModelId(), title);
	}
	
	protected Task(ModelId modelId, String title) {
		super(modelId, title);
		
		this.tags = new TagList();
		
		this.contacts = new ContactGroup();
		this.contacts.addListChangeListener(this);
		this.contacts.addPropertyChangeListener(this);
		
		this.tasks = new TaskGroup();
		this.tasks.addListChangeListener(this);
		this.tasks.addPropertyChangeListener(this);
		
		this.files = new FileGroup();
		this.files.addListChangeListener(this);
		this.files.addPropertyChangeListener(this);
		
		this.setTags(new TagList());
		this.setFolder(null);
		this.setContext(null);
		this.setGoal(null);
		this.setLocation(null);
		this.setProgress(0);
		this.setCompleted(false);
		this.setCompletedOn(null);
		this.setStartDate(null);
		this.setStartDateReminder(0);
		this.setDueDate(null);
		this.setDueDateReminder(0);
		this.setRepeat(null);
		this.setRepeatFrom(TaskRepeatFrom.DUE_DATE);
		this.setStatus(TaskStatus.NONE);
		this.setLength(0);
		this.setTimer(new Timer());
		this.setPriority(TaskPriority.LOW);
		this.setStar(false);
		this.setNote(null);
		this.setContacts(new ContactGroup());
		this.setTasks(new TaskGroup());
		this.setFiles(new FileGroup());
		
		this.getFactory().register(this);
	}
	
	@Override
	public Task clone(ModelId modelId) {
		Task task = this.getFactory().create(modelId, this.getTitle());
		
		task.setTags(this.getTags());
		task.setFolder(this.getFolder());
		task.setContext(this.getContext());
		task.setGoal(this.getGoal());
		task.setLocation(this.getLocation());
		task.setParent(this.getParent());
		task.setProgress(this.getProgress());
		task.setCompleted(this.isCompleted());
		task.setCompletedOn(this.getCompletedOn());
		task.setStartDate(this.getStartDate());
		task.setStartDateReminder(this.getStartDateReminder());
		task.setDueDate(this.getDueDate());
		task.setDueDateReminder(this.getDueDateReminder());
		task.setRepeat(this.getRepeat());
		task.setRepeatFrom(this.getRepeatFrom());
		task.setStatus(this.getStatus());
		task.setLength(this.getLength());
		task.setTimer(this.getTimer());
		task.setPriority(this.getPriority());
		task.setStar(this.isStar());
		task.setNote(this.getNote());
		task.setContacts(this.getContacts());
		task.setTasks(this.getTasks());
		task.setFiles(this.getFiles());
		
		// After all other setXxx methods
		task.setOrder(this.getOrder());
		task.addProperties(this.getProperties());
		task.setModelStatus(this.getModelStatus());
		task.setModelCreationDate(Calendar.getInstance());
		task.setModelUpdateDate(Calendar.getInstance());
		
		return task;
	}
	
	@Override
	public TaskFactory<Task, TaskBean> getFactory() {
		return TaskFactory.getInstance();
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.TASK;
	}
	
	@Override
	public void loadBean(ModelBean b, boolean loadReferenceIds) {
		CheckUtils.isNotNull(b);
		CheckUtils.isInstanceOf(b, TaskBean.class);
		
		TaskBean bean = (TaskBean) b;
		
		Context context = null;
		
		if (bean.getContext() != null) {
			context = ContextFactory.getInstance().get(bean.getContext());
			if (context == null)
				context = ContextFactory.getInstance().createShell(
						bean.getContext());
		}
		
		Folder folder = null;
		
		if (bean.getFolder() != null) {
			folder = FolderFactory.getInstance().get(bean.getFolder());
			if (folder == null)
				folder = FolderFactory.getInstance().createShell(
						bean.getFolder());
		}
		
		Goal goal = null;
		
		if (bean.getGoal() != null) {
			goal = GoalFactory.getInstance().get(bean.getGoal());
			if (goal == null)
				goal = GoalFactory.getInstance().createShell(bean.getGoal());
		}
		
		Location location = null;
		
		if (bean.getLocation() != null) {
			location = LocationFactory.getInstance().get(bean.getLocation());
			if (location == null)
				location = LocationFactory.getInstance().createShell(
						bean.getLocation());
		}
		
		this.setTags(bean.getTags());
		this.setFolder(folder);
		this.setContext(context);
		this.setGoal(goal);
		this.setLocation(location);
		this.setProgress(bean.getProgress());
		this.setStartDate(bean.getStartDate());
		this.setStartDateReminder(bean.getStartDateReminder());
		this.setDueDate(bean.getDueDate());
		this.setDueDateReminder(bean.getDueDateReminder());
		this.setRepeat(bean.getRepeat());
		this.setRepeatFrom(bean.getRepeatFrom());
		this.setStatus(bean.getStatus());
		this.setLength(bean.getLength());
		this.setTimer(bean.getTimer());
		this.setPriority(bean.getPriority());
		this.setStar(bean.isStar());
		this.setNote(bean.getNote());
		this.setContacts((bean.getContacts() == null ? null : bean.getContacts().toContactGroup()));
		this.setTasks((bean.getTasks() == null ? null : bean.getTasks().toTaskGroup()));
		this.setFiles((bean.getFiles() == null ? null : bean.getFiles().toFileGroup()));
		
		// Set completed at the end (repeat)
		this.setCompleted(bean.isCompleted());
		
		if (bean.isCompleted() && bean.getCompletedOn() != null)
			this.setCompletedOn(bean.getCompletedOn());
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public TaskBean toBean() {
		TaskBean bean = (TaskBean) super.toBean();
		
		bean.setTags(this.getTags());
		bean.setFolder(this.getFolder() == null ? null : this.getFolder().getModelId());
		bean.setContext(this.getContext() == null ? null : this.getContext().getModelId());
		bean.setGoal(this.getGoal() == null ? null : this.getGoal().getModelId());
		bean.setLocation(this.getLocation() == null ? null : this.getLocation().getModelId());
		bean.setProgress(this.getProgress());
		bean.setStartDate(this.getStartDate());
		bean.setStartDateReminder(this.getStartDateReminder());
		bean.setDueDate(this.getDueDate());
		bean.setDueDateReminder(this.getDueDateReminder());
		bean.setRepeat(this.getRepeat());
		bean.setRepeatFrom(this.getRepeatFrom());
		bean.setStatus(this.getStatus());
		bean.setLength(this.getLength());
		bean.setTimer(this.getTimer());
		bean.setPriority(this.getPriority());
		bean.setStar(this.isStar());
		bean.setNote(this.getNote());
		bean.setContacts(this.getContacts().toContactGroupBean());
		bean.setTasks(this.getTasks().toTaskGroupBean());
		bean.setFiles(this.getFiles().toFileGroupBean());
		
		// Set completed at the end (repeat)
		bean.setCompleted(this.isCompleted());
		bean.setCompletedOn(this.getCompletedOn());
		
		return bean;
	}
	
	public TagList getTags() {
		return this.tags.clone();
	}
	
	public void setTags(TagList tags) {
		CheckUtils.isNotNull(tags);
		
		if (!this.checkBeforeSet(this.getTags(), tags))
			return;
		
		TagList oldTags = this.tags;
		this.tags = tags.clone();
		this.updateProperty(PROP_TAGS, oldTags, tags);
	}
	
	public Folder getFolder() {
		return this.folder;
	}
	
	public void setFolder(Folder folder) {
		if (!this.checkBeforeSet(this.getFolder(), folder))
			return;
		
		if (folder != null) {
			if (folder.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| folder.getModelStatus().equals(ModelStatus.DELETED)) {
				ApiLogger.getLogger().severe(
						"You cannot assign a deleted model");
				folder = null;
			}
		}
		
		if (this.folder != null)
			this.folder.removePropertyChangeListener(this);
		
		Folder oldFolder = this.folder;
		this.folder = folder;
		
		if (this.folder != null)
			this.folder.addPropertyChangeListener(this);
		
		this.updateProperty(PROP_FOLDER, oldFolder, folder);
	}
	
	public Context getContext() {
		return this.context;
	}
	
	public void setContext(Context context) {
		if (!this.checkBeforeSet(this.getContext(), context))
			return;
		
		if (context != null) {
			if (context.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| context.getModelStatus().equals(ModelStatus.DELETED)) {
				ApiLogger.getLogger().severe(
						"You cannot assign a deleted model");
				context = null;
			}
		}
		
		if (this.context != null)
			this.context.removePropertyChangeListener(this);
		
		Context oldContext = this.context;
		this.context = context;
		
		if (this.context != null)
			this.context.addPropertyChangeListener(this);
		
		this.updateProperty(PROP_CONTEXT, oldContext, context);
	}
	
	public Goal getGoal() {
		return this.goal;
	}
	
	public void setGoal(Goal goal) {
		if (!this.checkBeforeSet(this.getGoal(), goal))
			return;
		
		if (goal != null) {
			if (goal.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| goal.getModelStatus().equals(ModelStatus.DELETED)) {
				ApiLogger.getLogger().severe(
						"You cannot assign a deleted model");
				goal = null;
			}
		}
		
		if (this.goal != null)
			this.goal.removePropertyChangeListener(this);
		
		Goal oldGoal = this.goal;
		this.goal = goal;
		
		if (this.goal != null)
			this.goal.addPropertyChangeListener(this);
		
		this.updateProperty(PROP_GOAL, oldGoal, goal);
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(Location location) {
		if (!this.checkBeforeSet(this.getLocation(), location))
			return;
		
		if (location != null) {
			if (location.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| location.getModelStatus().equals(ModelStatus.DELETED)) {
				ApiLogger.getLogger().severe(
						"You cannot assign a deleted model");
				location = null;
			}
		}
		
		if (this.location != null)
			this.location.removePropertyChangeListener(this);
		
		Location oldLocation = this.location;
		this.location = location;
		
		if (this.location != null)
			this.location.addPropertyChangeListener(this);
		
		this.updateProperty(PROP_LOCATION, oldLocation, location);
	}
	
	public double getProgress() {
		return this.progress;
	}
	
	public void setProgress(double progress) {
		CheckUtils.isPercentage(progress);
		
		if (!this.checkBeforeSet(this.getProgress(), progress))
			return;
		
		double oldProgress = this.progress;
		this.progress = progress;
		
		boolean oldCompleted = this.completed;
		if (this.progress != 1)
			this.completed = false;
		else
			this.completed = true;
		
		Calendar oldCompletedOn = this.completedOn;
		if (this.progress != 1)
			this.completedOn = null;
		else
			this.completedOn = Calendar.getInstance();
		
		this.updateProperty(PROP_COMPLETED, oldCompleted, this.completed);
		this.updateProperty(PROP_COMPLETED_ON, oldCompletedOn, this.completedOn);
		this.updateProperty(PROP_PROGRESS, oldProgress, progress);
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	public void setCompleted(boolean completed) {
		if (!this.checkBeforeSet(this.isCompleted(), completed))
			return;
		
		boolean oldCompleted = this.completed;
		this.completed = completed;
		
		Calendar oldCompletedOn = this.completedOn;
		if (this.completed)
			this.completedOn = Calendar.getInstance();
		else
			this.completedOn = null;
		
		double oldProgress = this.progress;
		if (this.completed)
			this.progress = 1;
		else
			this.progress = 0;
		
		this.updateProperty(PROP_COMPLETED_ON, oldCompletedOn, this.completedOn);
		this.updateProperty(PROP_PROGRESS, oldProgress, this.progress);
		this.updateProperty(PROP_COMPLETED, oldCompleted, completed);
	}
	
	public Calendar getCompletedOn() {
		return DateUtils.cloneCalendar(this.completedOn);
	}
	
	public void setCompletedOn(Calendar completedOn) {
		if (!this.checkBeforeSet(this.getCompletedOn(), completedOn))
			return;
		
		Calendar oldCompletedOn = this.completedOn;
		this.completedOn = DateUtils.cloneCalendar(completedOn);
		
		boolean oldCompleted = this.completed;
		if (this.completedOn == null)
			this.completed = false;
		else
			this.completed = true;
		
		double oldProgress = this.progress;
		if (this.completedOn == null)
			this.progress = 0;
		else
			this.progress = 1;
		
		this.updateProperty(PROP_COMPLETED, oldCompleted, this.completed);
		this.updateProperty(PROP_PROGRESS, oldProgress, this.progress);
		this.updateProperty(PROP_COMPLETED_ON, oldCompletedOn, completedOn);
	}
	
	public boolean isOverDue(boolean dateOnly) {
		if (this.getDueDate() == null)
			return false;
		
		Calendar today = Calendar.getInstance();
		if (today.compareTo(this.getDueDate()) > 0) {
			if (dateOnly) {
				if (!org.apache.commons.lang3.time.DateUtils.isSameDay(
						today,
						this.getDueDate()))
					return true;
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isDueToday(boolean dateOnly) {
		if (this.getDueDate() == null)
			return false;
		
		Calendar today = Calendar.getInstance();
		if (org.apache.commons.lang3.time.DateUtils.isSameDay(
				today,
				this.getDueDate())) {
			if (dateOnly) {
				return true;
			} else {
				if (today.compareTo(this.getDueDate()) <= 0)
					return true;
			}
		}
		
		return false;
	}
	
	public Calendar getStartDate() {
		return DateUtils.cloneCalendar(this.startDate);
	}
	
	public void setStartDate(Calendar startDate) {
		if (!this.checkBeforeSet(this.getStartDate(), startDate))
			return;
		
		Calendar oldStartDate = this.startDate;
		this.startDate = DateUtils.cloneCalendar(startDate);
		this.updateProperty(PROP_START_DATE, oldStartDate, startDate);
	}
	
	public int getStartDateReminder() {
		return this.startDateReminder;
	}
	
	public void setStartDateReminder(int startDateReminder) {
		CheckUtils.isPositive(startDateReminder);
		
		if (!this.checkBeforeSet(this.getStartDateReminder(), startDateReminder))
			return;
		
		int oldStartDateReminder = this.startDateReminder;
		this.startDateReminder = startDateReminder;
		this.updateProperty(
				PROP_START_DATE_REMINDER,
				oldStartDateReminder,
				startDateReminder);
	}
	
	public Calendar getDueDate() {
		return DateUtils.cloneCalendar(this.dueDate);
	}
	
	public void setDueDate(Calendar dueDate) {
		if (!this.checkBeforeSet(this.getDueDate(), dueDate))
			return;
		
		Calendar oldDueDate = this.dueDate;
		this.dueDate = DateUtils.cloneCalendar(dueDate);
		this.updateProperty(PROP_DUE_DATE, oldDueDate, dueDate);
	}
	
	public int getDueDateReminder() {
		return this.dueDateReminder;
	}
	
	public void setDueDateReminder(int dueDateReminder) {
		CheckUtils.isPositive(dueDateReminder);
		
		if (!this.checkBeforeSet(this.getDueDateReminder(), dueDateReminder))
			return;
		
		int oldDueDateReminder = this.dueDateReminder;
		this.dueDateReminder = dueDateReminder;
		this.updateProperty(
				PROP_DUE_DATE_REMINDER,
				oldDueDateReminder,
				dueDateReminder);
	}
	
	public String getRepeat() {
		return this.repeat;
	}
	
	public void setRepeat(String repeat) {
		if (!this.checkBeforeSet(this.getRepeat(), repeat))
			return;
		
		if (repeat != null)
			repeat = repeat.trim();
		
		String oldRepeat = this.repeat;
		this.repeat = repeat;
		this.updateProperty(PROP_REPEAT, oldRepeat, repeat);
	}
	
	public TaskRepeatFrom getRepeatFrom() {
		return this.repeatFrom;
	}
	
	public void setRepeatFrom(TaskRepeatFrom repeatFrom) {
		CheckUtils.isNotNull(repeatFrom);
		
		if (!this.checkBeforeSet(this.getRepeatFrom(), repeatFrom))
			return;
		
		TaskRepeatFrom oldRepeatFrom = this.repeatFrom;
		this.repeatFrom = repeatFrom;
		this.updateProperty(PROP_REPEAT_FROM, oldRepeatFrom, repeatFrom);
	}
	
	public TaskStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(TaskStatus status) {
		CheckUtils.isNotNull(status);
		
		if (!this.checkBeforeSet(this.getStatus(), status))
			return;
		
		TaskStatus oldStatus = this.status;
		this.status = status;
		this.updateProperty(PROP_STATUS, oldStatus, status);
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void setLength(int length) {
		CheckUtils.isPositive(length);
		
		if (!this.checkBeforeSet(this.getLength(), length))
			return;
		
		int oldLength = this.length;
		this.length = length;
		this.updateProperty(PROP_LENGTH, oldLength, length);
	}
	
	public Timer getTimer() {
		return new Timer(this.timer);
	}
	
	public void setTimer(Timer timer) {
		CheckUtils.isNotNull(timer);
		
		if (!this.checkBeforeSet(this.getTimer(), timer))
			return;
		
		Timer oldTimer = this.timer;
		this.timer = new Timer(timer);
		this.updateProperty(PROP_TIMER, oldTimer, timer);
	}
	
	public TaskPriority getPriority() {
		return this.priority;
	}
	
	public void setPriority(TaskPriority priority) {
		CheckUtils.isNotNull(priority);
		
		if (!this.checkBeforeSet(this.getPriority(), priority))
			return;
		
		TaskPriority oldPriority = this.priority;
		this.priority = priority;
		this.updateProperty(PROP_PRIORITY, oldPriority, priority);
	}
	
	public boolean isStar() {
		return this.star;
	}
	
	public void setStar(boolean star) {
		if (!this.checkBeforeSet(this.isStar(), star))
			return;
		
		boolean oldStar = this.star;
		this.star = star;
		this.updateProperty(PROP_STAR, oldStar, star);
	}
	
	@Override
	public String getNote() {
		return this.note;
	}
	
	@Override
	public void setNote(String note) {
		if (!this.checkBeforeSet(this.getNote(), note))
			return;
		
		if (note != null)
			note = note.trim();
		
		String oldNote = this.note;
		this.note = note;
		this.updateProperty(PROP_NOTE, oldNote, note);
	}
	
	public ContactGroup getContacts() {
		return this.contacts;
	}
	
	public void setContacts(ContactGroup contacts) {
		this.contacts.clear();
		
		if (contacts != null)
			this.contacts.addAll(contacts.getList());
	}
	
	public TaskGroup getTasks() {
		return this.tasks;
	}
	
	public void setTasks(TaskGroup tasks) {
		this.tasks.clear();
		
		if (tasks != null)
			this.tasks.addAll(tasks.getList());
	}
	
	public FileGroup getFiles() {
		return this.files;
	}
	
	public void setFiles(FileGroup files) {
		this.files.clear();
		
		if (files != null)
			this.files.addAll(files.getList());
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getValue() instanceof ContactItem) {
			this.updateProperty(PROP_CONTACTS, null, this.contacts);
		}
		
		if (event.getValue() instanceof TaskItem) {
			this.updateProperty(PROP_TASKS, null, this.tasks);
		}
		
		if (event.getValue() instanceof FileItem) {
			this.updateProperty(PROP_FILES, null, this.files);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof ContactItem) {
			this.updateProperty(PROP_CONTACTS, null, this.contacts);
		}
		
		if (event.getSource() instanceof TaskItem) {
			this.updateProperty(PROP_TASKS, null, this.tasks);
		}
		
		if (event.getSource() instanceof FileItem) {
			this.updateProperty(PROP_FILES, null, this.files);
		}
		
		if (event.getSource() instanceof Folder
				&& event.getPropertyName().equals(PROP_MODEL_STATUS)) {
			Folder folder = (Folder) event.getSource();
			
			if (folder.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| folder.getModelStatus().equals(ModelStatus.DELETED))
				this.setFolder(null);
		}
		
		if (event.getSource() instanceof Context
				&& event.getPropertyName().equals(PROP_MODEL_STATUS)) {
			Context context = (Context) event.getSource();
			
			if (context.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| context.getModelStatus().equals(ModelStatus.DELETED))
				this.setContext(null);
		}
		
		if (event.getSource() instanceof Goal
				&& event.getPropertyName().equals(PROP_MODEL_STATUS)) {
			Goal goal = (Goal) event.getSource();
			
			if (goal.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| goal.getModelStatus().equals(ModelStatus.DELETED))
				this.setGoal(null);
		}
		
		if (event.getSource() instanceof Location
				&& event.getPropertyName().equals(PROP_MODEL_STATUS)) {
			Location location = (Location) event.getSource();
			
			if (location.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| location.getModelStatus().equals(ModelStatus.DELETED))
				this.setLocation(null);
		}
	}
	
	@Override
	public String toDetailedString() {
		StringBuffer buffer = new StringBuffer(super.toDetailedString());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		
		buffer.append("Tags: " + this.getTags() + "\n");
		if (this.getFolder() != null)
			buffer.append("Folder: " + this.getFolder() + "\n");
		if (this.getContext() != null)
			buffer.append("Context: " + this.getContext() + "\n");
		if (this.getGoal() != null)
			buffer.append("Goal: " + this.getGoal() + "\n");
		if (this.getLocation() != null)
			buffer.append("Location: " + this.getLocation() + "\n");
		if (this.getParent() != null)
			buffer.append("Parent: " + this.getParent() + "\n");
		buffer.append("Progress: " + this.getProgress() + "\n");
		buffer.append("Completed: " + this.isCompleted() + "\n");
		if (this.getCompletedOn() != null)
			buffer.append("Completed On: "
					+ dateFormat.format(this.getCompletedOn().getTime())
					+ "\n");
		if (this.getStartDate() != null)
			buffer.append("Start Date: "
					+ dateFormat.format(this.getStartDate().getTime())
					+ "\n");
		buffer.append("Start Date Reminder: "
				+ this.getStartDateReminder()
				+ "\n");
		if (this.getDueDate() != null)
			buffer.append("Due Date: "
					+ dateFormat.format(this.getDueDate().getTime())
					+ "\n");
		buffer.append("Due Date Reminder: " + this.getDueDateReminder() + "\n");
		buffer.append("Repeat: " + this.getRepeat() + "\n");
		if (this.getRepeatFrom() != null)
			buffer.append("Repeat From: " + this.getRepeatFrom() + "\n");
		buffer.append("Status: " + this.getStatus() + "\n");
		buffer.append("Length: " + this.getLength() + "\n");
		buffer.append("Timer: " + this.getTimer() + "\n");
		buffer.append("Priority: " + this.getPriority() + "\n");
		buffer.append("Star: " + this.isStar() + "\n");
		buffer.append("Note: " + this.getNote() + "\n");
		buffer.append("Contacts: " + this.getContacts() + "\n");
		buffer.append("Tasks: " + this.getTasks() + "\n");
		buffer.append("Files: " + this.getFiles() + "\n");
		
		return buffer.toString();
	}
	
}
