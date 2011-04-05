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
package com.leclercb.taskunifier.gui.commons.converters;

import java.util.Calendar;
import java.util.Date;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

public class TimeConverter extends AbstractConverter {
	
	public TimeConverter(ValueModel subject) {
		super(subject);
	}
	
	@Override
	public void setValue(Object date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) date);
		
		this.subject.setValue((calendar.get(Calendar.HOUR_OF_DAY) * 60)
				+ calendar.get(Calendar.MINUTE));
	}
	
	@Override
	public Object convertFromSubject(Object length) {
		int hour = 0;
		int minute = 0;
		
		if (length != null) {
			hour = ((Integer) length) / 60;
			minute = ((Integer) length) % 60;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(0, 0, 0, hour, minute, 0);
		
		return calendar.getTime();
	}
	
}
