package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import java.awt.Component;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

public class TaskSelectedHighlightPredicate implements HighlightPredicate {
	
	@Override
	public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
		return adapter.isSelected();
	}
	
}
