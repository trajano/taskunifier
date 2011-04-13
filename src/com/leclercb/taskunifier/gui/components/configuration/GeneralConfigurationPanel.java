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

import com.leclercb.taskunifier.gui.commons.renderers.SimpleDateFormatListCellRenderer;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;

public class GeneralConfigurationPanel extends DefaultConfigurationPanel {
	
	private boolean languageOnly;
	
	public GeneralConfigurationPanel(boolean languageOnly) {
		super(
				languageOnly ? null : Help.getHelpFile("configuration_general.html"));
		this.languageOnly = languageOnly;
		this.initialize();
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setLocaleProperty(
				"general.locale",
				(Locale) this.getValue("LANGUAGE"));
		
		if (this.languageOnly) {
			Translations.setLocale(Main.SETTINGS.getLocaleProperty("general.locale"));
		}
		
		if (!this.languageOnly) {
			Main.SETTINGS.setSimpleDateFormatProperty(
					"date.date_format",
					(SimpleDateFormat) this.getValue("DATE_FORMAT"));
			Main.SETTINGS.setSimpleDateFormatProperty(
					"date.time_format",
					(SimpleDateFormat) this.getValue("TIME_FORMAT"));
			Main.SETTINGS.setBooleanProperty(
					"task.show_edit_window_on_add",
					(Boolean) this.getValue("SHOW_EDIT_WINDOW_ON_ADD"));
			Main.SETTINGS.setBooleanProperty(
					"searcher.show_completed_tasks",
					(Boolean) this.getValue("SHOW_COMPLETED_TASKS"));
			Main.SETTINGS.setBooleanProperty(
					"searcher.show_completed_tasks_at_the_end",
					(Boolean) this.getValue("SHOW_COMPLETED_TASKS_AT_THE_END"));
		}
	}
	
	private void initialize() {
		Locale generalLanguageValue = Translations.getDefaultLocale();
		
		if (Main.SETTINGS.getLocaleProperty("general.locale") != null)
			generalLanguageValue = Main.SETTINGS.getLocaleProperty("general.locale");
		
		if (!this.languageOnly) {
			this.addField(new ConfigurationField(
					"LANGUAGE_AFTER_RESTART",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString("configuration.general.language_changed_after_restart"))));
		}
		
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
		
		if (!this.languageOnly) {
			SimpleDateFormat generalDateFormatValue = new SimpleDateFormat(
					"dd/MM/yyyy");
			SimpleDateFormat generalTimeFormatValue = new SimpleDateFormat(
					"dd/MM/yyyy");
			Boolean generalShowEditWindowOnAdd = false;
			Boolean generalShowCompletedTasks = true;
			Boolean generalShowCompletedTasksAtTheEnd = false;
			
			if (Main.SETTINGS.getSimpleDateFormatProperty("date.date_format") != null)
				generalDateFormatValue = Main.SETTINGS.getSimpleDateFormatProperty("date.date_format");
			
			if (Main.SETTINGS.getSimpleDateFormatProperty("date.time_format") != null)
				generalTimeFormatValue = Main.SETTINGS.getSimpleDateFormatProperty("date.time_format");
			
			if (Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add") != null)
				generalShowEditWindowOnAdd = Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add");
			
			if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks") != null)
				generalShowCompletedTasks = Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks");
			
			if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end") != null)
				generalShowCompletedTasksAtTheEnd = Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end");
			
			this.addField(new ConfigurationField(
					"SEPARATOR_1",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"FORMATS_AFTER_RESTART",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString("configuration.general.formats_changed_after_restart"))));
			
			this.addField(new ConfigurationField(
					"DATE_FORMAT",
					Translations.getString("configuration.general.date_format"),
					new ConfigurationFieldType.ComboBox(
							DateTimeFormatUtils.getAvailableDateFormats(),
							generalDateFormatValue)));
			
			((ConfigurationFieldType.ComboBox) this.getField("DATE_FORMAT").getType()).getFieldComponent().setRenderer(
					new SimpleDateFormatListCellRenderer());
			
			this.addField(new ConfigurationField(
					"TIME_FORMAT",
					Translations.getString("configuration.general.time_format"),
					new ConfigurationFieldType.ComboBox(
							DateTimeFormatUtils.getAvailableTimeFormats(),
							generalTimeFormatValue)));
			
			((ConfigurationFieldType.ComboBox) this.getField("TIME_FORMAT").getType()).getFieldComponent().setRenderer(
					new SimpleDateFormatListCellRenderer());
			
			this.addField(new ConfigurationField(
					"SEPARATOR_2",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SHOW_EDIT_WINDOW_ON_ADD",
					Translations.getString("configuration.general.show_edit_window_on_add"),
					new ConfigurationFieldType.CheckBox(
							generalShowEditWindowOnAdd)));
			
			this.addField(new ConfigurationField(
					"SEPARATOR_3",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SETTINGS_AFTER_RESTART",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString("configuration.general.settings_changed_after_restart"))));
			
			this.addField(new ConfigurationField(
					"SHOW_COMPLETED_TASKS",
					Translations.getString("configuration.general.show_completed_tasks"),
					new ConfigurationFieldType.CheckBox(
							generalShowCompletedTasks)));
			
			this.addField(new ConfigurationField(
					"SHOW_COMPLETED_TASKS_AT_THE_END",
					Translations.getString("configuration.general.show_completed_tasks_at_the_end"),
					new ConfigurationFieldType.CheckBox(
							generalShowCompletedTasksAtTheEnd)));
		}
	}
	
}
