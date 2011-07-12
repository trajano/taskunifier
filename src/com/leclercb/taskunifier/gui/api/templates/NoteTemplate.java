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
package com.leclercb.taskunifier.gui.api.templates;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.templates.converters.NoteTemplateConverter;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Reviewed
@XStreamConverter(NoteTemplateConverter.class)
public class NoteTemplate implements Cloneable, Serializable, PropertyChangeSupported {
	
	public static final String PROP_TITLE = "title";
	public static final String PROP_NOTE_TITLE = "noteTitle";
	public static final String PROP_NOTE_FOLDER = "noteFolder";
	public static final String PROP_NOTE_NOTE = "noteNote";
	
	@XStreamOmitField
	private PropertyChangeSupport propertyChangeSupport;
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("notetitle")
	private String noteTitle;
	
	@XStreamAlias("notefolder")
	private ModelId noteFolder;
	
	@XStreamAlias("notenote")
	private String noteNote;
	
	public NoteTemplate() {
		this("");
	}
	
	public NoteTemplate(String title) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setTitle(title);
		this.setNoteTitle(null);
		this.setNoteFolder(null);
		this.setNoteNote(null);
	}
	
	public void applyToNote(Note note) {
		if (note == null)
			return;
		
		if (this.noteTitle != null && this.noteTitle.length() != 0)
			note.setTitle(this.noteTitle);
		
		if (this.noteFolder != null)
			note.setFolder(FolderFactory.getInstance().get(this.noteFolder));
		
		if (this.noteNote != null && this.noteNote.length() != 0)
			note.setNote(this.noteNote);
	}
	
	@Override
	public NoteTemplate clone() {
		NoteTemplate template = new NoteTemplate(this.title);
		
		template.setNoteTitle(this.noteTitle);
		template.setNoteFolder(this.noteFolder);
		template.setNoteNote(this.noteNote);
		
		return template;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		CheckUtils.isNotNull(title, "Title cannot be null");
		String oldTitle = this.title;
		this.title = title;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TITLE,
				oldTitle,
				title);
	}
	
	public String getNoteTitle() {
		return this.noteTitle;
	}
	
	public void setNoteTitle(String noteTitle) {
		String oldNoteTitle = this.noteTitle;
		this.noteTitle = noteTitle;
		this.propertyChangeSupport.firePropertyChange(
				PROP_NOTE_TITLE,
				oldNoteTitle,
				noteTitle);
	}
	
	public ModelId getNoteFolder() {
		return this.noteFolder;
	}
	
	public void setNoteFolder(ModelId noteFolder) {
		ModelId oldNoteFolder = this.noteFolder;
		this.noteFolder = noteFolder;
		this.propertyChangeSupport.firePropertyChange(
				PROP_NOTE_FOLDER,
				oldNoteFolder,
				noteFolder);
	}
	
	public String getNoteNote() {
		return this.noteNote;
	}
	
	public void setNoteNote(String noteNote) {
		String oldNoteNote = this.noteNote;
		this.noteNote = noteNote;
		this.propertyChangeSupport.firePropertyChange(
				PROP_NOTE_NOTE,
				oldNoteNote,
				noteNote);
	}
	
	@Override
	public String toString() {
		return this.title;
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
	
}
