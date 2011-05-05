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
import javax.swing.JTabbedPane;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ConfigurationDialog extends JDialog {
	
	private JTabbedPane tabbedPane;
	
	private ConfigurationPanel generalConfigurationPanel;
	private ConfigurationPanel synchronizationConfigurationPanel;
	private PluginConfigurationPanel pluginConfigurationPanel;
	private ConfigurationPanel proxyConfigurationPanel;
	private ConfigurationPanel columnsConfigurationPanel;
	private ConfigurationPanel themeConfigurationPanel;
	
	public ConfigurationDialog(Frame frame) {
		super(frame, true);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.configuration"));
		this.setSize(700, 600);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.tabbedPane = new JTabbedPane();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		this.add(this.tabbedPane, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
		this.initializeGeneralPanel();
		this.initializeProxyPanel();
		this.initializeColumnsPanel();
		this.initializeThemePanel();
		this.initializeSynchronizationPanel();
		this.initializePluginPanel();
		
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("api.id"))
					ConfigurationDialog.this.refreshSynchronizationPanels();
			}
			
		});
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
	
	private void initializeGeneralPanel() {
		this.generalConfigurationPanel = new GeneralConfigurationPanel(false);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.general"),
				ComponentFactory.createJScrollPane(
						this.generalConfigurationPanel,
						false));
	}
	
	private void initializeProxyPanel() {
		this.proxyConfigurationPanel = new ProxyConfigurationPanel();
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.proxy"),
				ComponentFactory.createJScrollPane(
						this.proxyConfigurationPanel,
						false));
	}
	
	private void initializeColumnsPanel() {
		this.columnsConfigurationPanel = new ColumnsConfigurationPanel();
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.columns"),
				ComponentFactory.createJScrollPane(
						this.columnsConfigurationPanel,
						false));
	}
	
	private void initializeThemePanel() {
		this.themeConfigurationPanel = new ThemeConfigurationPanel(
				new Window[] { this, this.getOwner() });
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.theme"),
				ComponentFactory.createJScrollPane(
						this.themeConfigurationPanel,
						false));
	}
	
	private void initializeSynchronizationPanel() {
		this.synchronizationConfigurationPanel = new SynchronizationConfigurationPanel(
				false);
		this.tabbedPane.addTab(
				Translations.getString("configuration.tab.synchronization"),
				ComponentFactory.createJScrollPane(
						this.synchronizationConfigurationPanel,
						false));
	}
	
	private void initializePluginPanel() {
		this.pluginConfigurationPanel = new PluginConfigurationPanel(
				false,
				SynchronizerUtils.getPlugin());
		
		this.tabbedPane.addTab(
				SynchronizerUtils.getPlugin().getName(),
				ComponentFactory.createJScrollPane(
						this.pluginConfigurationPanel,
						false));
	}
	
	private void saveAndApplyConfig() {
		try {
			this.pluginConfigurationPanel.saveAndApplyConfig();
			
			this.generalConfigurationPanel.saveAndApplyConfig();
			this.proxyConfigurationPanel.saveAndApplyConfig();
			this.columnsConfigurationPanel.saveAndApplyConfig();
			this.themeConfigurationPanel.saveAndApplyConfig();
			this.synchronizationConfigurationPanel.saveAndApplyConfig();
			
			Main.saveSettings();
			
			this.refreshSynchronizationPanels();
		} catch (Exception e) {
			ErrorDialog errorDialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("error.save_settings"),
					e,
					true);
			errorDialog.setVisible(true);
			
			return;
		}
	}
	
	private void refreshSynchronizationPanels() {
		int selectedTab = this.tabbedPane.getSelectedIndex();
		
		this.tabbedPane.removeTabAt(this.tabbedPane.getTabCount() - 1);
		this.tabbedPane.removeTabAt(this.tabbedPane.getTabCount() - 1);
		
		this.initializeSynchronizationPanel();
		this.initializePluginPanel();
		
		try {
			this.tabbedPane.setSelectedIndex(selectedTab);
		} catch (IndexOutOfBoundsException e) {

		}
	}
	
}
