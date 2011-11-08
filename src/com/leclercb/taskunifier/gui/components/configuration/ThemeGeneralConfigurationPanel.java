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

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.theme.LookAndFeelFieldType;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ThemeGeneralConfigurationPanel extends DefaultConfigurationPanel {
	
	public ThemeGeneralConfigurationPanel(ConfigurationGroup configuration) {
		super(configuration, "configuration_theme_general");
		
		this.initialize();
		this.pack();
	}
	
	private void initialize() {
		this.addField(new ConfigurationField(
				"LOOK_AND_FEEL",
				Translations.getString("configuration.theme.look_and_feel"),
				new LookAndFeelFieldType()));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"SHOW_BADGES_PERFORMANCE",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.theme.show_badges_performance"))));
		
		this.addField(new ConfigurationField(
				"SHOW_NOTE_BADGES",
				Translations.getString("configuration.theme.show_note_badges"),
				new ConfigurationFieldType.CheckBox("notesearcher.show_badges")));
		
		this.addField(new ConfigurationField(
				"SHOW_TASK_BADGES",
				Translations.getString("configuration.theme.show_task_badges"),
				new ConfigurationFieldType.CheckBox("tasksearcher.show_badges")));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"NOTES_SPLIT",
				Translations.getString("configuration.theme.notes_split"),
				true,
				new ConfigurationFieldType.RadioButton(
						"view.notes.window.split",
						new String[] {
								Translations.getString("configuration.theme.notes_split.vertical"),
								Translations.getString("configuration.theme.notes_split.horizontal") },
						new String[] { "0", "1" })));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_3",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"COLORS_IMPORTANCE_ENABLED",
				Translations.getString("configuration.theme.colors_by_importance_enabled"),
				new ConfigurationFieldType.CheckBox(
						"theme.color.importance.enabled")));
		
		this.addField(new ConfigurationField(
				"COLOR_TASK_PROGRESS",
				Translations.getString("configuration.theme.color_task_progress"),
				new ConfigurationFieldType.ColorChooser("theme.color.progress")));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_4",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"COLORS_ENABLED",
				Translations.getString("configuration.theme.colors_enabled"),
				new ConfigurationFieldType.CheckBox("theme.color.enabled")));
		
		this.addField(new ConfigurationField(
				"COLOR_EVEN",
				Translations.getString("configuration.theme.color_even"),
				new ConfigurationFieldType.ColorChooser("theme.color.even")));
		
		this.addField(new ConfigurationField(
				"COLOR_ODD",
				Translations.getString("configuration.theme.color_odd"),
				new ConfigurationFieldType.ColorChooser("theme.color.odd")));
	}
	
}
