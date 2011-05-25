package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ProxyLoginFieldType extends ConfigurationFieldTypeExt.TextField {
	
	public ProxyLoginFieldType() {
		super(Main.SETTINGS, "proxy.login");
	}
	
	@Override
	public String getPropertyValue() {
		if (Main.SETTINGS.getStringProperty("proxy.login") != null)
			return Main.SETTINGS.getStringProperty("proxy.login");
		else
			return "";
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty("proxy.login", this.getFieldValue());
	}
	
}
