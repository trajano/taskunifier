package com.leclercb.taskunifier.gui.commons.values;

import java.util.Calendar;

import org.jdesktop.swingx.renderer.StringValue;

import com.leclercb.taskunifier.gui.translations.Translations;

public class StringValueCalendarField implements StringValue {
	
	public static final StringValueCalendarField INSTANCE = new StringValueCalendarField();
	
	private StringValueCalendarField() {
		
	}
	
	@Override
	public String getString(Object value) {
		if (value == null || !(value instanceof Integer))
			return " ";
		
		int field = (Integer) value;
		
		if (field == Calendar.MINUTE)
			return Translations.getString("date.minute");
		
		if (field == Calendar.HOUR_OF_DAY)
			return Translations.getString("date.hour");
		
		if (field == Calendar.DAY_OF_MONTH)
			return Translations.getString("date.day");
		
		if (field == Calendar.WEEK_OF_YEAR)
			return Translations.getString("date.week");
		
		if (field == Calendar.MONTH)
			return Translations.getString("date.month");
		
		if (field == Calendar.YEAR)
			return Translations.getString("date.year");
		
		return " ";
	}
	
}
