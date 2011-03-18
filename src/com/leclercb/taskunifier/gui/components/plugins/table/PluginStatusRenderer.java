package com.leclercb.taskunifier.gui.components.plugins.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;
import com.leclercb.taskunifier.gui.swing.ColorBadgeIcon;

public class PluginStatusRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		PluginStatus status = (PluginStatus) value;
		switch (status) {
			case DELETED:
				this.setIcon(new ColorBadgeIcon(Color.RED, 10, 10));
				break;
			case INSTALLED:
				this.setIcon(new ColorBadgeIcon(Color.GREEN, 10, 10));
				break;
			case TO_INSTALL:
				this.setIcon(new ColorBadgeIcon(Color.BLUE, 10, 10));
				break;
			case TO_UPDATE:
				this.setIcon(new ColorBadgeIcon(Color.ORANGE, 10, 10));
				break;
		}
		
		this.setText(status.toString());
		
		return super.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				row,
				column);
		
	}
	
}
