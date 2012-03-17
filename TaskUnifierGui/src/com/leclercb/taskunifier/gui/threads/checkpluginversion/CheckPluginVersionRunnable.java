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
package com.leclercb.taskunifier.gui.threads.checkpluginversion;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.progress.DefaultProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.api.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.main.frame.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUWorker;
import com.leclercb.taskunifier.gui.swing.TUWorkerDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class CheckPluginVersionRunnable implements Runnable {
	
	private SynchronizerGuiPlugin[] syncPlugins;
	private boolean silent;
	
	public CheckPluginVersionRunnable(boolean silent) {
		this(silent, SynchronizerUtils.getSynchronizerPlugin());
	}
	
	public CheckPluginVersionRunnable(
			boolean silent,
			SynchronizerGuiPlugin... syncPlugins) {
		CheckUtils.isNotNull(syncPlugins);
		
		this.silent = silent;
		this.syncPlugins = syncPlugins;
	}
	
	@Override
	public void run() {
		Plugin[] plugins = PluginsUtils.loadAndUpdatePluginsFromXML(
				true,
				true,
				false,
				true);
		
		if (plugins == null)
			return;
		
		final List<Plugin> pluginsToUpdate = new ArrayList<Plugin>();
		
		for (SynchronizerGuiPlugin syncPlugin : this.syncPlugins) {
			try {
				Plugin plugin = null;
				
				if (syncPlugin.getId().equals(
						DummyGuiPlugin.getInstance().getId())) {
					this.showNoNewVersion(syncPlugin, this.silent);
					continue;
				}
				
				for (Plugin p : plugins) {
					if (syncPlugin.getId().equals(p.getId()))
						plugin = p;
				}
				
				if (plugin == null) {
					this.showNoNewVersion(syncPlugin, this.silent);
					continue;
				}
				
				String version = plugin.getVersion();
				
				if (version == null || version.length() > 10)
					throw new Exception();
				
				if (syncPlugin.getVersion().compareTo(version) < 0) {
					GuiLogger.getLogger().info(
							"New plugin \""
									+ syncPlugin.getName()
									+ "\" version available : "
									+ version);
					
					int result = 0;
					
					if (!this.silent) {
						String[] options = new String[] {
								Translations.getString("general.update"),
								Translations.getString("general.cancel") };
						
						result = JOptionPane.showOptionDialog(
								MainFrame.getInstance().getFrame(),
								Translations.getString(
										"action.check_plugin_version.new_plugin_version_available",
										version,
										syncPlugin.getName()),
								Translations.getString("general.information"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE,
								null,
								options,
								options[0]);
					}
					
					if (result == 0) {
						pluginsToUpdate.add(plugin);
					}
				} else {
					this.showNoNewVersion(syncPlugin, this.silent);
				}
			} catch (Throwable t) {
				if (this.silent) {
					GuiLogger.getLogger().warning(
							"An error occured while checking for plugin updates");
				} else {
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							Translations.getString("error.check_plugin_version_error"),
							null,
							null,
							t,
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
				}
			}
		}
		
		if (pluginsToUpdate.size() > 0) {
			TUWorkerDialog<Void> dialog = new TUWorkerDialog<Void>(
					MainFrame.getInstance().getFrame(),
					Translations.getString("general.manage_plugins"));
			
			ProgressMonitor monitor = new ProgressMonitor();
			monitor.addListChangeListener(dialog);
			
			dialog.setWorker(new TUWorker<Void>(monitor) {
				
				@Override
				protected Void longTask() throws Exception {
					for (Plugin plugin : pluginsToUpdate) {
						PluginsUtils.updatePlugin(
								plugin,
								this.getWorkerMonitor());
						this.publish(new DefaultProgressMessage(" "));
					}
					
					return null;
				}
				
			});
			
			dialog.setVisible(true);
		}
	}
	
	public void showNoNewVersion(
			SynchronizerGuiPlugin syncPlugin,
			boolean silent) {
		GuiLogger.getLogger().info(
				"No new plugin \""
						+ syncPlugin.getName()
						+ "\" version available");
		
		if (!silent) {
			JOptionPane.showMessageDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString(
							"action.check_plugin_version.no_new_plugin_version_available",
							syncPlugin.getVersion(),
							syncPlugin.getName()),
					Translations.getString("general.information"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}
