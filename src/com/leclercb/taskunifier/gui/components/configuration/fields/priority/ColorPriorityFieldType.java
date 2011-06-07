package com.leclercb.taskunifier.gui.components.configuration.fields.priority;

import java.awt.Color;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ColorPriorityFieldType extends ConfigurationFieldTypeExt.ColorChooser {
	
	private String key;
	
	public ColorPriorityFieldType(TaskPriority priority) {
		super(Main.SETTINGS, toKey(priority));
		
		CheckUtils.isNotNull(priority, "Priority cannot be null");
		this.key = toKey(priority);
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
	
	private static String toKey(TaskPriority priority) {
		return "theme.color.priority." + priority.name().toLowerCase();
	}
	
}
