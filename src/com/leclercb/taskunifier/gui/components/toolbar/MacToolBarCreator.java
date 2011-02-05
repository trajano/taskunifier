package com.leclercb.taskunifier.gui.components.toolbar;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSeparator;

import com.explodingpixels.macwidgets.UnifiedToolBar;

public class MacToolBarCreator implements ToolBarCreator {
	
	private UnifiedToolBar toolBar;
	
	public MacToolBarCreator() {
		this.toolBar = new UnifiedToolBar();
	}
	
	@Override
	public JComponent getComponent() {
		return this.toolBar.getComponent();
	}
	
	@Override
	public void addElement(Action action) {
		this.addElement(new JButton(action));
	}
	
	@Override
	public void addElement(JButton button) {
		button.setText(null);
		button.putClientProperty("JButton.buttonType", "textured");
		//button.setVerticalTextPosition(SwingConstants.BOTTOM);
		//button.setHorizontalTextPosition(SwingConstants.CENTER);
		
		this.toolBar.addComponentToLeft(button);
	}
	
	@Override
	public void addSeparator() {
		this.toolBar.addComponentToLeft(new JSeparator());
	}
	
}
