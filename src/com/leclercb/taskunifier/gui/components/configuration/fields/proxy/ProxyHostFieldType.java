package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ProxyHostFieldType extends ConfigurationFieldTypeExt.TextField {
	
	public ProxyHostFieldType() {
		super(Main.SETTINGS, "proxy.host");
	}
	
	@Override
	public String getPropertyValue() {
		if (Main.SETTINGS.getStringProperty("proxy.host") != null)
			return Main.SETTINGS.getStringProperty("proxy.host");
		else
			return "";
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty("proxy.host", this.getFieldValue());
	}
	
}
