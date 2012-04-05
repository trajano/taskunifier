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
package com.leclercb.taskunifier.gui.components.views;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;

public class ViewList implements ListChangeSupported, PropertyChangeSupported {
	
	public static final String PROP_CURRENT_VIEW = "currentView";
	
	private static ViewList INSTANCE = null;
	
	public static ViewList getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ViewList();
		}
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<ViewItem> views;
	private ViewItem currentView;
	
	private ViewList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.views = new ArrayList<ViewItem>();
	}
	
	public ViewItem getCurrentView() {
		return this.currentView;
	}
	
	public void setCurrentView(ViewItem currentView) {
		this.setCurrentView(currentView, false);
	}
	
	public void setCurrentView(ViewItem currentView, boolean force) {
		if (!force
				&& currentView != null
				&& currentView.equals(this.currentView))
			return;
		
		if (currentView != null && !this.views.contains(currentView))
			throw new RuntimeException("View list doesn't contain view \""
					+ currentView.getLabel()
					+ "\"");
		
		ViewItem oldCurrentView = this.currentView;
		
		if (force)
			oldCurrentView = null;
		
		this.currentView = currentView;
		this.propertyChangeSupport.firePropertyChange(
				PROP_CURRENT_VIEW,
				oldCurrentView,
				currentView);
	}
	
	public int getIndexOf(ViewItem view) {
		return this.views.indexOf(view);
	}
	
	public ViewItem getView(int index) {
		return this.views.get(index);
	}
	
	public ViewItem[] getViews() {
		return this.views.toArray(new ViewItem[0]);
	}
	
	public int getViewCount() {
		return this.views.size();
	}
	
	public void addView(ViewItem view) {
		CheckUtils.isNotNull(view);
		
		if (this.views.contains(view))
			return;
		
		this.views.add(view);
		int index = this.views.indexOf(view);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				view);
	}
	
	public void removeView(ViewItem view) {
		CheckUtils.isNotNull(view);
		
		int index = this.views.indexOf(view);
		if (this.views.remove(view)) {
			if (view.equals(this.currentView)) {
				if (index < this.views.size())
					this.setCurrentView(this.views.get(index));
				else
					this.setCurrentView(null);
			}
			
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					view);
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
	public void removePropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				propertyName,
				listener);
	}
	
}
