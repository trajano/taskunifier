package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class MinimizeToSystemTrayFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public MinimizeToSystemTrayFieldType() {
		super(Main.SETTINGS, "window.minimize_to_system_tray");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("window.minimize_to_system_tray") != null)
			return Main.SETTINGS.getBooleanProperty("window.minimize_to_system_tray");
		else
			return false;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"window.minimize_to_system_tray",
				this.getFieldValue());
	}
	
}
