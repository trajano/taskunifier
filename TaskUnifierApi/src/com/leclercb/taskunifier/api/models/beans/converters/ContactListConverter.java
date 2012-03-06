package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ModelType;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class ContactListConverter extends ModelListConverter<Contact> {
	
	public ContactListConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(ModelType.CONTACT, mapper, reflectionProvider);
	}
	
}
