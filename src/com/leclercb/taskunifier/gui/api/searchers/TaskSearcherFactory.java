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
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
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
	}
	
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
		
		if (this.contains(searcher))
			return;
		
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
			TaskSearcherType type,
			int order,
			String title,
			TaskFilter filter,
			TaskSorter sorter) {
		TaskSearcher searcher = new TaskSearcher(
				type,
				order,
				title,
				filter,
				sorter);
		this.register(searcher);
		return searcher;
	}
	
	public TaskSearcher create(
			TaskSearcherType type,
			int order,
			String title,
			String icon,
			TaskFilter filter,
			TaskSorter sorter) {
		TaskSearcher searcher = new TaskSearcher(
				type,
				order,
				title,
				icon,
				filter,
				sorter);
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
	 * The listener will be notified when a searcher is updated
	 * for the specified property name.
	 * 
	 * @param propertyName
	 *            the property name to listen to
	 * @param listener
	 *            the listener to notify
	 */
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
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
