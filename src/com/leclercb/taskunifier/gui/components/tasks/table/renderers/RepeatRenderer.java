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

import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class RepeatRenderer extends DefaultRenderer {
	
	public RepeatRenderer() {

	}
	
	@Override
	public void setValue(Object value) {
		if (!(value instanceof String)) {
			this.setText("");
			this.setForeground(Color.BLACK);
			return;
		}
		
		this.setText((String) value);
		
		if (SynchronizerUtils.getPlugin().getSynchronizerApi().isValidRepeatValue(
				(String) value))
			this.setForeground(Color.BLACK);
		else
			this.setForeground(Color.RED);
	}
	
}
