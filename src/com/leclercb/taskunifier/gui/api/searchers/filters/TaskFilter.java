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
package com.leclercb.taskunifier.gui.api.searchers.filters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.ListUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskFilter implements ListChangeListener, PropertyChangeListener, ListChangeSupported, PropertyChangeSupported, Serializable, Cloneable {
	
	public static final String PROP_LINK = "link";
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private TaskFilter parent;
	private TaskFilterLink link;
	private List<TaskFilter> filters;
	private List<TaskFilterElement> elements;
	
	public TaskFilter() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setParent(null);
		this.setLink(TaskFilterLink.AND);
		
		this.filters = new ArrayList<TaskFilter>();
		this.elements = new ArrayList<TaskFilterElement>();
	}
	
	@Override
	public TaskFilter clone() {
		TaskFilter filter = new TaskFilter();
		filter.setLink(this.link);
		
		for (TaskFilterElement e : this.elements)
			filter.addElement(e.clone());
		
		for (TaskFilter f : this.filters)
			filter.addFilter(f.clone());
		
		return filter;
	}
	
	public TaskFilter getParent() {
		return this.parent;
	}
	
	private void setParent(TaskFilter parent) {
		this.parent = parent;
	}
	
	public TaskFilterLink getLink() {
		return this.link;
	}
	
	public void setLink(TaskFilterLink link) {
		CheckUtils.isNotNull(link, "Link cannot be null");
		TaskFilterLink oldLink = this.link;
		this.link = link;
		this.propertyChangeSupport.firePropertyChange(PROP_LINK, oldLink, link);
	}
	
	public int getIndexOf(TaskFilterElement element) {
		return this.elements.indexOf(element);
	}
	
	public int getElementCount() {
		return this.elements.size();
	}
	
	public TaskFilterElement getElement(int index) {
		return this.elements.get(index);
	}
	
	public List<TaskFilterElement> getElements() {
		return Collections.unmodifiableList(this.elements);
	}
	
	public void addElement(TaskFilterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		this.elements.add(element);
		
		if (element.getParent() != null) {
			element.getParent().removeElement(element);
		}
		
		element.setParent(this);
		element.addPropertyChangeListener(this);
		int index = this.elements.indexOf(element);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				element);
	}
	
	public void removeElement(TaskFilterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		
		int index = this.elements.indexOf(element);
		if (this.elements.remove(element)) {
			element.setParent(null);
			element.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					element);
		}
	}
	
	public int getIndexOf(TaskFilter filter) {
		return this.filters.indexOf(filter);
	}
	
	public int getFilterCount() {
		return this.filters.size();
	}
	
	public TaskFilter getFilter(int index) {
		return this.filters.get(index);
	}
	
	public List<TaskFilter> getFilters() {
		return Collections.unmodifiableList(this.filters);
	}
	
	public void addFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		this.filters.add(filter);
		
		if (filter.getParent() != null) {
			filter.getParent().removeFilter(filter);
		}
		
		filter.setParent(this);
		filter.addListChangeListener(this);
		filter.addPropertyChangeListener(this);
		int index = this.filters.indexOf(filter);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				filter);
	}
	
	public void removeFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		
		int index = this.filters.indexOf(filter);
		if (this.filters.remove(filter)) {
			filter.setParent(null);
			filter.removeListChangeListener(this);
			filter.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					filter);
		}
	}
	
	public boolean include(Task task) {
		if (this.link == TaskFilterLink.AND) {
			for (TaskFilterElement element : this.elements) {
				if (!element.include(task))
					return false;
			}
			
			for (TaskFilter filter : this.filters) {
				if (!filter.include(task))
					return false;
			}
			
			return true;
		} else {
			for (TaskFilterElement element : this.elements) {
				if (element.include(task))
					return true;
			}
			
			for (TaskFilter filter : this.filters) {
				if (filter.include(task))
					return true;
			}
			
			return false;
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
	public void listChange(ListChangeEvent event) {
		this.listChangeSupport.fireListChange(event);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public String toString() {
		if (this.elements.size() == 0 && this.filters.size() == 0)
			return "";
		
		StringBuffer buffer = new StringBuffer(
				Translations.getString("general.filter") + ": ((");
		
		List<Object> list = new ArrayList<Object>();
		list.addAll(this.elements);
		list.addAll(this.filters);
		
		buffer.append(ListUtils.listToString(
				list,
				") "
						+ TranslationsUtils.translateTaskFilterLink(this.link)
						+ " ("));
		
		buffer.append("))");
		
		return buffer.toString();
	}
	
}
