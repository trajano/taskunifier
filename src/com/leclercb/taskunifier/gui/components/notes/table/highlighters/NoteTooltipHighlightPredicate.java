package com.leclercb.taskunifier.gui.components.notes.table.highlighters;

import java.awt.Component;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import com.leclercb.taskunifier.gui.components.notes.NoteColumn;

public class NoteTooltipHighlightPredicate implements HighlightPredicate {

	@Override
	public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
		if (adapter.getColumnIdentifierAt(adapter.convertColumnIndexToModel(adapter.column)) != NoteColumn.TITLE)
			return false;

		return true;
	}

}
