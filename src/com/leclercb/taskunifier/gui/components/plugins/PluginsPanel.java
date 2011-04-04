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
package com.leclercb.taskunifier.gui.components.plugins;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;
import com.leclercb.taskunifier.gui.components.plugins.table.PluginTable;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class PluginsPanel extends JPanel implements ListSelectionListener {
	
	private PluginTable table;
	
	private JTextArea history;
	
	private JButton installButton;
	private JButton updateButton;
	private JButton deleteButton;
	
	public PluginsPanel() {
		this.initialize();
	}
	
	private Plugin[] createPlugins() {
		PluginWaitDialog<Plugin[]> dialog = new PluginWaitDialog<Plugin[]>(
				MainFrame.getInstance().getFrame(),
				Translations.getString("general.manage_plugins")) {
			
			@Override
			public Plugin[] doActions(ProgressMonitor monitor) throws Throwable {
				return PluginsUtils.loadPluginsFromXML(monitor);
			}
			
		};
		dialog.setVisible(true);
		
		Plugin[] plugins = dialog.getResult();
		
		if (plugins == null)
			return new Plugin[0];
		
		List<SynchronizerGuiPlugin> loadedPlugins = Main.API_PLUGINS.getPlugins();
		for (SynchronizerGuiPlugin p : loadedPlugins) {
			for (int i = 0; i < plugins.length; i++) {
				if (p.getId().equals(plugins[i].getId())) {
					if (p.getVersion().compareTo(plugins[i].getVersion()) < 0)
						plugins[i].setStatus(PluginStatus.TO_UPDATE);
					else
						plugins[i].setStatus(PluginStatus.INSTALLED);
				}
			}
		}
		
		return plugins;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.table = new PluginTable(this.createPlugins());
		this.table.getSelectionModel().addListSelectionListener(this);
		
		this.add(
				ComponentFactory.createJScrollPane(this.table, false),
				BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		this.history = new JTextArea(5, 10);
		this.history.setEditable(false);
		bottomPanel.add(
				ComponentFactory.createJScrollPane(this.history, true),
				BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "INSTALL") {
					PluginWaitDialog<Void> dialog = new PluginWaitDialog<Void>(
							MainFrame.getInstance().getFrame(),
							Translations.getString("general.manage_plugins")) {
						
						@Override
						public Void doActions(ProgressMonitor monitor)
								throws Throwable {
							PluginsUtils.installPlugin(
									PluginsPanel.this.table.getSelectedPlugin(),
									monitor);
							return null;
						}
						
					};
					dialog.setVisible(true);
				}
				
				if (event.getActionCommand() == "UPDATE") {
					PluginWaitDialog<Void> dialog = new PluginWaitDialog<Void>(
							MainFrame.getInstance().getFrame(),
							Translations.getString("general.manage_plugins")) {
						
						@Override
						public Void doActions(ProgressMonitor monitor)
								throws Throwable {
							PluginsUtils.updatePlugin(
									PluginsPanel.this.table.getSelectedPlugin(),
									monitor);
							return null;
						}
						
					};
					dialog.setVisible(true);
				}
				
				if (event.getActionCommand() == "DELETE") {
					PluginWaitDialog<Void> dialog = new PluginWaitDialog<Void>(
							MainFrame.getInstance().getFrame(),
							Translations.getString("general.manage_plugins")) {
						
						@Override
						public Void doActions(ProgressMonitor monitor)
								throws Throwable {
							PluginsUtils.deletePlugin(
									PluginsPanel.this.table.getSelectedPlugin(),
									monitor);
							return null;
						}
						
					};
					dialog.setVisible(true);
				}
				
				PluginsPanel.this.valueChanged(null);
			}
			
		};
		
		this.installButton = new JButton(
				Translations.getString("general.install"));
		this.installButton.setActionCommand("INSTALL");
		this.installButton.addActionListener(listener);
		this.installButton.setEnabled(false);
		buttonsPanel.add(this.installButton);
		
		this.updateButton = new JButton(
				Translations.getString("general.update"));
		this.updateButton.setActionCommand("UPDATE");
		this.updateButton.addActionListener(listener);
		this.updateButton.setEnabled(false);
		buttonsPanel.add(this.updateButton);
		
		this.deleteButton = new JButton(
				Translations.getString("general.delete"));
		this.deleteButton.setActionCommand("DELETE");
		this.deleteButton.addActionListener(listener);
		this.deleteButton.setEnabled(false);
		buttonsPanel.add(this.deleteButton);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent evt) {
		if (evt != null && evt.getValueIsAdjusting())
			return;
		
		Plugin plugin = this.table.getSelectedPlugin();
		
		if (plugin == null) {
			this.history.setText(null);
			
			this.installButton.setEnabled(false);
			this.updateButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
			return;
		}
		
		this.history.setText(plugin.getHistory());
		
		this.installButton.setEnabled(plugin.getStatus() == PluginStatus.TO_INSTALL);
		this.updateButton.setEnabled(plugin.getStatus() == PluginStatus.TO_UPDATE);
		this.deleteButton.setEnabled(plugin.getStatus() == PluginStatus.INSTALLED
				|| plugin.getStatus() == PluginStatus.TO_UPDATE);
	}
	
}
