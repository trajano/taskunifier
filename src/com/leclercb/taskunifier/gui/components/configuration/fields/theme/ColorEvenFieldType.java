package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import java.awt.Color;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ColorEvenFieldType extends ConfigurationFieldTypeExt.ColorChooser {
	
	public ColorEvenFieldType() {
		super(Main.SETTINGS, "theme.color.even");
	}
	
	@Override
	public Color getPropertyValue() {
		return Main.SETTINGS.getColorProperty("theme.color.even");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setColorProperty("theme.color.even", this.getFieldValue());
	}
	
}
