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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.api.synchronizer.AbstractCall;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;
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
					SynchronizerUtils.initializeProxy();
					VersionCall call = new VersionCall();
					
					String version = call.getVersion();
					
					if (Constants.VERSION.compareTo(version) < 0) {
						GuiLogger.getLogger().info(
								"New version available : " + version);
						
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
								Translations.getString("error.check_version_error"));
						errorDialog.setVisible(true);
					}
				} finally {
					SynchronizerUtils.removeProxy();
				}
			}
			
		});
		
		thread.start();
	}
	
	private static class VersionCall extends AbstractCall {
		
		public String getVersion() throws Exception {
			InputStream stream = this.call(Constants.VERSION_FILE);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			stream.close();
			return sb.toString().trim();
		}
		
	}
	
}
