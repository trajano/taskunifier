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

import java.net.Proxy;
import java.net.Proxy.Type;

import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.RegexFormatter;

public class ProxyConfigurationPanel extends ConfigurationPanel {

	public ProxyConfigurationPanel() {
		super("configuration_proxy.html");
		this.initialize();
		this.pack();
	}

	@Override
	public void saveAndApplyConfig() {
		Settings.setBooleanProperty("proxy.enabled", (Boolean) this.getValue("ENABLED"));
		Settings.setEnumProperty("proxy.type", Proxy.Type.class, (Proxy.Type) this.getValue("TYPE"));
		Settings.setStringProperty("proxy.host", (String) this.getValue("HOST"));
		Settings.setStringProperty("proxy.port", (String) this.getValue("PORT"));
		Settings.setStringProperty("proxy.login", (String) this.getValue("LOGIN"));
		Settings.setStringProperty("proxy.password", (String) this.getValue("PASSWORD"));
	}

	private void initialize() {
		Boolean proxyEnabledValue = false;
		Proxy.Type proxyTypeValue = Proxy.Type.HTTP;
		String proxyHostValue = "";
		String proxyPortValue = "0";
		String proxyLoginValue = "";
		String proxyPasswordValue = "";

		if (Settings.getBooleanProperty("proxy.enabled") != null)
			proxyEnabledValue = Settings.getBooleanProperty("proxy.enabled");

		if (Settings.getEnumProperty("proxy.type", Proxy.Type.class) != null)
			proxyTypeValue = (Type) Settings.getEnumProperty("proxy.type", Proxy.Type.class);

		if (Settings.getStringProperty("proxy.host") != null)
			proxyHostValue = Settings.getStringProperty("proxy.host");

		if (Settings.getIntegerProperty("proxy.port") != null)
			proxyPortValue = Settings.getStringProperty("proxy.port");

		if (Settings.getStringProperty("proxy.login") != null)
			proxyLoginValue = Settings.getStringProperty("proxy.login");

		if (Settings.getStringProperty("proxy.password") != null)
			proxyPasswordValue = Settings.getStringProperty("proxy.password");

		this.addField(new ConfigurationField(
				"ENABLED", 
				Translations.getString("configuration.proxy.enabled"), 
				new ConfigurationFieldType.CheckBox(proxyEnabledValue)));

		this.addField(new ConfigurationField(
				"TYPE", 
				Translations.getString("configuration.proxy.type"), 
				new ConfigurationFieldType.ComboBox(Proxy.Type.values(), proxyTypeValue)));

		this.addField(new ConfigurationField(
				"HOST", 
				Translations.getString("configuration.proxy.host"), 
				new ConfigurationFieldType.TextField(proxyHostValue)));

		this.addField(new ConfigurationField(
				"PORT", 
				Translations.getString("configuration.proxy.port"), 
				new ConfigurationFieldType.FormattedTextField(new RegexFormatter("^[0-9]{1,4}$"), proxyPortValue)));

		this.addField(new ConfigurationField(
				"LOGIN", 
				Translations.getString("configuration.proxy.login"), 
				new ConfigurationFieldType.TextField(proxyLoginValue)));

		this.addField(new ConfigurationField(
				"PASSWORD", 
				Translations.getString("configuration.proxy.password"), 
				new ConfigurationFieldType.PasswordField(proxyPasswordValue)));
	}

}
