package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ShowWindowEditOnAddFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public ShowWindowEditOnAddFieldType() {
		super(Main.SETTINGS, "task.show_edit_window_on_add");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add") != null)
			return Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add");
		else
			return false;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"task.show_edit_window_on_add",
				this.getFieldValue());
	}
	
}
