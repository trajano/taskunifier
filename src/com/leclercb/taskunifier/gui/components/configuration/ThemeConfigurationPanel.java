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

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JCheckBox;

import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.commons.gui.swing.lookandfeel.exc.LookAndFeelException;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ThemeConfigurationPanel extends DefaultConfigurationPanel {
	
	private Window[] windows;
	
	public ThemeConfigurationPanel(Window[] windows) {
		this.windows = windows;
		this.initialize();
		this.initializeListeners();
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		// Look And Feel & Theme
		LookAndFeelDescriptor laf = (LookAndFeelDescriptor) this.getValue("LOOK_AND_FEEL");
		Main.SETTINGS.setStringProperty(
				"theme.lookandfeel",
				laf.getIdentifier());
		
		// Badges
		Main.SETTINGS.setBooleanProperty(
				"searcher.show_badges",
				(Boolean) this.getValue("SHOW_BADGES"));
		
		// Colors
		Main.SETTINGS.setBooleanProperty(
				"theme.color.importance.enabled",
				(Boolean) this.getValue("COLORS_IMPORTANCE_ENABLED"));
		Main.SETTINGS.setBooleanProperty(
				"theme.color.enabled",
				(Boolean) this.getValue("COLORS_ENABLED"));
		Main.SETTINGS.setColorProperty(
				"theme.color.even",
				(Color) this.getValue("COLOR_EVEN"));
		Main.SETTINGS.setColorProperty(
				"theme.color.odd",
				(Color) this.getValue("COLOR_ODD"));
	}
	
	private void applyTheme() {
		LookAndFeelDescriptor laf = (LookAndFeelDescriptor) this.getValue("LOOK_AND_FEEL");
		
		try {
			if (this.windows != null)
				for (int i = 0; i < this.windows.length; i++)
					laf.setLookAndFeel(this.windows[i]);
		} catch (LookAndFeelException e) {
			ErrorDialog errorDialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					null,
					e,
					true);
			errorDialog.setVisible(true);
			
			return;
		}
	}
	
	private void initialize() {
		LookAndFeelDescriptor themeLookAndFeelValue = null;
		Boolean themeShowBadges = false;
		Boolean themeColorImportanceEnabledValue = false;
		Boolean themeColorEnabledValue = false;
		Color themeColorEvenValue = Color.WHITE;
		Color themeColorOddValue = Color.WHITE;
		
		if (Main.SETTINGS.getStringProperty("theme.lookandfeel") != null)
			themeLookAndFeelValue = LookAndFeelUtils.getLookAndFeel(Main.SETTINGS.getStringProperty("theme.lookandfeel"));
		
		if (Main.SETTINGS.getBooleanProperty("searcher.show_badges") != null)
			themeShowBadges = Main.SETTINGS.getBooleanProperty("searcher.show_badges");
		
		if (Main.SETTINGS.getBooleanProperty("theme.color.importance.enabled") != null)
			themeColorImportanceEnabledValue = Main.SETTINGS.getBooleanProperty("theme.color.importance.enabled");
		
		if (Main.SETTINGS.getBooleanProperty("theme.color.enabled") != null)
			themeColorEnabledValue = Main.SETTINGS.getBooleanProperty("theme.color.enabled");
		
		if (Main.SETTINGS.getColorProperty("theme.color.even") != null)
			themeColorEvenValue = Main.SETTINGS.getColorProperty("theme.color.even");
		
		if (Main.SETTINGS.getColorProperty("theme.color.odd") != null)
			themeColorOddValue = Main.SETTINGS.getColorProperty("theme.color.odd");
		
		// Sort look and feels by name
		List<LookAndFeelDescriptor> lookAndFeels = new ArrayList<LookAndFeelDescriptor>(
				LookAndFeelUtils.getLookAndFeels());
		Collections.sort(lookAndFeels, new Comparator<LookAndFeelDescriptor>() {
			
			@Override
			public int compare(
					LookAndFeelDescriptor laf1,
					LookAndFeelDescriptor laf2) {
				return laf1.getName().compareTo(laf2.getName());
			}
			
		});
		
		this.addField(new ConfigurationField(
				"LOOK_AND_FEEL",
				Translations.getString("configuration.theme.look_and_feel"),
				new ConfigurationFieldType.ComboBox(
						lookAndFeels.toArray(),
						themeLookAndFeelValue)));
		
		this.addField(new ConfigurationField(
				"APPLY_THEME",
				null,
				new ConfigurationFieldType.Button(
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
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.theme.look_and_feel_preview"))));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"SETTINGS_AFTER_RESTART",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.general.settings_changed_after_restart"))));
		
		this.addField(new ConfigurationField(
				"SHOW_BADGES_PERFORMANCE",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.theme.show_badges_performance"))));
		
		this.addField(new ConfigurationField(
				"SHOW_BADGES",
				Translations.getString("configuration.theme.show_badges"),
				new ConfigurationFieldType.CheckBox(themeShowBadges)));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"COLOR_CHANGED_NEXT_STARTUP",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.theme.colors_changed_after_restart"))));
		
		this.addField(new ConfigurationField(
				"COLORS_IMPORTANCE_ENABLED",
				Translations.getString("configuration.theme.colors_by_importance_enabled"),
				new ConfigurationFieldType.CheckBox(
						themeColorImportanceEnabledValue)));
		
		final JCheckBox colorsByImportance = ((ConfigurationFieldType.CheckBox) this.getField(
				"COLORS_IMPORTANCE_ENABLED").getType()).getFieldComponent();
		
		this.addField(new ConfigurationField(
				"COLORS_ENABLED",
				Translations.getString("configuration.theme.colors_enabled"),
				new ConfigurationFieldType.CheckBox(themeColorEnabledValue)));
		
		this.addField(new ConfigurationField(
				"COLOR_EVEN",
				Translations.getString("configuration.theme.color_even"),
				new ConfigurationFieldType.ColorChooser(themeColorEvenValue)));
		
		this.addField(new ConfigurationField(
				"COLOR_ODD",
				Translations.getString("configuration.theme.color_odd"),
				new ConfigurationFieldType.ColorChooser(themeColorOddValue)));
		
		this.setEnabled("COLORS_ENABLED", !colorsByImportance.isSelected());
		this.setEnabled("COLOR_EVEN", !colorsByImportance.isSelected());
		this.setEnabled("COLOR_ODD", !colorsByImportance.isSelected());
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ThemeConfigurationPanel.this.setEnabled(
						"COLORS_ENABLED",
						!colorsByImportance.isSelected());
				ThemeConfigurationPanel.this.setEnabled(
						"COLOR_EVEN",
						!colorsByImportance.isSelected());
				ThemeConfigurationPanel.this.setEnabled(
						"COLOR_ODD",
						!colorsByImportance.isSelected());
			}
			
		};
		
		colorsByImportance.addActionListener(listener);
	}
	
}
