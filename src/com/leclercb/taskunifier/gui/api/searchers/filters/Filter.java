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
package com.leclercb.taskunifier.gui.api.searchers.filters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import com.leclercb.commons.api.utils.ListUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.models.properties.ModelProperties;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public abstract class Filter<M extends Model, MP extends ModelProperties<M>, F extends Filter<M, MP, F, FE>, FE extends FilterElement<M, MP, F>> implements ListChangeListener, PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	public static final String PROP_LINK = "link";
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private F parent;
	private FilterLink link;
	private List<F> filters;
	private List<FE> elements;
	
	public Filter() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setParent(null);
		this.setLink(FilterLink.AND);
		
		this.filters = new ArrayList<F>();
		this.elements = new ArrayList<FE>();
	}
	
	public F getParent() {
		return this.parent;
	}
	
	private void setParent(F parent) {
		this.parent = parent;
	}
	
	public FilterLink getLink() {
		return this.link;
	}
	
	public void setLink(FilterLink link) {
		CheckUtils.isNotNull(link, "Link cannot be null");
		FilterLink oldLink = this.link;
		this.link = link;
		this.propertyChangeSupport.firePropertyChange(PROP_LINK, oldLink, link);
	}
	
	public int getIndexOf(TaskFilterElement element) {
		return this.elements.indexOf(element);
	}
	
	public int getElementCount() {
		return this.elements.size();
	}
	
	public FE getElement(int index) {
		return this.elements.get(index);
	}
	
	public List<FE> getElements() {
		return Collections.unmodifiableList(new ArrayList<FE>(this.elements));
	}
	
	@SuppressWarnings("unchecked")
	public void addElement(FE element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		this.elements.add(element);
		
		if (element.getParent() != null) {
			element.getParent().removeElement(element);
		}
		
		element.setParent((F) this);
		element.addPropertyChangeListener(this);
		int index = this.elements.indexOf(element);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				element);
	}
	
	public void removeElement(FE element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		
		int index = this.elements.indexOf(element);
		if (this.elements.remove(element)) {
			element.setParent(null);
			element.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					element);
		}
	}
	
	public int getIndexOf(F filter) {
		return this.filters.indexOf(filter);
	}
	
	public int getFilterCount() {
		return this.filters.size();
	}
	
	public F getFilter(int index) {
		return this.filters.get(index);
	}
	
	public List<F> getFilters() {
		return Collections.unmodifiableList(new ArrayList<F>(this.filters));
	}
	
	@SuppressWarnings("unchecked")
	public void addFilter(F filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		this.filters.add(filter);
		
		if (filter.getParent() != null) {
			filter.getParent().removeFilter(filter);
		}
		
		filter.setParent((F) this);
		filter.addListChangeListener(this);
		filter.addPropertyChangeListener(this);
		int index = this.filters.indexOf(filter);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				filter);
	}
	
	public void removeFilter(F filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		
		int index = this.filters.indexOf(filter);
		if (this.filters.remove(filter)) {
			filter.setParent(null);
			filter.removeListChangeListener(this);
			filter.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					filter);
		}
	}
	
	public boolean include(M model) {
		if (this.link == FilterLink.AND) {
			for (FE element : this.elements) {
				if (!element.include(model))
					return false;
			}
			
			for (F filter : this.filters) {
				if (!filter.include(model))
					return false;
			}
			
			return true;
		} else {
			for (FE element : this.elements) {
				if (element.include(model))
					return true;
			}
			
			for (F filter : this.filters) {
				if (filter.include(model))
					return true;
			}
			
			return false;
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
	public void listChange(ListChangeEvent event) {
		this.listChangeSupport.fireListChange(event);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public String toString() {
		if (this.elements.size() == 0 && this.filters.size() == 0)
			return "";
		
		StringBuffer buffer = new StringBuffer(
				Translations.getString("general.filter") + ": ((");
		
		List<Object> list = new ArrayList<Object>();
		list.addAll(this.elements);
		list.addAll(this.filters);
		
		buffer.append(ListUtils.listToString(
				list,
				") " + TranslationsUtils.translateFilterLink(this.link) + " ("));
		
		buffer.append("))");
		
		return buffer.toString();
	}
	
}
