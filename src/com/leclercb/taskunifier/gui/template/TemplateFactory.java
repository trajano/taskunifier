/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.template;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;

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
	private List<Template> templates;
	
	private TemplateFactory() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.defaultTemplate = null;
		this.templates = new ArrayList<Template>();
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
		return Collections.unmodifiableList(new ArrayList<Template>(
				this.templates));
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
			if (this.defaultTemplate.equals(template))
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
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
}
