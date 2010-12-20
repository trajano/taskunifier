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

import java.text.DateFormat;
import java.util.Calendar;

public class LengthRenderer extends DefaultRenderer {
	
	private DateFormat formatter;
	
	public LengthRenderer(DateFormat formatter) {
		this.formatter = formatter;
	}
	
	@Override
	public void setValue(Object value) {
		if (value == null || !(value instanceof Integer)) {
			this.setText("");
			return;
		}
		
		int hour = 0;
		int minute = 0;
		
		if (value != null) {
			hour = ((Integer) value) / 60;
			minute = ((Integer) value) % 60;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(0, 0, 0, hour, minute, 0);
		
		this.setText(this.formatter.format(calendar.getTime()));
	}
	
}
