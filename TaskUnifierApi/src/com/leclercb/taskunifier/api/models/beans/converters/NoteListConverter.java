package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Note;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class NoteListConverter extends ModelListConverter<Note> {
	
	public NoteListConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(ModelType.NOTE, mapper, reflectionProvider);
	}
	
}
