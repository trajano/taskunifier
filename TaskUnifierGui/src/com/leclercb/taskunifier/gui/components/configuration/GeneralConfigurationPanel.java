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

import org.apache.commons.lang3.SystemUtils;

import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.LocaleFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.ShortcutKeyFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class GeneralConfigurationPanel extends DefaultConfigurationPanel {
	
	private boolean languageOnly;
	private boolean welcome;
	
	public GeneralConfigurationPanel(
			ConfigurationGroup configuration,
			boolean languageOnly,
			boolean welcome) {
		super(
				configuration,
				!languageOnly && !welcome,
				languageOnly ? null : "configuration_general");
		this.languageOnly = languageOnly;
		this.welcome = welcome;
		this.initialize();
		this.pack();
	}
	
	private void initialize() {
		this.addField(new ConfigurationField(
				"LANGUAGE",
				Translations.getString("configuration.general.language"),
				true,
				new LocaleFieldType(this.languageOnly)));
		
		if (!this.languageOnly) {
			this.addField(new ConfigurationField(
					"SEPARATOR_1",
					null,
					new ConfigurationFieldType.Separator()));
			
			if (SystemUtils.IS_OS_WINDOWS) {
				this.addField(new ConfigurationField(
						"QUICK_TASK_GLOBAL_HOT_KEY",
						Translations.getString("configuration.general.global_hot_key.quick_task"),
						true,
						new ShortcutKeyFieldType(
								Main.getSettings(),
								"general.global_hot_key.quick_task")));
			}
			
			this.addField(new ConfigurationField(
					"MINIMIZE_TO_SYSTEM_TRAY",
					null,
					true,
					new ConfigurationFieldType.CheckBox(
							Main.getSettings(),
							"window.minimize_to_system_tray",
							Translations.getString("configuration.general.minimize_to_system_tray"),
							true)));
			
			this.addField(new ConfigurationField(
					"SEPARATOR_2",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"POSTPONE_TASK_FROM_CURRENT_DATE",
					Translations.getString("configuration.general.postpone_task"),
					new ConfigurationFieldType.RadioButton(
							Main.getSettings(),
							"task.postpone_from_current_date",
							new String[] {
									Translations.getString("configuration.general.postpone_task.from_current_date"),
									Translations.getString("configuration.general.postpone_task.from_start_due_date") },
							new String[] { "true", "false" })));
			
			this.addField(new ConfigurationField(
					"SHOW_EDIT_WINDOW_ON_ADD",
					null,
					new ConfigurationFieldType.CheckBox(
							Main.getSettings(),
							"task.show_edit_window_on_add",
							Translations.getString("configuration.general.show_edit_window_on_add"),
							false)));
			
			this.addField(new ConfigurationField(
					"SEPARATOR_3",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SHOW_COMPLETED_TASKS_AT_THE_END",
					null,
					new ConfigurationFieldType.CheckBox(
							Main.getSettings(),
							"tasksearcher.show_completed_tasks_at_the_end",
							Translations.getString("configuration.general.show_completed_tasks_at_the_end"),
							false)));
			
			if (!this.welcome) {
				this.addField(new ConfigurationField(
						"RESET_GENERAL_SEARCHERS",
						null,
						new ConfigurationFieldType.Button(
								new ActionResetGeneralSearchers(22, 22))));
			}
		}
	}
	
}
