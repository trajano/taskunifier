package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class UseDueTimeFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public UseDueTimeFieldType() {
		super(Main.SETTINGS, "date.use_due_time");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("date.use_due_time") != null)
			return Main.SETTINGS.getBooleanProperty("date.use_due_time");
		else
			return true;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"date.use_due_time",
				this.getFieldValue());
	}
	
}
