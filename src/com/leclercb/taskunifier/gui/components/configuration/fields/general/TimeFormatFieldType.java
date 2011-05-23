package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.text.SimpleDateFormat;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.values.StringValueSimpleDateFormat;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;

public class TimeFormatFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public TimeFormatFieldType() {
		super(
				DateTimeFormatUtils.getAvailableTimeFormats(),
				Main.SETTINGS,
		"date.time_format");
		
		this.setRenderer(new DefaultListRenderer(
				new StringValueSimpleDateFormat()));
	}
	
	@Override
	public Object getPropertyValue() {
		if (Main.SETTINGS.getSimpleDateFormatProperty("date.time_format") != null)
			return Main.SETTINGS.getSimpleDateFormatProperty("date.time_format");
		else
			return new SimpleDateFormat("HH:mm");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setSimpleDateFormatProperty(
				"date.time_format",
				(SimpleDateFormat) this.getFieldValue());
	}
	
}
