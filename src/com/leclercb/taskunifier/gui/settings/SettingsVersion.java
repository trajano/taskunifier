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
import java.util.logging.Level;

import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.SettingsUtils;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

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
	
	private static String updateSettings_0_8_7_to_0_9_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 0.8.7 to 0.9.0");
		
		ActionResetGeneralSearchers.resetGeneralSearchers();
		
		Main.SETTINGS.setStringProperty("notecolumn.folder.order", "1");
		Main.SETTINGS.setStringProperty("notecolumn.folder.visible", "true");
		Main.SETTINGS.setStringProperty("notecolumn.folder.width", "150");
		Main.SETTINGS.setStringProperty("notecolumn.model.order", "4");
		Main.SETTINGS.setStringProperty("notecolumn.model.visible", "false");
		Main.SETTINGS.setStringProperty("notecolumn.model.width", "150");
		Main.SETTINGS.setStringProperty("notecolumn.note.order", "3");
		Main.SETTINGS.setStringProperty("notecolumn.note.visible", "false");
		Main.SETTINGS.setStringProperty("notecolumn.note.width", "300");
		Main.SETTINGS.setStringProperty("notecolumn.title.order", "2");
		Main.SETTINGS.setStringProperty("notecolumn.title.visible", "true");
		Main.SETTINGS.setStringProperty("notecolumn.title.width", "300");
		Main.SETTINGS.setStringProperty("taskcolumn.model.order", "20");
		Main.SETTINGS.setStringProperty("taskcolumn.model.visible", "false");
		Main.SETTINGS.setStringProperty("taskcolumn.model.width", "150");
		
		Main.SETTINGS.setStringProperty(
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
		
		Main.SETTINGS.setStringProperty("taskcolumn.show_children.order", "3");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.show_children.visible",
				"true");
		Main.SETTINGS.setStringProperty("taskcolumn.show_children.width", "40");
		
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
		
		Main.SETTINGS.setStringProperty("taskcolumn.progress.order", "2");
		Main.SETTINGS.setStringProperty("taskcolumn.progress.visible", "true");
		Main.SETTINGS.setStringProperty("taskcolumn.progress.width", "80");
		
		Main.SETTINGS.setStringProperty("theme.color.progress", "-3355393");
		
		Main.SETTINGS.setStringProperty(
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
		
		Main.SETTINGS.setStringProperty("date.use_due_time", "true");
		Main.SETTINGS.setStringProperty("date.use_start_time", "true");
		
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
		
		Main.SETTINGS.setStringProperty(
				"searcher.category.tag.expanded",
				"true");
		
		Main.SETTINGS.setStringProperty("proxy.use_system_proxies", "false");
		
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
		
		Main.SETTINGS.setStringProperty(
				"searcher.task.default_sorter",
				Main.SETTINGS.getStringProperty("searcher.default_sorter"));
		
		Main.SETTINGS.setStringProperty(
				"searcher.task.selected.value",
				Main.SETTINGS.getStringProperty("searcher.selected.value"));
		
		Main.SETTINGS.setStringProperty(
				"searcher.task.selected.type",
				Main.SETTINGS.getStringProperty("searcher.selected.type"));
		
		Main.SETTINGS.remove("searcher.default_sorter");
		Main.SETTINGS.remove("searcher.selected.value");
		Main.SETTINGS.remove("searcher.selected.type");
		
		try {
			FileUtils.moveFile(new File(Main.DATA_FOLDER
					+ File.separator
					+ "templates.xml"), new File(Main.DATA_FOLDER
					+ File.separator
					+ "task_templates.xml"));
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while moving templates.xml",
					t);
		}
		
		try {
			FileUtils.moveFile(new File(Main.DATA_FOLDER
					+ File.separator
					+ "searchers.xml"), new File(Main.DATA_FOLDER
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
					SynchronizerUtils.resetSynchronizer();
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
		
		Main.SETTINGS.setStringProperty("tips.show_on_startup", "true");
		
		return "1.0.2";
	}
	
	private static String updateSettings_1_0_2_to_1_0_3() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.2 to 1.0.3");
		
		Main.SETTINGS.setStringProperty("notesearcher.show_badges", "true");
		Main.SETTINGS.setStringProperty(
				"notesearcher.category.folder.expanded",
				"true");
		
		Main.SETTINGS.replaceKey(
				"searcher.show_badges",
				"tasksearcher.show_badges");
		Main.SETTINGS.replaceKey(
				"searcher.show_completed_tasks",
				"tasksearcher.show_completed_tasks");
		Main.SETTINGS.replaceKey(
				"searcher.show_completed_tasks_at_the_end",
				"tasksearcher.show_completed_tasks_at_the_end");
		Main.SETTINGS.replaceKey(
				"searcher.category.general.expanded",
				"tasksearcher.category.general.expanded");
		Main.SETTINGS.replaceKey(
				"searcher.category.context.expanded",
				"tasksearcher.category.context.expanded");
		Main.SETTINGS.replaceKey(
				"searcher.category.folder.expanded",
				"tasksearcher.category.folder.expanded");
		Main.SETTINGS.replaceKey(
				"searcher.category.goal.expanded",
				"tasksearcher.category.goal.expanded");
		Main.SETTINGS.replaceKey(
				"searcher.category.location.expanded",
				"tasksearcher.category.location.expanded");
		Main.SETTINGS.replaceKey(
				"searcher.category.tag.expanded",
				"tasksearcher.category.tag.expanded");
		Main.SETTINGS.replaceKey(
				"searcher.category.personal.expanded",
				"tasksearcher.category.personal.expanded");
		
		Main.SETTINGS.setStringProperty(
				"view.notes.window.horizontal_split",
				Main.SETTINGS.getProperty("window.horizontal_split"));
		Main.SETTINGS.setStringProperty(
				"view.notes.window.vertical_split",
				Main.SETTINGS.getProperty("window.vertical_split"));
		
		Main.SETTINGS.setStringProperty(
				"view.tasks.window.horizontal_split",
				Main.SETTINGS.getProperty("window.horizontal_split"));
		Main.SETTINGS.setStringProperty(
				"view.tasks.window.vertical_split",
				Main.SETTINGS.getProperty("window.vertical_split"));
		
		Main.SETTINGS.remove("window.horizontal_split");
		Main.SETTINGS.remove("window.vertical_split");
		
		Main.SETTINGS.setStringProperty("logger.api.level", "INFO");
		Main.SETTINGS.setStringProperty("logger.gui.level", "INFO");
		Main.SETTINGS.setStringProperty("logger.plugin.level", "INFO");
		
		return "1.0.3";
	}
	
	private static String updateSettings_1_0_3_to_1_0_4() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.0.3 to 1.0.4");
		
		Main.SETTINGS.setStringProperty("task.indent_subtasks", "true");
		
		Main.SETTINGS.setStringProperty(
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
		
		Main.SETTINGS.setStringProperty("taskcolumn.timer.order", "19");
		Main.SETTINGS.setStringProperty("taskcolumn.timer.visible", "true");
		Main.SETTINGS.setStringProperty("taskcolumn.timer.width", "50");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.model_creation_date.order",
				"24");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.model_creation_date.visible",
				"false");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.model_creation_date.width",
				"100");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.model_update_date.order",
				"25");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.model_update_date.visible",
				"false");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.model_update_date.width",
				"100");
		
		Main.SETTINGS.setStringProperty("notecolumn.note.order", "3");
		Main.SETTINGS.setStringProperty("notecolumn.note.visible", "true");
		Main.SETTINGS.setStringProperty("notecolumn.note.width", "100");
		
		Main.AFTER_START.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SynchronizerUtils.resetSynchronizer();
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
		
		Main.SETTINGS.setStringProperty("taskcolumn.model_edit.order", "5");
		Main.SETTINGS.setStringProperty("taskcolumn.model_edit.visible", "true");
		Main.SETTINGS.setStringProperty("taskcolumn.model_edit.width", "50");
		
		Main.SETTINGS.setStringProperty(
				"notecolumn.model_creation_date.order",
				"5");
		Main.SETTINGS.setStringProperty(
				"notecolumn.model_creation_date.visible",
				"false");
		Main.SETTINGS.setStringProperty(
				"notecolumn.model_creation_date.width",
				"100");
		Main.SETTINGS.setStringProperty(
				"notecolumn.model_update_date.order",
				"6");
		Main.SETTINGS.setStringProperty(
				"notecolumn.model_update_date.visible",
				"false");
		Main.SETTINGS.setStringProperty(
				"notecolumn.model_update_date.width",
				"100");
		
		return "1.2.2";
	}
	
	private static String updateSettings_1_2_2_to_1_3_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.2.2 to 1.3.0");
		
		Main.SETTINGS.remove("taskcolumn.reminder.order");
		Main.SETTINGS.remove("taskcolumn.reminder.visible");
		Main.SETTINGS.remove("taskcolumn.reminder.width");
		
		Main.SETTINGS.setStringProperty(
				"taskcolumn.start_date_reminder.order",
				"18");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.start_date_reminder.visible",
				"true");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.start_date_reminder.width",
				"100");
		
		Main.SETTINGS.setStringProperty(
				"taskcolumn.due_date_reminder.order",
				"19");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.due_date_reminder.visible",
				"true");
		Main.SETTINGS.setStringProperty(
				"taskcolumn.due_date_reminder.width",
				"100");
		
		Main.SETTINGS.setStringProperty("taskcolumn.order.order", "27");
		Main.SETTINGS.setStringProperty("taskcolumn.order.visible", "false");
		Main.SETTINGS.setStringProperty("taskcolumn.order.width", "50");
		
		return "1.3.0";
	}
	
	private static String updateSettings_1_3_0_to_1_4_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.3.0 to 1.4.0");
		
		Main.SETTINGS.setStringProperty(
				"general.toolbar",
				"ADD_NOTE;ADD_TASK;ADD_SUBTASK;ADD_TEMPLATE_TASK_MENU;DELETE;SEPARATOR;SYNCHRONIZE;SCHEDULED_SYNC;SEPARATOR;CONFIGURATION");
		
		return "1.4.0";
	}
	
	private static String updateSettings_1_4_0_to_1_5_0() {
		GuiLogger.getLogger().info(
				"Update settings from version 1.4.0 to 1.5.0");
		
		Main.SETTINGS.setStringProperty("window.task_edit.height", "700");
		Main.SETTINGS.setStringProperty("window.task_edit.location_x", "0");
		Main.SETTINGS.setStringProperty("window.task_edit.location_y", "0");
		Main.SETTINGS.setStringProperty("window.task_edit.width", "900");
		
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
		
		Main.SETTINGS.setStringProperty("general.communicator.port", "4576");
		
		Main.SETTINGS.setStringProperty("date.show_day_of_week", "false");
		
		Main.SETTINGS.setStringProperty(
				"view.calendar.window.horizontal_split",
				"300");
		
		return "1.7.0";
	}
	
}
