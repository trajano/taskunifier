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
package com.leclercb.taskunifier.gui.api.searchers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskSearcher implements Cloneable, Serializable, PropertyChangeSupported, ListChangeListener, PropertyChangeListener {
	
	public static final String PROP_TYPE = "type";
	public static final String PROP_ORDER = "order";
	public static final String PROP_TITLE = "title";
	public static final String PROP_ICON = "icon";
	public static final String PROP_FILTER = "filter";
	public static final String PROP_SORTER = "sorter";
	public static final String PROP_TEMPLATE = "template";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private TaskSearcherType type;
	private int order;
	private String title;
	private String icon;
	private TaskFilter filter;
	private TaskSorter sorter;
	private TaskTemplate template;
	
	public TaskSearcher(
			TaskSearcherType type,
			int order,
			String title,
			TaskFilter filter,
			TaskSorter sorter) {
		this(type, order, title, null, filter, sorter);
	}
	
	public TaskSearcher(
			TaskSearcherType type,
			int order,
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter) {
		this(type, order, title, icon, filter, sorter, null);
	}
	
	public TaskSearcher(
			TaskSearcherType type,
			int order,
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter,
			TaskTemplate template) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setType(type);
		this.setOrder(order);
		this.setTitle(title);
		this.setIcon(icon);
		this.setFilter(filter);
		this.setSorter(sorter);
		this.setTemplate(template);
	}
	
	@Override
	public TaskSearcher clone() {
		return new TaskSearcher(
				this.type,
				this.order,
				this.title,
				this.icon,
				this.filter.clone(),
				this.sorter.clone(),
				(this.template == null ? null : this.template.clone()));
	}
	
	public TaskSearcherType getType() {
		return this.type;
	}
	
	public void setType(TaskSearcherType type) {
		CheckUtils.isNotNull(type, "Type cannot be null");
		TaskSearcherType oldType = this.type;
		this.type = type;
		this.propertyChangeSupport.firePropertyChange(PROP_TYPE, oldType, type);
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public void setOrder(int order) {
		int oldOrder = this.order;
		this.order = order;
		this.propertyChangeSupport.firePropertyChange(
				PROP_ORDER,
				oldOrder,
				order);
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
	
	public String getIcon() {
		return this.icon;
	}
	
	public void setIcon(String icon) {
		String oldIcon = this.icon;
		this.icon = icon;
		this.propertyChangeSupport.firePropertyChange(PROP_ICON, oldIcon, icon);
	}
	
	public TaskFilter getFilter() {
		return this.filter;
	}
	
	public void setFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		
		if (this.filter != null) {
			this.filter.removeListChangeListener(this);
			this.filter.removePropertyChangeListener(this);
		}
		
		TaskFilter oldFilter = this.filter;
		this.filter = filter;
		
		this.filter.addListChangeListener(this);
		this.filter.addPropertyChangeListener(this);
		
		this.propertyChangeSupport.firePropertyChange(
				PROP_FILTER,
				oldFilter,
				filter);
	}
	
	public TaskSorter getSorter() {
		return this.sorter;
	}
	
	public void setSorter(TaskSorter sorter) {
		CheckUtils.isNotNull(sorter, "Sorter cannot be null");
		
		if (this.sorter != null) {
			this.sorter.removeListChangeListener(this);
			this.sorter.removePropertyChangeListener(this);
		}
		
		TaskSorter oldSorter = this.sorter;
		this.sorter = sorter;
		
		this.sorter.addListChangeListener(this);
		this.sorter.addPropertyChangeListener(this);
		
		this.propertyChangeSupport.firePropertyChange(
				PROP_SORTER,
				oldSorter,
				sorter);
	}
	
	public TaskTemplate getTemplate() {
		return this.template;
	}
	
	public void setTemplate(TaskTemplate template) {
		TaskTemplate oldTemplate = this.template;
		this.template = template;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TEMPLATE,
				oldTemplate,
				template);
	}
	
	@Override
	public String toString() {
		return this.title;
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
	public void listChange(ListChangeEvent evt) {
		this.fireChange(evt.getSource());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.fireChange(evt.getSource());
	}
	
	private void fireChange(Object source) {
		if (source instanceof TaskFilter || source instanceof TaskFilterElement) {
			this.propertyChangeSupport.firePropertyChange(
					PROP_FILTER,
					null,
					this.filter);
		}
		
		if (source instanceof TaskSorter || source instanceof TaskSorterElement) {
			this.propertyChangeSupport.firePropertyChange(
					PROP_SORTER,
					null,
					this.sorter);
		}
	}
	
}
