package com.leclercb.taskunifier.gui.commons.models;

import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.gui.swing.models.DefaultSortedComboBoxModel;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.commons.comparators.SynchronizerGuiPluginComparator;
import com.leclercb.taskunifier.gui.main.Main;

public class SynchronizerGuiPluginModel extends DefaultSortedComboBoxModel implements ListChangeListener {
	
	public SynchronizerGuiPluginModel() {
		super(new SynchronizerGuiPluginComparator());
		this.initialize();
	}
	
	private void initialize() {
		List<SynchronizerGuiPlugin> plugins = Main.API_PLUGINS.getPlugins();
		for (SynchronizerGuiPlugin plugin : plugins) {
			this.addElement(plugin);
		}
		
		Main.API_PLUGINS.addListChangeListener(this);
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.addElement(event.getValue());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.removeElement(event.getValue());
		}
	}
	
}
