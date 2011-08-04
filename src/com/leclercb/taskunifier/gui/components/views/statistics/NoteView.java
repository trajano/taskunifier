package com.leclercb.taskunifier.gui.components.views.statistics;

import com.leclercb.taskunifier.gui.components.notes.NoteTableView;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherView;
import com.leclercb.taskunifier.gui.components.views.View;

public interface NoteView extends View {
	
	public abstract NoteSearcherView getNoteSearcherView();
	
	public abstract NoteTableView getNoteTableView();
	
}
