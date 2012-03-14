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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.TaskList.TaskItem;
import com.leclercb.taskunifier.api.models.beans.TaskListBean;
import com.leclercb.taskunifier.api.models.beans.TaskListBean.TaskItemBean;

public class TaskList implements Cloneable, Serializable, Iterable<TaskItem>, PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<TaskItem> tasks;
	
	public TaskList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.tasks = new ArrayList<TaskItem>();
	}
	
	@Override
	protected TaskList clone() {
		TaskList list = new TaskList();
		list.tasks.addAll(this.tasks);
		return list;
	}
	
	@Override
	public Iterator<TaskItem> iterator() {
		return this.tasks.iterator();
	}
	
	public List<TaskItem> getList() {
		return Collections.unmodifiableList(new ArrayList<TaskItem>(this.tasks));
	}
	
	public void add(TaskItem item) {
		CheckUtils.isNotNull(item);
		this.tasks.add(item);
		
		item.addPropertyChangeListener(this);
		int index = this.tasks.indexOf(item);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				item);
	}
	
	public void addAll(Collection<TaskItem> items) {
		if (items == null)
			return;
		
		for (TaskItem item : items)
			this.add(item);
	}
	
	public void remove(TaskItem item) {
		CheckUtils.isNotNull(item);
		
		int index = this.tasks.indexOf(item);
		if (this.tasks.remove(item)) {
			item.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					item);
		}
	}
	
	public void clear() {
		for (TaskItem item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.tasks.size();
	}
	
	public int getIndexOf(TaskItem item) {
		return this.tasks.indexOf(item);
	}
	
	public TaskItem get(int index) {
		return this.tasks.get(index);
	}
	
	@Override
	public String toString() {
		List<String> tasks = new ArrayList<String>();
		for (TaskItem task : this.tasks) {
			if (task.toString().length() != 0)
				tasks.add(task.toString());
		}
		
		return StringUtils.join(tasks, ", ");
	}
	
	public TaskListBean toTaskGroupBean() {
		TaskListBean list = new TaskListBean();
		
		for (TaskItem item : this)
			list.add(item.toTaskItemBean());
		
		return list;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				propertyName,
				listener);
	}
	
	public static class TaskItem implements PropertyChangeSupported, PropertyChangeListener {
		
		public static final String PROP_TASK = "task";
		public static final String PROP_LINK = "link";
		
		private PropertyChangeSupport propertyChangeSupport;
		
		private Task task;
		private String link;
		
		public TaskItem() {
			this(null, null);
		}
		
		public TaskItem(Task task, String link) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			this.setTask(task);
			this.setLink(link);
		}
		
		public Task getTask() {
			return this.task;
		}
		
		public void setTask(Task task) {
			if (task != null) {
				if (task.getModelStatus().equals(ModelStatus.TO_DELETE)
						|| task.getModelStatus().equals(ModelStatus.DELETED)) {
					ApiLogger.getLogger().severe(
							"You cannot assign a deleted model");
					task = null;
				}
			}
			
			if (this.task != null)
				this.task.removePropertyChangeListener(this);
			
			Task oldTask = this.task;
			this.task = task;
			
			if (this.task != null)
				this.task.addPropertyChangeListener(this);
			
			this.propertyChangeSupport.firePropertyChange(
					PROP_TASK,
					oldTask,
					task);
		}
		
		public String getLink() {
			return this.link;
		}
		
		public void setLink(String link) {
			String oldLink = this.link;
			this.link = link;
			this.propertyChangeSupport.firePropertyChange(
					PROP_LINK,
					oldLink,
					link);
		}
		
		@Override
		public String toString() {
			String task = (this.task == null ? "" : this.task.toString());
			String link = (this.link == null ? "" : this.link);
			
			if (link.length() != 0)
				link = "(" + this.link + ")";
			
			if (task.length() == 0)
				return link;
			
			if (link.length() == 0)
				return task;
			
			return task + " " + link;
		}
		
		public TaskItemBean toTaskItemBean() {
			ModelId id = null;
			
			if (this.task != null)
				id = this.task.getModelId();
			
			return new TaskItemBean(id, this.link);
		}
		
		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(listener);
		}
		
		@Override
		public void addPropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(
					propertyName,
					listener);
		}
		
		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(listener);
		}
		
		@Override
		public void removePropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(
					propertyName,
					listener);
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getPropertyName().equals(Model.PROP_MODEL_STATUS)) {
				Task task = (Task) event.getSource();
				
				if (task.getModelStatus().equals(ModelStatus.TO_DELETE)
						|| task.getModelStatus().equals(ModelStatus.DELETED))
					this.setTask(null);
			}
		}
		
	}
	
}
