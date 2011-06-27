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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.utils.IgnoreCaseString;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class TaskTagList implements ListChangeSupported, ListChangeListener, PropertyChangeListener {
	
	private static TaskTagList INSTANCE;
	
	public static TaskTagList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskTagList();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	
	private List<IgnoreCaseString> tags;
	private SortedSet<IgnoreCaseString> sortedTags;
	
	private TaskTagList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.tags = new ArrayList<IgnoreCaseString>();
		this.sortedTags = new TreeSet<IgnoreCaseString>();
		
		this.initialize();
	}
	
	public String[] getTags() {
		return IgnoreCaseString.to(this.sortedTags.toArray(new IgnoreCaseString[0]));
	}
	
	public void removeTag(String tag) {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			String[] tags = task.getTags();
			
			boolean removed = false;
			for (int i = 0; i < tags.length; i++) {
				if (tags[i].equalsIgnoreCase(tag)) {
					removed = true;
					tags[i] = null;
				}
			}
			
			if (removed)
				task.setTags(tags);
		}
	}
	
	private void initialize() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			if (!task.getModelStatus().equals(ModelStatus.LOADED)
					&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				continue;
			}
			
			this.tags.addAll(Arrays.asList(IgnoreCaseString.as(task.getTags())));
			this.sortedTags.addAll(this.tags);
		}
		
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
	}
	
	@Override
	public void listChange(ListChangeEvent evt) {
		Task task = (Task) evt.getValue();
		
		if (evt.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			if (task.getModelStatus().equals(ModelStatus.LOADED)
					|| task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				this.addTags(task.getTags());
			}
		}
		
		if (evt.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.removeTags(task.getTags());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Task task = (Task) evt.getSource();
		
		if (evt.getPropertyName().equals(Model.PROP_MODEL_STATUS)) {
			ModelStatus oldStatus = (ModelStatus) evt.getOldValue();
			ModelStatus newStatus = (ModelStatus) evt.getNewValue();
			
			boolean oldStatusVisible = (ModelStatus.LOADED.equals(oldStatus) || ModelStatus.TO_UPDATE.equals(oldStatus));
			boolean newStatusVisible = (ModelStatus.LOADED.equals(newStatus) || ModelStatus.TO_UPDATE.equals(newStatus));
			
			if (!oldStatusVisible && newStatusVisible) {
				this.addTags(task.getTags());
			} else if (oldStatusVisible && !newStatusVisible) {
				this.removeTags(task.getTags());
			}
		}
		
		if (evt.getPropertyName().equals(Task.PROP_TAGS)) {
			if (task.getModelStatus().equals(ModelStatus.LOADED)
					|| task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				this.addTags((String[]) evt.getNewValue());
				this.removeTags((String[]) evt.getOldValue());
			}
		}
	}
	
	private void addTags(String[] t) {
		List<IgnoreCaseString> tags = Arrays.asList(IgnoreCaseString.as(t));
		
		for (IgnoreCaseString tag : tags) {
			this.tags.add(tag);
			
			if (!this.sortedTags.contains(tag)) {
				this.sortedTags.add(tag);
				
				int index = 0;
				Iterator<IgnoreCaseString> it = this.sortedTags.iterator();
				while (it.hasNext()) {
					if (it.next().equals(tag))
						break;
					
					index++;
				}
				
				this.listChangeSupport.fireListChange(
						ListChangeEvent.VALUE_ADDED,
						index,
						tag.toString());
			}
		}
	}
	
	private void removeTags(String[] t) {
		List<IgnoreCaseString> tags = Arrays.asList(IgnoreCaseString.as(t));
		
		for (IgnoreCaseString tag : tags) {
			this.tags.remove(tag);
			
			if (!this.tags.contains(tag)) {
				if (this.sortedTags.contains(tag)) {
					this.sortedTags.remove(tag);
					this.listChangeSupport.fireListChange(
							ListChangeEvent.VALUE_REMOVED,
							-1,
							tag.toString());
				}
			}
		}
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
}
