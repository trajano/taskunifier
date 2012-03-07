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
import java.util.Calendar;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
import com.leclercb.taskunifier.api.models.FileList.FileItem;
import com.leclercb.taskunifier.api.models.TaskList.TaskItem;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;

public class Task extends AbstractModelParent<Task> implements ModelNote, PropertyChangeListener, ListChangeListener {
	
	public static final String PROP_TAGS = "tags";
	public static final String PROP_FOLDER = "folder";
	public static final String PROP_CONTEXTS = "contexts";
	public static final String PROP_GOALS = "goals";
	public static final String PROP_LOCATIONS = "locations";
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
	private ModelList<Context> contexts;
	private ModelList<Goal> goals;
	private ModelList<Location> locations;
	private double progress;
	private boolean completed;
	private Calendar completedOn;
	private Calendar startDate;
	private int startDateReminder;
	private Calendar dueDate;
	private int dueDateReminder;
	private String repeat;
	private TaskRepeatFrom repeatFrom;
	private String status;
	private int length;
	private Timer timer;
	private TaskPriority priority;
	private boolean star;
	private String note;
	private ContactList contacts;
	private TaskList tasks;
	private FileList files;
	
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
		
		this.contexts = new ModelList<Context>();
		this.contexts.addListChangeListener(this);
		
		this.goals = new ModelList<Goal>();
		this.goals.addListChangeListener(this);
		
		this.locations = new ModelList<Location>();
		this.locations.addListChangeListener(this);
		
		this.contacts = new ContactList();
		this.contacts.addListChangeListener(this);
		this.contacts.addPropertyChangeListener(this);
		
		this.tasks = new TaskList();
		this.tasks.addListChangeListener(this);
		this.tasks.addPropertyChangeListener(this);
		
		this.files = new FileList();
		this.files.addListChangeListener(this);
		this.files.addPropertyChangeListener(this);
		
		this.setTags(new TagList());
		this.setFolder(null);
		this.setContexts(new ModelList<Context>());
		this.setGoals(new ModelList<Goal>());
		this.setLocations(new ModelList<Location>());
		this.setProgress(0);
		this.setCompleted(false);
		this.setCompletedOn(null);
		this.setStartDate(null);
		this.setStartDateReminder(0);
		this.setDueDate(null);
		this.setDueDateReminder(0);
		this.setRepeat(null);
		this.setRepeatFrom(TaskRepeatFrom.DUE_DATE);
		this.setStatus(null);
		this.setLength(0);
		this.setTimer(new Timer());
		this.setPriority(TaskPriority.LOW);
		this.setStar(false);
		this.setNote(null);
		this.setContacts(new ContactList());
		this.setTasks(new TaskList());
		this.setFiles(new FileList());
		
		this.getFactory().register(this);
	}
	
	@Override
	public Task clone(ModelId modelId) {
		Task task = this.getFactory().create(modelId, this.getTitle());
		
		task.setTags(this.getTags());
		task.setFolder(this.getFolder());
		task.setContexts(this.getContexts());
		task.setGoals(this.getGoals());
		task.setLocations(this.getLocations());
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
		
		Folder folder = null;
		
		if (bean.getFolder() != null) {
			folder = FolderFactory.getInstance().get(bean.getFolder());
			if (folder == null)
				folder = FolderFactory.getInstance().createShell(
						bean.getFolder());
		}
		
		this.setTags(bean.getTags());
		this.setFolder(folder);
		this.setContexts((bean.getContexts() == null ? null : bean.getContexts().toModelList(
				new ModelList<Context>(),
				ModelType.CONTEXT)));
		this.setGoals((bean.getGoals() == null ? null : bean.getGoals().toModelList(
				new ModelList<Goal>(),
				ModelType.GOAL)));
		this.setLocations((bean.getLocations() == null ? null : bean.getLocations().toModelList(
				new ModelList<Location>(),
				ModelType.LOCATION)));
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
		bean.setFolder((this.getFolder() == null ? null : this.getFolder().getModelId()));
		bean.setContexts(this.getContexts().toModelBeanList());
		bean.setGoals(this.getGoals().toModelBeanList());
		bean.setLocations(this.getLocations().toModelBeanList());
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
	
	public ModelList<Context> getContexts() {
		return this.contexts;
	}
	
	public void setContexts(ModelList<Context> contexts) {
		this.contexts.clear();
		
		if (contexts != null)
			this.contexts.addAll(contexts.getList());
	}
	
	public ModelList<Goal> getGoals() {
		return this.goals;
	}
	
	public void setGoals(ModelList<Goal> goals) {
		this.goals.clear();
		
		if (goals != null)
			this.goals.addAll(goals.getList());
	}
	
	public ModelList<Location> getLocations() {
		return this.locations;
	}
	
	public void setLocations(ModelList<Location> locations) {
		this.locations.clear();
		
		if (locations != null)
			this.locations.addAll(locations.getList());
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
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		if (!this.checkBeforeSet(this.getStatus(), status))
			return;
		
		String oldStatus = this.status;
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
	
	public ContactList getContacts() {
		return this.contacts;
	}
	
	public void setContacts(ContactList contacts) {
		this.contacts.clear();
		
		if (contacts != null)
			this.contacts.addAll(contacts.getList());
	}
	
	public TaskList getTasks() {
		return this.tasks;
	}
	
	public void setTasks(TaskList tasks) {
		this.tasks.clear();
		
		if (tasks != null)
			this.tasks.addAll(tasks.getList());
	}
	
	public FileList getFiles() {
		return this.files;
	}
	
	public void setFiles(FileList files) {
		this.files.clear();
		
		if (files != null)
			this.files.addAll(files.getList());
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getSource().equals(this.contexts)) {
			this.updateProperty(PROP_CONTEXTS, null, this.contexts);
		}
		
		if (event.getSource().equals(this.goals)) {
			this.updateProperty(PROP_GOALS, null, this.goals);
		}
		
		if (event.getSource().equals(this.locations)) {
			this.updateProperty(PROP_LOCATIONS, null, this.locations);
		}
		
		if (event.getSource().equals(this.contacts)) {
			this.updateProperty(PROP_CONTACTS, null, this.contacts);
		}
		
		if (event.getSource().equals(this.tasks)) {
			this.updateProperty(PROP_TASKS, null, this.tasks);
		}
		
		if (event.getSource().equals(this.files)) {
			this.updateProperty(PROP_FILES, null, this.files);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof Folder
				&& event.getPropertyName().equals(PROP_MODEL_STATUS)) {
			Folder folder = (Folder) event.getSource();
			
			if (folder.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| folder.getModelStatus().equals(ModelStatus.DELETED))
				this.setFolder(null);
		}
		
		if (event.getSource() instanceof ContactItem) {
			this.updateProperty(PROP_CONTACTS, null, this.contacts);
		}
		
		if (event.getSource() instanceof TaskItem) {
			this.updateProperty(PROP_TASKS, null, this.tasks);
		}
		
		if (event.getSource() instanceof FileItem) {
			this.updateProperty(PROP_FILES, null, this.files);
		}
	}
	
}
