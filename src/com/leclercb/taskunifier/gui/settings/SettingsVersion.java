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

import java.io.File;

import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;

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
			version = updateSettings_0_6_4_to_0_7_0();
		
		if (version.equals("0.7.0"))
			version = updateSettings_0_7_0_to_0_7_1();
		
		if (version.equals("0.7.1"))
			version = updateSettings_0_7_1_to_0_7_2();
		
		if (version.equals("0.7.2"))
			version = updateSettings_0_7_2_to_0_7_3();
		
		if (version.equals("0.7.3"))
			version = updateSettings_0_7_3_to_0_7_4();
		
		if (version.equals("0.7.4"))
			version = updateSettings_0_7_4_to_0_8_0();
		
		if (version.equals("0.8.0"))
			version = "0.8.1";
		
		if (version.equals("0.8.1"))
			version = updateSettings_0_8_1_to_0_8_2();
		
		if (version.equals("0.8.2"))
			version = updateSettings_0_8_2_to_0_8_3();
		
		if (version.equals("0.8.3"))
			version = updateSettings_0_8_3_to_0_8_4();
		
		if (version.equals("0.8.4"))
			version = updateSettings_0_8_4_to_0_8_5();
		
		if (version.equals("0.8.5"))
			version = updateSettings_0_8_5_to_0_8_6();
		
		if (version.equals("0.8.6"))
			version = updateSettings_0_8_6_to_0_8_7();
		
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
	
	private static String updateSettings_0_6_4_to_0_7_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.4 to 0.7.0");
		
		Main.SETTINGS.remove("task.default.completed");
		Main.SETTINGS.remove("task.default.context");
		Main.SETTINGS.remove("task.default.due_date");
		Main.SETTINGS.remove("task.default.folder");
		Main.SETTINGS.remove("task.default.goal");
		Main.SETTINGS.remove("task.default.location");
		Main.SETTINGS.remove("task.default.length");
		Main.SETTINGS.remove("task.default.note");
		Main.SETTINGS.remove("task.default.priority");
		Main.SETTINGS.remove("task.default.reminder");
		Main.SETTINGS.remove("task.default.repeat");
		Main.SETTINGS.remove("task.default.repeat_from");
		Main.SETTINGS.remove("task.default.star");
		Main.SETTINGS.remove("task.default.start_date");
		Main.SETTINGS.remove("task.default.status");
		Main.SETTINGS.remove("task.default.tags");
		Main.SETTINGS.remove("task.default.title");
		
		Main.SETTINGS.remove("synchronizer.last_context_edit");
		Main.SETTINGS.remove("synchronizer.last_folder_edit");
		Main.SETTINGS.remove("synchronizer.last_goal_edit");
		Main.SETTINGS.remove("synchronizer.last_location_edit");
		Main.SETTINGS.remove("synchronizer.last_task_edit");
		Main.SETTINGS.remove("synchronizer.last_task_delete");
		
		Main.SETTINGS.remove("toodledo.token");
		Main.SETTINGS.remove("toodledo.token_creation_date");
		Main.SETTINGS.remove("toodledo.userid");
		
		if ("KEEP_TOODLEDO".equals(Main.SETTINGS.getStringProperty("synchronizer.choice")))
			Main.SETTINGS.setStringProperty("synchronizer.choice", "KEEP_API");
		
		Main.SETTINGS.setStringProperty("api.id", "1");
		Main.SETTINGS.setStringProperty("api.version", "1.0");
		
		Main.SETTINGS.setStringProperty("review.showed", "false");
		
		Main.SETTINGS.setStringProperty("searcher.show_completed_tasks", "true");
		Main.SETTINGS.setStringProperty(
				"searcher.show_completed_tasks_at_the_end",
				"false");
		
		Main.SETTINGS.setStringProperty("proxy.use_system_proxy", "false");
		
		return "0.7.0";
	}
	
	private static String updateSettings_0_7_0_to_0_7_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.0 to 0.7.1");
		
		if (SystemUtils.IS_OS_MAC)
			Main.SETTINGS.setStringProperty(
					"theme.lookandfeel",
					UIManager.getSystemLookAndFeelClassName());
		
		Main.SETTINGS.remove("theme.color.searcher_list");
		
		return "0.7.1";
	}
	
	private static String updateSettings_0_7_1_to_0_7_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.1 to 0.7.2");
		
		Main.SETTINGS.setStringProperty("new_version.showed", "0.7.2");
		
		Main.SETTINGS.remove("proxy.use_system_proxy");
		Main.SETTINGS.remove("proxy.type");
		
		return "0.7.2";
	}
	
	private static String updateSettings_0_7_2_to_0_7_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.2 to 0.7.3");
		
		if ("MMM dd, yyyy".equals(Main.SETTINGS.getStringProperty("date.date_format")))
			Main.SETTINGS.setStringProperty("date.date_format", "MM/dd/yyyy");
		
		return "0.7.3";
	}
	
	private static String updateSettings_0_7_3_to_0_7_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.3 to 0.7.4");
		
		Main.SETTINGS.setStringProperty("synchronizer.sync_start", "false");
		Main.SETTINGS.setStringProperty("synchronizer.sync_exit", "false");
		
		return "0.7.4";
	}
	
	private static String updateSettings_0_7_4_to_0_8_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.4 to 0.8.0");
		
		Main.SETTINGS.setStringProperty("searcher.show_badges", "false");
		Main.SETTINGS.setStringProperty("task.show_edit_window_on_add", "false");
		
		return "0.8.0";
	}
	
	private static String updateSettings_0_8_1_to_0_8_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.1 to 0.8.2");
		
		Main.SETTINGS.setStringProperty("api.id", "0");
		Main.SETTINGS.setStringProperty("api.version", "1.0");
		// Main.SETTINGS.remove("api.version");
		
		Main.SETTINGS.remove("toodledo.toodledo.token_creation_date");
		Main.SETTINGS.remove("toodledo.toodledo.token");
		Main.SETTINGS.remove("toodledo.toodledo.userid");
		
		return "0.8.2";
	}
	
	private static String updateSettings_0_8_2_to_0_8_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.2 to 0.8.3");
		
		return "0.8.3";
	}
	
	private static String updateSettings_0_8_3_to_0_8_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.3 to 0.8.4");
		
		try {
			String oldPluginsDir = Main.RESOURCES_FOLDER
					+ File.separator
					+ "plugins";
			
			FileUtils.copyDirectory(new File(oldPluginsDir), new File(
					Main.PLUGINS_FOLDER));
		} catch (Throwable t) {}
		
		return "0.8.4";
	}
	
	private static String updateSettings_0_8_4_to_0_8_5() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.4 to 0.8.5");
		
		return "0.8.5";
	}
	
	private static String updateSettings_0_8_5_to_0_8_6() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.5 to 0.8.6");
		
		return "0.8.6";
	}
	
	private static String updateSettings_0_8_6_to_0_8_7() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.6 to 0.8.7");
		
		return "0.8.7";
	}
	
}
