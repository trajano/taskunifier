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
package com.leclercb.taskunifier.gui.components.notes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;

public class NoteColumnsProperties implements PropertyChangeListener {
	
	public static final String PROP_ORDER = "order";
	public static final String PROP_WIDTH = "width";
	public static final String PROP_VISIBLE = "visible";
	
	private String propertyName;
	private boolean readOnly;
	private Map<NoteColumn, NoteColumnProperties> columns;
	
	public NoteColumnsProperties(String propertyName, boolean readOnly) {
		this.columns = new HashMap<NoteColumn, NoteColumnProperties>();
		
		for (NoteColumn column : NoteColumn.values()) {
			this.columns.put(column, new NoteColumnProperties(column));
		}
		
		this.setPropertyName(propertyName);
		this.setReadOnly(readOnly);
	}
	
	public NoteColumnProperties get(NoteColumn column) {
		return this.columns.get(column);
	}
	
	public String getPropertyName() {
		return this.propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		CheckUtils.isNotNull(propertyName);
		
		Main.getSettings().removePropertyChangeListener(this);
		
		this.propertyName = propertyName;
		
		for (NoteColumn column : NoteColumn.values()) {
			NoteColumnProperties properties = this.columns.get(column);
			
			properties.setOrder(Main.getSettings().getIntegerProperty(
					propertyName + "." + column.name().toLowerCase() + ".order",
					0));
			
			properties.setWidth(Main.getSettings().getIntegerProperty(
					propertyName + "." + column.name().toLowerCase() + ".width",
					100));
			
			properties.setVisible(Main.getSettings().getBooleanProperty(
					propertyName
							+ "."
							+ column.name().toLowerCase()
							+ ".visible",
					true));
		}
		
		Main.getSettings().addPropertyChangeListener(
				new WeakPropertyChangeListener(Main.getSettings(), this));
	}
	
	public boolean isReadOnly() {
		return this.readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public NoteColumn[] getVisibleNoteColumns() {
		List<NoteColumn> columns = new ArrayList<NoteColumn>(
				Arrays.asList(NoteColumn.values()));
		
		for (NoteColumn column : NoteColumn.values()) {
			if (!this.columns.get(column).isVisible()) {
				columns.remove(column);
			}
		}
		
		return columns.toArray(new NoteColumn[0]);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().startsWith(this.propertyName)) {
			if (evt.getNewValue() == null)
				return;
			
			for (NoteColumn column : NoteColumn.values()) {
				NoteColumnProperties properties = this.columns.get(column);
				
				if (evt.getPropertyName().equals(
						this.propertyName
								+ "."
								+ column.name().toLowerCase()
								+ ".order"))
					properties.setOrder(Integer.parseInt(evt.getNewValue().toString()));
				
				if (evt.getPropertyName().equals(
						this.propertyName
								+ "."
								+ column.name().toLowerCase()
								+ ".width"))
					properties.setWidth(Integer.parseInt(evt.getNewValue().toString()));
				
				if (evt.getPropertyName().equals(
						this.propertyName
								+ "."
								+ column.name().toLowerCase()
								+ ".visible"))
					properties.setVisible(Boolean.parseBoolean(evt.getNewValue().toString()));
			}
		}
	}
	
	public class NoteColumnProperties {
		
		private PropertyChangeSupport propertyChangeSupport;
		
		private NoteColumn column;
		
		private int order;
		private int width;
		private boolean visible;
		
		public NoteColumnProperties(NoteColumn column) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			CheckUtils.isNotNull(column);
			this.column = column;
		}
		
		public NoteColumn getColumn() {
			return this.column;
		}
		
		public int getOrder() {
			return this.order;
		}
		
		public void setOrder(int order) {
			if (order == this.getOrder())
				return;
			
			int oldOrder = this.getOrder();
			this.order = order;
			
			if (!NoteColumnsProperties.this.readOnly) {
				Main.getSettings().setIntegerProperty(
						NoteColumnsProperties.this.propertyName
								+ "."
								+ this.column.name().toLowerCase()
								+ ".order",
						order);
			}
			
			this.propertyChangeSupport.firePropertyChange(
					PROP_ORDER,
					oldOrder,
					order);
		}
		
		public int getWidth() {
			return this.width;
		}
		
		public void setWidth(int width) {
			if (width == this.getWidth())
				return;
			
			int oldWidth = this.getWidth();
			this.width = width;
			
			if (!NoteColumnsProperties.this.readOnly) {
				Main.getSettings().setIntegerProperty(
						NoteColumnsProperties.this.propertyName
								+ "."
								+ this.column.name().toLowerCase()
								+ ".width",
						width);
			}
			
			this.propertyChangeSupport.firePropertyChange(
					PROP_WIDTH,
					oldWidth,
					width);
		}
		
		public boolean isVisible() {
			return this.visible;
		}
		
		public void setVisible(boolean visible) {
			if (visible == this.isVisible())
				return;
			
			boolean oldVisible = this.isVisible();
			this.visible = visible;
			
			if (!NoteColumnsProperties.this.readOnly) {
				Main.getSettings().setBooleanProperty(
						NoteColumnsProperties.this.propertyName
								+ "."
								+ this.column.name().toLowerCase()
								+ ".visible",
						visible);
			}
			
			this.propertyChangeSupport.firePropertyChange(
					PROP_VISIBLE,
					oldVisible,
					visible);
		}
		
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(listener);
		}
		
		public void addPropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(
					propertyName,
					listener);
		}
		
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(listener);
		}
		
	}
	
}
