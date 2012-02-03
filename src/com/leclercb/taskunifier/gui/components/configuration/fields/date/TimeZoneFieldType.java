package com.leclercb.taskunifier.gui.components.configuration.fields.date;

import java.util.Arrays;
import java.util.TimeZone;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.values.StringValueTimeZone;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;

public class TimeZoneFieldType extends ConfigurationFieldType.ComboBox {
	
	public TimeZoneFieldType() {
		super(getValues(), Main.getSettings(), "date.timezone");
		
		this.setRenderer(new DefaultListRenderer(StringValueTimeZone.INSTANCE));
	}
	
	@Override
	public Object getPropertyValue() {
		return Main.getSettings().getStringProperty("date.timezone");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.getSettings().setStringProperty(
				"date.timezone",
				(String) this.getFieldValue());
	}
	
	private static String[] getValues() {
		String[] timezones = TimeZone.getAvailableIDs();
		Arrays.sort(timezones);
		
		return ArrayUtils.addAll(new String[] { null }, timezones);
	}
	
}
