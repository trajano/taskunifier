package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import com.leclercb.commons.gui.swing.formatters.RegexFormatter;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ProxyPortFieldType extends ConfigurationFieldTypeExt.FormattedTextField {
	
	public ProxyPortFieldType() {
		super(new RegexFormatter("^[0-9]{1,4}$"), Main.SETTINGS, "proxy.port");
	}
	
	@Override
	public String getPropertyValue() {
		if (Main.SETTINGS.getStringProperty("proxy.port") != null)
			return Main.SETTINGS.getStringProperty("proxy.port");
		else
			return "0";
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty("proxy.port", this.getFieldValue());
	}
	
}
