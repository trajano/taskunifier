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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;
import com.leclercb.taskunifier.gui.translations.Translations;

public final class Synchronizing implements PropertyChangeSupported {
	
	public static final String PROP_SYNCHRONIZING = "synchronizing";
	
	private static Synchronizing INSTANCE = null;
	
	public static Synchronizing getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Synchronizing();
		}
		
		return INSTANCE;
	}
	
	private int synchronizingLevel = 0;
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private Synchronizing() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public synchronized boolean isSynchronizing() {
		return this.synchronizingLevel != 0;
	}
	
	public synchronized void setSynchronizing(final boolean synchronizing) {
		final boolean oldSynchronizing = this.isSynchronizing();
		
		if (synchronizing) {
			this.synchronizingLevel++;
		} else {
			if (this.synchronizingLevel > 0)
				this.synchronizingLevel--;
		}
		
		if (oldSynchronizing != this.isSynchronizing()) {
			TUSwingUtilities.executeOrInvokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					Synchronizing.this.propertyChangeSupport.firePropertyChange(
							PROP_SYNCHRONIZING,
							oldSynchronizing,
							synchronizing);
				}
				
			});
		}
	}
	
	public void showSynchronizingMessage() {
		TUSwingUtilities.executeOrInvokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				JOptionPane.showMessageDialog(
						null,
						Translations.getString("general.synchronization_ongoing"),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
		});
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
