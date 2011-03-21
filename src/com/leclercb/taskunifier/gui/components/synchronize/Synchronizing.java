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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.leclercb.commons.gui.logger.GuiLogger;

public class Synchronizing {
	
	public static final String PROP_SYNCHRONIZING = "synchronizing";
	
	private static boolean synchronizing = false;
	private static PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			Synchronizing.class);
	
	public synchronized static boolean isSynchronizing() {
		return synchronizing;
	}
	
	public synchronized static boolean setSynchronizing(boolean synchronizing) {
		if (Synchronizing.synchronizing && synchronizing) {
			GuiLogger.getLogger().info(
					"Cannot synchronize because already synchronizing");
			return false;
		}
		
		if (Synchronizing.synchronizing == synchronizing)
			return false;
		
		boolean oldSynchronizing = Synchronizing.synchronizing;
		Synchronizing.synchronizing = synchronizing;
		propertyChangeSupport.firePropertyChange(
				PROP_SYNCHRONIZING,
				oldSynchronizing,
				synchronizing);
		
		return true;
	}
	
	public static void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public static void removePropertyChangeListener(
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
