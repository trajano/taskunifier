package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import java.util.Calendar;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum DaysCondition implements Condition<Integer, Calendar> {
	
	GREATER_THAN,
	GREATER_THAN_OR_EQUALS,
	LESS_THAN,
	LESS_THAN_OR_EQUALS,
	EQUALS;
	
	private DaysCondition() {

	}
	
	@Override
	public Class<?> getValueType() {
		return Integer.class;
	}
	
	@Override
	public Class<?> getTaskValueType() {
		return Calendar.class;
	}
	
	@Override
	public boolean include(Integer value, Calendar taskValue) {
		long milliSeconds1 = taskValue.getTimeInMillis();
		long milliSeconds2 = Calendar.getInstance().getTimeInMillis();
		long diff = milliSeconds1 - milliSeconds2;
		double diffDays = diff / (24 * 60 * 60 * 1000.0);
		
		if (this == GREATER_THAN) {
			return diffDays > value;
		}
		
		if (this == GREATER_THAN_OR_EQUALS) {
			return diffDays >= value;
		}
		
		if (this == LESS_THAN) {
			return diffDays < value;
		}
		
		if (this == LESS_THAN_OR_EQUALS) {
			return diffDays <= value;
		}
		
		if (this == EQUALS) {
			return diffDays == value;
		}
		
		return false;
	}
	
}
