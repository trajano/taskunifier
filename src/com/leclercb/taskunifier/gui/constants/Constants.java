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
package com.leclercb.taskunifier.gui.constants;

import javax.swing.SortOrder;
import javax.swing.undo.UndoableEditSupport;

import com.leclercb.commons.gui.swing.undo.TransferActionListener;
import com.leclercb.commons.gui.swing.undo.UndoFireManager;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.Main;
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

public final class Constants {
	
	private Constants() {

	}
	
	public static final String TITLE = "TaskUnifier";
	public static final String VERSION = "0.7.5";
	
	public static final String VERSION_FILE = "http://taskunifier.sourceforge.net/version.txt";
	public static final String DOWNLOAD_URL = "http://sourceforge.net/projects/taskunifier/files/binaries/";
	public static final String DONATE_URL = "http://sourceforge.net/donate/index.php?group_id=380204";
	public static final String REVIEW_URL = "http://sourceforge.net/projects/taskunifier/reviews/";
	public static final String BUG_URL = "http://sourceforge.net/tracker/?group_id=380204";
	public static final String FEATURE_REQUEST_URL = "http://sourceforge.net/tracker/?group_id=380204";
	
	public static final UndoFireManager UNDO_MANAGER = new UndoFireManager();
	public static final UndoableEditSupport UNDO_EDIT_SUPPORT = new UndoableEditSupport();
	
	public static final TransferActionListener TRANSFER_ACTION_LISTENER = new TransferActionListener();
	
	public static final TaskSearcher[] GENERAL_TASK_SEARCHERS;
	
	static {
		UNDO_EDIT_SUPPORT.addUndoableEditListener(UNDO_MANAGER);
		
		GENERAL_TASK_SEARCHERS = new TaskSearcher[7];
		
		TaskFilter filter;
		TaskSorter sorter;
		
		sorter = new TaskSorter();
		
		if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end") != null
				&& Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end"))
			sorter.addElement(new TaskSorterElement(
					0,
					TaskColumn.COMPLETED,
					SortOrder.ASCENDING));
		
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				2,
				TaskColumn.PRIORITY,
				SortOrder.DESCENDING));
		sorter.addElement(new TaskSorterElement(
				3,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		// All Tasks
		filter = new TaskFilter();
		
		GENERAL_TASK_SEARCHERS[0] = new TaskSearcher(
				Translations.getString("searcherlist.general.all_tasks"),
				Images.getResourceFile("document.png"),
				filter,
				sorter.clone());
		
		// Overdue
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		filter.addElement(new TaskFilterElement(
				TaskColumn.DUE_DATE,
				DaysCondition.LESS_THAN_OR_EQUALS,
				0));
		
		GENERAL_TASK_SEARCHERS[1] = new TaskSearcher(
				Translations.getString("searcherlist.general.overdue"),
				Images.getResourceFile("warning.gif"),
				filter,
				sorter.clone());
		
		// Hot List
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		filter.addElement(new TaskFilterElement(
				TaskColumn.DUE_DATE,
				DaysCondition.LESS_THAN_OR_EQUALS,
				3));
		filter.addElement(new TaskFilterElement(
				TaskColumn.PRIORITY,
				EnumCondition.GREATER_THAN_OR_EQUALS,
				TaskPriority.HIGH));
		
		GENERAL_TASK_SEARCHERS[2] = new TaskSearcher(
				Translations.getString("searcherlist.general.hot_list"),
				Images.getResourceFile("hot_pepper.png"),
				filter,
				sorter.clone());
		
		// Importance
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		
		TaskSorter importanceSorter = new TaskSorter();
		
		importanceSorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.IMPORTANCE,
				SortOrder.DESCENDING));
		
		GENERAL_TASK_SEARCHERS[3] = new TaskSearcher(
				Translations.getString("searcherlist.general.importance"),
				Images.getResourceFile("importance.png"),
				filter,
				importanceSorter);
		
		// Starred
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		filter.addElement(new TaskFilterElement(
				TaskColumn.STAR,
				StringCondition.EQUALS,
				"true"));
		
		GENERAL_TASK_SEARCHERS[4] = new TaskSearcher(
				Translations.getString("searcherlist.general.starred"),
				Images.getResourceFile("star.png"),
				filter,
				sorter.clone());
		
		// Next Action
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		filter.addElement(new TaskFilterElement(
				TaskColumn.STATUS,
				EnumCondition.EQUALS,
				TaskStatus.NEXT_ACTION));
		
		GENERAL_TASK_SEARCHERS[5] = new TaskSearcher(
				Translations.getString("searcherlist.general.next_action"),
				Images.getResourceFile("next.png"),
				filter,
				sorter.clone());
		
		// Completed
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"true"));
		
		GENERAL_TASK_SEARCHERS[6] = new TaskSearcher(
				Translations.getString("searcherlist.general.completed"),
				Images.getResourceFile("check.png"),
				filter,
				sorter.clone());
	}
	
}
