package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.gui.commons.models.SynchronizerChoiceModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueSynchronizerChoice;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ChoiceFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public ChoiceFieldType() {
		super(
				new SynchronizerChoiceModel(),
				Main.SETTINGS,
				"synchronizer.choice");
		
		this.setRenderer(new DefaultListRenderer(
				new StringValueSynchronizerChoice()));
	}
	
	@Override
	public Object getPropertyValue() {
		return Main.SETTINGS.getEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class);
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class,
				(SynchronizerChoice) this.getFieldValue());
	}
	
}
