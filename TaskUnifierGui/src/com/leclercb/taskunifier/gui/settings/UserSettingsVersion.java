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
package com.leclercb.taskunifier.gui.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.DeprecatedModelId;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelFactory;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.resources.Resources;

public final class UserSettingsVersion {
	
	private UserSettingsVersion() {
		
	}
	
	public static void updateSettings() {
		String version = Main.getUserSettings().getStringProperty(
				"general.user.version");
		
		if (version == null)
			version = "2.9.0";
		
		if (version.equals("2.9.0"))
			version = updateUserSettings_2_9_0_to_3_0_0();
		
		cleanSettings();
		Main.saveUserSettings();
	}
	
	private static void cleanSettings() {
		try {
			Properties defaultProperties = new Properties();
			defaultProperties.load(Resources.class.getResourceAsStream("default_user_settings.properties"));
			
			for (String key : defaultProperties.stringPropertyNames()) {
				String value = defaultProperties.getProperty(key);
				
				if (value == null || value.length() == 0)
					continue;
				
				if (Main.getUserSettings().getStringProperty(key) == null) {
					GuiLogger.getLogger().warning("Clean user settings: " + key);
					Main.getUserSettings().remove(key);
				}
			}
		} catch (Throwable t) {
			
		}
	}
	
	private static String updateUserSettings_2_9_0_to_3_0_0() {
		GuiLogger.getLogger().info(
				"Update user settings from version 2.9.0 to 3.0.0");
		
		Main.AFTER_START.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				for (ModelType type : ModelType.values()) {
					ModelFactory<?, ?, ?, ?> factory = ModelFactoryUtils.getFactory(type);
					for (Object object : factory.getList()) {
						Model model = (Model) object;
						
						if (model.getModelId() instanceof DeprecatedModelId)
							if (!((DeprecatedModelId) model.getModelId()).isNew())
								if (model.getModelReferenceId("toodledo") == null)
									model.addModelReferenceId(
											"toodledo",
											model.getModelId().getId());
					}
				}
			}
			
		});
		
		return "3.0.0";
	}
	
}
