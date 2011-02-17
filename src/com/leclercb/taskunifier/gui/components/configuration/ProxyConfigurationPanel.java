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
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.leclercb.commons.gui.swing.formatters.RegexFormatter;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ProxyConfigurationPanel extends DefaultConfigurationPanel {
	
	public ProxyConfigurationPanel() {
		super("configuration_proxy.html");
		this.initialize();
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"proxy.enabled",
				(Boolean) this.getValue("ENABLED"));
		Main.SETTINGS.setStringProperty(
				"proxy.host",
				(String) this.getValue("HOST"));
		Main.SETTINGS.setStringProperty(
				"proxy.port",
				(String) this.getValue("PORT"));
		Main.SETTINGS.setStringProperty(
				"proxy.login",
				(String) this.getValue("LOGIN"));
		Main.SETTINGS.setStringProperty(
				"proxy.password",
				(String) this.getValue("PASSWORD"));
	}
	
	private void initialize() {
		Boolean proxyEnabledValue = false;
		String proxyHostValue = "";
		String proxyPortValue = "0";
		String proxyLoginValue = "";
		String proxyPasswordValue = "";
		
		if (Main.SETTINGS.getBooleanProperty("proxy.enabled") != null)
			proxyEnabledValue = Main.SETTINGS.getBooleanProperty("proxy.enabled");
		
		if (Main.SETTINGS.getStringProperty("proxy.host") != null)
			proxyHostValue = Main.SETTINGS.getStringProperty("proxy.host");
		
		if (Main.SETTINGS.getIntegerProperty("proxy.port") != null)
			proxyPortValue = Main.SETTINGS.getStringProperty("proxy.port");
		
		if (Main.SETTINGS.getStringProperty("proxy.login") != null)
			proxyLoginValue = Main.SETTINGS.getStringProperty("proxy.login");
		
		if (Main.SETTINGS.getStringProperty("proxy.password") != null)
			proxyPasswordValue = Main.SETTINGS.getStringProperty("proxy.password");
		
		this.addField(new ConfigurationField(
				"ENABLED",
				Translations.getString("configuration.proxy.enabled"),
				new ConfigurationFieldType.CheckBox(proxyEnabledValue)));
		
		final JCheckBox proxyEnabledField = ((ConfigurationFieldType.CheckBox) this.getField(
				"ENABLED").getType()).getFieldComponent();
		
		this.addField(new ConfigurationField(
				"HOST",
				Translations.getString("configuration.proxy.host"),
				new ConfigurationFieldType.TextField(proxyHostValue)));
		
		this.addField(new ConfigurationField(
				"PORT",
				Translations.getString("configuration.proxy.port"),
				new ConfigurationFieldType.FormattedTextField(
						new RegexFormatter("^[0-9]{1,4}$"),
						proxyPortValue)));
		
		this.addField(new ConfigurationField(
				"LOGIN",
				Translations.getString("configuration.proxy.login"),
				new ConfigurationFieldType.TextField(proxyLoginValue)));
		
		this.addField(new ConfigurationField(
				"PASSWORD",
				Translations.getString("configuration.proxy.password"),
				new ConfigurationFieldType.PasswordField(proxyPasswordValue)));
		
		this.setEnabled("HOST", proxyEnabledField.isSelected());
		this.setEnabled("PORT", proxyEnabledField.isSelected());
		this.setEnabled("LOGIN", proxyEnabledField.isSelected());
		this.setEnabled("PASSWORD", proxyEnabledField.isSelected());
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ProxyConfigurationPanel.this.setEnabled(
						"HOST",
						proxyEnabledField.isSelected());
				ProxyConfigurationPanel.this.setEnabled(
						"PORT",
						proxyEnabledField.isSelected());
				ProxyConfigurationPanel.this.setEnabled(
						"LOGIN",
						proxyEnabledField.isSelected());
				ProxyConfigurationPanel.this.setEnabled(
						"PASSWORD",
						proxyEnabledField.isSelected());
			}
			
		};
		
		proxyEnabledField.addActionListener(listener);
	}
}
