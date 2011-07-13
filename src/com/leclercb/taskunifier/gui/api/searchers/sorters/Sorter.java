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
package com.leclercb.taskunifier.gui.api.searchers.sorters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
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
import com.thoughtworks.xstream.annotations.XStreamAlias;

public abstract class Sorter<M extends Model, MP extends ModelProperties<M>, SE extends SorterElement<M, MP>> implements PropertyChangeListener, ListChangeSupported, PropertyChangeSupported, Serializable {
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	@XStreamAlias("element")
	private List<SE> elements;
	
	public Sorter() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.elements = new ArrayList<SE>();
	}
	
	public int getIndexOf(TaskSorterElement element) {
		return this.elements.indexOf(element);
	}
	
	public int getElementCount() {
		return this.elements.size();
	}
	
	public SE getElement(int index) {
		return this.elements.get(index);
	}
	
	public List<SE> getElements() {
		List<SE> sortElements = new ArrayList<SE>(this.elements);
		
		Collections.sort(sortElements);
		
		return Collections.unmodifiableList(sortElements);
	}
	
	public void addElement(SE element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		this.elements.add(element);
		element.addPropertyChangeListener(this);
		int index = this.elements.indexOf(element);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				element);
	}
	
	public void removeElement(SE element) {
		CheckUtils.isNotNull(element, "Searcher cannot be null");
		
		int index = this.elements.indexOf(element);
		if (this.elements.remove(element)) {
			element.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					element);
		}
	}
	
	@Override
	public String toString() {
		return Translations.getString("general.sort")
				+ ": "
				+ ListUtils.listToString(this.elements, ", ");
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
