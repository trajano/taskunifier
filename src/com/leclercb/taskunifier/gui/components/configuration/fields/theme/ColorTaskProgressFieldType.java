package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import java.awt.Color;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ColorTaskProgressFieldType extends ConfigurationFieldTypeExt.ColorChooser {
	
	public ColorTaskProgressFieldType() {
		super(Main.SETTINGS, "theme.color.progress");
	}
	
	@Override
	public Color getPropertyValue() {
		if (Main.SETTINGS.getColorProperty("theme.color.progress") != null)
			return Main.SETTINGS.getColorProperty("theme.color.progress");
		else
			return Color.WHITE;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setColorProperty(
				"theme.color.progress",
				this.getFieldValue());
	}
	
}
