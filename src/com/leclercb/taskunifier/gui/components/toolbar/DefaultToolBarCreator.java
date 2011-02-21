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

import javax.swing.JComponent;
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
	public void addElementToLeft(JComponent component) {
		this.toolBar.add(component);
	}
	
	@Override
	public void addElementToCenter(JComponent component) {
		this.toolBar.add(component);
	}
	
	@Override
	public void addElementToRight(JComponent component) {
		this.toolBar.add(component);
	}
	
	@Override
	public void addSeparatorToLeft() {
		this.toolBar.addSeparator();
	}
	
	@Override
	public void addSeparatorToCenter() {
		this.toolBar.addSeparator();
	}
	
	@Override
	public void addSeparatorToRight() {
		this.toolBar.addSeparator();
	}
	
}
