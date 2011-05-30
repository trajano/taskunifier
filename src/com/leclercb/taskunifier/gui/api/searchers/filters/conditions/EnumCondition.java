package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum EnumCondition implements Condition<Enum<?>, Enum<?>> {
	
	EQUALS,
	NOT_EQUALS,
	GREATER_THAN,
	GREATER_THAN_OR_EQUALS,
	LESS_THAN,
	LESS_THAN_OR_EQUALS;
	
	private EnumCondition() {

	}
	
	@Override
	public Class<?> getValueType() {
		return Enum.class;
	}
	
	@Override
	public Class<?> getTaskValueType() {
		return Enum.class;
	}
	
	@Override
	public boolean include(Enum<?> value, Enum<?> taskValue) {
		if (this == EQUALS) {
			return taskValue.equals(value);
		}
		
		if (this == NOT_EQUALS) {
			return !(taskValue.equals(value));
		}
		
		if (this == GREATER_THAN) {
			return taskValue.ordinal() > value.ordinal();
		}
		
		if (this == GREATER_THAN_OR_EQUALS) {
			return taskValue.ordinal() >= value.ordinal();
		}
		
		if (this == LESS_THAN) {
			return taskValue.ordinal() < value.ordinal();
		}
		
		if (this == LESS_THAN_OR_EQUALS) {
			return taskValue.ordinal() <= value.ordinal();
		}
		
		return false;
	}
	
}
