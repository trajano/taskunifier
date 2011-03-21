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
package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.Comparator;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;

public class TaskSearcherComparator implements Comparator<TaskSearcher> {
	
	@Override
	public int compare(TaskSearcher ts1, TaskSearcher ts2) {
		String s1 = ts1 == null ? null : ts1.getTitle().toLowerCase();
		String s2 = ts2 == null ? null : ts2.getTitle().toLowerCase();
		
		return CompareUtils.compare(s1, s2);
	}
	
}
