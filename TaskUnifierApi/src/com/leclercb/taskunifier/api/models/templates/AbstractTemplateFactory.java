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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;

abstract class AbstractTemplateFactory<T extends Template<?, ?>> implements PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	public static final String PROP_DEFAULT_TEMPLATE = "defaultTemplate";
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private T defaultTemplate;
	private List<T> templates;
	
	public AbstractTemplateFactory() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.defaultTemplate = null;
		this.templates = new ArrayList<T>();
	}
	
	public T getDefaultTemplate() {
		return this.defaultTemplate;
	}
	
	public void setDefaultTemplate(T defaultTemplate) {
		if (defaultTemplate != null && !this.contains(defaultTemplate))
			this.register(defaultTemplate);
		
		T oldDefaultTemplate = this.defaultTemplate;
		this.defaultTemplate = defaultTemplate;
		this.propertyChangeSupport.firePropertyChange(
				PROP_DEFAULT_TEMPLATE,
				oldDefaultTemplate,
				defaultTemplate);
	}
	
	public boolean contains(T template) {
		return this.templates.contains(template);
	}
	
	public int size() {
		return this.templates.size();
	}
	
	public List<T> getList() {
		return Collections.unmodifiableList(new ArrayList<T>(this.templates));
	}
	
	public T get(int index) {
		return this.templates.get(index);
	}
	
	public int getIndexOf(T template) {
		return this.templates.indexOf(template);
	}
	
	public void delete(T template) {
		this.unregister(template);
	}
	
	public void deleteAll() {
		List<T> templates = new ArrayList<T>(this.templates);
		for (T template : templates)
			this.unregister(template);
	}
	
	public void register(T template) {
		CheckUtils.isNotNull(template);
		
		if (this.contains(template))
			return;
		
		this.templates.add(template);
		template.addPropertyChangeListener(this);
		int index = this.templates.indexOf(template);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				template);
	}
	
	public void unregister(T template) {
		CheckUtils.isNotNull(template);
		
		int index = this.templates.indexOf(template);
		if (this.templates.remove(template)) {
			if (EqualsUtils.equals(this.defaultTemplate, template))
				this.setDefaultTemplate(null);
			
			template.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					template);
		}
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
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	public abstract T create(String title);
	
	public abstract void decodeFromXML(InputStream input);
	
	public abstract void encodeToXML(OutputStream output);
	
}
