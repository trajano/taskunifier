package com.leclercb.taskunifier.gui.components.toolbar;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;

public interface ToolBarCreator {
	
	public abstract JComponent getComponent();
	
	public abstract void addElement(Action action);
	
	public abstract void addElement(JButton button);
	
	public abstract void addSeparator();
	
}
