package com.leclercb.taskunifier.gui.components.plugins.list;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginListRenderer extends DefaultListCellRenderer {
	
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
		
		Plugin plugin = (Plugin) value;
		
		StringBuffer text = new StringBuffer();
		text.append(plugin.getName() + " - " + plugin.getVersion() + "<br />");
		text.append(Translations.getString("plugin.author")
				+ ": "
				+ plugin.getAuthor());
		
		this.setIcon(plugin.getLogo());
		this.setText("<html>" + text.toString() + "</html>");
		
		return component;
	}
	
}
