package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class SyncAtStartFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public SyncAtStartFieldType() {
		super(Main.SETTINGS, "synchronizer.sync_start");
	}
	
	@Override
	public Boolean getPropertyValue() {
		return Main.SETTINGS.getBooleanProperty("synchronizer.sync_start");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.sync_start",
				this.getFieldValue());
	}
	
}
