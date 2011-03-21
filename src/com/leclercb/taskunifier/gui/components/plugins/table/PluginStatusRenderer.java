/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
