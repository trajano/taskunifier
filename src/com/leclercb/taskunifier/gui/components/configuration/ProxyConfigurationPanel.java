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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.leclercb.commons.gui.swing.formatters.RegexFormatter;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ProxyConfigurationPanel extends DefaultConfigurationPanel {
	
	public ProxyConfigurationPanel() {
		super(Help.getHelpFile("configuration_proxy.html"));
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
		this.addField(new ConfigurationField(
				"ENABLED",
				Translations.getString("configuration.proxy.enabled"),
				new ConfigurationFieldType.CheckBox(
						Main.SETTINGS,
						"proxy.enabled") {
					
					@Override
					public Boolean getPropertyValue() {
						if (Main.SETTINGS.getBooleanProperty("proxy.enabled") != null)
							return Main.SETTINGS.getBooleanProperty("proxy.enabled");
						else
							return false;
					}
					
				}));
		
		final JCheckBox proxyEnabledField = ((ConfigurationFieldType.CheckBox) this.getField(
				"ENABLED").getType()).getFieldComponent();
		
		this.addField(new ConfigurationField(
				"HOST",
				Translations.getString("configuration.proxy.host"),
				new ConfigurationFieldType.TextField(
						Main.SETTINGS,
						"proxy.host") {
					
					@Override
					public String getPropertyValue() {
						if (Main.SETTINGS.getStringProperty("proxy.host") != null)
							return Main.SETTINGS.getStringProperty("proxy.host");
						else
							return "";
					}
					
				}));
		
		this.addField(new ConfigurationField(
				"PORT",
				Translations.getString("configuration.proxy.port"),
				new ConfigurationFieldType.FormattedTextField(
						new RegexFormatter("^[0-9]{1,4}$"),
						Main.SETTINGS,
						"proxy.port") {
					
					@Override
					public String getPropertyValue() {
						if (Main.SETTINGS.getStringProperty("proxy.port") != null)
							return Main.SETTINGS.getStringProperty("proxy.port");
						else
							return "0";
					}
					
				}));
		
		this.addField(new ConfigurationField(
				"LOGIN",
				Translations.getString("configuration.proxy.login"),
				new ConfigurationFieldType.TextField(
						Main.SETTINGS,
						"proxy.login") {
					
					@Override
					public String getPropertyValue() {
						if (Main.SETTINGS.getStringProperty("proxy.login") != null)
							return Main.SETTINGS.getStringProperty("proxy.login");
						else
							return "";
					}
					
				}));
		
		this.addField(new ConfigurationField(
				"PASSWORD",
				Translations.getString("configuration.proxy.password"),
				new ConfigurationFieldType.PasswordField(
						Main.SETTINGS,
						"proxy.password") {
					
					@Override
					public String getPropertyValue() {
						if (Main.SETTINGS.getStringProperty("proxy.password") != null)
							return Main.SETTINGS.getStringProperty("proxy.password");
						else
							return "";
					}
					
				}));
		
		this.disableFields();
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ProxyConfigurationPanel.this.disableFields();
			}
			
		};
		
		proxyEnabledField.addActionListener(listener);
	}
	
	private void disableFields() {
		JCheckBox proxyEnabledField = ((ConfigurationFieldType.CheckBox) this.getField(
				"ENABLED").getType()).getFieldComponent();
		
		this.setEnabled("HOST", proxyEnabledField.isSelected());
		this.setEnabled("PORT", proxyEnabledField.isSelected());
		this.setEnabled("LOGIN", proxyEnabledField.isSelected());
		this.setEnabled("PASSWORD", proxyEnabledField.isSelected());
	}
	
}
