package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.text.SimpleDateFormat;

import com.leclercb.taskunifier.gui.commons.renderers.SimpleDateFormatListCellRenderer;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;

public class DateFormatFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public DateFormatFieldType() {
		super(
				DateTimeFormatUtils.getAvailableDateFormats(),
				Main.SETTINGS,
				"date.date_format");
		
		this.setRenderer(new SimpleDateFormatListCellRenderer());
	}
	
	@Override
	public Object getPropertyValue() {
		if (Main.SETTINGS.getSimpleDateFormatProperty("date.date_format") != null)
			return Main.SETTINGS.getSimpleDateFormatProperty("date.date_format");
		else
			return new SimpleDateFormat("dd/MM/yyyy");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setSimpleDateFormatProperty(
				"date.date_format",
				(SimpleDateFormat) this.getFieldValue());
	}
	
}
