package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;

public class PluginConfigurationPanel extends ConfigurationPanel {
	
	private ConfigurationPanel configPanel;
	
	public PluginConfigurationPanel(
			boolean welcome,
			SynchronizerGuiPlugin plugin) {
		this.initialize(welcome, plugin);
	}
	
	private void initialize(boolean welcome, SynchronizerGuiPlugin plugin) {
		this.setLayout(new BorderLayout());
		
		configPanel = plugin.getConfigurationPanel(welcome);
		
		String info = plugin.getAuthor() + " - " + plugin.getVersion();
		JLabel pluginInfo = new JLabel(info, JLabel.RIGHT);
		pluginInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(configPanel, BorderLayout.CENTER);
		this.add(pluginInfo, BorderLayout.SOUTH);
	}
	
	@Override
	public void saveAndApplyConfig() {
		configPanel.saveAndApplyConfig();
	}
	
}
