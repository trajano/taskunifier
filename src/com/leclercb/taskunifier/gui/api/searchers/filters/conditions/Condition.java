package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public interface Condition<ValueType, TaskValueType> {
	
	public abstract Class<?> getValueType();
	
	public abstract Class<?> getTaskValueType();
	
	public abstract String name();
	
	public abstract boolean include(ValueType value, TaskValueType taskValue);
	
}
