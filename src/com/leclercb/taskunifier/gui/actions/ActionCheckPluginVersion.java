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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.api.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.components.plugins.PluginWaitDialog;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ActionCheckPluginVersion extends AbstractAction {
	
	private boolean silent;
	
	public ActionCheckPluginVersion(boolean silent) {
		this(silent, 32, 32);
	}
	
	public ActionCheckPluginVersion(boolean silent, int width, int height) {
		super(
				Translations.getString("action.check_plugin_version"),
				Images.getResourceImage("download.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.check_plugin_version"));
		
		this.silent = silent;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionCheckPluginVersion.checkPluginVersion(this.silent);
	}
	
	public static void checkPluginVersion(final boolean silent) {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Plugin plugin = null;
					String pluginId = SynchronizerUtils.getPlugin().getId();
					
					if (pluginId.equals(DummyGuiPlugin.getInstance().getId())) {
						this.showNoNewVersion(silent);
						return;
					}
					
					Plugin[] plugins = PluginsUtils.loadAndUpdatePluginsFromXML(
							false,
							true);
					
					for (Plugin p : plugins) {
						if (pluginId.equals(p.getId()))
							plugin = p;
					}
					
					if (plugin == null) {
						this.showNoNewVersion(silent);
						return;
					}
					
					String version = plugin.getVersion();
					
					if (version == null || version.length() > 10)
						throw new Exception();
					
					if (SynchronizerUtils.getPlugin().getVersion().compareTo(
							version) < 0) {
						GuiLogger.getLogger().info(
								"New plugin \""
										+ SynchronizerUtils.getPlugin().getName()
										+ "\" version available : "
										+ version);
						
						String showed = Main.SETTINGS.getStringProperty("new_plugin_version."
								+ SynchronizerUtils.getPlugin().getId()
								+ ".showed");
						
						if (!silent || !EqualsUtils.equals(version, showed)) {
							Main.SETTINGS.setStringProperty(
									"new_plugin_version."
											+ SynchronizerUtils.getPlugin().getId()
											+ ".showed",
									version);
							
							int result = 0;
							
							if (!silent) {
								String[] options = new String[] {
										Translations.getString("general.update"),
										Translations.getString("general.cancel") };
								
								result = JOptionPane.showOptionDialog(
										MainFrame.getInstance().getFrame(),
										Translations.getString(
												"action.check_plugin_version.new_plugin_version_available",
												version,
												SynchronizerUtils.getPlugin().getName()),
										Translations.getString("general.information"),
										JOptionPane.YES_NO_OPTION,
										JOptionPane.INFORMATION_MESSAGE,
										null,
										options,
										options[0]);
							}
							
							if (result == 0) {
								final Plugin pluginToUpdate = plugin;
								
								PluginWaitDialog<Void> dialog = new PluginWaitDialog<Void>(
										MainFrame.getInstance().getFrame(),
										Translations.getString("general.manage_plugins")) {
									
									@Override
									public Void doActions(
											ProgressMonitor monitor)
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
						this.showNoNewVersion(silent);
					}
				} catch (Throwable t) {
					if (silent) {
						GuiLogger.getLogger().warning(
								"An error occured while checking for updates");
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
			
			public void showNoNewVersion(boolean silent) {
				GuiLogger.getLogger().info(
						"No new plugin \""
								+ SynchronizerUtils.getPlugin().getName()
								+ "\" version available");
				
				if (!silent) {
					JOptionPane.showMessageDialog(
							MainFrame.getInstance().getFrame(),
							Translations.getString(
									"action.check_plugin_version.no_new_plugin_version_available",
									SynchronizerUtils.getPlugin().getVersion(),
									SynchronizerUtils.getPlugin().getName()),
							Translations.getString("general.information"),
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
		
		thread.start();
	}
	
}
