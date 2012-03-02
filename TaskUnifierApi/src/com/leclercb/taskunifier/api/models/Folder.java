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

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.FolderBean;
import com.leclercb.taskunifier.api.models.beans.ModelBean;

public class Folder extends AbstractModel {
	
	public static final String PROP_ARCHIVED = "archived";
	
	private boolean archived;
	
	protected Folder(FolderBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}
	
	protected Folder(String title) {
		this(new ModelId(), title);
	}
	
	protected Folder(ModelId modelId, String title) {
		super(modelId, title);
		
		this.setArchived(false);
		
		this.getFactory().register(this);
	}
	
	@Override
	public Folder clone(ModelId modelId) {
		Folder folder = this.getFactory().create(modelId, this.getTitle());
		
		folder.setArchived(this.isArchived());
		
		// After all other setXxx methods
		folder.setOrder(this.getOrder());
		folder.addProperties(this.getProperties());
		folder.setModelStatus(this.getModelStatus());
		folder.setModelCreationDate(Calendar.getInstance());
		folder.setModelUpdateDate(Calendar.getInstance());
		
		return folder;
	}
	
	@Override
	public FolderFactory<Folder, FolderBean> getFactory() {
		return FolderFactory.getInstance();
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.FOLDER;
	}
	
	@Override
	public void loadBean(ModelBean b, boolean loadReferenceIds) {
		CheckUtils.isNotNull(b);
		CheckUtils.isInstanceOf(b, FolderBean.class);
		
		FolderBean bean = (FolderBean) b;
		
		this.setArchived(bean.isArchived());
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public FolderBean toBean() {
		FolderBean bean = (FolderBean) super.toBean();
		
		bean.setArchived(this.isArchived());
		
		return bean;
	}
	
	public boolean isArchived() {
		return this.archived;
	}
	
	public void setArchived(boolean archived) {
		if (!this.checkBeforeSet(this.isArchived(), archived))
			return;
		
		boolean oldArchived = this.archived;
		this.archived = archived;
		this.updateProperty(PROP_ARCHIVED, oldArchived, archived);
	}
	
}
