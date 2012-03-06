package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class TaskListConverter extends ModelListConverter<Task> {
	
	public TaskListConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(ModelType.TASK, mapper, reflectionProvider);
	}
	
}
