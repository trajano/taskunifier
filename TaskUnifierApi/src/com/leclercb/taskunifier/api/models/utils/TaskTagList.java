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
package com.leclercb.taskunifier.api.models.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.taskunifier.api.models.BasicModel;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;

public final class TaskTagList implements ListChangeSupported, ListChangeListener, PropertyChangeListener {
	
	private static TaskTagList INSTANCE;
	
	public static TaskTagList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskTagList();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	
	private List<Tag> tags;
	private SortedSet<Tag> sortedTags;
	
	private TaskTagList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.tags = new ArrayList<Tag>();
		this.sortedTags = new TreeSet<Tag>();
		
		this.initialize();
	}
	
	public int getIndexOf(Tag tag) {
		return this.getTags().getIndexOf(tag);
	}
	
	public int getTagCount() {
		return this.sortedTags.size();
	}
	
	public Tag getTag(int index) {
		return this.getTags().getTag(index);
	}
	
	public TagList getTags() {
		TagList list = new TagList();
		list.addTags(this.sortedTags);
		return list;
	}
	
	public void editTag(Tag oldTag, Tag newTag) {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			TagList tags = task.getTags();
			if (tags.replaceTag(oldTag, newTag))
				task.setTags(tags);
		}
	}
	
	public void removeTag(Tag tag) {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			TagList tags = task.getTags();
			if (tags.removeTag(tag))
				task.setTags(tags);
		}
	}
	
	private void initialize() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			if (!task.getModelStatus().isEndUserStatus()) {
				continue;
			}
			
			this.tags.addAll(task.getTags().asList());
			this.sortedTags.addAll(this.tags);
		}
		
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
	}
	
	@Override
	public void listChange(ListChangeEvent evt) {
		Task task = (Task) evt.getValue();
		
		if (evt.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			if (task.getModelStatus().isEndUserStatus()) {
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
		
		if (evt.getPropertyName().equals(BasicModel.PROP_MODEL_STATUS)) {
			ModelStatus oldStatus = (ModelStatus) evt.getOldValue();
			ModelStatus newStatus = (ModelStatus) evt.getNewValue();
			
			if (!oldStatus.isEndUserStatus() && newStatus.isEndUserStatus()) {
				this.addTags(task.getTags());
			} else if (oldStatus.isEndUserStatus()
					&& !newStatus.isEndUserStatus()) {
				this.removeTags(task.getTags());
			}
		}
		
		if (evt.getPropertyName().equals(Task.PROP_TAGS)) {
			if (task.getModelStatus().isEndUserStatus()) {
				this.addTags((TagList) evt.getNewValue());
				this.removeTags((TagList) evt.getOldValue());
			}
		}
	}
	
	private void addTags(TagList tags) {
		for (Tag tag : tags) {
			this.tags.add(tag);
			
			if (!this.sortedTags.contains(tag)) {
				this.sortedTags.add(tag);
				
				int index = 0;
				Iterator<Tag> it = this.sortedTags.iterator();
				while (it.hasNext()) {
					if (it.next().equals(tag))
						break;
					
					index++;
				}
				
				this.listChangeSupport.fireListChange(
						ListChangeEvent.VALUE_ADDED,
						index,
						tag);
			}
		}
	}
	
	private void removeTags(TagList tags) {
		for (Tag tag : tags) {
			this.tags.remove(tag);
			
			if (!this.tags.contains(tag)) {
				if (this.sortedTags.contains(tag)) {
					this.sortedTags.remove(tag);
					this.listChangeSupport.fireListChange(
							ListChangeEvent.VALUE_REMOVED,
							-1,
							tag);
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
