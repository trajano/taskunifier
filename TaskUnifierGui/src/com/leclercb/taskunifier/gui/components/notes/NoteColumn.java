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
package com.leclercb.taskunifier.gui.components.notes;

import java.util.Calendar;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.models.properties.ModelProperties;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum NoteColumn implements ModelProperties<Note> {
	
	MODEL(Note.class, Translations.getString("general.note.id"), false),
	MODEL_CREATION_DATE(Calendar.class, Translations.getString("general.creation_date"), false),
	MODEL_UPDATE_DATE(Calendar.class, Translations.getString("general.update_date"), false),
	TITLE(String.class, Translations.getString("general.note.title"), true),
	FOLDER(ModelList.class, Translations.getString("general.note.folder"), true),
	NOTE(String.class, Translations.getString("general.note.note"), false);
	
	private Class<?> type;
	private String label;
	private boolean editable;
	
	private NoteColumn(Class<?> type, String label, boolean editable) {
		this.setType(type);
		this.setLabel(label);
		this.setEditable(editable);
	}
	
	@Override
	public Class<?> getType() {
		return this.type;
	}
	
	private void setType(Class<?> type) {
		this.type = type;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	@Override
	public Object getProperty(Note note) {
		if (note == null)
			return null;
		
		switch (this) {
			case MODEL:
				return note;
			case MODEL_CREATION_DATE:
				return note.getModelCreationDate();
			case MODEL_UPDATE_DATE:
				return note.getModelUpdateDate();
			case TITLE:
				return note.getTitle();
			case FOLDER:
				return note.getFolders();
			case NOTE:
				return note.getNote();
			default:
				return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setProperty(Note note, Object value) {
		if (note == null)
			return;
		
		switch (this) {
			case MODEL:
				break;
			case MODEL_CREATION_DATE:
				break;
			case MODEL_UPDATE_DATE:
				break;
			case TITLE:
				note.setTitle((String) value);
				break;
			case FOLDER:
				note.setFolders((ModelList<Folder>) value);
				break;
			case NOTE:
				note.setNote((String) value);
				break;
		}
	}
	
}
