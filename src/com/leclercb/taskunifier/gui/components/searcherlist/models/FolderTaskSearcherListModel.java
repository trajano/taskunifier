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
package com.leclercb.taskunifier.gui.components.searcherlist.models;

import javax.swing.SortOrder;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.models.FolderListModel;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.ModelCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.translations.Translations;

public class FolderTaskSearcherListModel extends FolderListModel implements TaskSearcherListModel {

	@Override
	public TaskSearcher getTaskSearcher(int index) {
		Folder folder = (Folder) getElementAt(index);

		TaskSorter sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(0, TaskColumn.DUE_DATE, SortOrder.ASCENDING));

		TaskFilter filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(TaskColumn.FOLDER, ModelCondition.EQUALS, folder));

		String title = (folder == null? Translations.getString("searcherlist.none") : folder.getTitle());
		TaskSearcher searcher = new TaskSearcher(title, filter, sorter);

		return searcher;
	}

}
