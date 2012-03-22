package com.leclercb.taskunifier.gui.commons.values;

import org.jdesktop.swingx.renderer.StringValue;

import com.leclercb.taskunifier.gui.utils.TaskPostponeList.PostponeItem;

public class StringValueTaskPostponeItem implements StringValue {
	
	public static final StringValueTaskPostponeItem INSTANCE = new StringValueTaskPostponeItem();
	
	private StringValueTaskPostponeItem() {
		
	}
	
	@Override
	public String getString(Object value) {
		if (!(value instanceof PostponeItem))
			return " ";
		
		return value.toString();
	}
	
}
