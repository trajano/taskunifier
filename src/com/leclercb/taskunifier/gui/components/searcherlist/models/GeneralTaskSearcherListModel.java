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

import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.SortOrder;

import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.DaysCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.EnumCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.translations.Translations;

public class GeneralTaskSearcherListModel extends AbstractListModel implements
		TaskSearcherListModel {

	private static final TaskSearcher[] TASK_SEARCHERS;

	static {
		TASK_SEARCHERS = new TaskSearcher[5];

		TaskFilter filter;
		TaskSorter sorter;

		// All Tasks
		filter = new TaskFilter();

		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(0, TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(1, TaskColumn.TITLE,
				SortOrder.ASCENDING));

		TASK_SEARCHERS[0] = new TaskSearcher(
				Translations.getString("searcherlist.general.all_tasks"),
				Images.getResourceFile("document.png"), filter, sorter);

		// Hot List
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(TaskColumn.COMPLETED,
				StringCondition.EQUALS, "false"));
		filter.addElement(new TaskFilterElement(TaskColumn.DUE_DATE,
				DaysCondition.LESS_THAN_OR_EQUALS, 3));
		filter.addElement(new TaskFilterElement(TaskColumn.PRIORITY,
				EnumCondition.GREATER_THAN_OR_EQUALS, TaskPriority.HIGH));

		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(0, TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(1, TaskColumn.TITLE,
				SortOrder.ASCENDING));

		TASK_SEARCHERS[1] = new TaskSearcher(
				Translations.getString("searcherlist.general.hot_list"),
				Images.getResourceFile("hot_pepper.png"), filter, sorter);

		// Starred
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(TaskColumn.COMPLETED,
				StringCondition.EQUALS, "false"));
		filter.addElement(new TaskFilterElement(TaskColumn.STAR,
				StringCondition.EQUALS, "true"));

		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(0, TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(1, TaskColumn.TITLE,
				SortOrder.ASCENDING));

		TASK_SEARCHERS[2] = new TaskSearcher(
				Translations.getString("searcherlist.general.starred"),
				Images.getResourceFile("star.png"), filter, sorter);

		// Next Action
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(TaskColumn.COMPLETED,
				StringCondition.EQUALS, "false"));
		filter.addElement(new TaskFilterElement(TaskColumn.STATUS,
				EnumCondition.EQUALS, TaskStatus.NEXT_ACTION));

		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(0, TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(1, TaskColumn.TITLE,
				SortOrder.ASCENDING));

		TASK_SEARCHERS[3] = new TaskSearcher(
				Translations.getString("searcherlist.general.next_action"),
				Images.getResourceFile("next.png"), filter, sorter);

		// Completed
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(TaskColumn.COMPLETED,
				StringCondition.EQUALS, "true"));

		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(0, TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(1, TaskColumn.TITLE,
				SortOrder.ASCENDING));

		TASK_SEARCHERS[4] = new TaskSearcher(
				Translations.getString("searcherlist.general.completed"),
				Images.getResourceFile("check.png"), filter, sorter);
	}

	private List<TaskSearcher> searchers;

	public GeneralTaskSearcherListModel() {
		this.searchers = Arrays.asList(TASK_SEARCHERS);
	}

	@Override
	public TaskSearcher getTaskSearcher(int index) {
		return (TaskSearcher) this.getElementAt(index);
	}

	@Override
	public Object getElementAt(int index) {
		return this.searchers.get(index);
	}

	@Override
	public int getSize() {
		return this.searchers.size();
	}

}
