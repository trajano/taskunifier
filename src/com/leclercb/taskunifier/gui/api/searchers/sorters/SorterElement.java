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

import java.beans.PropertyChangeListener;

import javax.swing.SortOrder;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.models.properties.ModelProperties;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Reviewed
public class SorterElement<M extends Model, MP extends ModelProperties<M>> implements Comparable<SorterElement<M, MP>>, PropertyChangeSupported {
	
	public static final String PROP_ORDER = "order";
	public static final String PROP_PROPERTY = "property";
	public static final String PROP_SORT_ORDER = "sortOrder";
	
	@XStreamOmitField
	private PropertyChangeSupport propertyChangeSupport;
	
	@XStreamAlias("order")
	private int order;
	
	@XStreamAlias("column")
	private MP property;
	
	@XStreamAlias("sortorder")
	private SortOrder sortOrder;
	
	public SorterElement(int order, MP property, SortOrder sortOrder) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setOrder(order);
		this.setProperty(property);
		this.setSortOrder(sortOrder);
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public void setOrder(int order) {
		int oldOrder = this.order;
		this.order = order;
		this.propertyChangeSupport.firePropertyChange(
				PROP_ORDER,
				oldOrder,
				order);
	}
	
	public MP getProperty() {
		return this.property;
	}
	
	public void setProperty(MP property) {
		CheckUtils.isNotNull(property, "Property cannot be null");
		MP oldProperty = this.property;
		this.property = property;
		this.propertyChangeSupport.firePropertyChange(
				PROP_PROPERTY,
				oldProperty,
				property);
	}
	
	public SortOrder getSortOrder() {
		return this.sortOrder;
	}
	
	public void setSortOrder(SortOrder sortOrder) {
		CheckUtils.isNotNull(sortOrder, "Sort order cannot be null");
		SortOrder oldSortOrder = this.sortOrder;
		this.sortOrder = sortOrder;
		this.propertyChangeSupport.firePropertyChange(
				PROP_SORT_ORDER,
				oldSortOrder,
				sortOrder);
	}
	
	@Override
	public String toString() {
		return this.property
				+ " ("
				+ TranslationsUtils.translateSortOrder(this.sortOrder)
				+ ")";
	}
	
	@Override
	public int compareTo(SorterElement<M, MP> element) {
		if (element == null)
			return 1;
		
		return new Integer(this.order).compareTo(element.order);
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
