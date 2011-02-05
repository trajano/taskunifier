package com.leclercb.taskunifier.gui.components.statusbar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.translations.Translations;

public abstract class AbstractStatusBar extends JPanel {
	
	protected StatusElement lastSynchronizationDate;
	protected StatusElement scheduledSyncStatus;
	
	public AbstractStatusBar() {

	}
	
	protected final void initializeLastSynchronizationDate() {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				Main.SETTINGS.getStringProperty("date.date_format")
						+ " "
						+ Main.SETTINGS.getStringProperty("date.time_format"));
		
		String date = Translations.getString("statusbar.never");
		
		if (Main.SETTINGS.getCalendarProperty("synchronizer.last_synchronization_date") != null)
			date = dateFormat.format(Main.SETTINGS.getCalendarProperty(
					"synchronizer.last_synchronization_date").getTime());
		
		this.lastSynchronizationDate.setText(Translations.getString("statusbar.last_synchronization_date")
				+ ": "
				+ date);
		
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getPropertyName().equals(
						"synchronizer.last_synchronization_date")) {
					String date = Translations.getString("statusbar.never");
					
					if (Main.SETTINGS.getCalendarProperty("synchronizer.last_synchronization_date") != null)
						date = dateFormat.format(Main.SETTINGS.getCalendarProperty(
								"synchronizer.last_synchronization_date").getTime());
					
					AbstractStatusBar.this.lastSynchronizationDate.setText(Translations.getString("statusbar.last_synchronization_date")
							+ ": "
							+ date);
				}
			}
			
		});
	}
	
	public final void initializeScheduledSyncStatus(
			final ScheduledSyncThread thread) {
		this.updateScheduledSyncStatusText(thread);
		
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						"synchronizer.scheduler_enabled")) {
					AbstractStatusBar.this.updateScheduledSyncStatusText(thread);
				}
			}
			
		});
		
		thread.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						ScheduledSyncThread.PROP_REMAINING_SLEEP_TIME)) {
					AbstractStatusBar.this.updateScheduledSyncStatusText(thread);
				}
			}
			
		});
	}
	
	private final void updateScheduledSyncStatusText(ScheduledSyncThread thread) {
		String text = null;
		
		if (Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled")) {
			long sleep = thread.getRemainingSleepTime();
			sleep = sleep / 1000;
			
			String time = "";
			time += (sleep / 3600) + "h ";
			time += ((sleep % 3600) / 60) + "m ";
			time += (sleep % 60) + "s";
			
			text = Translations.getString("statusbar.next_scheduled_sync", time);
		} else {
			text = Translations.getString(
					"statusbar.next_scheduled_sync",
					Translations.getString("statusbar.never"));
		}
		
		AbstractStatusBar.this.scheduledSyncStatus.setText(text);
	}
	
}
