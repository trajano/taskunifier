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
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;

public class PluginConfigurationPanel extends ConfigurationPanel {
	
	private ConfigurationPanel configPanel;
	
	public PluginConfigurationPanel(
			boolean welcome,
			SynchronizerGuiPlugin plugin) {
		this.initialize(welcome, plugin);
	}
	
	private void initialize(boolean welcome, SynchronizerGuiPlugin plugin) {
		this.setLayout(new BorderLayout());
		
		this.configPanel = plugin.getConfigurationPanel(welcome);
		
		String info = plugin.getName()
				+ " - "
				+ plugin.getAuthor()
				+ " - "
				+ plugin.getVersion();
		JLabel pluginInfo = new JLabel(info, SwingConstants.RIGHT);
		pluginInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(this.configPanel, BorderLayout.CENTER);
		this.add(pluginInfo, BorderLayout.SOUTH);
	}
	
	@Override
	public void saveAndApplyConfig() {
		this.configPanel.saveAndApplyConfig();
	}
	
}
