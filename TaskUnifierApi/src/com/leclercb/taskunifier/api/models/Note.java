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

import java.util.Calendar;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.beans.NoteBean;

public class Note extends AbstractModel implements ModelNote, ListChangeListener {
	
	public static final String PROP_FOLDERS = "folders";
	
	private ModelList<Folder> folders;
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
		
		this.folders = new ModelList<Folder>();
		this.folders.addListChangeListener(this);
		
		this.setFolders(new ModelList<Folder>());
		this.setNote(null);
		
		this.getFactory().register(this);
	}
	
	@Override
	public Note clone(ModelId modelId) {
		Note note = this.getFactory().create(modelId, this.getTitle());
		
		note.setFolders(this.getFolders());
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
		
		this.setFolders((bean.getFolders() == null ? null : bean.getFolders().toModelList(
				new ModelList<Folder>(),
				ModelType.FOLDER)));
		this.setNote(bean.getNote());
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public NoteBean toBean() {
		NoteBean bean = (NoteBean) super.toBean();
		
		bean.setFolders(this.getFolders().toModelBeanList());
		bean.setNote(this.getNote());
		
		return bean;
	}
	
	public ModelList<Folder> getFolders() {
		return this.folders;
	}
	
	public void setFolders(ModelList<Folder> folder) {
		this.folders.clear();
		
		if (this.folders != null)
			this.folders.addAll(this.folders.getList());
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
	public void listChange(ListChangeEvent event) {
		if (event.getSource().equals(this.folders)) {
			this.updateProperty(PROP_FOLDERS, null, this.folders);
		}
	}
	
}
