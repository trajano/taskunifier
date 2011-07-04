package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class UseSystemProxiesFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public UseSystemProxiesFieldType() {
		super(Main.SETTINGS, "proxy.use_system_proxies");
	}
	
	@Override
	public Boolean getPropertyValue() {
		return Main.SETTINGS.getBooleanProperty("proxy.use_system_proxies");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"proxy.use_system_proxies",
				this.getFieldValue());
	}
	
}
