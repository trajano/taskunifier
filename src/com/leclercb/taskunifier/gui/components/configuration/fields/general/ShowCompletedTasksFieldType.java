package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ShowCompletedTasksFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public ShowCompletedTasksFieldType() {
		super(Main.SETTINGS, "searcher.show_completed_tasks");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks") != null)
			return Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks");
		else
			return true;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"searcher.show_completed_tasks",
				this.getFieldValue());
	}
	
}
