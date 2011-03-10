package com.leclercb.taskunifier.gui.components.welcome;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.leclercb.taskunifier.gui.components.configuration.GeneralConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.PluginConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.ProxyConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.SynchronizationConfigurationPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.CardPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.SettingsPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.WelcomePanel;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class WelcomeDialog extends JDialog {
	
	private CardPanel[] panels = new CardPanel[] {
			new WelcomePanel(),
			new SettingsPanel(
					Translations.getString("configuration.tab.general"),
					new GeneralConfigurationPanel(false)),
			new SettingsPanel(
					Translations.getString("configuration.tab.proxy"),
					new ProxyConfigurationPanel()),
			new SettingsPanel(
					Translations.getString("configuration.tab.synchronization"),
					new SynchronizationConfigurationPanel(true)),
			new SettingsPanel(
					SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName(),
					new PluginConfigurationPanel(
							false,
							SynchronizerUtils.getPlugin())) };
	
	private JPanel cardPanel;
	private int currentPanel;
	
	public WelcomeDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		// For API Configuration Panel
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("api.id")) {
					SettingsPanel synchronizationPanel = (SettingsPanel) WelcomeDialog.this.panels[WelcomeDialog.this.panels.length - 2];
					SettingsPanel servicePanel = (SettingsPanel) WelcomeDialog.this.panels[WelcomeDialog.this.panels.length - 1];
					
					synchronizationPanel.reset(
							Translations.getString("configuration.tab.synchronization"),
							new SynchronizationConfigurationPanel(true));
					
					servicePanel.reset(
							SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName(),
							new PluginConfigurationPanel(
									false,
									SynchronizerUtils.getPlugin()));
				}
			}
			
		});
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.welcome"));
		this.setSize(700, 600);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = new JPanel();
		this.add(panel, BorderLayout.CENTER);
		
		this.currentPanel = 0;
		
		panel.setLayout(new BorderLayout());
		
		this.cardPanel = new JPanel();
		this.cardPanel.setLayout(new CardLayout());
		panel.add(this.cardPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		panel.add(buttonsPanel, BorderLayout.SOUTH);
		
		int i = 0;
		for (CardPanel cp : this.panels)
			this.cardPanel.add(cp, "" + i++);
		
		this.initializeButtons(buttonsPanel);
	}
	
	private void initializeButtons(JPanel buttonsPanel) {
		final JButton previousButton = new JButton(
				Translations.getString("general.previous"));
		
		final JButton nextButton = new JButton(
				Translations.getString("general.next"));
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				WelcomeDialog.this.panels[WelcomeDialog.this.currentPanel].applyChanges();
				
				if (event.getActionCommand() == "PREVIOUS") {
					if (WelcomeDialog.this.currentPanel != 0) {
						WelcomeDialog.this.currentPanel--;
						((CardLayout) WelcomeDialog.this.cardPanel.getLayout()).previous(WelcomeDialog.this.cardPanel);
					}
				}
				
				if (event.getActionCommand() == "NEXT") {
					if (WelcomeDialog.this.currentPanel < WelcomeDialog.this.panels.length - 1) {
						WelcomeDialog.this.currentPanel++;
						((CardLayout) WelcomeDialog.this.cardPanel.getLayout()).next(WelcomeDialog.this.cardPanel);
					} else {
						WelcomeDialog.this.dispose();
					}
				}
				
				if (WelcomeDialog.this.currentPanel == WelcomeDialog.this.panels.length - 1)
					nextButton.setText(Translations.getString("general.finish"));
				else
					nextButton.setText(Translations.getString("general.next"));
				
				if (WelcomeDialog.this.currentPanel == 0)
					previousButton.setEnabled(false);
				else
					previousButton.setEnabled(true);
			}
			
		};
		
		previousButton.setActionCommand("PREVIOUS");
		previousButton.addActionListener(listener);
		previousButton.setEnabled(false);
		buttonsPanel.add(previousButton);
		
		nextButton.setActionCommand("NEXT");
		nextButton.addActionListener(listener);
		buttonsPanel.add(nextButton);
	}
	
}
