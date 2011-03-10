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
package com.leclercb.taskunifier.gui.commons.events;

import com.leclercb.commons.api.event.ListenerList;
import com.leclercb.taskunifier.api.models.Task;

public class TaskSelectionChangeSupport implements TaskSelectionChangeSupported {
	
	private ListenerList<TaskSelectionListener> listeners;
	
	private Object source;
	
	public TaskSelectionChangeSupport(Object source) {
		this.listeners = new ListenerList<TaskSelectionListener>();
		this.source = source;
	}
	
	@Override
	public void addTaskSelectionChangeListener(TaskSelectionListener listener) {
		this.listeners.addListener(listener);
	}
	
	@Override
	public void removeTaskSelectionChangeListener(TaskSelectionListener listener) {
		this.listeners.removeListener(listener);
	}
	
	public void fireTaskSelectionChange(TaskSelectionChangeEvent event) {
		for (TaskSelectionListener listener : this.listeners)
			listener.taskSelectionChange(event);
	}
	
	public void fireTaskSelectionChange(Task[] selectedTasks) {
		this.fireTaskSelectionChange(new TaskSelectionChangeEvent(
				this.source,
				selectedTasks));
	}
	
}
