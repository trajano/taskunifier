package com.leclercb.taskunifier.gui.commons.values;

import org.jdesktop.swingx.renderer.StringValue;

import com.leclercb.taskunifier.gui.utils.TimeValue;

public class StringValueTimeValue implements StringValue {
	
	public static final StringValueTimeValue INSTANCE = new StringValueTimeValue();
	
	private StringValueTimeValue() {
		
	}
	
	@Override
	public String getString(Object value) {
		if (!(value instanceof TimeValue))
			return " ";
		
		return ((TimeValue) value).getLabel();
	}
	
}
