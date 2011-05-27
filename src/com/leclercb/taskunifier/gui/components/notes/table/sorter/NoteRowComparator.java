/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.notes.table.sorter;

import java.util.Comparator;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class NoteRowComparator implements Comparator<Note> {
	
	private static NoteRowComparator INSTANCE;
	
	public static NoteRowComparator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new NoteRowComparator();
		
		return INSTANCE;
	}
	
	private NoteRowComparator() {

	}
	
	@Override
	public int compare(Note note1, Note note2) {
		int result = 0;
		
		NoteColumn[] noteColumns = new NoteColumn[] {
				NoteColumn.FOLDER,
				NoteColumn.TITLE };
		
		for (NoteColumn noteColumn : noteColumns) {
			Object o1 = noteColumn.getValue(note1);
			Object o2 = noteColumn.getValue(note2);
			
			result = this.compare(noteColumn, o1, o2);
			
			if (result != 0)
				return result;
		}
		
		return result;
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
