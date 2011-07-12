package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.Comparator;
import java.util.List;

import javax.swing.SortOrder;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorterElement;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;

public class NoteComparator implements Comparator<Note> {
	
	private NoteSorter sorter;
	
	public NoteComparator() {
		this.sorter = null;
	}
	
	public NoteSorter getNoteSorter() {
		return this.sorter;
	}
	
	public void setNoteSorter(NoteSorter sorter) {
		this.sorter = sorter;
	}
	
	@Override
	public int compare(Note note1, Note note2) {
		if (this.sorter == null)
			return 0;
		
		List<NoteSorterElement> sortElements = this.sorter.getElements();
		
		for (NoteSorterElement element : sortElements) {
			int result = this.compare(element.getProperty(), note1, note2);
			
			result *= (element.getSortOrder().equals(SortOrder.ASCENDING) ? 1 : -1);
			
			if (result != 0)
				return result;
		}
		
		return this.compare(NoteColumn.MODEL, note1, note2);
	}
	
	private int compare(NoteColumn column, Object o1, Object o2) {
		int result = 0;
		
		switch (column) {
			case MODEL:
				result = CompareUtils.compare(
						((Note) o1).getModelId(),
						((Note) o2).getModelId());
				break;
			case TITLE:
				result = CompareUtils.compare((String) o1, (String) o2);
				break;
			case FOLDER:
				result = this.compareModels(((Folder) o1), ((Folder) o2));
				break;
			case NOTE:
				result = CompareUtils.compare((String) o1, (String) o2);
				break;
			default:
				result = 0;
				break;
		}
		
		return result;
	}
	
	private int compareModels(Model model1, Model model2) {
		if (model1 == null && model2 == null)
			return 0;
		
		if (model1 == null)
			return 1;
		
		if (model2 == null)
			return -1;
		
		return model1.getTitle().compareTo(model2.getTitle());
	}
	
}
