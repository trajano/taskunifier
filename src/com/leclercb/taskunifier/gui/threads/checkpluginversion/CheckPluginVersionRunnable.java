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

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.api.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUMonitorWaitDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class CheckPluginVersionRunnable implements Runnable {
	
	private SynchronizerGuiPlugin[] syncPlugins;
	private boolean silent;
	
	public CheckPluginVersionRunnable(boolean silent) {
		this(SynchronizerUtils.getSynchronizerPlugin(), silent);
	}
	
	public CheckPluginVersionRunnable(
			SynchronizerGuiPlugin syncPlugin,
			boolean silent) {
		this(new SynchronizerGuiPlugin[] { syncPlugin }, silent);
	}
	
	public CheckPluginVersionRunnable(
			SynchronizerGuiPlugin[] syncPlugins,
			boolean silent) {
		CheckUtils.isNotNull(syncPlugins);
		
		this.syncPlugins = syncPlugins;
		this.silent = silent;
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
		
		for (SynchronizerGuiPlugin syncPlugin : this.syncPlugins) {
			try {
				Plugin plugin = null;
				
				if (syncPlugin.getId().equals(
						DummyGuiPlugin.getInstance().getId())) {
					this.showNoNewVersion(syncPlugin, this.silent);
					return;
				}
				
				for (Plugin p : plugins) {
					if (syncPlugin.getId().equals(p.getId()))
						plugin = p;
				}
				
				if (plugin == null) {
					this.showNoNewVersion(syncPlugin, this.silent);
					return;
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
					
					String showed = Main.getSettings().getStringProperty(
							"new_plugin_version."
									+ syncPlugin.getId()
									+ ".showed");
					
					if (!this.silent || !EqualsUtils.equals(version, showed)) {
						Main.getSettings().setStringProperty(
								"new_plugin_version."
										+ syncPlugin.getId()
										+ ".showed",
								version);
						
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
							final Plugin pluginToUpdate = plugin;
							
							TUMonitorWaitDialog<Void> dialog = new TUMonitorWaitDialog<Void>(
									MainFrame.getInstance().getFrame(),
									Translations.getString("general.manage_plugins")) {
								
								@Override
								public Void doActions(ProgressMonitor monitor)
										throws Throwable {
									PluginsUtils.updatePlugin(
											pluginToUpdate,
											monitor);
									return null;
								}
								
							};
							
							dialog.setVisible(true);
						}
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
