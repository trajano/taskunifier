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
