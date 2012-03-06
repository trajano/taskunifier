package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ModelType;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class ContextListConverter extends ModelListConverter<Context> {
	
	public ContextListConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(ModelType.CONTEXT, mapper, reflectionProvider);
	}
	
}
