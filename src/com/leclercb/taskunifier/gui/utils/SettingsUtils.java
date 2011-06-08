package com.leclercb.taskunifier.gui.utils;

import java.util.List;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class SettingsUtils {
	
	private SettingsUtils() {

	}
	
	public static void resetImportanceColors() {
		Main.SETTINGS.setStringProperty("theme.color.importance.0", "-1");
		Main.SETTINGS.setStringProperty("theme.color.importance.1", "-1");
		Main.SETTINGS.setStringProperty("theme.color.importance.2", "-1");
		Main.SETTINGS.setStringProperty("theme.color.importance.3", "-6684673");
		Main.SETTINGS.setStringProperty("theme.color.importance.4", "-3342337");
		Main.SETTINGS.setStringProperty("theme.color.importance.5", "-6684724");
		Main.SETTINGS.setStringProperty("theme.color.importance.6", "-3342388");
		Main.SETTINGS.setStringProperty("theme.color.importance.7", "-3342439");
		Main.SETTINGS.setStringProperty("theme.color.importance.8", "-52");
		Main.SETTINGS.setStringProperty("theme.color.importance.9", "-103");
		Main.SETTINGS.setStringProperty("theme.color.importance.10", "-13159");
		Main.SETTINGS.setStringProperty("theme.color.importance.11", "-13108");
		Main.SETTINGS.setStringProperty("theme.color.importance.12", "-26215");
	}
	
	public static void resetPriorityColors() {
		Main.SETTINGS.setStringProperty(
				"theme.color.priority.negative",
				"-8355712");
		Main.SETTINGS.setStringProperty("theme.color.priority.low", "-256");
		Main.SETTINGS.setStringProperty(
				"theme.color.priority.medium",
				"-16711936");
		Main.SETTINGS.setStringProperty("theme.color.priority.high", "-14336");
		Main.SETTINGS.setStringProperty("theme.color.priority.top", "-65536");
	}
	
	public static void removeCompletedCondition() {
		List<TaskSearcher> searchers = TaskSearcherFactory.getInstance().getList();
		for (TaskSearcher searcher : searchers) {
			removeCompletedCondition(searcher.getFilter());
		}
	}
	
	private static void removeCompletedCondition(TaskFilter filter) {
		List<TaskFilterElement> elements = filter.getElements();
		List<TaskFilter> filters = filter.getFilters();
		
		for (TaskFilterElement e : elements) {
			if (e.getColumn() == TaskColumn.COMPLETED)
				if (e.getValue().equals(Boolean.FALSE))
					filter.removeElement(e);
		}
		
		for (TaskFilter f : filters) {
			removeCompletedCondition(f);
		}
	}
	
}
