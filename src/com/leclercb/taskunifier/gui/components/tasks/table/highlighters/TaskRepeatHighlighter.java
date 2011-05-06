package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import java.awt.Color;
import java.awt.Component;

import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

public class TaskRepeatHighlighter extends AbstractHighlighter {
	
	public TaskRepeatHighlighter(HighlightPredicate predicate) {
		super(predicate);
	}
	
	@Override
	protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
		renderer.setForeground(Color.RED);
		return renderer;
	}
	
}
