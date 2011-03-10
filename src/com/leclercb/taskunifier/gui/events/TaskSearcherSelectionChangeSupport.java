/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2011  Benjamin Leclerc
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
package com.leclercb.taskunifier.gui.events;

import com.leclercb.commons.api.event.ListenerList;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;

public class TaskSearcherSelectionChangeSupport implements TaskSearcherSelectionChangeSupported {
	
	private ListenerList<TaskSearcherSelectionListener> listeners;
	
	private Object source;
	
	public TaskSearcherSelectionChangeSupport(Object source) {
		this.listeners = new ListenerList<TaskSearcherSelectionListener>();
		this.source = source;
	}
	
	@Override
	public void addTaskSearcherSelectionChangeListener(
			TaskSearcherSelectionListener listener) {
		this.listeners.addListener(listener);
	}
	
	@Override
	public void removeTaskSearcherSelectionChangeListener(
			TaskSearcherSelectionListener listener) {
		this.listeners.removeListener(listener);
	}
	
	public void fireTaskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		for (TaskSearcherSelectionListener listener : this.listeners)
			listener.taskSearcherSelectionChange(event);
	}
	
	public void fireTaskSearcherSelectionChange(
			TaskSearcher selectedTaskSearcher) {
		this.fireTaskSearcherSelectionChange(new TaskSearcherSelectionChangeEvent(
				this.source,
				selectedTaskSearcher));
	}
	
}
