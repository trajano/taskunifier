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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;

public class TemplateFactory implements PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	public static final String PROP_DEFAULT_TEMPLATE = "defaultTemplate";
	
	private static TemplateFactory FACTORY;
	
	public static TemplateFactory getInstance() {
		if (FACTORY == null)
			FACTORY = new TemplateFactory();
		
		return FACTORY;
	}
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private Template defaultTemplate;
	private EventList<Template> templates;
	
	private TemplateFactory() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.defaultTemplate = null;
		this.templates = GlazedLists.threadSafeList(new BasicEventList<Template>());
	}
	
	public Template getDefaultTemplate() {
		return this.defaultTemplate;
	}
	
	public void setDefaultTemplate(Template defaultTemplate) {
		Template oldDefaultTemplate = this.defaultTemplate;
		this.defaultTemplate = defaultTemplate;
		this.propertyChangeSupport.firePropertyChange(
				PROP_DEFAULT_TEMPLATE,
				oldDefaultTemplate,
				defaultTemplate);
	}
	
	public boolean contains(String id) {
		return (this.get(id) != null);
	}
	
	public int size() {
		return this.templates.size();
	}
	
	public List<Template> getList() {
		return GlazedLists.readOnlyList(GlazedLists.eventList(this.templates));
	}
	
	public EventList<Template> getEventList() {
		return GlazedLists.readOnlyList(this.templates);
	}
	
	public Template get(int index) {
		return this.templates.get(index);
	}
	
	public Template get(String id) {
		for (Template template : this.templates)
			if (template.getId().equals(id))
				return template;
		
		return null;
	}
	
	public int getIndexOf(Template template) {
		return this.templates.indexOf(template);
	}
	
	public void delete(Template template) {
		this.unregister(template);
	}
	
	public void deleteAll() {
		List<Template> templates = new ArrayList<Template>(this.templates);
		for (Template template : templates)
			this.unregister(template);
	}
	
	public void register(Template template) {
		CheckUtils.isNotNull(template, "Template cannot be null");
		
		if (this.contains(template.getId()))
			throw new IllegalArgumentException("ID already exists in factory");
		
		this.templates.add(template);
		template.addPropertyChangeListener(this);
		int index = this.templates.indexOf(template);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				template);
	}
	
	public void unregister(Template template) {
		CheckUtils.isNotNull(template, "Template cannot be null");
		
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
	
	public Template create(String title) {
		Template template = new Template(title);
		this.register(template);
		return template;
	}
	
	public Template create(String id, String title) {
		Template template = new Template(id, title);
		this.register(template);
		return template;
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
	
}
