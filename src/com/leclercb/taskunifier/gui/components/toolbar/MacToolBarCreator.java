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
package com.leclercb.taskunifier.gui.components.toolbar;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

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
		// button.putClientProperty("JButton.buttonType", "textured");
		button.setOpaque(false);
		button.setBorderPainted(false);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		
		String text = button.getText() == null ? "" : button.getText();
		text = text.length() > 30 ? text.substring(0, 30 - 2) + "..." : text;
		
		button.setText(text);
		
		this.toolBar.addComponentToLeft(button);
	}
	
	@Override
	public void addSeparator() {
		this.toolBar.addComponentToLeft(new JSeparator());
	}
	
}
