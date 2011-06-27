package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.utils.FormatterUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class KeepTasksForFieldType extends ConfigurationFieldTypeExt.FormattedTextField {
	
	public KeepTasksForFieldType() {
		super(
				FormatterUtils.getRegexFormatter("^[0-9]{1,4}$"),
				Main.SETTINGS,
				"synchronizer.keep_tasks_completed_for_x_days");
	}
	
	@Override
	public String getPropertyValue() {
		return Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days");
	}
	
	@Override
	public void saveAndApplyConfig() {
		if (!EqualsUtils.equals(
				Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days"),
				this.getFieldValue()))
			SynchronizerUtils.resetSynchronizer();
		
		Main.SETTINGS.setStringProperty(
				"synchronizer.keep_tasks_completed_for_x_days",
				this.getFieldValue());
	}
	
}
