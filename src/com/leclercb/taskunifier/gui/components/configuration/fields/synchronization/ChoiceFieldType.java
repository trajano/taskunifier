package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.gui.commons.models.SynchronizerChoiceModel;
import com.leclercb.taskunifier.gui.commons.renderers.SynchronizerChoiceListCellRenderer;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ChoiceFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public ChoiceFieldType() {
		super(
				new SynchronizerChoiceModel(),
				Main.SETTINGS,
				"synchronizer.choice");
		
		this.setRenderer(new SynchronizerChoiceListCellRenderer());
	}
	
	@Override
	public Object getPropertyValue() {
		if (Main.SETTINGS.getEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class) != null)
			return Main.SETTINGS.getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
		else
			return SynchronizerChoice.KEEP_LAST_UPDATED;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class,
				(SynchronizerChoice) this.getFieldValue());
	}
	
}
