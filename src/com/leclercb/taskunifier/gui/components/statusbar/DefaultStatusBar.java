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
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DefaultStatusBar extends AbstractStatusBar {
	
	public DefaultStatusBar() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.setBorder(new EmptyBorder(1, 1, 1, 1));
		
		this.scheduledSyncStatus = new DefaultStatusElement();
		this.add(this.scheduledSyncStatus.getComponent());
		
		this.lastSynchronizationDate = new DefaultStatusElement();
		this.add(this.lastSynchronizationDate.getComponent());
		
		this.initializeLastSynchronizationDate();
	}
	
	private class DefaultStatusElement extends JPanel implements StatusElement {
		
		private JLabel label;
		
		public DefaultStatusElement() {
			this.initialize();
		}
		
		private void initialize() {
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			
			this.label = new JLabel("");
			this.label.setHorizontalAlignment(SwingConstants.TRAILING);
			this.label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			this.add(this.label, BorderLayout.CENTER);
		}
		
		@Override
		public void setText(String text) {
			this.label.setText(text);
		}
		
		@Override
		public JComponent getComponent() {
			return this;
		}
		
	}
	
}
