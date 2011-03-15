package com.leclercb.taskunifier.gui.commons.converters;

import java.util.Calendar;
import java.util.Date;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

public class LengthConverter extends AbstractConverter {
	
	public LengthConverter(ValueModel subject) {
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
