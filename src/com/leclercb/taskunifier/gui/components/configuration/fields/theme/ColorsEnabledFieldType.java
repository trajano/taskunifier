package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ColorsEnabledFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public ColorsEnabledFieldType() {
		super(Main.SETTINGS, "theme.color.enabled");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("theme.color.enabled") != null)
			return Main.SETTINGS.getBooleanProperty("theme.color.enabled");
		else
			return true;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"theme.color.enabled",
				this.getFieldValue());
	}
	
}
