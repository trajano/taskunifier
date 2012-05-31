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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.properties.events.ReloadPropertiesListener;
import com.leclercb.commons.api.properties.events.WeakReloadPropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.swing.buttons.TUApplyButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class PluginConfigurationDialog extends JDialog implements ConfigurationGroup, ReloadPropertiesListener {
	
	private static PluginConfigurationDialog INSTANCE;
	
	public static PluginConfigurationDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PluginConfigurationDialog();
		
		return INSTANCE;
	}
	
	private SynchronizerGuiPlugin plugin;
	
	private JPanel pluginContainerPanel;
	private ConfigurationPanel pluginConfigurationPanel;
	
	private PluginConfigurationDialog() {
		super();
		
		this.initialize();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.setLocationRelativeTo(FrameUtils.getCurrentFrame());
		}
		
		super.setVisible(visible);
	}
	
	public void setPlugin(SynchronizerGuiPlugin plugin) {
		CheckUtils.isNotNull(plugin);
		
		if (EqualsUtils.equals(this.plugin, plugin))
			return;
		
		this.plugin = plugin;
		
		this.setTitle(plugin.getName());
		this.initializePluginPanel();
	}
	
	private void initialize() {
		this.setModal(true);
		this.setSize(800, 500);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		JXHeader header = new JXHeader();
		header.setTitle(Translations.getString("header.title.configuration"));
		header.setDescription(Translations.getString("header.description.configuration"));
		header.setIcon(ImageUtils.getResourceImage("settings.png", 32, 32));
		
		this.add(header, BorderLayout.NORTH);
		
		this.pluginContainerPanel = new JPanel();
		this.pluginContainerPanel.setLayout(new BorderLayout());
		
		this.initializeButtonsPanel();
		this.initializePluginPanel();
		
		this.add(this.pluginContainerPanel, BorderLayout.CENTER);
		
		Main.getUserSettings().addReloadPropertiesListener(
				new WeakReloadPropertiesListener(Main.getUserSettings(), this));
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					PluginConfigurationDialog.this.saveAndApplyConfig();
					PluginConfigurationDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand().equals("CANCEL")) {
					PluginConfigurationDialog.this.cancelConfig();
					PluginConfigurationDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand().equals("APPLY")) {
					PluginConfigurationDialog.this.saveAndApplyConfig();
				}
			}
			
		};
		
		JButton okButton = new TUOkButton(listener);
		JButton cancelButton = new TUCancelButton(listener);
		JButton applyButton = new TUApplyButton(listener);
		
		JPanel panel = new TUButtonsPanel(okButton, cancelButton, applyButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
	}
	
	private void initializePluginPanel() {
		if (this.plugin == null)
			return;
		
		this.pluginContainerPanel.removeAll();
		
		this.pluginConfigurationPanel = new PluginConfigurationPanel(
				this,
				false,
				this.plugin);
		
		this.pluginContainerPanel.add(ComponentFactory.createJScrollPane(
				this.pluginConfigurationPanel,
				false), BorderLayout.CENTER);
		
		this.pluginContainerPanel.validate();
	}
	
	@Override
	public void saveAndApplyConfig() {
		try {
			this.pluginConfigurationPanel.saveAndApplyConfig();
			
			Main.saveSettings();
			Main.saveUserSettings();
		} catch (Exception e) {
			GuiLogger.getLogger().log(Level.SEVERE, e.getMessage(), e);
			
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.save_settings"),
					null,
					"GUI",
					e,
					Level.SEVERE,
					null);
			
			JXErrorPane.showDialog(FrameUtils.getCurrentFrame(), info);
			
			return;
		}
	}
	
	@Override
	public void cancelConfig() {
		try {
			this.pluginConfigurationPanel.cancelConfig();
		} catch (Exception e) {
			GuiLogger.getLogger().log(Level.SEVERE, e.getMessage(), e);
			
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.save_settings"),
					null,
					"GUI",
					e,
					Level.SEVERE,
					null);
			
			JXErrorPane.showDialog(FrameUtils.getCurrentFrame(), info);
			
			return;
		}
	}
	
	@Override
	public void reloadProperties() {
		PluginConfigurationDialog.this.cancelConfig();
	}
	
}
