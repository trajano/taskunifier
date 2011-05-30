package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum NumberCondition implements Condition<Number, Number> {
	
	GREATER_THAN,
	GREATER_THAN_OR_EQUALS,
	LESS_THAN,
	LESS_THAN_OR_EQUALS,
	EQUALS,
	NOT_EQUALS;
	
	private NumberCondition() {

	}
	
	@Override
	public Class<?> getValueType() {
		return Number.class;
	}
	
	@Override
	public Class<?> getTaskValueType() {
		return Number.class;
	}
	
	@Override
	public boolean include(Number value, Number taskValue) {
		if (this == GREATER_THAN) {
			return taskValue.doubleValue() > value.doubleValue();
		}
		
		if (this == GREATER_THAN_OR_EQUALS) {
			return taskValue.doubleValue() >= value.doubleValue();
		}
		
		if (this == LESS_THAN) {
			return taskValue.doubleValue() < value.doubleValue();
		}
		
		if (this == LESS_THAN_OR_EQUALS) {
			return taskValue.doubleValue() <= value.doubleValue();
		}
		
		if (this == EQUALS) {
			return taskValue.doubleValue() == value.doubleValue();
		}
		
		if (this == NOT_EQUALS) {
			return taskValue.doubleValue() != value.doubleValue();
		}
		
		return false;
	}
	
}
