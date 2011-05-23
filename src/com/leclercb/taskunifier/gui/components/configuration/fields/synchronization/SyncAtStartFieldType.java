package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class SyncAtStartFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public SyncAtStartFieldType() {
		super(Main.SETTINGS, "synchronizer.sync_start");
	}
	
	@Override
	public Boolean getPropertyValue() {
		if (Main.SETTINGS.getBooleanProperty("synchronizer.sync_start") != null)
			return Main.SETTINGS.getBooleanProperty("synchronizer.sync_start");
		else
			return false;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.sync_start",
				this.getFieldValue());
	}
	
}
