package com.leclercb.taskunifier.gui.components.plugins.list;

import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;

import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class PluginList extends JXList {
	
	public PluginList() {
		this.initialize();
	}
	
	public Plugin[] getPlugins() {
		return ((PluginListModel) this.getModel()).getPlugins();
	}
	
	public void setPlugins(Plugin[] plugins) {
		((PluginListModel) this.getModel()).setPlugins(plugins);
		
		String currentId = SynchronizerUtils.getPlugin().getId();
		Plugin currentPlugin = null;
		for (Plugin plugin : plugins) {
			if (plugin.getId().equals(currentId)) {
				currentPlugin = plugin;
				break;
			}
		}
		
		this.setSelectedValue(currentPlugin, true);
	}
	
	public Plugin getSelectedPlugin() {
		return (Plugin) this.getSelectedValue();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setModel(new PluginListModel());
		this.setCellRenderer(new PluginListRenderer());
	}
	
}
