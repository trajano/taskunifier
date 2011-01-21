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
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.renderers.SynchronizerChoiceListCellRenderer;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizationConfigurationPanel extends ConfigurationPanel {
	
	public SynchronizationConfigurationPanel(boolean welcome) {
		super("configuration_synchronization.html");
		this.initialize(welcome);
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
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
		SynchronizerChoice toodledoChoiceValue = SynchronizerChoice.KEEP_LAST_UPDATED;
		String toodledoKeepValue = "14";
		
		if (Main.SETTINGS.getEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class) != null)
			toodledoChoiceValue = (SynchronizerChoice) Main.SETTINGS.getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
		
		if (Main.SETTINGS.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days") != null)
			toodledoKeepValue = Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days");
		
		ConfigurationFieldType.ComboBox comboBox = new ConfigurationFieldType.ComboBox(
				SynchronizerChoice.values(),
				toodledoChoiceValue);
		
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
						toodledoKeepValue)));
		
		if (!welcome) {
			this.addField(new ConfigurationField(
					"SEPARATOR_1",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL_LABEL",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString("configuration.synchronization.synchronize_all"))));
			
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
							Translations.getString("configuration.synchronization.reset_all"))));
			
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
	}
	
}
