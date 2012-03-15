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

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;

public class Synchronizing {
	
	public static final String PROP_SYNCHRONIZING = "synchronizing";
	
	private static int synchronizingLevel = 0;
	
	private static PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			Synchronizing.class);
	
	public static synchronized boolean isSynchronizing() {
		return synchronizingLevel != 0;
	}
	
	public static synchronized void setSynchronizing(final boolean synchronizing) {
		final boolean oldSynchronizing = isSynchronizing();
		
		if (synchronizing) {
			synchronizingLevel++;
		} else {
			if (synchronizingLevel > 0)
				synchronizingLevel--;
		}
		
		if (oldSynchronizing != isSynchronizing()) {
			TUSwingUtilities.executeOrInvokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					propertyChangeSupport.firePropertyChange(
							PROP_SYNCHRONIZING,
							oldSynchronizing,
							synchronizing);
				}
				
			});
		}
	}
	
	public static void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public static void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public static void removePropertyChangeListener(
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
