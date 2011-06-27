package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import java.awt.Color;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ColorOddFieldType extends ConfigurationFieldTypeExt.ColorChooser {
	
	public ColorOddFieldType() {
		super(Main.SETTINGS, "theme.color.odd");
	}
	
	@Override
	public Color getPropertyValue() {
		return Main.SETTINGS.getColorProperty("theme.color.odd");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setColorProperty("theme.color.odd", this.getFieldValue());
	}
	
}
