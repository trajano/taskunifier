package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ShowCompletedTasksAtTheEndFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public ShowCompletedTasksAtTheEndFieldType() {
		super(Main.SETTINGS, "searcher.show_completed_tasks_at_the_end");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end") != null)
			return Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end");
		else
			return false;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"searcher.show_completed_tasks_at_the_end",
				this.getFieldValue());
	}
	
}
