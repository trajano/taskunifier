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

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class ThemeConfigurationPanel extends ConfigurationPanel {
	
	private JTabbedPane tabbedPane;
	
	private ConfigurationPanel generalConfigurationPanel;
	private ConfigurationPanel noteFieldsConfigurationPanel;
	private ConfigurationPanel taskFieldsConfigurationPanel;
	private ConfigurationPanel priorityConfigurationPanel;
	private ConfigurationPanel importanceConfigurationPanel;
	
	public ThemeConfigurationPanel(ConfigurationGroup configurationGroup) {
		super(configurationGroup);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.tabbedPane = new JTabbedPane();
		this.add(this.tabbedPane, BorderLayout.CENTER);
		
		this.initializeGeneralPanel();
		this.initializeNoteFieldsPanel();
		this.initializeTaskFieldsPanel();
		this.initializePriorityPanel();
		this.initializeImportancePanel();
	}
	
	private void initializeGeneralPanel() {
		this.generalConfigurationPanel = new ThemeGeneralConfigurationPanel(
				this);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.general"),
				ComponentFactory.createJScrollPane(
						this.generalConfigurationPanel,
						false));
	}
	
	private void initializeNoteFieldsPanel() {
		this.noteFieldsConfigurationPanel = new ThemeNoteFieldsConfigurationPanel(
				this);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.note_fields"),
				ComponentFactory.createJScrollPane(
						this.noteFieldsConfigurationPanel,
						false));
	}
	
	private void initializeTaskFieldsPanel() {
		this.taskFieldsConfigurationPanel = new ThemeTaskFieldsConfigurationPanel(
				this);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.task_fields"),
				ComponentFactory.createJScrollPane(
						this.taskFieldsConfigurationPanel,
						false));
	}
	
	private void initializePriorityPanel() {
		this.priorityConfigurationPanel = new ThemePriorityConfigurationPanel(
				this);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.priority"),
				ComponentFactory.createJScrollPane(
						this.priorityConfigurationPanel,
						false));
	}
	
	private void initializeImportancePanel() {
		this.importanceConfigurationPanel = new ThemeImportanceConfigurationPanel(
				this);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.importance"),
				ComponentFactory.createJScrollPane(
						this.importanceConfigurationPanel,
						false));
	}
	
	@Override
	public void saveAndApplyConfig() {
		this.generalConfigurationPanel.saveAndApplyConfig();
		this.noteFieldsConfigurationPanel.saveAndApplyConfig();
		this.taskFieldsConfigurationPanel.saveAndApplyConfig();
		this.priorityConfigurationPanel.saveAndApplyConfig();
		this.importanceConfigurationPanel.saveAndApplyConfig();
	}
	
	@Override
	public void cancelConfig() {
		this.generalConfigurationPanel.cancelConfig();
		this.noteFieldsConfigurationPanel.cancelConfig();
		this.taskFieldsConfigurationPanel.cancelConfig();
		this.priorityConfigurationPanel.cancelConfig();
		this.importanceConfigurationPanel.cancelConfig();
	}
	
}
