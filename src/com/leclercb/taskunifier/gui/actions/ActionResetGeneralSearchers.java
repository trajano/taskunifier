package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.SortOrder;

import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.DaysCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.EnumCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ActionResetGeneralSearchers extends AbstractAction {
	
	public ActionResetGeneralSearchers() {
		this(32, 32);
	}
	
	public ActionResetGeneralSearchers(int width, int height) {
		super(
				Translations.getString("action.reset_general_searchers"),
				Images.getResourceImage("undo.png", width, height));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionResetGeneralSearchers.resetGeneralSearchers();
	}
	
	public static void resetGeneralSearchers() {
		List<TaskSearcher> oldSearchers = TaskSearcherFactory.getInstance().getList();
		
		TaskFilter filter;
		TaskSorter sorter;
		
		sorter = new TaskSorter();
		
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
		
		// Not Completed
		filter = new TaskFilter();
		
		TaskSearcherFactory.getInstance().create(
				TaskSearcherType.GENERAL,
				1,
				Translations.getString("searcherlist.general.not_completed"),
				Images.getResourceFile("check.png"),
				filter,
				sorter.clone());
		
		// Overdue
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.DUE_DATE,
				DaysCondition.LESS_THAN_OR_EQUALS,
				0));
		
		TaskSearcherFactory.getInstance().create(
				TaskSearcherType.GENERAL,
				2,
				Translations.getString("searcherlist.general.overdue"),
				Images.getResourceFile("warning.gif"),
				filter,
				sorter.clone());
		
		// Hot List
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.DUE_DATE,
				DaysCondition.LESS_THAN_OR_EQUALS,
				3));
		filter.addElement(new TaskFilterElement(
				TaskColumn.PRIORITY,
				EnumCondition.GREATER_THAN_OR_EQUALS,
				TaskPriority.HIGH));
		
		TaskSearcherFactory.getInstance().create(
				TaskSearcherType.GENERAL,
				3,
				Translations.getString("searcherlist.general.hot_list"),
				Images.getResourceFile("hot_pepper.png"),
				filter,
				sorter.clone());
		
		// Importance
		filter = new TaskFilter();
		
		TaskSorter importanceSorter = new TaskSorter();
		
		importanceSorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.IMPORTANCE,
				SortOrder.DESCENDING));
		
		TaskSearcherFactory.getInstance().create(
				TaskSearcherType.GENERAL,
				4,
				Translations.getString("searcherlist.general.importance"),
				Images.getResourceFile("importance.png"),
				filter,
				importanceSorter);
		
		// Starred
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.STAR,
				StringCondition.EQUALS,
				"true"));
		
		TaskSearcherFactory.getInstance().create(
				TaskSearcherType.GENERAL,
				5,
				Translations.getString("searcherlist.general.starred"),
				Images.getResourceFile("star.png"),
				filter,
				sorter.clone());
		
		// Next Action
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.STATUS,
				EnumCondition.EQUALS,
				TaskStatus.NEXT_ACTION));
		
		TaskSearcherFactory.getInstance().create(
				TaskSearcherType.GENERAL,
				6,
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
		
		TaskSearcherFactory.getInstance().create(
				TaskSearcherType.GENERAL,
				7,
				Translations.getString("searcherlist.general.completed"),
				Images.getResourceFile("check.png"),
				filter,
				sorter.clone());
		
		for (TaskSearcher searcher : oldSearchers) {
			if (searcher.getType().equals(TaskSearcherType.GENERAL))
				TaskSearcherFactory.getInstance().delete(searcher);
		}
	}
	
}
