package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import java.util.Calendar;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum CalendarCondition implements Condition<Calendar, Calendar> {
	
	AFTER,
	BEFORE,
	EQUALS;
	
	private CalendarCondition() {

	}
	
	@Override
	public Class<?> getValueType() {
		return Calendar.class;
	}
	
	@Override
	public Class<?> getTaskValueType() {
		return Calendar.class;
	}
	
	@Override
	public boolean include(Calendar value, Calendar taskValue) {
		if (this == AFTER) {
			return taskValue.compareTo(value) > 0;
		}
		
		if (this == BEFORE) {
			return taskValue.compareTo(value) < 0;
		}
		
		if (this == EQUALS) {
			return taskValue.equals(value);
		}
		
		return false;
	}
	
}
