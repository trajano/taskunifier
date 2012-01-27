package com.leclercb.taskunifier.gui.components.configuration.fields.publication;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class PublisherPluginModel extends AbstractTableModel implements ListChangeListener {
	
	private List<SynchronizerGuiPlugin> plugins;
	
	public PublisherPluginModel() {
		this.plugins = new ArrayList<SynchronizerGuiPlugin>();
		this.initialize();
	}
	
	private void initialize() {
		List<SynchronizerGuiPlugin> plugins = Main.getApiPlugins().getPlugins();
		for (SynchronizerGuiPlugin plugin : plugins) {
			if (plugin.isPublisher())
				this.plugins.add(plugin);
		}
		
		Main.getApiPlugins().addListChangeListener(this);
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public int getRowCount() {
		return this.plugins.size();
	}
	
	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return Translations.getString("configuration.publication.plugin.enabled");
			case 1:
				return Translations.getString("configuration.publication.plugin.name");
			case 2:
				return Translations.getString("general.configuration");
			default:
				return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0:
				return Boolean.class;
			case 1:
				return String.class;
			case 2:
				return SynchronizerGuiPlugin.class;
			default:
				return null;
		}
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		SynchronizerGuiPlugin plugin = this.plugins.get(row);
		
		if (col == 0) {
			SynchronizerGuiPlugin[] plugins = SynchronizerUtils.getPublisherPlugins();
			for (SynchronizerGuiPlugin p : plugins) {
				if (plugin.getId().equals(p.getId()))
					return true;
			}
			
			return false;
		}
		
		if (col == 1) {
			return plugin.getName();
		}
		
		if (col == 2) {
			return plugin;
		}
		
		return null;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 1)
			return false;
		
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
			case 0:
				if ((Boolean) value)
					SynchronizerUtils.addPublisherPlugin(this.plugins.get(row));
				else
					SynchronizerUtils.removePublisherPlugin(this.plugins.get(row));
				break;
		}
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) event.getValue();
		
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			if (plugin.isPublisher())
				this.plugins.add(plugin);
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.plugins.remove(plugin);
		}
	}
	
}
