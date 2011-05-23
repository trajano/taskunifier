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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanelExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SettingsDialog extends JDialog {
	
	private static SettingsDialog INSTANCE;
	
	public static SettingsDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SettingsDialog();
		
		return INSTANCE;
	}
	
	public static enum ConfigurationPanel {
		
		GENERAL,
		PROXY,
		COLUMNS,
		THEME,
		SYNCHRONIZATION,
		PLUGIN;
		
	}
	
	private JTabbedPane tabbedPane;
	
	private ConfigurationPanelExt generalConfigurationPanel;
	private ConfigurationPanelExt proxyConfigurationPanel;
	private ConfigurationPanelExt columnsConfigurationPanel;
	private ConfigurationPanelExt themeConfigurationPanel;
	private ConfigurationPanelExt synchronizationConfigurationPanel;
	private ConfigurationPanelExt pluginConfigurationPanel;
	
	private SettingsDialog() {
		super(MainFrame.getInstance().getFrame(), true);
		
		this.initialize();
	}
	
	public void setSelectedConfigurationPanel(ConfigurationPanel panel) {
		CheckUtils.isNotNull(panel, "Configuration panel cannot be null");
		
		switch (panel) {
			case GENERAL:
				this.tabbedPane.setSelectedIndex(0);
				break;
			case PROXY:
				this.tabbedPane.setSelectedIndex(1);
				break;
			case COLUMNS:
				this.tabbedPane.setSelectedIndex(2);
				break;
			case THEME:
				this.tabbedPane.setSelectedIndex(3);
				break;
			case SYNCHRONIZATION:
				this.tabbedPane.setSelectedIndex(4);
				break;
			case PLUGIN:
				this.tabbedPane.setSelectedIndex(5);
				break;
		}
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.configuration"));
		this.setSize(700, 600);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JXHeader header = new JXHeader();
		header.setTitle(Translations.getString("header.title.configuration"));
		header.setDescription(Translations.getString("header.description.configuration"));
		header.setIcon(Images.getResourceImage("settings.png", 32, 32));
		
		this.tabbedPane = new JTabbedPane();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		this.add(header, BorderLayout.NORTH);
		this.add(this.tabbedPane, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
		this.initializeGeneralPanel();
		this.initializeProxyPanel();
		this.initializeColumnsPanel();
		this.initializeThemePanel();
		this.initializeSynchronizationPanel();
		this.initializePluginPanel();
		
		Main.SETTINGS.addPropertyChangeListener(
				"api.id",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						SettingsDialog.this.refreshSynchronizationPanels();
					}
					
				});
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					SettingsDialog.this.saveAndApplyConfig();
					SettingsDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand() == "CANCEL") {
					SettingsDialog.this.generalConfigurationPanel.cancelConfig();
					SettingsDialog.this.synchronizationConfigurationPanel.cancelConfig();
					SettingsDialog.this.pluginConfigurationPanel.cancelConfig();
					SettingsDialog.this.proxyConfigurationPanel.cancelConfig();
					SettingsDialog.this.columnsConfigurationPanel.cancelConfig();
					SettingsDialog.this.themeConfigurationPanel.cancelConfig();
					
					SettingsDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand() == "APPLY") {
					SettingsDialog.this.saveAndApplyConfig();
				}
			}
			
		};
		
		JButton okButton = ComponentFactory.createButtonOk(listener);
		JButton cancelButton = ComponentFactory.createButtonCancel(listener);
		JButton applyButton = ComponentFactory.createButtonApply(listener);
		
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(applyButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
	private void initializeGeneralPanel() {
		this.generalConfigurationPanel = new GeneralConfigurationPanel(
				false,
				false);
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
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.save_settings"),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
			return;
		}
	}
	
	private void refreshSynchronizationPanels() {
		int selectedTab = this.tabbedPane.getSelectedIndex();
		
		this.tabbedPane.removeTabAt(this.tabbedPane.getTabCount() - 1);
		
		this.initializePluginPanel();
		
		try {
			this.tabbedPane.setSelectedIndex(selectedTab);
		} catch (IndexOutOfBoundsException e) {

		}
	}
	
}
