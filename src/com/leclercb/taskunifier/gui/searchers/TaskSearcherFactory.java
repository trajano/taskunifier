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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;

public class TaskSearcherFactory implements PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	private static TaskSearcherFactory FACTORY;
	
	public static TaskSearcherFactory getInstance() {
		if (FACTORY == null)
			FACTORY = new TaskSearcherFactory();
		
		return FACTORY;
	}
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<TaskSearcher> searchers;
	
	private TaskSearcherFactory() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.searchers = new ArrayList<TaskSearcher>();
	}
	
	public boolean contains(TaskSearcher searcher) {
		return this.searchers.contains(searcher);
	}
	
	public int size() {
		return this.searchers.size();
	}
	
	public List<TaskSearcher> getList() {
		return Collections.unmodifiableList(new ArrayList<TaskSearcher>(
				this.searchers));
	}
	
	public TaskSearcher get(int index) {
		return this.searchers.get(index);
	}
	
	public TaskSearcher get(String id) {
		for (TaskSearcher searcher : this.searchers)
			if (searcher.getId().equals(id))
				return searcher;
		
		return null;
	}
	
	/**
	 * Returns the index of the given searcher. Returns -1 if the searcher does
	 * not exist.
	 * 
	 * @param searcher
	 *            searcher to find
	 * @return the index of the given searcher or -1 if no searcher has been
	 *         found
	 */
	public int getIndexOf(TaskSearcher searcher) {
		return this.searchers.indexOf(searcher);
	};
	
	public void delete(TaskSearcher searcher) {
		this.unregister(searcher);
	}
	
	public void deleteAll() {
		List<TaskSearcher> searchers = new ArrayList<TaskSearcher>(
				this.searchers);
		for (TaskSearcher searcher : searchers)
			this.unregister(searcher);
	}
	
	public void register(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");
		this.searchers.add(searcher);
		searcher.addPropertyChangeListener(this);
		int index = this.searchers.indexOf(searcher);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				searcher);
	}
	
	public void unregister(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");
		
		int index = this.searchers.indexOf(searcher);
		if (this.searchers.remove(searcher)) {
			searcher.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					searcher);
		}
	}
	
	public TaskSearcher create(
			String title,
			TaskFilter filter,
			TaskSorter sorter) {
		TaskSearcher searcher = new TaskSearcher(title, filter, sorter);
		this.register(searcher);
		return searcher;
	}
	
	public TaskSearcher create(
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter) {
		TaskSearcher searcher = new TaskSearcher(title, icon, filter, sorter);
		this.register(searcher);
		return searcher;
	}
	
	/**
	 * The listener will be notified when a new searcher is added to the factory
	 * or when a searcher is removed from the factory.
	 * 
	 * @param listener
	 *            the listener to notify
	 */
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	/**
	 * Removes the listener from the list change listener list.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	/**
	 * The listener will be notified when a searcher is updated.
	 * 
	 * @param listener
	 *            the listener to notify
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	/**
	 * Removes the listener from the property change listener list.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	/**
	 * Called when a searcher is updated. Shouldn't be called manually.
	 * 
	 * @param evt
	 *            event of the model
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
}
