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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.utils.IgnoreCaseString;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class TagList implements ListChangeSupported, PropertyChangeListener {
	
	private static TagList INSTANCE;
	
	public static TagList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TagList();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	
	private List<IgnoreCaseString> tags;
	
	private TagList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.tags = new ArrayList<IgnoreCaseString>();
		
		this.initialize();
	}
	
	public String[] getTags() {
		Set<IgnoreCaseString> uniqueTags = new HashSet<IgnoreCaseString>(
				this.tags);
		List<IgnoreCaseString> tags = new ArrayList<IgnoreCaseString>(
				uniqueTags);
		
		Collections.sort(tags);
		
		return IgnoreCaseString.to(tags.toArray(new IgnoreCaseString[0]));
	}
	
	private void initialize() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			if (!task.getModelStatus().equals(ModelStatus.LOADED)
					&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				continue;
			}
			
			this.tags.addAll(Arrays.asList(IgnoreCaseString.as(task.getTags())));
		}
		
		TaskFactory.getInstance().addPropertyChangeListener(
				Task.PROP_TAGS,
				this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		List<IgnoreCaseString> tags = null;
		
		tags = Arrays.asList(IgnoreCaseString.as((String[]) evt.getOldValue()));
		this.tags.removeAll(tags);
		
		for (IgnoreCaseString tag : tags) {
			if (!this.tags.contains(tag))
				this.listChangeSupport.fireListChange(
						ListChangeEvent.VALUE_REMOVED,
						-1,
						tag.toString());
		}
		
		tags = Arrays.asList(IgnoreCaseString.as((String[]) evt.getNewValue()));
		
		List<IgnoreCaseString> newTags = new ArrayList<IgnoreCaseString>();
		for (IgnoreCaseString tag : tags) {
			if (!this.tags.contains(tag))
				newTags.add(tag);
		}
		
		this.tags.addAll(tags);
		
		for (IgnoreCaseString tag : newTags) {
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_ADDED,
					-1,
					tag.toString());
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
