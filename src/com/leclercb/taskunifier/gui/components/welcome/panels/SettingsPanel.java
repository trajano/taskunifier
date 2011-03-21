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
package com.leclercb.taskunifier.gui.components.welcome.panels;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.utils.Images;

public class SettingsPanel extends CardPanel {
	
	private String title;
	private ConfigurationPanel panel;
	
	public SettingsPanel(String title, ConfigurationPanel panel) {
		this.reset(title, panel);
	}
	
	public void reset(String title, ConfigurationPanel panel) {
		CheckUtils.isNotNull(title, "Title cannot be null");
		CheckUtils.isNotNull(panel, "Panel cannot be null");
		
		this.title = title;
		this.panel = panel;
		
		this.removeAll();
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		panel.add(
				new JLabel(Images.getResourceImage("settings.png", 48, 48)),
				BorderLayout.WEST);
		panel.add(new JLabel(this.title));
		
		this.add(panel, BorderLayout.NORTH);
		
		this.add(this.panel, BorderLayout.CENTER);
	}
	
	@Override
	public void applyChanges() {
		if (this.panel != null)
			this.panel.saveAndApplyConfig();
	}
	
}
