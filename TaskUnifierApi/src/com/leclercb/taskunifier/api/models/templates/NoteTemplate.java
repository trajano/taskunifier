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
package com.leclercb.taskunifier.api.models.templates;

import java.util.Calendar;

import com.leclercb.taskunifier.api.models.AbstractBasicModel;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.beans.NoteBean;
import com.leclercb.taskunifier.api.models.beans.converters.FolderConverter;
import com.leclercb.taskunifier.api.models.templates.converters.NoteTemplateConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamConverter(NoteTemplateConverter.class)
public class NoteTemplate extends AbstractBasicModel implements Template<Note, NoteBean> {
	
	public static final String PROP_NOTE_TITLE = "noteTitle";
	public static final String PROP_NOTE_FOLDER_FORCE = "noteFolderForce";
	public static final String PROP_NOTE_FOLDER = "noteFolder";
	public static final String PROP_NOTE_NOTE = "noteNote";
	
	@XStreamAlias("notetitle")
	private String noteTitle;
	
	@XStreamAlias("notefolderforce")
	private boolean noteFolderForce;
	
	@XStreamAlias("notefolder")
	@XStreamConverter(FolderConverter.class)
	private Folder noteFolder;
	
	@XStreamAlias("notenote")
	private String noteNote;
	
	public NoteTemplate() {
		super(new ModelId(), "");
	}
	
	public NoteTemplate(String title) {
		super(new ModelId(), title);
	}
	
	public NoteTemplate(ModelId modelId, String title) {
		super(modelId, title);
		
		this.setTitle(title);
		this.setNoteTitle(null);
		this.setNoteFolder(null, false);
		this.setNoteNote(null);
	}
	
	@Override
	public NoteTemplate clone(ModelId modelId) {
		NoteTemplate template = new NoteTemplate(modelId, this.getTitle());
		
		template.setNoteTitle(this.noteTitle);
		template.setNoteFolder(this.noteFolder, this.noteFolderForce);
		template.setNoteNote(this.noteNote);
		
		// After all other setXxx methods
		template.addProperties(this.getProperties());
		template.setModelStatus(this.getModelStatus());
		template.setModelCreationDate(Calendar.getInstance());
		template.setModelUpdateDate(Calendar.getInstance());
		
		return template;
	}
	
	@Override
	public void applyTo(Note note) {
		if (note == null)
			return;
		
		if (this.noteTitle != null && this.noteTitle.length() != 0)
			note.setTitle(this.noteTitle);
		
		if (this.noteFolderForce || this.noteFolder != null)
			note.setFolder(this.noteFolder);
		
		if (this.noteNote != null && this.noteNote.length() != 0)
			note.setNote(this.noteNote);
	}
	
	@Override
	public void applyTo(NoteBean note) {
		if (note == null)
			return;
		
		if (this.noteTitle != null && this.noteTitle.length() != 0)
			note.setTitle(this.noteTitle);
		
		if (this.noteFolderForce || this.noteFolder != null)
			note.setFolder(this.noteFolder.getModelId());
		
		if (this.noteNote != null && this.noteNote.length() != 0)
			note.setNote(this.noteNote);
	}
	
	public String getNoteTitle() {
		return this.noteTitle;
	}
	
	public void setNoteTitle(String noteTitle) {
		String oldNoteTitle = this.noteTitle;
		this.noteTitle = noteTitle;
		this.updateProperty(PROP_NOTE_TITLE, oldNoteTitle, noteTitle);
	}
	
	public boolean isNoteFolderForce() {
		return this.noteFolderForce;
	}
	
	public void setNoteFolderForce(boolean force) {
		boolean oldNoteFolderForce = this.noteFolderForce;
		this.noteFolderForce = force;
		this.updateProperty(PROP_NOTE_FOLDER_FORCE, oldNoteFolderForce, force);
	}
	
	public Folder getNoteFolder() {
		return this.noteFolder;
	}
	
	public void setNoteFolder(Folder noteFolder) {
		this.setNoteFolder(noteFolder, false);
	}
	
	public void setNoteFolder(Folder noteFolder, boolean force) {
		Folder oldNoteFolder = this.noteFolder;
		this.noteFolder = noteFolder;
		
		boolean oldNoteFolderForce = this.noteFolderForce;
		this.noteFolderForce = force;
		
		this.updateProperty(PROP_NOTE_FOLDER_FORCE, oldNoteFolderForce, force);
		
		this.updateProperty(PROP_NOTE_FOLDER, oldNoteFolder, noteFolder);
	}
	
	public String getNoteNote() {
		return this.noteNote;
	}
	
	public void setNoteNote(String noteNote) {
		String oldNoteNote = this.noteNote;
		this.noteNote = noteNote;
		this.updateProperty(PROP_NOTE_NOTE, oldNoteNote, noteNote);
	}
	
}
