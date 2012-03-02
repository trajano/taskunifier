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
package com.leclercb.taskunifier.api.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.beans.NoteBean;

public class Note extends AbstractModel implements ModelNote, PropertyChangeListener {
	
	public static final String PROP_FOLDER = "folder";
	
	private Folder folder;
	private String note;
	
	protected Note(NoteBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}
	
	protected Note(String title) {
		this(new ModelId(), title);
	}
	
	protected Note(ModelId modelId, String title) {
		super(modelId, title);
		
		this.setFolder(null);
		this.setNote(null);
		
		this.getFactory().register(this);
	}
	
	@Override
	public Note clone(ModelId modelId) {
		Note note = this.getFactory().create(modelId, this.getTitle());
		
		note.setFolder(this.getFolder());
		note.setNote(this.getNote());
		
		// After all other setXxx methods
		note.setOrder(this.getOrder());
		note.addProperties(this.getProperties());
		note.setModelStatus(this.getModelStatus());
		note.setModelCreationDate(Calendar.getInstance());
		note.setModelUpdateDate(Calendar.getInstance());
		
		return note;
	}
	
	@Override
	public NoteFactory<Note, NoteBean> getFactory() {
		return NoteFactory.getInstance();
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.NOTE;
	}
	
	@Override
	public void loadBean(ModelBean b, boolean loadReferenceIds) {
		CheckUtils.isNotNull(b);
		CheckUtils.isInstanceOf(b, NoteBean.class);
		
		NoteBean bean = (NoteBean) b;
		
		Folder folder = null;
		
		if (bean.getFolder() != null) {
			folder = FolderFactory.getInstance().get(bean.getFolder());
			if (folder == null)
				folder = FolderFactory.getInstance().createShell(
						bean.getFolder());
		}
		
		this.setFolder(folder);
		this.setNote(bean.getNote());
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public NoteBean toBean() {
		NoteBean bean = (NoteBean) super.toBean();
		
		bean.setFolder(this.getFolder() == null ? null : this.getFolder().getModelId());
		bean.setNote(this.getNote());
		
		return bean;
	}
	
	public Folder getFolder() {
		return this.folder;
	}
	
	public void setFolder(Folder folder) {
		if (!this.checkBeforeSet(this.getFolder(), folder))
			return;
		
		if (folder != null) {
			if (folder.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| folder.getModelStatus().equals(ModelStatus.DELETED)) {
				ApiLogger.getLogger().severe(
						"You cannot assign a deleted model");
				folder = null;
			}
		}
		
		if (this.folder != null)
			this.folder.removePropertyChangeListener(this);
		
		Folder oldFolder = this.folder;
		this.folder = folder;
		
		if (this.folder != null)
			this.folder.addPropertyChangeListener(this);
		
		this.updateProperty(PROP_FOLDER, oldFolder, folder);
	}
	
	@Override
	public String getNote() {
		return this.note;
	}
	
	@Override
	public void setNote(String note) {
		if (!this.checkBeforeSet(this.getNote(), note))
			return;
		
		String oldNote = this.note;
		this.note = note;
		this.updateProperty(PROP_NOTE, oldNote, note);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PROP_MODEL_STATUS)) {
			Folder folder = (Folder) event.getSource();
			
			if (folder.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| folder.getModelStatus().equals(ModelStatus.DELETED))
				this.setFolder(null);
		}
	}
	
	@Override
	public String toDetailedString() {
		StringBuffer buffer = new StringBuffer(super.toDetailedString());
		
		if (this.getFolder() != null)
			buffer.append("Folder: " + this.getFolder() + "\n");
		if (this.getNote() != null)
			buffer.append("Note: " + this.getNote() + "\n");
		
		return buffer.toString();
	}
	
}
