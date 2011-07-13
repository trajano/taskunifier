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

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.models.properties.ModelProperties;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum NoteColumn implements ModelProperties<Note> {
	
	MODEL(Note.class, Translations.getString("general.id"), false),
	TITLE(String.class, Translations.getString("general.note.title"), true),
	FOLDER(Folder.class, Translations.getString("general.note.folder"), true),
	NOTE(String.class, Translations.getString("general.note.note"), false);
	
	public static final String PROP_ORDER = "order";
	public static final String PROP_WIDTH = "width";
	public static final String PROP_VISIBLE = "visible";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private Class<?> type;
	private String label;
	private boolean editable;
	
	private NoteColumn(Class<?> type, String label, boolean editable) {
		this.propertyChangeSupport = new PropertyChangeSupport(NoteColumn.class);
		
		this.setType(type);
		this.setLabel(label);
		this.setEditable(editable);
		
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().startsWith("notecolumn")) {
					if (evt.getNewValue() == null)
						return;
					
					if (evt.getPropertyName().equals(
							"notecolumn."
									+ NoteColumn.this.name().toLowerCase()
									+ ".order"))
						NoteColumn.this.setOrder(Integer.parseInt(evt.getNewValue().toString()));
					
					if (evt.getPropertyName().equals(
							"notecolumn."
									+ NoteColumn.this.name().toLowerCase()
									+ ".width"))
						NoteColumn.this.setWidth(Integer.parseInt(evt.getNewValue().toString()));
					
					if (evt.getPropertyName().equals(
							"notecolumn."
									+ NoteColumn.this.name().toLowerCase()
									+ ".visible"))
						NoteColumn.this.setVisible(Boolean.parseBoolean(evt.getNewValue().toString()));
				}
			}
			
		});
	}
	
	@Override
	public Class<?> getType() {
		return this.type;
	}
	
	private void setType(Class<?> type) {
		this.type = type;
	}
	
	public int getOrder() {
		Integer order = Main.SETTINGS.getIntegerProperty("notecolumn."
				+ this.name().toLowerCase()
				+ ".order");
		
		if (order == null)
			return 0;
		
		return order;
	}
	
	public void setOrder(int order) {
		if (order == this.getOrder())
			return;
		
		int oldOrder = this.getOrder();
		Main.SETTINGS.setIntegerProperty("notecolumn."
				+ this.name().toLowerCase()
				+ ".order", order);
		this.propertyChangeSupport.firePropertyChange(
				PROP_ORDER,
				oldOrder,
				order);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	public int getWidth() {
		Integer width = Main.SETTINGS.getIntegerProperty("notecolumn."
				+ this.name().toLowerCase()
				+ ".width");
		
		if (width == null)
			return 100;
		
		return width;
	}
	
	public void setWidth(int width) {
		if (width == this.getWidth())
			return;
		
		int oldWidth = this.getWidth();
		Main.SETTINGS.setIntegerProperty("notecolumn."
				+ this.name().toLowerCase()
				+ ".width", width);
		this.propertyChangeSupport.firePropertyChange(
				PROP_WIDTH,
				oldWidth,
				width);
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isVisible() {
		Boolean visible = Main.SETTINGS.getBooleanProperty("notecolumn."
				+ this.name().toLowerCase()
				+ ".visible");
		
		if (visible == null)
			return true;
		
		return visible;
	}
	
	public void setVisible(boolean visible) {
		if (visible == this.isVisible())
			return;
		
		boolean oldVisible = this.isVisible();
		Main.SETTINGS.setBooleanProperty("notecolumn."
				+ this.name().toLowerCase()
				+ ".visible", visible);
		this.propertyChangeSupport.firePropertyChange(
				PROP_VISIBLE,
				oldVisible,
				visible);
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	@Override
	public Object getProperty(Note note) {
		if (note == null)
			return null;
		
		switch (this) {
			case MODEL:
				return note;
			case TITLE:
				return note.getTitle();
			case FOLDER:
				return note.getFolder();
			case NOTE:
				return note.getNote();
			default:
				return null;
		}
	}
	
	@Override
	public void setProperty(Note note, Object value) {
		if (note == null)
			return;
		
		switch (this) {
			case MODEL:
				break;
			case TITLE:
				note.setTitle((String) value);
				break;
			case FOLDER:
				note.setFolder((Folder) value);
				break;
			case NOTE:
				note.setNote((String) value);
				break;
		}
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
