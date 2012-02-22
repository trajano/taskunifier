package com.leclercb.taskunifier.gui.components.modelnote;

import javax.swing.JComponent;

import com.leclercb.commons.api.event.action.ActionSupported;

public interface HTMLEditorInterface extends ActionSupported {
	
	public static final String ACTION_TEXT_CHANGED = "ACTION_TEXT_CHANGED";
	
	public abstract JComponent getComponent();
	
	public abstract String getText();
	
	public abstract void setText(
			String text,
			boolean canEdit,
			boolean discardAllEdits);
	
	public abstract boolean edit();
	
	public abstract void view();
	
}
