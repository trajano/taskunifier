package com.leclercb.taskunifier.gui.components.plugins;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;
import com.leclercb.taskunifier.gui.components.plugins.table.PluginTable;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class PluginsPanel extends JPanel implements ListSelectionListener {
	
	private PluginTable table;
	
	private JButton installButton;
	private JButton updateButton;
	private JButton deleteButton;
	
	public PluginsPanel() {
		this.initialize();
	}
	
	private Plugin[] createPlugins() {
		Plugin[] plugins = PluginsUtils.loadPluginsFromXML();
		
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
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "INSTALL") {
					PluginsUtils.installPlugin(PluginsPanel.this.table.getSelectedPlugin());
				}
				
				if (event.getActionCommand() == "UPDATE") {
					PluginsUtils.updatePlugin(PluginsPanel.this.table.getSelectedPlugin());
				}
				
				if (event.getActionCommand() == "DELETE") {
					PluginsUtils.deletePlugin(PluginsPanel.this.table.getSelectedPlugin());
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
			this.installButton.setEnabled(false);
			this.updateButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
			return;
		}
		
		this.installButton.setEnabled(plugin.getStatus() == PluginStatus.TO_INSTALL);
		this.updateButton.setEnabled(plugin.getStatus() == PluginStatus.TO_UPDATE);
		this.deleteButton.setEnabled(plugin.getStatus() != PluginStatus.TO_INSTALL);
	}
	
}
