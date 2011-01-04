/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.renderers.SimpleDateFormatListCellRenderer;
import com.leclercb.taskunifier.gui.translations.Translations;

public class GeneralConfigurationPanel extends ConfigurationPanel {
	
	public GeneralConfigurationPanel() {
		super("configuration_general.html");
		this.initialize();
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		Settings.setLocaleProperty(
				"general.locale",
				(Locale) this.getValue("LANGUAGE"));
		Settings.setSimpleDateFormatProperty(
				"date.date_format",
				(SimpleDateFormat) this.getValue("DATE_FORMAT"));
		Settings.setSimpleDateFormatProperty(
				"date.time_format",
				(SimpleDateFormat) this.getValue("TIME_FORMAT"));
		
		Settings.setLongProperty(
				"synchronizer.scheduler_sleep_time",
				((Integer) this.getValue("SCHEDULER_SLEEP_TIME")) * 1000l);
	}
	
	private void initialize() {
		Locale generalLanguageValue = Translations.getDefaultLocale();
		SimpleDateFormat generalDateFormatValue = new SimpleDateFormat(
				"dd/MM/yyyy");
		SimpleDateFormat generalTimeFormatValue = new SimpleDateFormat(
				"dd/MM/yyyy");
		Long schedulerSleepTime = 600l;
		
		if (Settings.getLocaleProperty("general.locale") != null)
			generalLanguageValue = Settings.getLocaleProperty("general.locale");
		
		if (Settings.getSimpleDateFormatProperty("date.date_format") != null)
			generalDateFormatValue = Settings.getSimpleDateFormatProperty("date.date_format");
		
		if (Settings.getSimpleDateFormatProperty("date.time_format") != null)
			generalTimeFormatValue = Settings.getSimpleDateFormatProperty("date.time_format");
		
		if (Settings.getLongProperty("synchronizer.scheduler_sleep_time") != null)
			schedulerSleepTime = Settings.getLongProperty("synchronizer.scheduler_sleep_time") / 1000;
		
		this.addField(new ConfigurationField(
				"LANGUAGE_AFTER_RESTART",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.general.language_changed_after_restart"))));
		
		ConfigurationFieldType.ComboBox comboBox = new ConfigurationFieldType.ComboBox(
				Translations.getLocales().toArray(),
				generalLanguageValue);
		
		comboBox.setRenderer(new DefaultListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(
					JList list,
					Object value,
					int index,
					boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(
						list,
						value,
						index,
						isSelected,
						cellHasFocus);
				
				this.setText(((Locale) value).getDisplayName());
				
				return component;
			}
			
		});
		
		this.addField(new ConfigurationField(
				"LANGUAGE",
				Translations.getString("configuration.general.language"),
				comboBox));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"FORMATS_AFTER_RESTART",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.general.formats_changed_after_restart"))));
		
		SimpleDateFormat[] dateFormats = new SimpleDateFormat[] {
				new SimpleDateFormat("MMM dd, yyyy"),
				new SimpleDateFormat("MM/dd/yyyy"),
				new SimpleDateFormat("dd/MM/yyyy"),
				new SimpleDateFormat("yyyy-MM-dd") };
		
		this.addField(new ConfigurationField(
				"DATE_FORMAT",
				Translations.getString("configuration.general.date_format"),
				new ConfigurationFieldType.ComboBox(
						dateFormats,
						generalDateFormatValue)));
		
		((ConfigurationFieldType.ComboBox) this.getField("DATE_FORMAT").getType()).getFieldComponent().setRenderer(
				new SimpleDateFormatListCellRenderer());
		
		SimpleDateFormat[] timeFormats = new SimpleDateFormat[] {
				new SimpleDateFormat("h:mm aa"),
				new SimpleDateFormat("HH:mm") };
		
		this.addField(new ConfigurationField(
				"TIME_FORMAT",
				Translations.getString("configuration.general.time_format"),
				new ConfigurationFieldType.ComboBox(
						timeFormats,
						generalTimeFormatValue)));
		
		((ConfigurationFieldType.ComboBox) this.getField("TIME_FORMAT").getType()).getFieldComponent().setRenderer(
				new SimpleDateFormatListCellRenderer());
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"SCHEDULER_SLEEP_TIME",
				Translations.getString("configuration.general.scheduler_sleep_time"),
				new ConfigurationFieldType.Spinner()));
		
		JSpinner spinner = (JSpinner) this.getField("SCHEDULER_SLEEP_TIME").getType().getFieldComponent();
		spinner.setModel(new SpinnerNumberModel(
				schedulerSleepTime.intValue(),
				10,
				5 * 3600,
				60));
		spinner.setEditor(new JSpinner.NumberEditor(spinner));
	}
	
}
