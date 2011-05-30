package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum StringCondition implements Condition<String, Object> {
	
	EQUALS,
	CONTAINS,
	STARTS_WITH,
	ENDS_WITH,
	NOT_EQUALS;
	
	private StringCondition() {

	}
	
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	
	@Override
	public Class<?> getTaskValueType() {
		return Object.class;
	}
	
	@Override
	public boolean include(String value, Object taskValue) {
		if (this == EQUALS) {
			return taskValue.toString().equalsIgnoreCase(value);
		}
		
		if (this == CONTAINS) {
			return taskValue.toString().toLowerCase().contains(
					value.toLowerCase());
		}
		
		if (this == STARTS_WITH) {
			return taskValue.toString().toLowerCase().startsWith(
					value.toLowerCase());
		}
		
		if (this == ENDS_WITH) {
			return taskValue.toString().toLowerCase().endsWith(
					value.toLowerCase());
		}
		
		if (this == NOT_EQUALS) {
			return !(taskValue.toString().equalsIgnoreCase(value));
		}
		
		return false;
	}
	
}
