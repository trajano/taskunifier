package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.ModelType;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class GoalListConverter extends ModelListConverter<Goal> {
	
	public GoalListConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(ModelType.GOAL, mapper, reflectionProvider);
	}
	
}
