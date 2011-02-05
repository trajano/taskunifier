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
package com.leclercb.taskunifier.gui.components.statusbar;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.explodingpixels.macwidgets.BottomBar;
import com.explodingpixels.macwidgets.BottomBarSize;
import com.explodingpixels.macwidgets.MacWidgetFactory;

public class MacStatusBar extends AbstractStatusBar {
	
	public MacStatusBar() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		BottomBar bottomBar = new BottomBar(BottomBarSize.LARGE);
		this.add(bottomBar.getComponent(), BorderLayout.CENTER);
		
		this.scheduledSyncStatus = new MacStatusElement();
		bottomBar.addComponentToCenter(this.scheduledSyncStatus.getComponent());
		
		this.lastSynchronizationDate = new MacStatusElement();
		bottomBar.addComponentToRight(this.lastSynchronizationDate.getComponent());
		
		this.initializeLastSynchronizationDate();
	}
	
	private static class MacStatusElement implements StatusElement {
		
		private JLabel label;
		
		public MacStatusElement() {
			this.label = MacWidgetFactory.createEmphasizedLabel("");
		}
		
		@Override
		public void setText(String text) {
			this.label.setText(text);
		}
		
		@Override
		public JComponent getComponent() {
			return this.label;
		}
		
	}
	
}
