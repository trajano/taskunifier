package com.leclercb.taskunifier.gui.commons.renderers;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;

public class SynchronizerGuiPluginListCellRenderer extends DefaultListCellRenderer {
	
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
		Component component = super.getListCellRendererComponent(
				list,
				value,
				index,
				isSelected,
				cellHasFocus);
		
		if (value == null || !(value instanceof SynchronizerGuiPlugin)) {
			this.setText(" ");
			return component;
		}
		
		SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) value;
		
		this.setText(plugin.getSynchronizerApi().getApiName()
				+ " ("
				+ plugin.getName()
				+ " - "
				+ plugin.getVersion()
				+ ")");
		return component;
	}
	
}
