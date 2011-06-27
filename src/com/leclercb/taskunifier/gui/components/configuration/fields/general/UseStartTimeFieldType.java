package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class UseStartTimeFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public UseStartTimeFieldType() {
		super(Main.SETTINGS, "date.use_start_time");
	}
	
	@Override
	public Boolean getPropertyValue() {
		return Main.SETTINGS.getBooleanProperty("date.use_start_time");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"date.use_start_time",
				this.getFieldValue());
	}
	
}
