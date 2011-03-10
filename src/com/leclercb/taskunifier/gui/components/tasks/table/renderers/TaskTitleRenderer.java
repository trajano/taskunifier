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

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.utils.Images;

public class TaskTitleRenderer extends DefaultRenderer {
	
	public TaskTitleRenderer() {

	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		Component component = super.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				row,
				column);
		
		if (value == null) {
			this.setIcon(null);
			component.setFont(this.getFont().deriveFont(Font.PLAIN));
			this.setText("");
			return component;
		}
		
		Task task = ((TaskTable) table).getTask(row);
		
		// Set Text & Font
		if (task.getParent() == null) {
			component.setFont(this.getFont().deriveFont(Font.BOLD));
			this.setText(task.getTitle());
		} else {
			component.setFont(this.getFont().deriveFont(Font.PLAIN));
			this.setText("          " + task.getTitle());
		}
		
		// Set Icon
		if (!task.isCompleted() && task.isOverDue())
			this.setIcon(Images.getResourceImage("warning.gif"));
		else
			this.setIcon(Images.getResourceImage("transparent.png"));
		
		return component;
	}
	
}
