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

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.swing.formatters.RegexFormatter;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.commons.renderers.SynchronizerChoiceListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.SynchronizerGuiPluginListCellRenderer;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
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
				Main.SETTINGS.getStringProperty("api.id"),
				((SynchronizerGuiPlugin) this.getValue("API")).getId()))
			SynchronizerUtils.resetSynchronizerAndDeleteModels();
		
		// First update version because there are listeners on api.id
		Main.SETTINGS.setStringProperty(
				"api.version",
				((SynchronizerGuiPlugin) this.getValue("API")).getVersion());
		
		Main.SETTINGS.setStringProperty(
				"api.id",
				((SynchronizerGuiPlugin) this.getValue("API")).getId());
		
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
		
		Main.SETTINGS.setLongProperty(
				"synchronizer.scheduler_sleep_time",
				((Integer) this.getValue("SCHEDULER_SLEEP_TIME")) * 1000l);
		
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.sync_start",
				(Boolean) this.getValue("SYNC_START"));
		
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.sync_exit",
				(Boolean) this.getValue("SYNC_EXIT"));
	}
	
	private void initialize(boolean welcome) {
		SynchronizerChoice synchronizationChoiceValue = SynchronizerChoice.KEEP_LAST_UPDATED;
		String synchronizationKeepValue = "14";
		Long synchronizationSchedulerSleepTime = 600l;
		Boolean synchronizationSyncAtStart = false;
		Boolean synchronizationSyncAtExit = false;
		
		if (Main.SETTINGS.getEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class) != null)
			synchronizationChoiceValue = (SynchronizerChoice) Main.SETTINGS.getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
		
		if (Main.SETTINGS.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days") != null)
			synchronizationKeepValue = Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days");
		
		if (Main.SETTINGS.getLongProperty("synchronizer.scheduler_sleep_time") != null)
			synchronizationSchedulerSleepTime = Main.SETTINGS.getLongProperty("synchronizer.scheduler_sleep_time") / 1000;
		
		if (Main.SETTINGS.getBooleanProperty("synchronizer.sync_start") != null)
			synchronizationSyncAtStart = Main.SETTINGS.getBooleanProperty("synchronizer.sync_start");
		
		if (Main.SETTINGS.getBooleanProperty("synchronizer.sync_exit") != null)
			synchronizationSyncAtExit = Main.SETTINGS.getBooleanProperty("synchronizer.sync_exit");
		
		this.addField(new ConfigurationField(
				"API_RESET_ALL",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.synchronization.api_reset_all"))));
		
		ConfigurationFieldType.ComboBox comboBox = null;
		
		comboBox = new ConfigurationFieldType.ComboBox(
				Main.API_PLUGINS.getPlugins().toArray(),
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
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"SCHEDULER_SLEEP_TIME",
				Translations.getString("configuration.synchronization.scheduler_sleep_time"),
				new ConfigurationFieldType.Spinner()));
		
		JSpinner spinner = (JSpinner) this.getField("SCHEDULER_SLEEP_TIME").getType().getFieldComponent();
		spinner.setModel(new SpinnerNumberModel(
				synchronizationSchedulerSleepTime.intValue(),
				10,
				5 * 3600,
				60));
		spinner.setEditor(new JSpinner.NumberEditor(spinner));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_3",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"SYNC_START",
				Translations.getString("configuration.synchronization.sync_start"),
				new ConfigurationFieldType.CheckBox(synchronizationSyncAtStart)));
		
		this.addField(new ConfigurationField(
				"SYNC_EXIT",
				Translations.getString("configuration.synchronization.sync_exit"),
				new ConfigurationFieldType.CheckBox(synchronizationSyncAtExit)));
		
		if (!welcome) {
			this.addField(new ConfigurationField(
					"SEPARATOR_4",
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
					new ConfigurationFieldType.Button(new ActionSynchronize(
							false) {
						
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
					new ConfigurationFieldType.Button(new ActionSynchronize(
							false) {
						
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
		if (SynchronizerUtils.getPlugin().getId().equals(
				DummyGuiPlugin.getInstance().getId())) {
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
