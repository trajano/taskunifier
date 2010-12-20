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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leclercb.taskunifier.api.event.ListenerList;
import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.event.listchange.ListChangeModel;
import com.leclercb.taskunifier.api.event.propertychange.PropertyChangeModel;
import com.leclercb.taskunifier.api.utils.CheckUtils;

public class TaskSearcherFactory implements PropertyChangeListener, ListChangeModel, PropertyChangeModel {
	
	private static TaskSearcherFactory FACTORY;
	
	public static TaskSearcherFactory getInstance() {
		if (FACTORY == null)
			FACTORY = new TaskSearcherFactory();
		
		return FACTORY;
	}
	
	private ListenerList<ListChangeListener> listChangeListenerList;
	private ListenerList<PropertyChangeListener> propertyChangeListenerList;
	
	private List<TaskSearcher> searchers;
	
	private TaskSearcherFactory() {
		this.listChangeListenerList = new ListenerList<ListChangeListener>();
		this.propertyChangeListenerList = new ListenerList<PropertyChangeListener>();
		
		this.searchers = new ArrayList<TaskSearcher>();
	}
	
	public boolean contains(TaskSearcher searcher) {
		return this.searchers.contains(searcher);
	}
	
	public int size() {
		return this.searchers.size();
	}
	
	public List<TaskSearcher> getList() {
		return Collections.unmodifiableList(this.searchers);
	}
	
	public TaskSearcher get(int index) {
		return this.searchers.get(index);
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
		this.fireListChange(ListChangeEvent.VALUE_ADDED, index, searcher);
	}
	
	public void unregister(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");
		
		int index = this.searchers.indexOf(searcher);
		if (this.searchers.remove(searcher)) {
			searcher.removePropertyChangeListener(this);
			this.fireListChange(ListChangeEvent.VALUE_REMOVED, index, searcher);
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
		this.listChangeListenerList.addListener(listener);
	}
	
	/**
	 * Removes the listener from the list change listener list.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeListenerList.addListener(listener);
	}
	
	/**
	 * The listener will be notified when a searcher is updated.
	 * 
	 * @param listener
	 *            the listener to notify
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeListenerList.addListener(listener);
	}
	
	/**
	 * Removes the listener from the property change listener list.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeListenerList.removeListener(listener);
	}
	
	/**
	 * Called when a searcher is updated. Shouldn't be called manually.
	 * 
	 * @param evt
	 *            event of the model
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.firePropertyChange(event);
	}
	
	protected void fireListChange(ListChangeEvent event) {
		for (ListChangeListener listener : this.listChangeListenerList)
			listener.listChange(event);
	}
	
	protected void fireListChange(int changeType, int index, Object value) {
		this.fireListChange(new ListChangeEvent(this, changeType, index, value));
	}
	
	protected void firePropertyChange(PropertyChangeEvent evt) {
		for (PropertyChangeListener listener : this.propertyChangeListenerList)
			listener.propertyChange(evt);
	}
	
	protected void firePropertyChange(
			String property,
			Object oldValue,
			Object newValue) {
		this.firePropertyChange(new PropertyChangeEvent(
				this,
				property,
				oldValue,
				newValue));
	}
	
}
