package com.leclercb.taskunifier.gui.components.modelnote;

import javax.swing.JComponent;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;

public interface HTMLEditorInterface extends PropertyChangeSupported {
	
	public static final String PROP_TEXT = "text";
	
	public abstract JComponent getComponent();
	
	public abstract String getText();
	
	public abstract void setText(String text);
	
	public abstract void setText(
			String text,
			boolean canEdit,
			boolean discardAllEdits);
	
	public abstract boolean edit();
	
}
