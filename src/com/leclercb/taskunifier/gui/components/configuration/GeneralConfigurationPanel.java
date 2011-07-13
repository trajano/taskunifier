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

import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.DateFormatFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.LocaleFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.MinimizeToSystemTrayFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.ShowCompletedTasksAtTheEndFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.ShowWindowEditOnAddFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.TimeFormatFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.UseDueTimeFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.general.UseStartTimeFieldType;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.translations.Translations;

public class GeneralConfigurationPanel extends DefaultConfigurationPanel {
	
	private boolean languageOnly;
	private boolean welcome;
	
	public GeneralConfigurationPanel(boolean languageOnly, boolean welcome) {
		super(
				!languageOnly && !welcome,
				languageOnly ? null : Help.getHelpFile("configuration_general.html"));
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
					new ConfigurationFieldTypeExt.Separator()));
			
			this.addField(new ConfigurationField(
					"DATE_FORMAT",
					Translations.getString("configuration.general.date_format"),
					true,
					new DateFormatFieldType()));
			
			this.addField(new ConfigurationField(
					"TIME_FORMAT",
					Translations.getString("configuration.general.time_format"),
					true,
					new TimeFormatFieldType()));
			
			this.addField(new ConfigurationField(
					"USE_DUE_TIME",
					Translations.getString("configuration.general.use_due_time"),
					true,
					new UseDueTimeFieldType()));
			
			this.addField(new ConfigurationField(
					"USE_START_TIME",
					Translations.getString("configuration.general.use_start_time"),
					true,
					new UseStartTimeFieldType()));
			
			this.addField(new ConfigurationField(
					"SEPARATOR_2",
					null,
					new ConfigurationFieldTypeExt.Separator()));
			
			this.addField(new ConfigurationField(
					"MINIMIZE_TO_SYSTEM_TRAY",
					Translations.getString("configuration.general.minimize_to_system_tray"),
					true,
					new MinimizeToSystemTrayFieldType()));
			
			this.addField(new ConfigurationField(
					"SEPARATOR_3",
					null,
					new ConfigurationFieldTypeExt.Separator()));
			
			this.addField(new ConfigurationField(
					"SHOW_EDIT_WINDOW_ON_ADD",
					Translations.getString("configuration.general.show_edit_window_on_add"),
					new ShowWindowEditOnAddFieldType()));
			
			this.addField(new ConfigurationField(
					"SEPARATOR_4",
					null,
					new ConfigurationFieldTypeExt.Separator()));
			
			this.addField(new ConfigurationField(
					"SHOW_COMPLETED_TASKS_AT_THE_END",
					Translations.getString("configuration.general.show_completed_tasks_at_the_end"),
					new ShowCompletedTasksAtTheEndFieldType()));
			
			if (!this.welcome) {
				this.addField(new ConfigurationField(
						"RESET_GENERAL_SEARCHERS",
						null,
						new ConfigurationFieldTypeExt.Button(
								new ActionResetGeneralSearchers(22, 22))));
			}
		}
	}
	
}
