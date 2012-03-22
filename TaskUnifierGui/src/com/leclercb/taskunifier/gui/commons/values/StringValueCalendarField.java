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
			return Translations.getString("postpone.minute");
		
		if (field == Calendar.HOUR_OF_DAY)
			return Translations.getString("postpone.hour");
		
		if (field == Calendar.DAY_OF_MONTH)
			return Translations.getString("postpone.day");
		
		if (field == Calendar.WEEK_OF_YEAR)
			return Translations.getString("postpone.week");
		
		if (field == Calendar.MONTH)
			return Translations.getString("postpone.month");
		
		if (field == Calendar.YEAR)
			return Translations.getString("postpone.year");
		
		return " ";
	}
	
}
