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
package com.leclercb.taskunifier.gui.components.searcherlist.transfer;

import java.io.Serializable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;

public class TaskSearcherTransferData implements Serializable {
	
	private String id;
	
	public TaskSearcherTransferData(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");
		
		this.id = searcher.getId();
	}
	
	public TaskSearcher getTaskSearcher() {
		return TaskSearcherFactory.getInstance().get(this.id);
	}
	
}
