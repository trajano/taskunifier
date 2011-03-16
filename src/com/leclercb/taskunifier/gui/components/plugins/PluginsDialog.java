package com.leclercb.taskunifier.gui.components.plugins;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginsDialog extends JDialog {
	
	public PluginsDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.plugins"));
		this.setSize(600, 300);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(mainPanel, BorderLayout.CENTER);
		
		PluginsPanel pluginsPanel = new PluginsPanel();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		mainPanel.add(pluginsPanel, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "CLOSE") {
					PluginsDialog.this.dispose();
				}
			}
			
		};
		
		JButton closeButton = new JButton(
				Translations.getString("general.close"));
		closeButton.setActionCommand("CLOSE");
		closeButton.addActionListener(listener);
		buttonsPanel.add(closeButton);
		
		this.getRootPane().setDefaultButton(closeButton);
	}
	
}
