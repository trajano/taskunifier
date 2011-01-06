package com.leclercb.taskunifier.gui.components.welcome.panels;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.images.Images;

public class SettingsPanel extends CardPanel {
	
	private String title;
	private ConfigurationPanel panel;
	
	public SettingsPanel(String title, ConfigurationPanel panel) {
		this.title = title;
		this.panel = panel;
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		panel.add(
				new JLabel(Images.getResourceImage("settings.png", 48, 48)),
				BorderLayout.WEST);
		panel.add(new JLabel(this.title));
		
		this.add(panel, BorderLayout.NORTH);
		
		this.add(this.panel, BorderLayout.CENTER);
	}
	
	@Override
	public void applyChanges() {
		this.panel.saveAndApplyConfig();
	}
	
}
