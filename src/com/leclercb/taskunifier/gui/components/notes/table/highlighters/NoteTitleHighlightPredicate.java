package com.leclercb.taskunifier.gui.components.notes.table.highlighters;

import java.awt.Component;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import com.leclercb.taskunifier.gui.components.notes.NoteColumn;

public class NoteTitleHighlightPredicate implements HighlightPredicate {
	
	@Override
	public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
		if (adapter.getColumnIdentifierAt(adapter.convertColumnIndexToModel(adapter.column)) != NoteColumn.TITLE)
			return false;
		
		Object value = adapter.getValue(adapter.convertColumnIndexToModel(adapter.column));
		
		if (value == null || !(value instanceof String))
			return false;
		
		return ((String) value).length() == 0;
	}
	
}
