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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.util.Calendar;

import javax.swing.SwingUtilities;

import com.leclercb.commons.gui.swing.notifications.Notification;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class BackgroundSynchronizer {
	
	public BackgroundSynchronizer() {

	}
	
	public void synchronize() {
		if (!Synchronizing.setSynchronizing(true))
			return;
		
		Notification notification = null;
		Connection connection = null;
		Synchronizer synchronizer = null;
		
		try {
			notification = new Notification(
					Images.getResourceImage("synchronize.png", 48, 48),
					Translations.getString("synchronizer.start_synchronization"));
			notification.setDuration(3000);
			notification.setLocation(
					MainFrame.getInstance().getFrame(),
					Notification.POSITION_BOTTOM_RIGHT);
			notification.setVisible(true);
			
			SynchronizerUtils.initializeProxy();
			
			if (SynchronizerUtils.getPlugin().needsLicense()) {
				if (!SynchronizerUtils.getPlugin().checkLicense()) {
					notification = new Notification(Images.getResourceImage(
							"synchronize.png",
							48,
							48), Translations.getString(
							"synchronizer.wait_no_license",
							Constants.WAIT_NO_LICENSE_TIME));
					notification.setDuration(3000);
					notification.setLocation(
							MainFrame.getInstance().getFrame(),
							Notification.POSITION_BOTTOM_LEFT);
					notification.setVisible(true);
					
					Thread.sleep(Constants.WAIT_NO_LICENSE_TIME * 1000);
				}
			}
			
			connection = SynchronizerUtils.getPlugin().getConnection();
			
			connection.loadParameters(Main.SETTINGS);
			connection.connect();
			connection.saveParameters(Main.SETTINGS);
			
			synchronizer = SynchronizerUtils.getPlugin().getSynchronizerApi().getSynchronizer(
					connection);
			
			SynchronizerChoice choice = (SynchronizerChoice) Main.SETTINGS.getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
			
			synchronizer.loadParameters(Main.SETTINGS);
			synchronizer.synchronize(choice);
			synchronizer.saveParameters(Main.SETTINGS);
			
			connection.disconnect();
			
			Main.SETTINGS.setCalendarProperty(
					"synchronizer.last_synchronization_date",
					Calendar.getInstance());
			
			notification = new Notification(
					Images.getResourceImage("synchronize.png", 48, 48),
					Translations.getString("synchronizer.synchronization_completed"));
			notification.setDuration(4000);
			notification.setLocation(
					MainFrame.getInstance().getFrame(),
					Notification.POSITION_BOTTOM_RIGHT);
			notification.setVisible(true);
		} catch (final SynchronizerException e) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					ErrorDialog errorDialog = new ErrorDialog(
							MainFrame.getInstance().getFrame(),
							null,
							e,
							!e.isExpected());
					errorDialog.setVisible(true);
				}
				
			});
		} catch (final Throwable e) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					ErrorDialog errorDialog = new ErrorDialog(
							MainFrame.getInstance().getFrame(),
							null,
							e,
							true);
					errorDialog.setVisible(true);
				}
				
			});
		} finally {
			SynchronizerUtils.removeProxy();
			
			SynchronizerUtils.removeOldCompletedTasks();
			
			Synchronizing.setSynchronizing(false);
		}
		
	}
	
}
