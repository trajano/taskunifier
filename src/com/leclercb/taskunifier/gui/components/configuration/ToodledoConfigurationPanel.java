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

import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.toodledo.ToodledoSynchronizerChoice;
import com.leclercb.taskunifier.gui.actions.ActionCreateAccount;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.RegexFormatter;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ToodledoConfigurationPanel extends ConfigurationPanel {

	public ToodledoConfigurationPanel() {
		this.initialize();
		this.pack();
	}

	@Override
	public void saveAndApplyConfig() {
		Settings.setStringProperty("toodledo.email", (String) this.getValue("EMAIL"));
		Settings.setStringProperty("toodledo.password", (String) this.getValue("PASSWORD"));
		Settings.setStringProperty("toodledo.userid", null);
		Settings.setStringProperty("toodledo.token", null);
		Settings.setEnumProperty("synchronizer.choice", (ToodledoSynchronizerChoice) this.getValue("CHOICE"));
		Settings.setStringProperty("synchronizer.keep_tasks_completed_for_x_days", (String) this.getValue("KEEP"));
	}

	private void initialize() {
		String toodledoEmailValue = "";
		String toodledoPasswordValue = "";
		ToodledoSynchronizerChoice toodledoChoiceValue = ToodledoSynchronizerChoice.KEEP_LAST_UPDATED;
		String toodledoKeepValue = "15";

		if (Settings.getStringProperty("toodledo.email") != null)
			toodledoEmailValue = Settings.getStringProperty("toodledo.email");

		if (Settings.getStringProperty("toodledo.password") != null)
			toodledoPasswordValue = Settings.getStringProperty("toodledo.password");

		if (Settings.getEnumProperty("synchronizer.choice", ToodledoSynchronizerChoice.class) != null)
			toodledoChoiceValue = (ToodledoSynchronizerChoice) Settings.getEnumProperty("synchronizer.choice", ToodledoSynchronizerChoice.class);

		if (Settings.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days") != null)
			toodledoKeepValue = Settings.getStringProperty("synchronizer.keep_tasks_completed_for_x_days");


		this.addField(new ConfigurationField(
				"EMAIL", 
				Translations.getString("configuration.toodledo.email"), 
				new ConfigurationFieldType.TextField(toodledoEmailValue)));

		this.addField(new ConfigurationField(
				"PASSWORD", 
				Translations.getString("configuration.toodledo.password"), 
				new ConfigurationFieldType.PasswordField(toodledoPasswordValue)));

		this.addField(new ConfigurationField(
				"CHOICE", 
				Translations.getString("configuration.toodledo.choice"), 
				new ConfigurationFieldType.ComboBox(ToodledoSynchronizerChoice.values(), toodledoChoiceValue)));

		this.addField(new ConfigurationField(
				"KEEP", 
				Translations.getString("configuration.toodledo.keep_tasks_for"), 
				new ConfigurationFieldType.FormattedTextField(new RegexFormatter("^[0-9]{1,3}$"), toodledoKeepValue)));

		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));

		this.addField(new ConfigurationField(
				"CREATE_ACCOUNT_LABEL", 
				null, 
				new ConfigurationFieldType.Label(Translations.getString("configuration.toodledo.create_account"))));

		this.addField(new ConfigurationField(
				"CREATE_ACCOUNT", 
				null, 
				new ConfigurationFieldType.Button(new ActionCreateAccount() {

					@Override
					public void actionPerformed(ActionEvent event) {
						saveAndApplyConfig();

						super.actionPerformed(event);
					}

				})));

		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldType.Separator()));

		this.addField(new ConfigurationField(
				"SYNCHRONIZE_ALL_LABEL", 
				null, 
				new ConfigurationFieldType.Label(Translations.getString("configuration.toodledo.synchronize_all"))));

		this.addField(new ConfigurationField(
				"SYNCHRONIZE_ALL", 
				null, 
				new ConfigurationFieldType.Button(new ActionSynchronize() {

					@Override
					public void actionPerformed(ActionEvent event) {
						saveAndApplyConfig();

						SynchronizerUtils.resetSynchronizerSettings();

						super.actionPerformed(event);
					}

				})));

		this.addField(new ConfigurationField(
				"RESET_ALL_LABEL", 
				null, 
				new ConfigurationFieldType.Label(Translations.getString("configuration.toodledo.reset_all"))));

		this.addField(new ConfigurationField(
				"RESET_ALL", 
				null, 
				new ConfigurationFieldType.Button(new ActionSynchronize() {

					@Override
					public void actionPerformed(ActionEvent event) {
						try {
							saveAndApplyConfig();

							TaskFactory.getInstance().deleteAll();
							ContextFactory.getInstance().deleteAll();
							FolderFactory.getInstance().deleteAll();
							GoalFactory.getInstance().deleteAll();

							SynchronizerUtils.resetSynchronizerSettings();

							super.actionPerformed(event);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				})));
	}

}
