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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.FileList.FileItem;
import com.leclercb.taskunifier.api.models.beans.FileListBean;
import com.leclercb.taskunifier.api.models.beans.FileListBean.FileItemBean;

public class FileList implements Cloneable, Serializable, Iterable<FileItem>, PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<FileItem> files;
	
	public FileList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.files = new ArrayList<FileItem>();
	}
	
	@Override
	protected FileList clone() {
		FileList list = new FileList();
		list.files.addAll(this.files);
		return list;
	}
	
	@Override
	public Iterator<FileItem> iterator() {
		return this.files.iterator();
	}
	
	public List<FileItem> getList() {
		return Collections.unmodifiableList(new ArrayList<FileItem>(this.files));
	}
	
	public void add(FileItem item) {
		CheckUtils.isNotNull(item);
		this.files.add(item);
		
		item.addPropertyChangeListener(this);
		int index = this.files.indexOf(item);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				item);
	}
	
	public void addAll(Collection<FileItem> items) {
		if (items == null)
			return;
		
		for (FileItem item : items)
			this.add(item);
	}
	
	public void remove(FileItem item) {
		CheckUtils.isNotNull(item);
		
		int index = this.files.indexOf(item);
		if (this.files.remove(item)) {
			item.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					item);
		}
	}
	
	public void clear() {
		for (FileItem item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.files.size();
	}
	
	public int getIndexOf(FileItem item) {
		return this.files.indexOf(item);
	}
	
	public FileItem get(int index) {
		return this.files.get(index);
	}
	
	@Override
	public String toString() {
		List<String> files = new ArrayList<String>();
		for (FileItem file : this.files) {
			if (file.toString().length() != 0)
				files.add(file.toString());
		}
		
		return StringUtils.join(files, ", ");
	}
	
	public FileListBean toFileGroupBean() {
		FileListBean list = new FileListBean();
		
		for (FileItem item : this)
			list.add(item.toFileItemBean());
		
		return list;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
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
	
	public static class FileItem implements PropertyChangeSupported {
		
		public static final String PROP_FILE = "file";
		public static final String PROP_LINK = "link";
		
		private PropertyChangeSupport propertyChangeSupport;
		
		private String file;
		private String link;
		
		public FileItem() {
			this(null, null);
		}
		
		public FileItem(String file, String link) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			this.setFile(file);
			this.setLink(link);
		}
		
		public String getFile() {
			return this.file;
		}
		
		public void setFile(String file) {
			String oldFile = this.file;
			this.file = file;
			this.propertyChangeSupport.firePropertyChange(
					PROP_FILE,
					oldFile,
					file);
		}
		
		public String getLink() {
			return this.link;
		}
		
		public void setLink(String link) {
			String oldLink = this.link;
			this.link = link;
			this.propertyChangeSupport.firePropertyChange(
					PROP_LINK,
					oldLink,
					link);
		}
		
		@Override
		public String toString() {
			String file = (this.file == null ? "" : this.file.toString());
			String link = (this.link == null ? "" : this.link);
			
			if (link.length() != 0)
				link = "(" + this.link + ")";
			
			if (file.length() == 0)
				return link;
			
			if (link.length() == 0)
				return file;
			
			return file + " " + link;
		}
		
		public FileItemBean toFileItemBean() {
			return new FileItemBean(this.file, this.link);
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
	
}
