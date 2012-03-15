/*
 * FileUnifier
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
 *   - Neither the name of FileUnifier or the names of its
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
package com.leclercb.taskunifier.api.models.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.FileList;
import com.leclercb.taskunifier.api.models.FileList.FileItem;
import com.leclercb.taskunifier.api.models.beans.FileListBean.FileItemBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class FileListBean implements Cloneable, Serializable, Iterable<FileItemBean> {
	
	@XStreamAlias("filelist")
	private List<FileItemBean> files;
	
	public FileListBean() {
		this.files = new ArrayList<FileItemBean>();
	}
	
	@Override
	protected FileListBean clone() {
		FileListBean list = new FileListBean();
		list.files.addAll(this.files);
		return list;
	}
	
	@Override
	public Iterator<FileItemBean> iterator() {
		return this.files.iterator();
	}
	
	public List<FileItemBean> getList() {
		return new ArrayList<FileItemBean>(this.files);
	}
	
	public void add(FileItemBean item) {
		CheckUtils.isNotNull(item);
		this.files.add(item);
	}
	
	public void addAll(Collection<FileItemBean> items) {
		if (items == null)
			return;
		
		for (FileItemBean item : items)
			this.add(item);
	}
	
	public void remove(FileItemBean item) {
		CheckUtils.isNotNull(item);
		this.files.remove(item);
	}
	
	public void clear() {
		for (FileItemBean item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.files.size();
	}
	
	public int getIndexOf(FileItemBean item) {
		return this.files.indexOf(item);
	}
	
	public FileItemBean get(int index) {
		return this.files.get(index);
	}
	
	public FileList toFileGroup() {
		FileList list = new FileList();
		
		for (FileItemBean item : this)
			list.add(item.toFileItem());
		
		return list;
	}
	
	@XStreamAlias("fileitem")
	public static class FileItemBean {
		
		@XStreamAlias("file")
		private String file;
		
		@XStreamAlias("link")
		private String link;
		
		public FileItemBean() {
			this(null, null);
		}
		
		public FileItemBean(String file, String link) {
			this.setFile(file);
			this.setLink(link);
		}
		
		public String getFile() {
			return this.file;
		}
		
		public void setFile(String file) {
			this.file = file;
		}
		
		public String getLink() {
			return this.link;
		}
		
		public void setLink(String link) {
			this.link = link;
		}
		
		public FileItem toFileItem() {
			return new FileItem(this.file, this.link);
		}
		
	}
	
}
