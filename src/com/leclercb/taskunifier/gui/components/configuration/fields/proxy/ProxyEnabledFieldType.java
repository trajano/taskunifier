package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ProxyEnabledFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public ProxyEnabledFieldType() {
		super(Main.SETTINGS, "proxy.enabled");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("proxy.enabled") != null)
			return Main.SETTINGS.getBooleanProperty("proxy.enabled");
		else
			return false;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty("proxy.enabled", this.getFieldValue());
	}
	
}
