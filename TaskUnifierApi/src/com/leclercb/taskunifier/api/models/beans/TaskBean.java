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
package com.leclercb.taskunifier.api.models.beans;

import java.util.Calendar;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.api.models.Timer;
import com.leclercb.taskunifier.api.models.beans.converters.CalendarConverter;
import com.leclercb.taskunifier.api.models.beans.converters.TagListConverter;
import com.leclercb.taskunifier.api.models.beans.converters.TimerConverter;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

public class TaskBean extends AbstractModelParentBean {
	
	@XStreamAlias("tags")
	@XStreamConverter(TagListConverter.class)
	private TagList tags;
	
	@XStreamAlias("folder")
	private ModelId folder;
	
	@XStreamAlias("contexts")
	private ModelBeanList contexts;
	
	@XStreamAlias("goals")
	private ModelBeanList goals;
	
	@XStreamAlias("locations")
	private ModelBeanList locations;
	
	@XStreamAlias("progress")
	private double progress;
	
	@XStreamAlias("completed")
	private boolean completed;
	
	@XStreamAlias("completedon")
	@XStreamConverter(CalendarConverter.class)
	private Calendar completedOn;
	
	@XStreamAlias("startdate")
	@XStreamConverter(CalendarConverter.class)
	private Calendar startDate;
	
	@XStreamAlias("startdatereminder")
	private int startDateReminder;
	
	@XStreamAlias("duedate")
	@XStreamConverter(CalendarConverter.class)
	private Calendar dueDate;
	
	@XStreamAlias("reminder")
	private int dueDateReminder;
	
	@XStreamAlias("repeat")
	private String repeat;
	
	@XStreamAlias("repeatfrom")
	private TaskRepeatFrom repeatFrom;
	
	@XStreamAlias("status")
	private String status;
	
	@XStreamAlias("length")
	private int length;
	
	@XStreamAlias("timer")
	@XStreamConverter(TimerConverter.class)
	private Timer timer;
	
	@XStreamAlias("priority")
	private TaskPriority priority;
	
	@XStreamAlias("star")
	private boolean star;
	
	@XStreamAlias("note")
	private String note;
	
	@XStreamAlias("contacts")
	private ContactListBean contacts;
	
	@XStreamAlias("tasks")
	private TaskListBean tasks;
	
	@XStreamAlias("files")
	private FileListBean files;
	
	public TaskBean() {
		this((ModelId) null);
	}
	
	public TaskBean(ModelId modelId) {
		super(modelId);
		
		this.setTags(new TagList());
		this.setFolder(null);
		this.setContexts(new ModelBeanList());
		this.setGoals(new ModelBeanList());
		this.setLocations(new ModelBeanList());
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
		this.setContacts(new ContactListBean());
		this.setTasks(new TaskListBean());
		this.setFiles(new FileListBean());
	}
	
	public TaskBean(TaskBean bean) {
		super(bean);
		
		this.setTags(bean.getTags());
		this.setFolder(bean.getFolder());
		this.setContexts(bean.getContexts());
		this.setGoals(bean.getGoals());
		this.setLocations(bean.getLocations());
		this.setProgress(bean.getProgress());
		this.setCompleted(bean.isCompleted());
		this.setCompletedOn(bean.getCompletedOn());
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
		this.setContacts(bean.getContacts());
		this.setTasks(bean.getTasks());
		this.setFiles(bean.getFiles());
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.TASK;
	}
	
	public TagList getTags() {
		return this.tags;
	}
	
	public void setTags(TagList tags) {
		this.tags = tags;
	}
	
	public ModelId getFolder() {
		return this.folder;
	}
	
	public void setFolder(ModelId folder) {
		this.folder = folder;
	}
	
	public ModelBeanList getContexts() {
		return this.contexts;
	}
	
	public void setContexts(ModelBeanList contexts) {
		this.contexts = contexts;
	}
	
	public ModelBeanList getGoals() {
		return this.goals;
	}
	
	public void setGoals(ModelBeanList goals) {
		this.goals = goals;
	}
	
	public ModelBeanList getLocations() {
		return this.locations;
	}
	
	public void setLocations(ModelBeanList locations) {
		this.locations = locations;
	}
	
	public double getProgress() {
		return this.progress;
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public Calendar getCompletedOn() {
		return this.completedOn;
	}
	
	public void setCompletedOn(Calendar completedOn) {
		this.completedOn = completedOn;
	}
	
	public Calendar getStartDate() {
		return this.startDate;
	}
	
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	public int getStartDateReminder() {
		return this.startDateReminder;
	}
	
	public void setStartDateReminder(int startDateReminder) {
		this.startDateReminder = startDateReminder;
	}
	
	public Calendar getDueDate() {
		return this.dueDate;
	}
	
	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}
	
	public int getDueDateReminder() {
		return this.dueDateReminder;
	}
	
	public void setDueDateReminder(int dueDateReminder) {
		this.dueDateReminder = dueDateReminder;
	}
	
	public String getRepeat() {
		return this.repeat;
	}
	
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	
	public TaskRepeatFrom getRepeatFrom() {
		return this.repeatFrom;
	}
	
	public void setRepeatFrom(TaskRepeatFrom repeatFrom) {
		this.repeatFrom = repeatFrom;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public Timer getTimer() {
		return this.timer;
	}
	
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	public TaskPriority getPriority() {
		return this.priority;
	}
	
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	
	public boolean isStar() {
		return this.star;
	}
	
	public void setStar(boolean star) {
		this.star = star;
	}
	
	public String getNote() {
		return this.note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public ContactListBean getContacts() {
		return this.contacts;
	}
	
	public void setContacts(ContactListBean contacts) {
		this.contacts = contacts;
	}
	
	public TaskListBean getTasks() {
		return this.tasks;
	}
	
	public void setTasks(TaskListBean tasks) {
		this.tasks = tasks;
	}
	
	public FileListBean getFiles() {
		return this.files;
	}
	
	public void setFiles(FileListBean files) {
		this.files = files;
	}
	
}
