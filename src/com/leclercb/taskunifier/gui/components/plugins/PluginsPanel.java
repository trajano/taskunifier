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

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.api.plugins.PluginStatus;
import com.leclercb.taskunifier.gui.api.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.components.plugins.list.PluginList;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class PluginsPanel extends JPanel implements ListSelectionListener {
	
	private PluginList list;
	private JTextArea history;
	
	public PluginsPanel() {
		this.initialize();
	}
	
	public void reloadPlugins() {
		this.list.setPlugins(PluginsUtils.loadAndUpdatePluginsFromXML(
				true,
				false));
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 5));
		
		this.list = new PluginList();
		this.list.addListSelectionListener(this);
		
		this.add(
				ComponentFactory.createJScrollPane(this.list, true),
				BorderLayout.CENTER);
		
		this.history = new JTextArea(5, 10);
		this.history.setEditable(false);
		
		this.add(
				ComponentFactory.createJScrollPane(this.history, true),
				BorderLayout.SOUTH);
	}
	
	public void installSelectedPlugin() {
		final Plugin plugin = this.list.getSelectedPlugin();
		
		if (plugin == null)
			return;
		
		if (plugin.getStatus() == PluginStatus.TO_INSTALL) {
			PluginWaitDialog<Void> dialog = new PluginWaitDialog<Void>(
					MainFrame.getInstance().getFrame(),
					Translations.getString("general.manage_plugins")) {
				
				@Override
				public Void doActions(ProgressMonitor monitor) throws Throwable {
					PluginsUtils.installPlugin(plugin, monitor);
					return null;
				}
				
			};
			
			dialog.setVisible(true);
		}
		
		if (plugin.getStatus() == PluginStatus.TO_UPDATE) {
			PluginWaitDialog<Void> dialog = new PluginWaitDialog<Void>(
					MainFrame.getInstance().getFrame(),
					Translations.getString("general.manage_plugins")) {
				
				@Override
				public Void doActions(ProgressMonitor monitor) throws Throwable {
					PluginsUtils.updatePlugin(plugin, monitor);
					return null;
				}
				
			};
			
			dialog.setVisible(true);
		}
		
		Main.SETTINGS.setStringProperty("api.id", plugin.getId());
		
		PluginsPanel.this.valueChanged(null);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent evt) {
		if (evt != null && evt.getValueIsAdjusting())
			return;
		
		Plugin plugin = this.list.getSelectedPlugin();
		
		if (plugin == null) {
			this.history.setText(null);
			return;
		}
		
		this.history.setText(plugin.getHistory());
		this.history.setCaretPosition(0);
	}
	
}
