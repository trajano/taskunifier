package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;

public class PluginConfigurationPanel extends ConfigurationPanel {
	
	private ConfigurationPanel configPanel;
	
	public PluginConfigurationPanel(
			boolean welcome,
			SynchronizerGuiPlugin plugin) {
		this.initialize(welcome, plugin);
	}
	
	private void initialize(boolean welcome, SynchronizerGuiPlugin plugin) {
		this.setLayout(new BorderLayout());
		
		this.configPanel = plugin.getConfigurationPanel(welcome);
		
		String info = plugin.getName()
				+ " - "
				+ plugin.getAuthor()
				+ " - "
				+ plugin.getVersion();
		JLabel pluginInfo = new JLabel(info, SwingConstants.RIGHT);
		pluginInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(this.configPanel, BorderLayout.CENTER);
		this.add(pluginInfo, BorderLayout.SOUTH);
	}
	
	@Override
	public void saveAndApplyConfig() {
		this.configPanel.saveAndApplyConfig();
	}
	
}
