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

import java.io.Serializable;

import com.leclercb.taskunifier.api.event.propertychange.AbstractPropertyChangeModel;
import com.leclercb.taskunifier.api.utils.CheckUtils;

public class TaskSearcher extends AbstractPropertyChangeModel implements Serializable {
	
	public static final String PROP_TITLE = "SEARCHER_TITLE";
	public static final String PROP_ICON = "SEARCHER_ICON";
	public static final String PROP_FILTER = "SEARCHER_FILTER";
	public static final String PROP_SORTER = "SEARCHER_SORTER";
	
	private String title;
	private String icon;
	private TaskFilter filter;
	private TaskSorter sorter;
	
	public TaskSearcher(String title, TaskFilter filter, TaskSorter sorter) {
		this(title, null, filter, sorter);
	}
	
	public TaskSearcher(
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter) {
		this.setTitle(title);
		this.setIcon(icon);
		this.setFilter(filter);
		this.setSorter(sorter);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		CheckUtils.isNotNull(title, "Title cannot be null");
		String oldTitle = this.title;
		this.title = title;
		this.firePropertyChange(PROP_TITLE, oldTitle, title);
	}
	
	public String getIcon() {
		return this.icon;
	}
	
	public void setIcon(String icon) {
		String oldIcon = this.icon;
		this.icon = icon;
		this.firePropertyChange(PROP_ICON, oldIcon, icon);
	}
	
	public TaskFilter getFilter() {
		return this.filter;
	}
	
	public void setFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		TaskFilter oldFilter = this.filter;
		this.filter = filter;
		this.firePropertyChange(PROP_FILTER, oldFilter, filter);
	}
	
	public TaskSorter getSorter() {
		return this.sorter;
	}
	
	public void setSorter(TaskSorter sorter) {
		CheckUtils.isNotNull(sorter, "Sorter cannot be null");
		TaskSorter oldSorter = this.sorter;
		this.sorter = sorter;
		this.firePropertyChange(PROP_SORTER, oldSorter, sorter);
	}
	
	@Override
	public String toString() {
		return this.title;
	}
	
}
