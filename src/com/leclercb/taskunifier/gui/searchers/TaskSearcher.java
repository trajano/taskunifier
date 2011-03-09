/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.searchers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.UUID;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsBuilder;
import com.leclercb.commons.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.template.Template;

public class TaskSearcher implements Serializable, Cloneable, PropertyChangeSupported {
	
	public static final String PROP_TITLE = "title";
	public static final String PROP_ICON = "icon";
	public static final String PROP_FILTER = "filter";
	public static final String PROP_SORTER = "sorter";
	public static final String PROP_TEMPLATE = "template";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private String id;
	private String title;
	private String icon;
	private TaskFilter filter;
	private TaskSorter sorter;
	private Template template;
	
	public TaskSearcher(String title, TaskFilter filter, TaskSorter sorter) {
		this(title, null, filter, sorter);
	}
	
	public TaskSearcher(
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter) {
		this(title, icon, filter, sorter, null);
	}
	
	public TaskSearcher(
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter,
			Template template) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setId(UUID.randomUUID().toString());
		
		this.setTitle(title);
		this.setIcon(icon);
		this.setFilter(filter);
		this.setSorter(sorter);
		this.setTemplate(template);
	}
	
	@Override
	public TaskSearcher clone() {
		return new TaskSearcher(
				this.title,
				this.icon,
				this.filter.clone(),
				this.sorter.clone(),
				this.template.clone());
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
		TaskFilter oldFilter = this.filter;
		this.filter = filter;
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
		TaskSorter oldSorter = this.sorter;
		this.sorter = sorter;
		this.propertyChangeSupport.firePropertyChange(
				PROP_SORTER,
				oldSorter,
				sorter);
	}
	
	public Template getTemplate() {
		return this.template;
	}
	
	public void setTemplate(Template template) {
		CheckUtils.isNotNull(template, "Template cannot be null");
		Template oldTemplate = this.template;
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
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof TaskSearcher) {
			TaskSearcher searcher = (TaskSearcher) o;
			
			return new EqualsBuilder().append(this.id, searcher.id).isEqual();
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
