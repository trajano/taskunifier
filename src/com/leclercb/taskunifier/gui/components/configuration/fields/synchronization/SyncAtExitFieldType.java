package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class SyncAtExitFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public SyncAtExitFieldType() {
		super(Main.SETTINGS, "synchronizer.sync_exit");
	}
	
	@Override
	public Boolean getPropertyValue() {
		return Main.SETTINGS.getBooleanProperty("synchronizer.sync_exit");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.sync_exit",
				this.getFieldValue());
	}
	
}
