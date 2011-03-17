package com.leclercb.taskunifier.gui.components.plugins.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.plugins.Plugin;
import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;
import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginTableModel extends AbstractTableModel implements PropertyChangeListener {
	
	private Plugin[] plugins;
	
	public PluginTableModel(Plugin[] plugins) {
		CheckUtils.isNotNull(plugins, "Plugins cannot be null");
		this.plugins = plugins;
		
		for (Plugin plugin : plugins) {
			plugin.addPropertyChangeListener(this);
		}
	}
	
	public Plugin getPlugin(int row) {
		return this.plugins[row];
	}
	
	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return Translations.getString("plugin.status");
			case 1:
				return Translations.getString("plugin.name");
			case 2:
				return Translations.getString("plugin.author");
			case 3:
				return Translations.getString("plugin.version");
			case 4:
				return Translations.getString("plugin.service_provider");
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
		if (col == 0)
			return PluginStatus.class;
		
		return String.class;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return this.plugins[row].getStatus();
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
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		for (int i = 0; i < this.plugins.length; i++) {
			if (this.plugins[i] == evt.getSource()) {
				this.fireTableRowsUpdated(i, i);
			}
		}
	}
	
}
