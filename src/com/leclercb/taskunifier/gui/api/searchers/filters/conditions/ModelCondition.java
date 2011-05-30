package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum ModelCondition implements Condition<Model, Model> {
	
	EQUALS,
	NOT_EQUALS;
	
	private ModelCondition() {

	}
	
	@Override
	public Class<?> getValueType() {
		return Model.class;
	}
	
	@Override
	public Class<?> getTaskValueType() {
		return Model.class;
	}
	
	@Override
	public boolean include(Model value, Model taskValue) {
		if (this == EQUALS) {
			return taskValue.equals(value);
		}
		
		if (this == NOT_EQUALS) {
			return !(taskValue.equals(value));
		}
		
		return false;
	}
	
}
