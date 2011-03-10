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
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.HttpUtils;
import com.leclercb.commons.api.utils.http.HttpResponse;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ActionCheckVersion extends AbstractAction {
	
	private boolean silent;
	
	public ActionCheckVersion(boolean silent) {
		this(silent, 32, 32);
	}
	
	public ActionCheckVersion(boolean silent, int width, int height) {
		super(
				Translations.getString("action.name.check_version"),
				Images.getResourceImage("download.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.check_version"));
		
		this.silent = silent;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		this.checkVersion();
	}
	
	public void checkVersion() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					HttpResponse response = null;
					
					Boolean proxyEnabled = Main.SETTINGS.getBooleanProperty("proxy.enabled");
					if (proxyEnabled != null && proxyEnabled) {
						response = HttpUtils.getHttpGetResponse(
								new URI(Constants.VERSION_FILE),
								Main.SETTINGS.getStringProperty("proxy.host"),
								Main.SETTINGS.getIntegerProperty("proxy.port"),
								Main.SETTINGS.getStringProperty("proxy.login"),
								Main.SETTINGS.getStringProperty("proxy.password"));
					} else {
						response = HttpUtils.getHttpGetResponse(new URI(
								Constants.VERSION_FILE));
					}
					
					if (!response.isSuccessfull())
						throw new Exception();
					
					String version = response.getContent().trim();
					
					if (version.length() > 10)
						throw new Exception();
					
					if (Constants.VERSION.compareTo(version) < 0) {
						GuiLogger.getLogger().info(
								"New version available : " + version);
						
						String showed = Main.SETTINGS.getStringProperty("new_version.showed");
						
						if (!ActionCheckVersion.this.silent
								|| !EqualsUtils.equals(version, showed)) {
							Main.SETTINGS.setStringProperty(
									"new_version.showed",
									version);
							
							String[] options = new String[] {
									Translations.getString("general.download"),
									Translations.getString("general.cancel") };
							
							int result = JOptionPane.showOptionDialog(
									MainFrame.getInstance().getFrame(),
									Translations.getString(
											"action.check_version.new_version_available",
											version),
									Translations.getString("general.information"),
									JOptionPane.YES_NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									options,
									options[0]);
							
							if (result == 0) {
								BrowserUtils.openDefaultBrowser(Constants.DOWNLOAD_URL);
							}
						}
					} else {
						GuiLogger.getLogger().info("No new version available");
						
						if (!ActionCheckVersion.this.silent) {
							JOptionPane.showMessageDialog(
									MainFrame.getInstance().getFrame(),
									Translations.getString(
											"action.check_version.no_new_version_available",
											Constants.VERSION),
									Translations.getString("general.information"),
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} catch (Exception e) {
					if (ActionCheckVersion.this.silent) {
						GuiLogger.getLogger().warning(
								"An error occured while checking for updates");
					} else {
						ErrorDialog errorDialog = new ErrorDialog(
								MainFrame.getInstance().getFrame(),
								Translations.getString("error.check_version_error"),
								e,
								false);
						errorDialog.setVisible(true);
					}
				} finally {
					SynchronizerUtils.removeProxy();
				}
			}
			
		});
		
		thread.start();
	}
	
}
