/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
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
		
		if (!this.languageOnly) {
			this.addField(new ConfigurationField(
					"LANGUAGE_AFTER_RESTART",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString("configuration.general.language_changed_after_restart"))));
		}
		
		ConfigurationFieldType.ComboBox comboBox = new ConfigurationFieldType.ComboBox(
				Translations.getLocales().toArray(),
				Main.SETTINGS,
				"general.locale") {
			
			@Override
			public Object getPropertyValue() {
				if (Main.SETTINGS.getLocaleProperty("general.locale") != null)
					return Main.SETTINGS.getLocaleProperty("general.locale");
				else
					return Translations.getDefaultLocale();
			}
			
		};
		
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
							Main.SETTINGS,
							"date.date_format") {
						
						@Override
						public Object getPropertyValue() {
							if (Main.SETTINGS.getSimpleDateFormatProperty("date.date_format") != null)
								return Main.SETTINGS.getSimpleDateFormatProperty("date.date_format");
							else
								return new SimpleDateFormat("dd/MM/yyyy");
						}
						
					}));
			
			((ConfigurationFieldType.ComboBox) this.getField("DATE_FORMAT").getType()).getFieldComponent().setRenderer(
					new SimpleDateFormatListCellRenderer());
			
			this.addField(new ConfigurationField(
					"TIME_FORMAT",
					Translations.getString("configuration.general.time_format"),
					new ConfigurationFieldType.ComboBox(
							DateTimeFormatUtils.getAvailableTimeFormats(),
							Main.SETTINGS,
							"date.time_format") {
						
						@Override
						public Object getPropertyValue() {
							if (Main.SETTINGS.getSimpleDateFormatProperty("date.time_format") != null)
								return Main.SETTINGS.getSimpleDateFormatProperty("date.time_format");
							else
								return new SimpleDateFormat("HH:mm");
						}
						
					}));
			
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
							Main.SETTINGS,
							"task.show_edit_window_on_add") {
						
						@Override
						public Boolean getPropertyValue() {
							if (Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add") != null)
								return Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add");
							else
								return false;
						}
						
					}));
			
			this.addField(new ConfigurationField(
					"SEPARATOR_3",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SHOW_COMPLETED_TASKS",
					Translations.getString("configuration.general.show_completed_tasks"),
					new ConfigurationFieldType.CheckBox(
							Main.SETTINGS,
							"searcher.show_completed_tasks") {
						
						@Override
						public Boolean getPropertyValue() {
							if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks") != null)
								return Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks");
							else
								return true;
						}
						
					}));
			
			this.addField(new ConfigurationField(
					"SHOW_COMPLETED_TASKS_AT_THE_END",
					Translations.getString("configuration.general.show_completed_tasks_at_the_end"),
					new ConfigurationFieldType.CheckBox(
							Main.SETTINGS,
							"searcher.show_completed_tasks_at_the_end") {
						
						@Override
						public Boolean getPropertyValue() {
							if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end") != null)
								return Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end");
							else
								return false;
						}
						
					}));
			
			this.addField(new ConfigurationField(
					"RESET_GENERAL_SEARCHERS",
					null,
					new ConfigurationFieldType.Button(
							new ActionResetGeneralSearchers(22, 22))));
		}
	}
	
}
