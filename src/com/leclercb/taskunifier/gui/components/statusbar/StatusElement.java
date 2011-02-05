package com.leclercb.taskunifier.gui.components.statusbar;

import javax.swing.JComponent;

interface StatusElement {
	
	public abstract void setText(String text);
	
	public abstract JComponent getComponent();
	
}
