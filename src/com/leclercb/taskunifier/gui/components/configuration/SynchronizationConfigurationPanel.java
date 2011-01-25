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

import java.awt.event.ActionEvent;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.swing.formatters.RegexFormatter;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.renderers.SynchronizerChoiceListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.SynchronizerGuiPluginListCellRenderer;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.synchronizer.dummy.DummyApi;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizationConfigurationPanel extends DefaultConfigurationPanel {
	
	public SynchronizationConfigurationPanel(boolean welcome) {
		super("configuration_synchronization.html");
		this.initialize(welcome);
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		if (!EqualsUtils.equals(
				Main.SETTINGS.getStringProperty("api"),
				((SynchronizerGuiPlugin) this.getValue("API")).getSynchronizerApi().getApiId()))
			SynchronizerUtils.resetSynchronizerAndDeleteModels();
		
		Main.SETTINGS.setStringProperty(
				"api",
				((SynchronizerGuiPlugin) this.getValue("API")).getSynchronizerApi().getApiId());
		
		Main.SETTINGS.setEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class,
				(SynchronizerChoice) this.getValue("CHOICE"));
		
		if (!EqualsUtils.equals(
				Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days"),
				this.getValue("KEEP")))
			SynchronizerUtils.resetSynchronizer();
		
		Main.SETTINGS.setStringProperty(
				"synchronizer.keep_tasks_completed_for_x_days",
				(String) this.getValue("KEEP"));
	}
	
	private void initialize(boolean welcome) {
		SynchronizerChoice synchronizationChoiceValue = SynchronizerChoice.KEEP_LAST_UPDATED;
		String synchronizationKeepValue = "14";
		
		if (Main.SETTINGS.getEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class) != null)
			synchronizationChoiceValue = (SynchronizerChoice) Main.SETTINGS.getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
		
		if (Main.SETTINGS.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days") != null)
			synchronizationKeepValue = Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days");
		
		this.addField(new ConfigurationField(
				"API_RESET_ALL",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.synchronization.api_reset_all"))));
		
		ConfigurationFieldType.ComboBox comboBox = null;
		
		comboBox = new ConfigurationFieldType.ComboBox(
				Main.API_PLUGINS.getPlugins().toArray(
						new SynchronizerGuiPlugin[0]),
				SynchronizerUtils.getPlugin());
		
		comboBox.setRenderer(new SynchronizerGuiPluginListCellRenderer());
		
		this.addField(new ConfigurationField(
				"API",
				Translations.getString("general.api"),
				comboBox));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		comboBox = new ConfigurationFieldType.ComboBox(
				SynchronizerChoice.values(),
				synchronizationChoiceValue);
		
		comboBox.setRenderer(new SynchronizerChoiceListCellRenderer());
		
		this.addField(new ConfigurationField(
				"CHOICE",
				Translations.getString("configuration.synchronization.choice"),
				comboBox));
		
		this.addField(new ConfigurationField(
				"KEEP",
				Translations.getString("configuration.synchronization.keep_tasks_for"),
				new ConfigurationFieldType.FormattedTextField(
						new RegexFormatter("^[0-9]{1,3}$"),
						synchronizationKeepValue)));
		
		if (!welcome) {
			this.addField(new ConfigurationField(
					"SEPARATOR_2",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL_LABEL",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString(
									"configuration.synchronization.synchronize_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()))));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL",
					null,
					new ConfigurationFieldType.Button(new ActionSynchronize() {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							SynchronizationConfigurationPanel.this.saveAndApplyConfig();
							
							SynchronizerUtils.resetSynchronizer();
							
							super.actionPerformed(event);
						}
						
					})));
			
			this.addField(new ConfigurationField(
					"RESET_ALL_LABEL",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString(
									"configuration.synchronization.reset_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()))));
			
			this.addField(new ConfigurationField(
					"RESET_ALL",
					null,
					new ConfigurationFieldType.Button(new ActionSynchronize() {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							try {
								SynchronizationConfigurationPanel.this.saveAndApplyConfig();
								
								SynchronizerUtils.resetSynchronizerAndDeleteModels();
								
								super.actionPerformed(event);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					})));
		}
		
		// Disable fields for DUMMY service
		if (SynchronizerUtils.getPlugin().getSynchronizerApi().getApiId().equals(
				DummyApi.getInstance().getApiId())) {
			if (this.containsId("CHOICE"))
				this.setEnabled("CHOICE", false);
			
			if (this.containsId("KEEP"))
				this.setEnabled("KEEP", false);
			
			if (this.containsId("SYNCHRONIZE_ALL"))
				this.setEnabled("SYNCHRONIZE_ALL", false);
			
			if (this.containsId("RESET_ALL"))
				this.setEnabled("RESET_ALL", false);
		}
	}
	
}
