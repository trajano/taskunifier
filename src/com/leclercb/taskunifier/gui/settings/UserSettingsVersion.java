package com.leclercb.taskunifier.gui.settings;

import java.util.Properties;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.resources.Resources;

public final class UserSettingsVersion {
	
	private UserSettingsVersion() {
		
	}
	
	public static void updateSettings() {
		String version = Main.getSettings().getStringProperty("general.version");
		
		if (version == null)
			version = "1.8.7";
		
		if (version.equals("1.8.7"))
			version = updateUserSettings_1_8_7_to_2_0_0();
		
		if (version.equals("2.0.0"))
			version = updateUserSettings_2_0_0_to_2_0_1();
		
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
	
	private static String updateUserSettings_1_8_7_to_2_0_0() {
		GuiLogger.getLogger().info(
				"Update user settings from version 1.8.7 to 2.0.0");
		
		Main.getUserSettings().setStringProperty("general.user.name", "Default");
		
		return "2.0.0";
	}
	
	private static String updateUserSettings_2_0_0_to_2_0_1() {
		GuiLogger.getLogger().info(
				"Update user settings from version 2.0.0 to 2.0.1");
		
		Main.getUserSettings().replaceKey("api.id", "plugin.synchronizer.id");
		
		return "2.0.1";
	}
	
}
