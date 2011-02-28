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
