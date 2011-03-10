package com.leclercb.taskunifier.gui.components.welcome.panels;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class WelcomePanel extends CardPanel {
	
	public WelcomePanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
		
		panel.add(new JLabel(
				Images.getResourceImage("logo.png", 128, 128),
				SwingConstants.CENTER));
		
		this.add(panel, BorderLayout.NORTH);
		
		this.add(new JLabel(
				Translations.getString("general.welcome_message"),
				SwingConstants.CENTER), BorderLayout.CENTER);
	}
	
	@Override
	public void applyChanges() {

	}
	
}
