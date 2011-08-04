package com.leclercb.taskunifier.gui.components.views;

import javax.swing.JPanel;

public interface View {
	
	public abstract ViewType getViewType();
	
	public abstract JPanel getViewContent();
	
}
