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
package com.leclercb.taskunifier.gui.components.notes.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.gui.commons.undoableedit.NoteUndoableEdit;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public class NoteTableModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {
	
	private UndoSupport undoSupport;
	
	public NoteTableModel(UndoSupport undoSupport) {
		CheckUtils.isNotNull(undoSupport, "Undo support cannot be null");
		this.undoSupport = undoSupport;
		
		NoteFactory.getInstance().addListChangeListener(this);
		NoteFactory.getInstance().addPropertyChangeListener(this);
	}
	
	public Note getNote(int row) {
		return NoteFactory.getInstance().get(row);
	}
	
	public NoteColumn getNoteColumn(int col) {
		return NoteColumn.values()[col];
	}
	
	@Override
	public int getColumnCount() {
		return NoteColumn.values().length;
	}
	
	@Override
	public int getRowCount() {
		return NoteFactory.getInstance().size();
	}
	
	@Override
	public String getColumnName(int col) {
		return NoteColumn.values()[col].getLabel();
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return NoteColumn.values()[col].getType();
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Note note = NoteFactory.getInstance().get(row);
		return NoteColumn.values()[col].getProperty(note);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return NoteColumn.values()[col].isEditable();
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		Note note = NoteFactory.getInstance().get(row);
		NoteColumn column = NoteColumn.values()[col];
		
		Object oldValue = column.getProperty(note);
		
		if (!EqualsUtils.equals(oldValue, value)) {
			column.setProperty(note, value);
			this.undoSupport.postEdit(new NoteUndoableEdit(
					note.getModelId(),
					column,
					value,
					oldValue));
		}
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (!((Note) event.getValue()).getModelStatus().isEndUserStatus())
			return;
		
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.fireTableRowsInserted(event.getIndex(), event.getIndex());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.fireTableRowsDeleted(event.getIndex(), event.getIndex());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(Model.PROP_MODEL_STATUS)) {
			this.fireTableDataChanged();
		} else {
			int index = NoteFactory.getInstance().getIndexOf(
					(Note) event.getSource());
			this.fireTableRowsUpdated(index, index);
		}
	}
	
}
