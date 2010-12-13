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
package com.leclercb.taskunifier.gui.components.tasks.table.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;

public class DefaultRenderer extends DefaultTableCellRenderer {

	private Color even;
	private Color odd;
	private Color selected;

	public DefaultRenderer() {
		if (Settings.getBooleanProperty("theme.color.enabled")) {
			even = Settings.getColorProperty("theme.color.even");
			odd = Settings.getColorProperty("theme.color.odd");
		} else {
			even = UIManager.getColor("Table.background");
			odd = UIManager.getColor("Table.background");
		}

		selected = UIManager.getColor("Table.selectionBackground");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component component = super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);

		if (value == null) {
			component.setBackground(getBackgroundColor(isSelected, row));
			return component;
		}

		Task task = ((TaskTable) table).getTask(row);

		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>(component.getFont().getAttributes());
		attributes.put(TextAttribute.STRIKETHROUGH, task.isCompleted());
		component.setFont(component.getFont().deriveFont(attributes));

		component.setBackground(getBackgroundColor(isSelected, row));

		return component;
	}

	private Color getBackgroundColor(boolean isSelected, int row) {
		if (isSelected)
			return selected;

		if (row % 2 == 0)
			return even;

		return odd;
	}

}
