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

public class CalendarConverter extends AbstractConverter {
	
	public CalendarConverter(ValueModel subject) {
		super(subject);
	}
	
	@Override
	public void setValue(Object date) {
		if (date == null) {
			this.subject.setValue(null);
			return;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(((Date) date).getTime());
		this.subject.setValue(calendar);
	}
	
	@Override
	public Object convertFromSubject(Object calendar) {
		if (calendar == null)
			return null;
		
		return ((Calendar) calendar).getTime();
	}
	
}
