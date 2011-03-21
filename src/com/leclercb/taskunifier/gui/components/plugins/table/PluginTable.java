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
package com.leclercb.taskunifier.gui.components.plugins.table;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.leclercb.taskunifier.gui.components.plugins.Plugin;
import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;

public class PluginTable extends JTable {
	
	public PluginTable(Plugin[] plugins) {
		this.initialize(plugins);
	}
	
	private void initialize(Plugin[] plugins) {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setModel(new PluginTableModel(plugins));
		this.setDefaultRenderer(PluginStatus.class, new PluginStatusRenderer());
	}
	
	public Plugin getSelectedPlugin() {
		int index = this.getSelectedRow();
		
		if (index == -1)
			return null;
		
		return ((PluginTableModel) this.getModel()).getPlugin(index);
	}
	
}
