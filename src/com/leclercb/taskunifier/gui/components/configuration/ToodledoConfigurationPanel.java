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

import javax.swing.JOptionPane;

import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.actions.ActionCreateAccount;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ToodledoConfigurationPanel extends ConfigurationPanel {
	
	public ToodledoConfigurationPanel(boolean welcome) {
		super("configuration_toodledo.html");
		this.initialize(welcome);
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty(
				"toodledo.email",
				(String) this.getValue("EMAIL"));
		Main.SETTINGS.setStringProperty(
				"toodledo.password",
				(String) this.getValue("PASSWORD"));
	}
	
	private void initialize(boolean welcome) {
		String toodledoEmailValue = "";
		String toodledoPasswordValue = "";
		
		if (Main.SETTINGS.getStringProperty("toodledo.email") != null)
			toodledoEmailValue = Main.SETTINGS.getStringProperty("toodledo.email");
		
		if (Main.SETTINGS.getStringProperty("toodledo.password") != null)
			toodledoPasswordValue = Main.SETTINGS.getStringProperty("toodledo.password");
		
		this.addField(new ConfigurationField(
				"EMAIL",
				Translations.getString("configuration.toodledo.email"),
				new ConfigurationFieldType.TextField(toodledoEmailValue)));
		
		this.addField(new ConfigurationField(
				"PASSWORD",
				Translations.getString("configuration.toodledo.password"),
				new ConfigurationFieldType.PasswordField(toodledoPasswordValue)));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"CREATE_ACCOUNT_LABEL",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.toodledo.create_account"))));
		
		this.addField(new ConfigurationField(
				"CREATE_ACCOUNT",
				null,
				new ConfigurationFieldType.Button(new ActionCreateAccount() {
					
					@Override
					public void createAccount() {
						ToodledoConfigurationPanel.this.saveAndApplyConfig();
						
						String email = Main.SETTINGS.getStringProperty("toodledo.email");
						String password = Main.SETTINGS.getStringProperty("toodledo.password");
						
						try {
							if (email == null)
								throw new Exception(
										Translations.getString("error.empty_email"));
							
							if (password == null)
								throw new Exception(
										Translations.getString("error.empty_password"));
							
							SynchronizerUtils.initializeProxy();
							SynchronizerUtils.getApi().createAccount(
									new Object[] { email, password });
							
							JOptionPane.showMessageDialog(
									MainFrame.getInstance().getFrame(),
									Translations.getString("action.create_account.account_created"),
									Translations.getString("general.information"),
									JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e) {
							ErrorDialog errorDialog = new ErrorDialog(
									MainFrame.getInstance().getFrame(),
									e,
									true);
							errorDialog.setVisible(true);
							
							return;
						}
					}
					
				})));
		
		if (!welcome) {
			this.addField(new ConfigurationField(
					"SEPARATOR_2",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL_LABEL",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString("configuration.toodledo.synchronize_all"))));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL",
					null,
					new ConfigurationFieldType.Button(new ActionSynchronize() {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							ToodledoConfigurationPanel.this.saveAndApplyConfig();
							
							SynchronizerUtils.getApi().resetSynchronizerParameters(
									Main.SETTINGS);
							
							super.actionPerformed(event);
						}
						
					})));
			
			this.addField(new ConfigurationField(
					"RESET_ALL_LABEL",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString("configuration.toodledo.reset_all"))));
			
			this.addField(new ConfigurationField(
					"RESET_ALL",
					null,
					new ConfigurationFieldType.Button(new ActionSynchronize() {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							try {
								ToodledoConfigurationPanel.this.saveAndApplyConfig();
								
								ContextFactory.getInstance().deleteAll();
								FolderFactory.getInstance().deleteAll();
								GoalFactory.getInstance().deleteAll();
								LocationFactory.getInstance().deleteAll();
								TaskFactory.getInstance().deleteAll();
								
								SynchronizerUtils.getApi().resetSynchronizerParameters(
										Main.SETTINGS);
								
								super.actionPerformed(event);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					})));
		}
	}
	
}
