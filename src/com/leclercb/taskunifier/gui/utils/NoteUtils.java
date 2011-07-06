package com.leclercb.taskunifier.gui.utils;

import java.util.List;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public final class NoteUtils {
	
	private NoteUtils() {

	}
	
	public static boolean showNote(
			Note note,
			String titleFilter,
			TaskFilter filter) {
		if (!note.getModelStatus().equals(ModelStatus.LOADED)
				&& !note.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			return false;
		}
		
		if (filter != null) {
			if (!EqualsUtils.equals(note.getFolder(), findFolder(filter)))
				return false;
		}
		
		if (titleFilter != null) {
			titleFilter = titleFilter.toLowerCase();
			String noteTitle = note.getTitle();
			String noteNote = note.getNote();
			
			if (noteTitle == null)
				noteTitle = "";
			
			if (noteNote == null)
				noteNote = "";
			
			if (!noteTitle.toLowerCase().contains(titleFilter)
					&& !noteNote.toLowerCase().contains(titleFilter))
				return false;
		}
		
		return true;
	}
	
	private static Folder findFolder(TaskFilter rootFilter) {
		List<TaskFilterElement> elements = rootFilter.getElements();
		for (TaskFilterElement e : elements) {
			if (e.getColumn() == TaskColumn.FOLDER) {
				return (Folder) e.getValue();
			}
		}
		
		List<TaskFilter> filters = rootFilter.getFilters();
		for (TaskFilter filter : filters) {
			Folder folder = findFolder(filter);
			
			if (folder != null)
				return folder;
		}
		
		return null;
	}
	
}
