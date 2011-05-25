package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ColorsByImportanceEnabledFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public ColorsByImportanceEnabledFieldType() {
		super(Main.SETTINGS, "theme.color.importance.enabled");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("theme.color.importance.enabled") != null)
			return Main.SETTINGS.getBooleanProperty("theme.color.importance.enabled");
		else
			return false;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"theme.color.importance.enabled",
				this.getFieldValue());
	}
	
}
