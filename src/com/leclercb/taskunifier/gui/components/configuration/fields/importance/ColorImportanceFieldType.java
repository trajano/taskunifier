package com.leclercb.taskunifier.gui.components.configuration.fields.importance;

import java.awt.Color;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ColorImportanceFieldType extends ConfigurationFieldTypeExt.ColorChooser {
	
	private String key;
	
	public ColorImportanceFieldType(int importance) {
		super(Main.SETTINGS, toKey(importance));
		
		CheckUtils.isNotNull(importance, "Importance cannot be null");
		this.key = toKey(importance);
	}
	
	@Override
	public Color getPropertyValue() {
		if (Main.SETTINGS.getColorProperty(this.key) != null)
			return Main.SETTINGS.getColorProperty(this.key);
		else
			return Color.WHITE;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setColorProperty(this.key, this.getFieldValue());
	}
	
	private static String toKey(int importance) {
		return "theme.color.importance." + importance;
	}
	
}
