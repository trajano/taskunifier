package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import java.awt.Color;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ColorEvenFieldType extends ConfigurationFieldTypeExt.ColorChooser {
	
	public ColorEvenFieldType() {
		super(Main.SETTINGS, "theme.color.even");
	}
	
	@Override
	public Color getPropertyValue() {
		if (Main.SETTINGS.getColorProperty("theme.color.even") != null)
			return Main.SETTINGS.getColorProperty("theme.color.even");
		else
			return Color.WHITE;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setColorProperty("theme.color.even", this.getFieldValue());
	}
	
}
