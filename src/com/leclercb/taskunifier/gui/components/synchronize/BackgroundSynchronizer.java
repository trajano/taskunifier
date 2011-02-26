package com.leclercb.taskunifier.gui.components.synchronize;

import java.util.Calendar;

import javax.swing.SwingUtilities;

import com.leclercb.commons.gui.swing.notifications.Notification;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class BackgroundSynchronizer {
	
	public BackgroundSynchronizer() {

	}
	
	public void synchronize() {
		Connection connection = null;
		Synchronizer synchronizer = null;
		
		try {
			SynchronizerUtils.initializeProxy();
			
			connection = SynchronizerUtils.getPlugin().getConnection();
			
			connection.loadParameters(Main.SETTINGS);
			
			connection.connect();
			
			synchronizer = SynchronizerUtils.getPlugin().getSynchronizerApi().getSynchronizer(
					connection);
			
			synchronizer.loadParameters(Main.SETTINGS);
			
			SynchronizerChoice choice = (SynchronizerChoice) Main.SETTINGS.getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
			
			synchronizer.synchronize(choice);
			
			connection.disconnect();
			
			Notification notification = new Notification(
					Images.getResourceImage("synchronize.png", 48, 48),
					Translations.getString("synchronizer.synchronization_completed"));
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
			
			if (connection != null)
				connection.saveParameters(Main.SETTINGS);
			
			if (synchronizer != null)
				synchronizer.saveParameters(Main.SETTINGS);
			
			SynchronizerUtils.removeOldCompletedTasks();
			
			Main.SETTINGS.setCalendarProperty(
					"synchronizer.last_synchronization_date",
					Calendar.getInstance());
		}
		
	}
	
}
