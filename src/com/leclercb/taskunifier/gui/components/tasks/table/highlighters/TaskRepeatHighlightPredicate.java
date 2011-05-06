package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import java.awt.Component;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class TaskRepeatHighlightPredicate implements HighlightPredicate {

	@Override
	public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
		if (adapter.getColumnIdentifierAt(adapter.convertColumnIndexToModel(adapter.column)) != TaskColumn.REPEAT)
			return false;

		Object value = adapter.getValue(adapter.convertColumnIndexToModel(adapter.column));

		if (value == null || !(value instanceof String))
			return false;

		return !SynchronizerUtils.getPlugin().getSynchronizerApi().isValidRepeatValue(
				(String) value);
	}

}
