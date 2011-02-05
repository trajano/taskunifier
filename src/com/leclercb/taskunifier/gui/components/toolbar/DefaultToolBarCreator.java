package com.leclercb.taskunifier.gui.components.toolbar;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

public class DefaultToolBarCreator implements ToolBarCreator {
	
	private JToolBar toolBar;
	
	public DefaultToolBarCreator() {
		this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		this.toolBar.setFloatable(false);
	}
	
	@Override
	public JToolBar getComponent() {
		return this.toolBar;
	}
	
	@Override
	public void addElement(Action action) {
		this.toolBar.add(action);
	}
	
	@Override
	public void addElement(JButton button) {
		this.toolBar.add(button);
	}
	
	@Override
	public void addSeparator() {
		this.toolBar.addSeparator();
	}
	
}
