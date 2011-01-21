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
package com.leclercb.taskunifier.gui.settings;

import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.logger.GuiLogger;

public final class SettingsVersion {
	
	private SettingsVersion() {

	}
	
	public static void updateSettings() {
		String version = Main.SETTINGS.getStringProperty("general.version");
		
		if (version == null)
			version = "0.5.2";
		
		if (version.equals("0.5.2"))
			version = updateSettings_0_5_2_to_0_6();
		
		if (version.equals("0.6"))
			version = "0.6.1";
		
		if (version.equals("0.6.1"))
			version = updateSettings_0_6_1_to_0_6_2();
		
		if (version.equals("0.6.2"))
			version = updateSettings_0_6_2_to_0_6_3();
		
		if (version.equals("0.6.3"))
			version = "0.6.4";
		
		if (version.equals("0.6.4"))
			version = updateSettings_0_6_4_to_0_6_5();
		
		Main.SETTINGS.setStringProperty("general.version", Constants.VERSION);
	}
	
	private static String updateSettings_0_5_2_to_0_6() {
		GuiLogger.getLogger().info("Update settings from version 0.5.2 to 0.6");
		
		Main.SETTINGS.setStringProperty("date.date_format", "dd/MM/yyyy");
		Main.SETTINGS.setStringProperty("date.time_format", "HH:mm");
		
		Main.SETTINGS.setStringProperty(
				"theme.lookandfeel",
				"com.jtattoo.plaf.luna.LunaLookAndFeel$Default");
		
		Main.SETTINGS.remove("date.simple_time_format");
		Main.SETTINGS.remove("date.date_time_format");
		
		return "0.6";
	}
	
	private static String updateSettings_0_6_1_to_0_6_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.1 to 0.6.2");
		
		Main.SETTINGS.setStringProperty("theme.color.searcher_list", "-3090718");
		
		return "0.6.2";
	}
	
	private static String updateSettings_0_6_2_to_0_6_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.2 to 0.6.3");
		
		Main.SETTINGS.setStringProperty(
				"synchronizer.scheduler_enabled",
				"false");
		Main.SETTINGS.setStringProperty(
				"synchronizer.scheduler_sleep_time",
				"600000");
		
		return "0.6.3";
	}
	
	private static String updateSettings_0_6_4_to_0_6_5() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.4 to 0.6.5");
		
		Main.SETTINGS.remove("synchronizer.last_context_edit");
		Main.SETTINGS.remove("synchronizer.last_folder_edit");
		Main.SETTINGS.remove("synchronizer.last_goal_edit");
		Main.SETTINGS.remove("synchronizer.last_location_edit");
		Main.SETTINGS.remove("synchronizer.last_task_edit");
		Main.SETTINGS.remove("synchronizer.last_task_delete");
		
		Main.SETTINGS.remove("toodledo.token");
		Main.SETTINGS.remove("toodledo.token_creation_date");
		Main.SETTINGS.remove("toodledo.userid");
		
		Main.SETTINGS.setStringProperty("searcher.show_completed_tasks", "true");
		Main.SETTINGS.setStringProperty(
				"searcher.show_completed_tasks_at_the_end",
				"false");
		
		Main.SETTINGS.setStringProperty("proxy.use_system_proxy", "false");
		
		return "0.6.5";
	}
	
}
