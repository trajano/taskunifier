package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.ModelType;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class LocationListConverter extends ModelListConverter<Location> {
	
	public LocationListConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(ModelType.LOCATION, mapper, reflectionProvider);
	}
	
}
