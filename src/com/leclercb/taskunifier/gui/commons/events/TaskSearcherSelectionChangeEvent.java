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

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;

public class TaskSearcherSelectionChangeEvent {
	
	private Object source;
	private TaskSearcher selectedTaskSearcher;
	
	public TaskSearcherSelectionChangeEvent(
			Object source,
			TaskSearcher selectedTaskSearcher) {
		CheckUtils.isNotNull(source, "Source cannot be null");
		
		this.source = source;
		this.selectedTaskSearcher = selectedTaskSearcher;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public TaskSearcher getSelectedTaskSearcher() {
		return this.selectedTaskSearcher;
	}
	
}
