package com.leclercb.taskunifier.gui.components.configuration.fields.publication.renderers;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class OptionsRenderer extends JButton implements TableCellRenderer {
	
	public OptionsRenderer() {
		super(
				Translations.getString("action.plugin_configuration"),
				ImageUtils.getResourceImage("settings.png", 24, 24));
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		return this;
	}
	
}
