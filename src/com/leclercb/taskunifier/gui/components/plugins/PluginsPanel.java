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
package com.leclercb.taskunifier.gui.components.plugins;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.api.plugins.PluginStatus;
import com.leclercb.taskunifier.gui.api.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.components.plugins.table.PluginTable;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class PluginsPanel extends JPanel implements ListSelectionListener {
	
	private PluginTable table;
	
	private JTextArea history;
	
	private JButton installButton;
	private JButton updateButton;
	private JButton deleteButton;
	
	public PluginsPanel() {
		this.initialize();
	}
	
	public void reloadPlugins() {
		this.table.setPlugins(PluginsUtils.loadAndUpdatePluginsFromXML(false));
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 5));
		
		this.table = new PluginTable();
		this.table.getSelectionModel().addListSelectionListener(this);
		
		this.add(
				ComponentFactory.createJScrollPane(this.table, true),
				BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		this.history = new JTextArea(5, 10);
		this.history.setEditable(false);
		bottomPanel.add(
				ComponentFactory.createJScrollPane(this.history, true),
				BorderLayout.CENTER);
		
		this.initializeButtonsPanel(bottomPanel);
	}
	
	private void initializeButtonsPanel(JPanel bottomPanel) {
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
		
		this.updateButton = new JButton(
				Translations.getString("general.update"));
		this.updateButton.setActionCommand("UPDATE");
		this.updateButton.addActionListener(listener);
		this.updateButton.setEnabled(false);
		
		this.deleteButton = new JButton(
				Translations.getString("general.delete"));
		this.deleteButton.setActionCommand("DELETE");
		this.deleteButton.addActionListener(listener);
		this.deleteButton.setEnabled(false);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				this.installButton,
				this.updateButton,
				this.deleteButton);
		
		bottomPanel.add(panel, BorderLayout.SOUTH);
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
		this.history.setCaretPosition(0);
		
		this.installButton.setEnabled(plugin.getStatus() == PluginStatus.TO_INSTALL);
		this.updateButton.setEnabled(plugin.getStatus() == PluginStatus.TO_UPDATE);
		this.deleteButton.setEnabled(plugin.getStatus() == PluginStatus.INSTALLED
				|| plugin.getStatus() == PluginStatus.TO_UPDATE);
	}
	
}
