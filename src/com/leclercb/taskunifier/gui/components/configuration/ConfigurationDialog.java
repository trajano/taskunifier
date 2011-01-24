/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ConfigurationDialog extends JDialog {
	
	private ConfigurationPanel generalConfigurationPanel;
	private ConfigurationPanel synchronizationConfigurationPanel;
	private ConfigurationPanel apiSynchronizerConfigurationPanel;
	private ConfigurationPanel proxyConfigurationPanel;
	private ConfigurationPanel templateConfigurationPanel;
	private ConfigurationPanel columnsConfigurationPanel;
	private ConfigurationPanel themeConfigurationPanel;
	
	public ConfigurationDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.configuration"));
		this.setSize(600, 600);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
		this.initializeGeneralPanel(tabbedPane);
		this.initializeProxyPanel(tabbedPane);
		this.initializeTemplatePanel(tabbedPane);
		this.initializeColumnsPanel(tabbedPane);
		this.initializeThemePanel(tabbedPane);
		this.initializeSynchronizationPanel(tabbedPane);
		this.initializeApiSynchronizerPanel(tabbedPane);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					ConfigurationDialog.this.saveAndApplyConfig();
					ConfigurationDialog.this.dispose();
				}
				
				if (event.getActionCommand() == "CANCEL") {
					ConfigurationDialog.this.dispose();
				}
				
				if (event.getActionCommand() == "APPLY") {
					ConfigurationDialog.this.saveAndApplyConfig();
				}
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
		
		JButton cancelButton = new JButton(
				Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
		
		JButton applyButton = new JButton(
				Translations.getString("general.apply"));
		applyButton.setActionCommand("APPLY");
		applyButton.addActionListener(listener);
		buttonsPanel.add(applyButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
	private void initializeGeneralPanel(JTabbedPane tabbedPane) {
		this.generalConfigurationPanel = new GeneralConfigurationPanel(false);
		tabbedPane.addTab(
				Translations.getString("configuration.tab.general"),
				new JScrollPane(this.generalConfigurationPanel));
	}
	
	private void initializeProxyPanel(JTabbedPane tabbedPane) {
		this.proxyConfigurationPanel = new ProxyConfigurationPanel();
		tabbedPane.addTab(
				Translations.getString("configuration.tab.proxy"),
				new JScrollPane(this.proxyConfigurationPanel));
	}
	
	private void initializeTemplatePanel(JTabbedPane tabbedPane) {
		this.templateConfigurationPanel = new TemplateConfigurationPanel();
		tabbedPane.addTab(
				Translations.getString("configuration.tab.template"),
				new JScrollPane(this.templateConfigurationPanel));
	}
	
	private void initializeColumnsPanel(JTabbedPane tabbedPane) {
		this.columnsConfigurationPanel = new ColumnsConfigurationPanel();
		tabbedPane.addTab(
				Translations.getString("configuration.tab.columns"),
				new JScrollPane(this.columnsConfigurationPanel));
	}
	
	private void initializeThemePanel(JTabbedPane tabbedPane) {
		this.themeConfigurationPanel = new ThemeConfigurationPanel(
				new Window[] { this, this.getOwner() });
		tabbedPane.addTab(
				Translations.getString("configuration.tab.theme"),
				new JScrollPane(this.themeConfigurationPanel));
	}
	
	private void initializeSynchronizationPanel(JTabbedPane tabbedPane) {
		this.synchronizationConfigurationPanel = new SynchronizationConfigurationPanel(
				false);
		tabbedPane.addTab(
				Translations.getString("configuration.tab.synchronization"),
				new JScrollPane(this.synchronizationConfigurationPanel));
	}
	
	private void initializeApiSynchronizerPanel(final JTabbedPane tabbedPane) {
		// For API Configuration Panel
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("api")) {
					tabbedPane.removeTabAt(tabbedPane.getTabCount() - 1);
					tabbedPane.removeTabAt(tabbedPane.getTabCount() - 1);
					
					ConfigurationDialog.this.synchronizationConfigurationPanel = new SynchronizationConfigurationPanel(
							false);
					
					tabbedPane.addTab(
							Translations.getString("configuration.tab.synchronization"),
							new JScrollPane(
									ConfigurationDialog.this.synchronizationConfigurationPanel));
					
					ConfigurationDialog.this.apiSynchronizerConfigurationPanel = SynchronizerUtils.getApi().getConfigurationPanel(
							false);
					
					if (ConfigurationDialog.this.apiSynchronizerConfigurationPanel != null)
						tabbedPane.addTab(
								SynchronizerUtils.getApi().getSynchronizerApi().getApiName(),
								new JScrollPane(
										ConfigurationDialog.this.apiSynchronizerConfigurationPanel));
					
					tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
				}
			}
			
		});
		
		this.apiSynchronizerConfigurationPanel = SynchronizerUtils.getApi().getConfigurationPanel(
				false);
		
		tabbedPane.addTab(
				SynchronizerUtils.getApi().getSynchronizerApi().getApiName(),
				new JScrollPane(this.apiSynchronizerConfigurationPanel));
	}
	
	private void saveAndApplyConfig() {
		try {
			this.apiSynchronizerConfigurationPanel.saveAndApplyConfig();
			
			this.generalConfigurationPanel.saveAndApplyConfig();
			this.proxyConfigurationPanel.saveAndApplyConfig();
			this.templateConfigurationPanel.saveAndApplyConfig();
			this.columnsConfigurationPanel.saveAndApplyConfig();
			this.themeConfigurationPanel.saveAndApplyConfig();
			this.synchronizationConfigurationPanel.saveAndApplyConfig();
			
			Main.saveSettings();
		} catch (Exception e) {
			ErrorDialog errorDialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("error.save_settings"),
					e);
			errorDialog.setVisible(true);
			
			return;
		}
	}
	
}
