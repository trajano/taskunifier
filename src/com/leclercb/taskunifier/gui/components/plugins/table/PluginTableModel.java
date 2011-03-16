package com.leclercb.taskunifier.gui.components.plugins.table;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.plugins.Plugin;

public class PluginTableModel extends AbstractTableModel {
	
	private Plugin[] plugins;
	
	public PluginTableModel(Plugin[] plugins) {
		CheckUtils.isNotNull(plugins, "Plugins cannot be null");
		this.plugins = plugins;
	}
	
	public Plugin getPlugin(int row) {
		return this.plugins[row];
	}
	
	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return "Status";
			case 1:
				return "Name";
			case 2:
				return "Author";
			case 3:
				return "Version";
			case 4:
				return "Service Provider";
			default:
				return null;
		}
	}
	
	@Override
	public int getColumnCount() {
		return 5;
	}
	
	@Override
	public int getRowCount() {
		return this.plugins.length;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return String.class;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return this.plugins[row].getStatus().toString();
			case 1:
				return this.plugins[row].getName();
			case 2:
				return this.plugins[row].getAuthor();
			case 3:
				return this.plugins[row].getVersion();
			case 4:
				return this.plugins[row].getServiceProvider();
			default:
				return null;
		}
	}
	
}
