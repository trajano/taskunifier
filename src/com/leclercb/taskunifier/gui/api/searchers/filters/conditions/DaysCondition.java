package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import java.util.Calendar;

import com.leclercb.commons.api.utils.DateUtils;
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
		Calendar conditionValue = Calendar.getInstance();
		taskValue = DateUtils.cloneCalendar(taskValue);
		
		conditionValue.set(
				conditionValue.get(Calendar.YEAR),
				conditionValue.get(Calendar.MONTH),
				conditionValue.get(Calendar.DAY_OF_MONTH),
				0,
				0,
				0);
		
		taskValue.set(
				taskValue.get(Calendar.YEAR),
				taskValue.get(Calendar.MONTH),
				taskValue.get(Calendar.DAY_OF_MONTH),
				0,
				0,
				0);
		
		long milliSeconds1 = taskValue.getTimeInMillis();
		long milliSeconds2 = conditionValue.getTimeInMillis();
		long diff = milliSeconds1 - milliSeconds2;
		long diffDays = Math.round(diff / (24 * 60 * 60 * 1000.0));
		
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
