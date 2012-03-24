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
package com.leclercb.taskunifier.gui.api.models.beans;

import java.util.List;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.beans.NoteBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ComNoteBean extends GuiNoteBean {
	
	@XStreamAlias("foldertitle")
	private String folderTitle;
	
	public ComNoteBean() {
		this((ModelId) null);
	}
	
	public ComNoteBean(ModelId modelId) {
		super(modelId);
		
		this.setFolderTitle(null);
	}
	
	public ComNoteBean(NoteBean bean) {
		super(bean);
		
		if (bean instanceof ComNoteBean)
			this.setFolderTitle(((ComNoteBean) bean).getFolderTitle());
	}
	
	public String getFolderTitle() {
		return this.folderTitle;
	}
	
	public void setFolderTitle(String folderTitle) {
		this.folderTitle = folderTitle;
	}
	
	public void loadTitles(boolean removeModelId) {
		if (this.getFolder() != null) {
			Folder folder = FolderFactory.getInstance().get(this.getFolder());
			if (folder != null)
				this.setFolderTitle(folder.getTitle());
			
			if (removeModelId)
				this.setFolder(null);
		}
	}
	
	public void loadModels(boolean removeTitle) {
		if (this.getFolder() == null) {
			if (this.getFolderTitle() != null) {
				List<Folder> models = FolderFactory.getInstance().getList();
				for (Folder model : models) {
					if (!model.getModelStatus().isEndUserStatus())
						continue;
					
					if (model.getTitle().equalsIgnoreCase(this.getFolderTitle())) {
						this.setFolder(model.getModelId());
						break;
					}
				}
				
				if (removeTitle)
					this.setFolderTitle(null);
			}
		}
	}
	
}
