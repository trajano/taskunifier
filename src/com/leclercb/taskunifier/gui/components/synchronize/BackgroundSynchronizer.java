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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.util.Calendar;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.gui.swing.notifications.Notification;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
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
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							e.getMessage(),
							null,
							null,
							e.isExpected() ? e : null,
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
				}
				
			});
		} catch (final Throwable t) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							t.getMessage(),
							null,
							null,
							t,
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
				}
				
			});
		} finally {
			SynchronizerUtils.removeProxy();
			
			SynchronizerUtils.removeOldCompletedTasks();
			
			Synchronizing.setSynchronizing(false);
		}
		
	}
	
}
