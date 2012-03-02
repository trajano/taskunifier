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
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.resources.Resources;
import com.leclercb.taskunifier.gui.utils.SettingsUtils;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public final class SettingsVersion {
	
	private SettingsVersion() {
		
	}
	
	public static void updateSettings() {
		String version = Main.getSettings().getStringProperty("general.version");
		
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
		
		if (version.equals("0.8.7"))
			version = updateSettings_0_8_7_to_0_9_0();
		
		if (version.equals("0.9.0"))
			version = updateSettings_0_9_0_to_0_9_1();
		
		if (version.equals("0.9.1"))
			version = updateSettings_0_9_1_to_0_9_2();
		
		if (version.equals("0.9.2"))
			version = updateSettings_0_9_2_to_0_9_3();
		
		if (version.equals("0.9.3"))
			version = updateSettings_0_9_3_to_0_9_4();
		
		if (version.equals("0.9.4"))
			version = updateSettings_0_9_4_to_0_9_5();
		
		if (version.equals("0.9.5"))
			version = updateSettings_0_9_5_to_0_9_6();
		
		if (version.equals("0.9.6"))
			version = updateSettings_0_9_6_to_0_9_7();
		
		if (version.equals("0.9.7"))
			version = updateSettings_0_9_7_to_0_9_8();
		
		if (version.equals("0.9.8"))
			version = updateSettings_0_9_8_to_0_9_9();
		
		if (version.equals("0.9.9"))
			version = updateSettings_0_9_9_to_0_9_9b();
		
		if (version.equals("0.9.9b"))
			version = updateSettings_0_9_9b_to_1_0_0();
		
		if (version.equals("1.0.0"))
			version = updateSettings_1_0_0_to_1_0_1();
		
		if (version.equals("1.0.1"))
			version = updateSettings_1_0_1_to_1_0_2();
		
		if (version.equals("1.0.2"))
			version = updateSettings_1_0_2_to_1_0_3();
		
		if (version.equals("1.0.3"))
			version = updateSettings_1_0_3_to_1_0_4();
		
		if (version.equals("1.0.4"))
			version = updateSettings_1_0_4_to_1_0_5();
		
		if (version.equals("1.0.5"))
			version = updateSettings_1_0_5_to_1_0_6();
		
		if (version.equals("1.0.6"))
			version = updateSettings_1_0_6_to_1_0_7();
		
		if (version.equals("1.0.7"))
			version = updateSettings_1_0_7_to_1_1_0();
		
		if (version.equals("1.1.0"))
			version = updateSettings_1_1_0_to_1_2_0();
		
		if (version.equals("1.2.0"))
			version = updateSettings_1_2_0_to_1_2_1();
		
		if (version.equals("1.2.1"))
			version = updateSettings_1_2_1_to_1_2_2();
		
		if (version.equals("1.2.2"))
			version = updateSettings_1_2_2_to_1_3_0();
		
		if (version.equals("1.3.0"))
			version = updateSettings_1_3_0_to_1_4_0();
		
		if (version.equals("1.4.0"))
			version = updateSettings_1_4_0_to_1_5_0();
		
		if (version.equals("1.5.0"))
			version = updateSettings_1_5_0_to_1_5_1();
		
		if (version.equals("1.5.1"))
			version = updateSettings_1_5_1_to_1_6_0();
		
		if (version.equals("1.6.0"))
			version = updateSettings_1_6_0_to_1_7_0();
		
		if (version.equals("1.7.0"))
			version = updateSettings_1_7_0_to_1_7_1();
		
		if (version.equals("1.7.1"))
			version = updateSettings_1_7_1_to_1_8_0();
		
		if (version.equals("1.8.0"))
			version = updateSettings_1_8_0_to_1_8_1();
		
		if (version.equals("1.8.1"))
			version = updateSettings_1_8_1_to_1_8_2();
		
		if (version.equals("1.8.2"))
			version = updateSettings_1_8_2_to_1_8_3();
		
		if (version.equals("1.8.3"))
			version = updateSettings_1_8_3_to_1_8_4();
		
		if (version.equals("1.8.4"))
			version = updateSettings_1_8_4_to_1_8_5();
		
		if (version.equals("1.8.5"))
			version = updateSettings_1_8_5_to_1_8_6();
		
		if (version.equals("1.8.6"))
			version = updateSettings_1_8_6_to_1_8_7();
		
		if (version.equals("1.8.7"))
			version = updateSettings_1_8_7_to_2_0_0();
		
		if (version.equals("2.0.0"))
			version = updateSettings_2_0_0_to_2_0_1();
		
		if (version.equals("2.0.1"))
			version = updateSettings_2_0_1_to_2_1_0();
		
		if (version.equals("2.1.0"))
			version = updateSettings_2_1_0_to_2_1_1();
		
		if (version.equals("2.1.1"))
			version = updateSettings_2_1_1_to_2_2_0();
		
		if (version.equals("2.2.0"))
			version = updateSettings_2_2_0_to_2_3_0();
		
		if (version.equals("2.9.0"))
			version = updateSettings_2_9_0_to_3_0_0();
		
		cleanSettings();
		Main.saveSettings();
	}
	
	private static void cleanSettings() {
		try {
			Properties defaultProperties = new Properties();
			defaultProperties.load(Resources.class.getResourceAsStream("default_settings.properties"));
			
			for (String key : defaultProperties.stringPropertyNames()) {
				String value = defaultProperties.getProperty(key);
				
				if (value == null || value.length() == 0)
					continue;
				
				if (Main.getSettings().getStringProperty(key) == null) {
					GuiLogger.getLogger().warning("Clean settings: " + key);
					Main.getSettings().remove(key);
				}
			}
		} catch (Throwable t) {
			
		}
	}
	
	private static String updateSettings_0_5_2_to_0_6() {
		GuiLogger.getLogger().info("Update settings from version 0.5.2 to 0.6");
		
		Main.getSettings().setStringProperty("date.date_format", "dd/MM/yyyy");
		Main.getSettings().setStringProperty("date.time_format", "HH:mm");
		
		Main.getSettings().setStringProperty(
				"theme.lookandfeel",
				"com.jtattoo.plaf.luna.LunaLookAndFeel$Default");
		
		Main.getSettings().remove("date.simple_time_format");
		Main.getSettings().remove("date.date_time_format");
		
		return "0.6";
	}
	
	private static String updateSettings_0_6_1_to_0_6_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.1 to 0.6.2");
		
		Main.getSettings().setStringProperty(
				"theme.color.searcher_list",
				"-3090718");
		
		return "0.6.2";
	}
	
	private static String updateSettings_0_6_2_to_0_6_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.2 to 0.6.3");
		
		Main.getSettings().setStringProperty(
				"synchronizer.scheduler_enabled",
				"false");
		Main.getSettings().setStringProperty(
				"synchronizer.scheduler_sleep_time",
				"600000");
		
		return "0.6.3";
	}
	
	private static String updateSettings_0_6_4_to_0_7_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.6.4 to 0.7.0");
		
		Main.getSettings().remove("task.default.completed");
		Main.getSettings().remove("task.default.context");
		Main.getSettings().remove("task.default.due_date");
		Main.getSettings().remove("task.default.folder");
		Main.getSettings().remove("task.default.goal");
		Main.getSettings().remove("task.default.location");
		Main.getSettings().remove("task.default.length");
		Main.getSettings().remove("task.default.note");
		Main.getSettings().remove("task.default.priority");
		Main.getSettings().remove("task.default.reminder");
		Main.getSettings().remove("task.default.repeat");
		Main.getSettings().remove("task.default.repeat_from");
		Main.getSettings().remove("task.default.star");
		Main.getSettings().remove("task.default.start_date");
		Main.getSettings().remove("task.default.status");
		Main.getSettings().remove("task.default.tags");
		Main.getSettings().remove("task.default.title");
		
		Main.getSettings().remove("synchronizer.last_context_edit");
		Main.getSettings().remove("synchronizer.last_folder_edit");
		Main.getSettings().remove("synchronizer.last_goal_edit");
		Main.getSettings().remove("synchronizer.last_location_edit");
		Main.getSettings().remove("synchronizer.last_task_edit");
		Main.getSettings().remove("synchronizer.last_task_delete");
		
		Main.getSettings().remove("toodledo.token");
		Main.getSettings().remove("toodledo.token_creation_date");
		Main.getSettings().remove("toodledo.userid");
		
		if ("KEEP_TOODLEDO".equals(Main.getSettings().getStringProperty(
				"synchronizer.choice")))
			Main.getSettings().setStringProperty(
					"synchronizer.choice",
					"KEEP_API");
		
		Main.getSettings().setStringProperty("api.id", "1");
		Main.getSettings().setStringProperty("api.version", "1.0");
		
		Main.getSettings().setStringProperty("review.showed", "false");
		
		Main.getSettings().setStringProperty(
				"searcher.show_completed_tasks",
				"true");
		Main.getSettings().setStringProperty(
				"searcher.show_completed_tasks_at_the_end",
				"false");
		
		Main.getSettings().setStringProperty("proxy.use_system_proxy", "false");
		
		return "0.7.0";
	}
	
	private static String updateSettings_0_7_0_to_0_7_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.0 to 0.7.1");
		
		if (SystemUtils.IS_OS_MAC)
			Main.getSettings().setStringProperty(
					"theme.lookandfeel",
					UIManager.getSystemLookAndFeelClassName());
		
		Main.getSettings().remove("theme.color.searcher_list");
		
		return "0.7.1";
	}
	
	private static String updateSettings_0_7_1_to_0_7_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.1 to 0.7.2");
		
		Main.getSettings().setStringProperty("new_version.showed", "0.7.2");
		
		Main.getSettings().remove("proxy.use_system_proxy");
		Main.getSettings().remove("proxy.type");
		
		return "0.7.2";
	}
	
	private static String updateSettings_0_7_2_to_0_7_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.2 to 0.7.3");
		
		if ("MMM dd, yyyy".equals(Main.getSettings().getStringProperty(
				"date.date_format")))
			Main.getSettings().setStringProperty(
					"date.date_format",
					"MM/dd/yyyy");
		
		return "0.7.3";
	}
	
	private static String updateSettings_0_7_3_to_0_7_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.3 to 0.7.4");
		
		Main.getSettings().setStringProperty("synchronizer.sync_start", "false");
		Main.getSettings().setStringProperty("synchronizer.sync_exit", "false");
		
		return "0.7.4";
	}
	
	private static String updateSettings_0_7_4_to_0_8_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.7.4 to 0.8.0");
		
		Main.getSettings().setStringProperty("searcher.show_badges", "false");
		Main.getSettings().setStringProperty(
				"task.show_edit_window_on_add",
				"false");
		
		return "0.8.0";
	}
	
	private static String updateSettings_0_8_1_to_0_8_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.1 to 0.8.2");
		
		Main.getSettings().setStringProperty("api.id", "0");
		Main.getSettings().setStringProperty("api.version", "1.0");
		// Main.SETTINGS.remove("api.version");
		
		Main.getSettings().remove("toodledo.toodledo.token_creation_date");
		Main.getSettings().remove("toodledo.toodledo.token");
		Main.getSettings().remove("toodledo.toodledo.userid");
		
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
			String oldPluginsDir = Main.getResourcesFolder()
					+ File.separator
					+ "plugins";
			
			FileUtils.copyDirectory(
					new File(oldPluginsDir),
					new File(Main.getPluginsFolder()));
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
	
	private static String updateSettings_0_8_7_to_0_9_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.7 to 0.9.0");
		
		ActionResetGeneralSearchers.resetGeneralSearchers();
		
		Main.getSettings().setStringProperty("notecolumn.folder.order", "1");
		Main.getSettings().setStringProperty(
				"notecolumn.folder.visible",
				"true");
		Main.getSettings().setStringProperty("notecolumn.folder.width", "150");
		Main.getSettings().setStringProperty("notecolumn.model.order", "4");
		Main.getSettings().setStringProperty(
				"notecolumn.model.visible",
				"false");
		Main.getSettings().setStringProperty("notecolumn.model.width", "150");
		Main.getSettings().setStringProperty("notecolumn.note.order", "3");
		Main.getSettings().setStringProperty("notecolumn.note.visible", "false");
		Main.getSettings().setStringProperty("notecolumn.note.width", "300");
		Main.getSettings().setStringProperty("notecolumn.title.order", "2");
		Main.getSettings().setStringProperty("notecolumn.title.visible", "true");
		Main.getSettings().setStringProperty("notecolumn.title.width", "300");
		Main.getSettings().setStringProperty("taskcolumn.model.order", "20");
		Main.getSettings().setStringProperty(
				"taskcolumn.model.visible",
				"false");
		Main.getSettings().setStringProperty("taskcolumn.model.width", "150");
		
		Main.getSettings().setStringProperty(
				"theme.color.importance.enabled",
				"true");
		
		return "0.9.0";
	}
	
	private static String updateSettings_0_9_0_to_0_9_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.0 to 0.9.1");
		
		return "0.9.1";
	}
	
	private static String updateSettings_0_9_1_to_0_9_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.1 to 0.9.2");
		
		return "0.9.2";
	}
	
	private static String updateSettings_0_9_2_to_0_9_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.2 to 0.9.3");
		
		return "0.9.3";
	}
	
	private static String updateSettings_0_9_3_to_0_9_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.3 to 0.9.4");
		
		Main.getSettings().setStringProperty(
				"taskcolumn.show_children.order",
				"3");
		Main.getSettings().setStringProperty(
				"taskcolumn.show_children.visible",
				"true");
		Main.getSettings().setStringProperty(
				"taskcolumn.show_children.width",
				"40");
		
		return "0.9.4";
	}
	
	private static String updateSettings_0_9_4_to_0_9_5() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.4 to 0.9.5");
		
		SettingsUtils.resetImportanceColors();
		SettingsUtils.resetPriorityColors();
		
		Main.AFTER_START.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsUtils.removeCompletedCondition();
			}
			
		});
		
		Main.getSettings().setStringProperty("taskcolumn.progress.order", "2");
		Main.getSettings().setStringProperty(
				"taskcolumn.progress.visible",
				"true");
		Main.getSettings().setStringProperty("taskcolumn.progress.width", "80");
		
		Main.getSettings().setStringProperty("theme.color.progress", "-3355393");
		
		Main.getSettings().setStringProperty(
				"window.minimize_to_system_tray",
				"false");
		
		return "0.9.5";
	}
	
	private static String updateSettings_0_9_5_to_0_9_6() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.5 to 0.9.6");
		
		return "0.9.6";
	}
	
	private static String updateSettings_0_9_6_to_0_9_7() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.6 to 0.9.7");
		
		Main.getSettings().setStringProperty("date.use_due_time", "true");
		Main.getSettings().setStringProperty("date.use_start_time", "true");
		
		return "0.9.7";
	}
	
	private static String updateSettings_0_9_7_to_0_9_8() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.7 to 0.9.8");
		
		return "0.9.8";
	}
	
	private static String updateSettings_0_9_8_to_0_9_9() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.8 to 0.9.9");
		
		Main.getSettings().setStringProperty(
				"searcher.category.tag.expanded",
				"true");
		
		Main.getSettings().setStringProperty(
				"proxy.use_system_proxies",
				"false");
		
		return "0.9.9";
	}
	
	private static String updateSettings_0_9_9_to_0_9_9b() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.9 to 0.9.9b");
		
		return "0.9.9b";
	}
	
	private static String updateSettings_0_9_9b_to_1_0_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.9.9b to 1.0.0");
		
		Main.getSettings().setStringProperty(
				"searcher.task.default_sorter",
				Main.getSettings().getStringProperty("searcher.default_sorter"));
		
		Main.getSettings().setStringProperty(
				"searcher.task.selected.value",
				Main.getSettings().getStringProperty("searcher.selected.value"));
		
		Main.getSettings().setStringProperty(
				"searcher.task.selected.type",
				Main.getSettings().getStringProperty("searcher.selected.type"));
		
		Main.getSettings().remove("searcher.default_sorter");
		Main.getSettings().remove("searcher.selected.value");
		Main.getSettings().remove("searcher.selected.type");
		
		try {
			FileUtils.moveFile(new File(Main.getDataFolder()
					+ File.separator
					+ "templates.xml"), new File(Main.getDataFolder()
					+ File.separator
					+ "task_templates.xml"));
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while moving templates.xml",
					t);
		}
		
		try {
			FileUtils.moveFile(new File(Main.getDataFolder()
					+ File.separator
					+ "searchers.xml"), new File(Main.getDataFolder()
					+ File.separator
					+ "task_searchers.xml"));
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while moving searchers.xml",
					t);
		}
		
		Main.AFTER_START.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SynchronizerUtils.resetAllSynchronizers();
				} catch (Throwable t) {
					
				}
			}
			
		});
		
		return "1.0.0";
	}
	
	private static String updateSettings_1_0_0_to_1_0_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.0 to 1.0.1");
		
		return "1.0.1";
	}
	
	private static String updateSettings_1_0_1_to_1_0_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.1 to 1.0.2");
		
		Main.getSettings().setStringProperty("tips.show_on_startup", "true");
		
		return "1.0.2";
	}
	
	private static String updateSettings_1_0_2_to_1_0_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.2 to 1.0.3");
		
		Main.getSettings().setStringProperty("notesearcher.show_badges", "true");
		Main.getSettings().setStringProperty(
				"notesearcher.category.folder.expanded",
				"true");
		
		Main.getSettings().replaceKey(
				"searcher.show_badges",
				"tasksearcher.show_badges");
		Main.getSettings().replaceKey(
				"searcher.show_completed_tasks",
				"tasksearcher.show_completed_tasks");
		Main.getSettings().replaceKey(
				"searcher.show_completed_tasks_at_the_end",
				"tasksearcher.show_completed_tasks_at_the_end");
		Main.getSettings().replaceKey(
				"searcher.category.general.expanded",
				"tasksearcher.category.general.expanded");
		Main.getSettings().replaceKey(
				"searcher.category.context.expanded",
				"tasksearcher.category.context.expanded");
		Main.getSettings().replaceKey(
				"searcher.category.folder.expanded",
				"tasksearcher.category.folder.expanded");
		Main.getSettings().replaceKey(
				"searcher.category.goal.expanded",
				"tasksearcher.category.goal.expanded");
		Main.getSettings().replaceKey(
				"searcher.category.location.expanded",
				"tasksearcher.category.location.expanded");
		Main.getSettings().replaceKey(
				"searcher.category.tag.expanded",
				"tasksearcher.category.tag.expanded");
		Main.getSettings().replaceKey(
				"searcher.category.personal.expanded",
				"tasksearcher.category.personal.expanded");
		
		Main.getSettings().setStringProperty(
				"view.notes.window.horizontal_split",
				Main.getSettings().getProperty("window.horizontal_split"));
		Main.getSettings().setStringProperty(
				"view.notes.window.vertical_split",
				Main.getSettings().getProperty("window.vertical_split"));
		
		Main.getSettings().setStringProperty(
				"view.tasks.window.horizontal_split",
				Main.getSettings().getProperty("window.horizontal_split"));
		Main.getSettings().setStringProperty(
				"view.tasks.window.vertical_split",
				Main.getSettings().getProperty("window.vertical_split"));
		
		Main.getSettings().remove("window.horizontal_split");
		Main.getSettings().remove("window.vertical_split");
		
		Main.getSettings().setStringProperty("logger.api.level", "INFO");
		Main.getSettings().setStringProperty("logger.gui.level", "INFO");
		Main.getSettings().setStringProperty("logger.plugin.level", "INFO");
		
		return "1.0.3";
	}
	
	private static String updateSettings_1_0_3_to_1_0_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.3 to 1.0.4");
		
		Main.getSettings().setStringProperty("task.indent_subtasks", "true");
		
		Main.getSettings().setStringProperty(
				"task.postpone_from_current_date",
				"false");
		
		return "1.0.4";
	}
	
	private static String updateSettings_1_0_4_to_1_0_5() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.4 to 1.0.5");
		
		return "1.0.5";
	}
	
	private static String updateSettings_1_0_5_to_1_0_6() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.5 to 1.0.6");
		
		return "1.0.6";
	}
	
	private static String updateSettings_1_0_6_to_1_0_7() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.6 to 1.0.7");
		
		return "1.0.7";
	}
	
	private static String updateSettings_1_0_7_to_1_1_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.7 to 1.1.0");
		
		return "1.1.0";
	}
	
	private static String updateSettings_1_1_0_to_1_2_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.1.0 to 1.2.0");
		
		Main.getSettings().setStringProperty("taskcolumn.timer.order", "19");
		Main.getSettings().setStringProperty("taskcolumn.timer.visible", "true");
		Main.getSettings().setStringProperty("taskcolumn.timer.width", "50");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_creation_date.order",
				"24");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_creation_date.visible",
				"false");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_creation_date.width",
				"100");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_update_date.order",
				"25");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_update_date.visible",
				"false");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_update_date.width",
				"100");
		
		Main.getSettings().setStringProperty("notecolumn.note.order", "3");
		Main.getSettings().setStringProperty("notecolumn.note.visible", "true");
		Main.getSettings().setStringProperty("notecolumn.note.width", "100");
		
		Main.AFTER_START.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SynchronizerUtils.resetAllSynchronizers();
				} catch (Throwable t) {
					
				}
			}
			
		});
		
		return "1.2.0";
	}
	
	private static String updateSettings_1_2_0_to_1_2_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.2.0 to 1.2.1");
		
		return "1.2.1";
	}
	
	private static String updateSettings_1_2_1_to_1_2_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.2.1 to 1.2.2");
		
		Main.getSettings().setStringProperty("taskcolumn.model_edit.order", "5");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_edit.visible",
				"true");
		Main.getSettings().setStringProperty(
				"taskcolumn.model_edit.width",
				"50");
		
		Main.getSettings().setStringProperty(
				"notecolumn.model_creation_date.order",
				"5");
		Main.getSettings().setStringProperty(
				"notecolumn.model_creation_date.visible",
				"false");
		Main.getSettings().setStringProperty(
				"notecolumn.model_creation_date.width",
				"100");
		Main.getSettings().setStringProperty(
				"notecolumn.model_update_date.order",
				"6");
		Main.getSettings().setStringProperty(
				"notecolumn.model_update_date.visible",
				"false");
		Main.getSettings().setStringProperty(
				"notecolumn.model_update_date.width",
				"100");
		
		return "1.2.2";
	}
	
	private static String updateSettings_1_2_2_to_1_3_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.2.2 to 1.3.0");
		
		Main.getSettings().remove("taskcolumn.reminder.order");
		Main.getSettings().remove("taskcolumn.reminder.visible");
		Main.getSettings().remove("taskcolumn.reminder.width");
		
		Main.getSettings().setStringProperty(
				"taskcolumn.start_date_reminder.order",
				"18");
		Main.getSettings().setStringProperty(
				"taskcolumn.start_date_reminder.visible",
				"true");
		Main.getSettings().setStringProperty(
				"taskcolumn.start_date_reminder.width",
				"100");
		
		Main.getSettings().setStringProperty(
				"taskcolumn.due_date_reminder.order",
				"19");
		Main.getSettings().setStringProperty(
				"taskcolumn.due_date_reminder.visible",
				"true");
		Main.getSettings().setStringProperty(
				"taskcolumn.due_date_reminder.width",
				"100");
		
		Main.getSettings().setStringProperty("taskcolumn.order.order", "27");
		Main.getSettings().setStringProperty(
				"taskcolumn.order.visible",
				"false");
		Main.getSettings().setStringProperty("taskcolumn.order.width", "50");
		
		return "1.3.0";
	}
	
	private static String updateSettings_1_3_0_to_1_4_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.3.0 to 1.4.0");
		
		Main.getSettings().setStringProperty(
				"general.toolbar",
				"ADD_NOTE;ADD_TASK;ADD_SUBTASK;ADD_TEMPLATE_TASK_MENU;DELETE;SEPARATOR;SYNCHRONIZE;SCHEDULED_SYNC;SEPARATOR;CONFIGURATION");
		
		return "1.4.0";
	}
	
	private static String updateSettings_1_4_0_to_1_5_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.4.0 to 1.5.0");
		
		Main.getSettings().setStringProperty("window.task_edit.height", "700");
		Main.getSettings().setStringProperty("window.task_edit.location_x", "0");
		Main.getSettings().setStringProperty("window.task_edit.location_y", "0");
		Main.getSettings().setStringProperty("window.task_edit.width", "900");
		
		return "1.5.0";
	}
	
	private static String updateSettings_1_5_0_to_1_5_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.5.0 to 1.5.1");
		
		return "1.5.1";
	}
	
	private static String updateSettings_1_5_1_to_1_6_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.5.1 to 1.6.0");
		
		return "1.6.0";
	}
	
	private static String updateSettings_1_6_0_to_1_7_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.6.0 to 1.7.0");
		
		Main.getSettings().setStringProperty(
				"general.communicator.port",
				"4576");
		
		Main.getSettings().setStringProperty("date.show_day_of_week", "false");
		
		Main.getSettings().setStringProperty(
				"view.calendar.window.horizontal_split",
				"300");
		
		Main.getSettings().replaceKey(
				"searcher.default_sorter",
				"tasksearcher.default_sorter");
		
		Main.getSettings().setStringProperty(
				"tasksearcher.calendar.category.general.expanded",
				"true");
		Main.getSettings().setStringProperty(
				"tasksearcher.calendar.category.context.expanded",
				"false");
		Main.getSettings().setStringProperty(
				"tasksearcher.calendar.category.folder.expanded",
				"false");
		Main.getSettings().setStringProperty(
				"tasksearcher.calendar.category.goal.expanded",
				"false");
		Main.getSettings().setStringProperty(
				"tasksearcher.calendar.category.location.expanded",
				"false");
		Main.getSettings().setStringProperty(
				"tasksearcher.calendar.category.tag.expanded",
				"false");
		Main.getSettings().setStringProperty(
				"tasksearcher.calendar.category.personal.expanded",
				"true");
		
		Main.getSettings().replaceKey(
				"notesearcher.category.folder.expanded",
				"notesearcher.notes.category.folder.expanded");
		Main.getSettings().replaceKey(
				"tasksearcher.category.general.expanded",
				"tasksearcher.tasks.category.general.expanded");
		Main.getSettings().replaceKey(
				"tasksearcher.category.context.expanded",
				"tasksearcher.tasks.category.context.expanded");
		Main.getSettings().replaceKey(
				"tasksearcher.category.folder.expanded",
				"tasksearcher.tasks.category.folder.expanded");
		Main.getSettings().replaceKey(
				"tasksearcher.category.goal.expanded",
				"tasksearcher.tasks.category.goal.expanded");
		Main.getSettings().replaceKey(
				"tasksearcher.category.location.expanded",
				"tasksearcher.tasks.category.location.expanded");
		Main.getSettings().replaceKey(
				"tasksearcher.category.tag.expanded",
				"tasksearcher.tasks.category.tag.expanded");
		Main.getSettings().replaceKey(
				"tasksearcher.category.personal.expanded",
				"tasksearcher.tasks.category.personal.expanded");
		
		return "1.7.0";
	}
	
	private static String updateSettings_1_7_0_to_1_7_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.7.0 to 1.7.1");
		
		return "1.7.1";
	}
	
	private static String updateSettings_1_7_1_to_1_8_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.7.1 to 1.8.0");
		
		Main.getSettings().setStringProperty("view.notes.window.split", "0");
		Main.getSettings().setStringProperty(
				"general.backup.auto_backup_every",
				"2");
		Main.getSettings().setStringProperty(
				"general.backup.backup_before_sync",
				"false");
		Main.getSettings().setStringProperty(
				"general.backup.keep_backups",
				"100");
		Main.getSettings().setStringProperty(
				"general.communicator.enabled",
				"true");
		Main.getSettings().setStringProperty("view.calendar.zoom", "80");
		
		return "1.8.0";
	}
	
	private static String updateSettings_1_8_0_to_1_8_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.0 to 1.8.1");
		
		return "1.8.1";
	}
	
	private static String updateSettings_1_8_1_to_1_8_2() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.1 to 1.8.2");
		
		Main.getSettings().setStringProperty("general.growl.enabled", "true");
		
		return "1.8.2";
	}
	
	private static String updateSettings_1_8_2_to_1_8_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.2 to 1.8.3");
		
		return "1.8.3";
	}
	
	private static String updateSettings_1_8_3_to_1_8_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.3 to 1.8.4");
		
		return "1.8.4";
	}
	
	private static String updateSettings_1_8_4_to_1_8_5() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.4 to 1.8.5");
		
		return "1.8.5";
	}
	
	private static String updateSettings_1_8_5_to_1_8_6() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.5 to 1.8.6");
		
		Main.getSettings().setStringProperty("taskcolumn.contacts.order", "30");
		Main.getSettings().setStringProperty(
				"taskcolumn.contacts.visible",
				"true");
		Main.getSettings().setStringProperty("taskcolumn.contacts.width", "50");
		
		Main.getSettings().setStringProperty(
				"taskcontactscolumn.link.order",
				"1");
		Main.getSettings().setStringProperty(
				"taskcontactscolumn.link.visible",
				"true");
		Main.getSettings().setStringProperty(
				"taskcontactscolumn.link.width",
				"200");
		Main.getSettings().setStringProperty(
				"taskcontactscolumn.contact.order",
				"2");
		Main.getSettings().setStringProperty(
				"taskcontactscolumn.contact.visible",
				"true");
		Main.getSettings().setStringProperty(
				"taskcontactscolumn.contact.width",
				"200");
		
		return "1.8.6";
	}
	
	private static String updateSettings_1_8_6_to_1_8_7() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.6 to 1.8.7");
		
		return "1.8.7";
	}
	
	private static String updateSettings_1_8_7_to_2_0_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.8.7 to 2.0.0");
		
		Main.getSettings().setStringProperty(
				"general.communicator.enabled",
				"true");
		
		Main.getSettings().setStringProperty("date.day_start_hour", "8");
		Main.getSettings().setStringProperty("date.day_break_hour", "12");
		Main.getSettings().setStringProperty("date.day_end_hour", "17");
		
		copyToUserFolder("contacts.xml");
		copyToUserFolder("contexts.xml");
		copyToUserFolder("folders.xml");
		copyToUserFolder("goals.xml");
		copyToUserFolder("locations.xml");
		copyToUserFolder("notes.xml");
		copyToUserFolder("settings.properties");
		copyToUserFolder("task_searchers.xml");
		copyToUserFolder("task_templates.xml");
		copyToUserFolder("tasks.xml");
		
		try {
			FileUtils.copyDirectory(new File(Main.getDataFolder()
					+ File.separator
					+ "backup"), new File(Main.getUserFolder()
					+ File.separator
					+ "backup"));
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while copying backup folder",
					t);
		}
		
		return "2.0.0";
	}
	
	private static void copyToUserFolder(String fileName) {
		try {
			FileUtils.copyFile(new File(Main.getDataFolder()
					+ File.separator
					+ fileName), new File(Main.getUserFolder()
					+ File.separator
					+ fileName));
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while copying " + fileName,
					t);
		}
	}
	
	private static String updateSettings_2_0_0_to_2_0_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 2.0.0 to 2.0.1");
		
		return "2.0.1";
	}
	
	private static String updateSettings_2_0_1_to_2_1_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 2.0.1 to 2.1.0");
		
		return "2.1.0";
	}
	
	private static String updateSettings_2_1_0_to_2_1_1() {
		GuiLogger.getLogger().info(
				"Update settings from version 2.1.0 to 2.1.1");
		
		return "2.1.1";
	}
	
	private static String updateSettings_2_1_1_to_2_2_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 2.1.1 to 2.2.0");
		
		Main.getSettings().setStringProperty("taskcolumn.files.order", "31");
		Main.getSettings().setStringProperty("taskcolumn.files.visible", "true");
		Main.getSettings().setStringProperty("taskcolumn.files.width", "50");
		Main.getSettings().setStringProperty("taskcolumn.tasks.order", "32");
		Main.getSettings().setStringProperty("taskcolumn.tasks.visible", "true");
		Main.getSettings().setStringProperty("taskcolumn.tasks.width", "50");
		
		return "2.2.0";
	}
	
	private static String updateSettings_2_2_0_to_2_3_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 2.2.0 to 2.3.0");
		
		return "2.3.0";
	}
	
	private static String updateSettings_2_9_0_to_3_0_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 2.9.0 to 3.0.0");
		
		return "3.0.0";
	}
	
}
