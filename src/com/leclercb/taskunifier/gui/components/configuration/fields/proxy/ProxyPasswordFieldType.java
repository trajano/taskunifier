package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ProxyPasswordFieldType extends ConfigurationFieldTypeExt.PasswordField {
	
	public ProxyPasswordFieldType() {
		super(Main.SETTINGS, "proxy.password");
	}
	
	@Override
	public String getPropertyValue() {
		if (Main.SETTINGS.getStringProperty("proxy.password") != null)
			return Main.SETTINGS.getStringProperty("proxy.password");
		else
			return "";
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty("proxy.password", this.getFieldValue());
	}
	
}
