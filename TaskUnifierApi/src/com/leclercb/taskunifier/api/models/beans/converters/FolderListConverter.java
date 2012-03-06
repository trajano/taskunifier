package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.ModelType;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class FolderListConverter extends ModelListConverter<Folder> {
	
	public FolderListConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(ModelType.FOLDER, mapper, reflectionProvider);
	}
	
}
