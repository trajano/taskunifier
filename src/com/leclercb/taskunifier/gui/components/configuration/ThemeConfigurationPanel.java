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

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.exc.LookAndFeelException;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.theme.ColorEvenFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.theme.ColorOddFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.theme.ColorsByImportanceEnabledFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.theme.ColorsEnabledFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.theme.LookAndFeelFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.theme.ShowBadgesFieldType;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ThemeConfigurationPanel extends DefaultConfigurationPanel {
	
	private Window[] windows;
	
	public ThemeConfigurationPanel(Window[] windows) {
		this.windows = windows;
		this.initialize();
		this.pack();
	}
	
	private void initialize() {
		this.addField(new ConfigurationField(
				"LOOK_AND_FEEL",
				Translations.getString("configuration.theme.look_and_feel"),
				new LookAndFeelFieldType()));
		
		this.addField(new ConfigurationField(
				"APPLY_THEME",
				null,
				new ConfigurationFieldTypeExt.Button(
						Translations.getString("general.apply"),
						new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								ThemeConfigurationPanel.this.applyTheme();
							}
							
						})));
		
		this.addField(new ConfigurationField(
				"LOOK_AND_FEEL_PREVIEW",
				null,
				new ConfigurationFieldTypeExt.Label(
						Translations.getString("configuration.theme.look_and_feel_preview"))));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldTypeExt.Separator()));
		
		this.addField(new ConfigurationField(
				"SETTINGS_AFTER_RESTART",
				null,
				new ConfigurationFieldTypeExt.Label(
						Translations.getString("configuration.general.settings_changed_after_restart"))));
		
		this.addField(new ConfigurationField(
				"SHOW_BADGES_PERFORMANCE",
				null,
				new ConfigurationFieldTypeExt.Label(
						Translations.getString("configuration.theme.show_badges_performance"))));
		
		this.addField(new ConfigurationField(
				"SHOW_BADGES",
				Translations.getString("configuration.theme.show_badges"),
				new ShowBadgesFieldType()));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldTypeExt.Separator()));
		
		this.addField(new ConfigurationField(
				"COLORS_IMPORTANCE_ENABLED",
				Translations.getString("configuration.theme.colors_by_importance_enabled"),
				new ColorsByImportanceEnabledFieldType()));
		
		this.addField(new ConfigurationField(
				"COLORS_ENABLED",
				Translations.getString("configuration.theme.colors_enabled"),
				new ColorsEnabledFieldType()));
		
		this.addField(new ConfigurationField(
				"COLOR_EVEN",
				Translations.getString("configuration.theme.color_even"),
				new ColorEvenFieldType()));
		
		this.addField(new ConfigurationField(
				"COLOR_ODD",
				Translations.getString("configuration.theme.color_odd"),
				new ColorOddFieldType()));
	}
	
	private void applyTheme() {
		LookAndFeelDescriptor laf = (LookAndFeelDescriptor) this.getValue("LOOK_AND_FEEL");
		
		try {
			if (this.windows != null)
				for (int i = 0; i < this.windows.length; i++)
					laf.setLookAndFeel(this.windows[i]);
		} catch (LookAndFeelException e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					e.getMessage(),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
			return;
		}
	}
	
}
