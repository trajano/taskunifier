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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.components.plugins.Plugin;
import com.leclercb.taskunifier.gui.components.plugins.PluginWaitDialog;
import com.leclercb.taskunifier.gui.components.plugins.PluginsUtils;
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
				Translations.getString("action.name.check_plugin_version"),
				Images.getResourceImage("download.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.check_plugin_version"));
		
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
					Plugin[] plugins = PluginsUtils.loadAndUpdatePluginsFromXML(true);
					
					Plugin plugin = null;
					String pluginId = SynchronizerUtils.getPlugin().getId();
					
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
							
							String[] options = new String[] {
									Translations.getString("general.update"),
									Translations.getString("general.cancel") };
							
							int result = JOptionPane.showOptionDialog(
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
						ErrorDialog errorDialog = new ErrorDialog(
								MainFrame.getInstance().getFrame(),
								Translations.getString("error.check_plugin_version_error"),
								t,
								false);
						errorDialog.setVisible(true);
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
