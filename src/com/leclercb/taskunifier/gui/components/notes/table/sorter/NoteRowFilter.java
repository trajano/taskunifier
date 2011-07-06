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

import java.util.List;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.components.notes.table.NoteTableModel;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class NoteRowFilter extends RowFilter<TableModel, Integer> {
	
	private TaskFilter filter;
	private String titleFilter;
	
	public NoteRowFilter(TaskFilter filter, String titleFilter) {
		this.setFilter(filter);
		this.setTitleFilter(titleFilter);
	}
	
	public TaskFilter getFilter() {
		return this.filter;
	}
	
	public void setFilter(TaskFilter filter) {
		this.filter = filter;
	}
	
	public String getTitleFilter() {
		return this.titleFilter;
	}
	
	public void setTitleFilter(String titleFilter) {
		this.titleFilter = titleFilter;
	}
	
	@Override
	public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
		NoteTableModel noteTableModel = (NoteTableModel) entry.getModel();
		Note note = noteTableModel.getNote(entry.getIdentifier());
		
		if (!note.getModelStatus().equals(ModelStatus.LOADED)
				&& !note.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			return false;
		}
		
		if (this.filter != null) {
			List<TaskFilterElement> elements = this.filter.getElements();
			for (TaskFilterElement e : elements) {
				if (e.getColumn() == TaskColumn.FOLDER) {
					if (!EqualsUtils.equals(note.getFolder(), e.getValue()))
						return false;
					
					break;
				}
			}
		}
		
		if (this.titleFilter != null) {
			String filter = this.titleFilter.toLowerCase();
			
			if (!note.getTitle().toLowerCase().contains(filter)
					&& !note.getNote().toLowerCase().contains(filter))
				return false;
		}
		
		return true;
	}
	
}
