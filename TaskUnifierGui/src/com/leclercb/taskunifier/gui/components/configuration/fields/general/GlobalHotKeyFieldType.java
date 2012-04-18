package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;

public class GlobalHotKeyFieldType extends ConfigurationFieldType.Panel {
	
	private String propertyName;
	
	public GlobalHotKeyFieldType(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
