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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.models.properties.ModelProperties;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.table.TUColumn;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum NoteColumn implements ModelProperties<Note>, TUColumn<Note>, PropertyChangeListener, PropertyChangeSupported {
	
	MODEL(Note.class, Translations.getString("general.note.id"), false, true),
	MODEL_CREATION_DATE(Calendar.class, Translations.getString("general.creation_date"), false, true),
	MODEL_UPDATE_DATE(Calendar.class, Translations.getString("general.update_date"), false, true),
	TITLE(String.class, Translations.getString("general.note.title"), true, true),
	FOLDER(Folder.class, Translations.getString("general.note.folder"), true, true),
	NOTE(String.class, Translations.getString("general.note.note"), false, true);
	
	public static final String PROP_USED = "used";
	
	public static NoteColumn[] getUsedColumns() {
		return getUsedColumns(true);
	}
	
	public static NoteColumn[] getUsedColumns(boolean includeNote) {
		List<NoteColumn> columns = new ArrayList<NoteColumn>();
		
		for (NoteColumn column : values()) {
			if (column.isUsable() && column.isUsed())
				columns.add(column);
		}
		
		if (!includeNote)
			columns.remove(NoteColumn.NOTE);
		
		return columns.toArray(new NoteColumn[0]);
	}
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private Class<?> type;
	private String label;
	private boolean editable;
	private boolean usable;
	private boolean used;
	
	private NoteColumn(
			Class<?> type,
			String label,
			boolean editable,
			boolean usable) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setType(type);
		this.setLabel(label);
		this.setEditable(editable);
		this.setUsable(usable);
		
		this.setUsed(Main.getSettings().getBooleanProperty(
				"note.field." + this.name().toLowerCase() + ".used",
				true));
		
		Main.getSettings().addPropertyChangeListener(
				"note.field." + this.name().toLowerCase() + ".used",
				new WeakPropertyChangeListener(Main.getSettings(), this));
	}
	
	@Override
	public Class<?> getType() {
		return this.type;
	}
	
	private void setType(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isUsable() {
		return this.usable;
	}
	
	public void setUsable(boolean usable) {
		this.usable = usable;
	}
	
	public boolean isUsed() {
		return this.used;
	}
	
	public void setUsed(boolean used) {
		if (used == this.isUsed())
			return;
		
		boolean oldUsed = this.isUsed();
		this.used = used;
		
		Main.getSettings().setBooleanProperty(
				"note.field." + this.name().toLowerCase() + ".used",
				used);
		
		this.propertyChangeSupport.firePropertyChange(PROP_USED, oldUsed, used);
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
				return note.getFolder();
			case NOTE:
				return note.getNote();
			default:
				return null;
		}
	}
	
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
				note.setFolder((Folder) value);
				break;
			case NOTE:
				note.setNote((String) value);
				break;
		}
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.setUsed(Boolean.parseBoolean(evt.getNewValue().toString()));
	}
	
}
