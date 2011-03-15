package com.leclercb.taskunifier.gui.commons.converters;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.api.utils.ArrayUtils;

public class TagsConverter extends AbstractConverter {
	
	public TagsConverter(ValueModel subject) {
		super(subject);
	}
	
	@Override
	public void setValue(Object tags) {
		this.subject.setValue(((String) tags).split(","));
	}
	
	@Override
	public Object convertFromSubject(Object tags) {
		return ArrayUtils.arrayToString((String[]) tags, ", ");
	}
	
}
