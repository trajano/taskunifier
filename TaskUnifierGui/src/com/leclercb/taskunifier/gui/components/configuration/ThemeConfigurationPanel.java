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
