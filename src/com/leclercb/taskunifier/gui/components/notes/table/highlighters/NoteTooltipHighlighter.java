package com.leclercb.taskunifier.gui.components.notes.table.highlighters;

import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.ToolTipHighlighter;

import com.leclercb.taskunifier.gui.components.notes.table.renderers.StringValueTitle;

public class NoteTooltipHighlighter extends ToolTipHighlighter {

	public NoteTooltipHighlighter(HighlightPredicate predicate) {
		super(predicate, new StringValueTitle());
	}

}
