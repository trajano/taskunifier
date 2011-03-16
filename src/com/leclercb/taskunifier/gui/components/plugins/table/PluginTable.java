package com.leclercb.taskunifier.gui.components.plugins.table;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.leclercb.taskunifier.gui.components.plugins.Plugin;

public class PluginTable extends JTable {
	
	public PluginTable(Plugin[] plugins) {
		this.initialize(plugins);
	}
	
	private void initialize(Plugin[] plugins) {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.setModel(new PluginTableModel(plugins));
	}
	
	public Plugin getSelectedPlugin() {
		int index = this.getSelectedRow();
		
		if (index == -1)
			return null;
		
		return ((PluginTableModel) this.getModel()).getPlugin(index);
	}
	
}
