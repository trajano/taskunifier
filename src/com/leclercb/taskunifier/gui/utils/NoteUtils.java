package com.leclercb.taskunifier.gui.utils;

import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;

public final class NoteUtils {
	
	private NoteUtils() {

	}
	
	public static boolean showNote(Note note, NoteFilter filter) {
		if (!note.getModelStatus().equals(ModelStatus.LOADED)
				&& !note.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			return false;
		}
		
		if (filter == null)
			return true;
		
		return filter.include(note);
	}
	
}
