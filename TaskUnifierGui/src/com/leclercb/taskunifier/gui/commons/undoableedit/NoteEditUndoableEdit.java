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
package com.leclercb.taskunifier.gui.commons.undoableedit;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.translations.Translations;

public class NoteEditUndoableEdit extends AbstractUndoableEdit {
	
	private ModelId note;
	private NoteColumn column;
	private Object newValue;
	private Object oldValue;
	
	public NoteEditUndoableEdit(
			ModelId note,
			NoteColumn column,
			Object newValue,
			Object oldValue) {
		CheckUtils.isNotNull(note);
		CheckUtils.isNotNull(column);
		
		this.note = note;
		this.column = column;
		this.newValue = newValue;
		this.oldValue = oldValue;
	}
	
	@Override
	public String getPresentationName() {
		return Translations.getString("undo.edit");
	}
	
	@Override
	public void undo() throws CannotUndoException {
		Note note = NoteFactory.getInstance().get(this.note);
		
		if (note == null)
			return;
		
		if (!note.getModelStatus().isEndUserStatus())
			return;
		
		this.column.setProperty(note, this.oldValue);
		
		super.undo();
	}
	
	@Override
	public void redo() throws CannotRedoException {
		Note note = NoteFactory.getInstance().get(this.note);
		
		if (note == null)
			return;
		
		if (!note.getModelStatus().isEndUserStatus())
			return;
		
		this.column.setProperty(note, this.newValue);
		
		super.redo();
	}
	
}
