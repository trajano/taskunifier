package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.gui.components.modelnote.ModelNoteView;
import com.leclercb.taskunifier.gui.components.notes.NoteTableView;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherView;

public interface NoteView extends View {
	
	public abstract NoteSearcherView getNoteSearcherView();
	
	public abstract NoteTableView getNoteTableView();
	
	public abstract ModelNoteView getModelNoteView();
	
}
