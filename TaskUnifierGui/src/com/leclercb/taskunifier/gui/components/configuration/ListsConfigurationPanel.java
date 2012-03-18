package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class ListsConfigurationPanel extends ConfigurationPanel {
	
	private JTabbedPane tabbedPane;
	
	private ConfigurationPanel taskStatusesConfigurationPanel;
	private ConfigurationPanel postponeListConfigurationPanel;
	
	public ListsConfigurationPanel(ConfigurationGroup configurationGroup) {
		super(configurationGroup);
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.tabbedPane = new JTabbedPane();
		this.add(this.tabbedPane, BorderLayout.CENTER);
		
		this.initializeTaskStatusesPanel();
		this.initializePostponeListPanel();
	}
	
	private void initializeTaskStatusesPanel() {
		this.taskStatusesConfigurationPanel = new TaskStatusesConfigurationPanel(
				this);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.task_statuses"),
				ComponentFactory.createJScrollPane(
						this.taskStatusesConfigurationPanel,
						false));
	}
	
	private void initializePostponeListPanel() {
		this.postponeListConfigurationPanel = new PostponeListConfigurationPanel(
				this);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.postpone_list"),
				ComponentFactory.createJScrollPane(
						this.postponeListConfigurationPanel,
						false));
	}
	
	@Override
	public void saveAndApplyConfig() {
		this.taskStatusesConfigurationPanel.saveAndApplyConfig();
		this.postponeListConfigurationPanel.saveAndApplyConfig();
	}
	
	@Override
	public void cancelConfig() {
		this.taskStatusesConfigurationPanel.cancelConfig();
		this.postponeListConfigurationPanel.cancelConfig();
	}
	
}
